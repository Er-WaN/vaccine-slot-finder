package com.wandev.vaccineslotfinder.service;

import com.wandev.vaccineslotfinder.domain.Param;
import com.wandev.vaccineslotfinder.domain.User;
import com.wandev.vaccineslotfinder.domain.VaccinationCenter;
import com.wandev.vaccineslotfinder.domain.VaccinationSlot;
import com.wandev.vaccineslotfinder.repository.ParamRepository;
import com.wandev.vaccineslotfinder.repository.UserRepository;
import com.wandev.vaccineslotfinder.repository.VaccinationCenterRepository;
import com.wandev.vaccineslotfinder.repository.VaccinationSlotRepository;
import com.wandev.vaccineslotfinder.service.doctolib.DoctolibApiParam;
import com.wandev.vaccineslotfinder.service.doctolib.DoctolibResponse;
import com.wandev.vaccineslotfinder.service.dto.NotifyFreeSlotDTO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Find free vaccination slot from Doctolib API
 */
@Service
@Transactional
public class FindSlotService {

    /**
     * Logger
     */
    private final Logger log = LoggerFactory.getLogger(FindSlotService.class);

    /**
     * Mail Service
     */
    private final MailService mailService;

    /**
     * Vaccination Center Repository
     */
    private final VaccinationCenterRepository vaccinationCenterRepository;

    /**
     * Params Repository
     */
    private final ParamRepository paramRepository;

    /**
     * Vaccination Slot Repository
     */
    private final VaccinationSlotRepository vaccinationSlotRepository;

    /**
     * User Repository
     */
    private final UserRepository userRepository;

    /**
     * Date format for only hours
     */
    private final SimpleDateFormat dateHourFormat = new SimpleDateFormat("hh:mm");

    /**
     * Date format
     */
    private final SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Constructor
     *
     * @param mailService Mail Service
     * @param vaccinationCenterRepository Vaccination Center Repository
     * @param paramRepository Params Repository
     * @param vaccinationSlotRepository Vaccination Slot Repository
     * @param userRepository User Repository
     */
    public FindSlotService(
        MailService mailService,
        VaccinationCenterRepository vaccinationCenterRepository,
        ParamRepository paramRepository,
        VaccinationSlotRepository vaccinationSlotRepository,
        UserRepository userRepository
    ) {
        this.mailService = mailService;
        this.vaccinationCenterRepository = vaccinationCenterRepository;
        this.paramRepository = paramRepository;
        this.vaccinationSlotRepository = vaccinationSlotRepository;
        this.userRepository = userRepository;
    }

    /**
     * Find free vaccination slot from Doctolib API
     *
     * @throws ParseException If date from API non parsable
     */
    @Scheduled(cron = "${findslot-delay}")
    public void findSlot() throws ParseException {
        final List<NotifyFreeSlotDTO> newFreeSlotsList = new ArrayList<>();
        boolean notifyEmail = false;
        final Instant creationDate = Instant.now();

        // 1 - Recover vaccination center
        List<VaccinationCenter> vaccinationCenters = vaccinationCenterRepository.findByEnabledTrue();
        log.info("{} vaccination center(s) found", vaccinationCenters.size());

        // 2 - For each vaccination center call API and handle data
        for (VaccinationCenter vaccinationCenter : vaccinationCenters) {
            boolean newSlotForCenter = false;

            NotifyFreeSlotDTO notifyFreeSlotDTO = new NotifyFreeSlotDTO();
            notifyFreeSlotDTO.setCenter(vaccinationCenter);

            final String finalUrl = replaceParamsUrl(vaccinationCenter.getApiUrl());

            log.info("###################################");
            log.info("ID : {}", vaccinationCenter.getId());
            log.info("Name : {}", vaccinationCenter.getName());
            log.info("Address : {}", vaccinationCenter.getAddress());
            log.info("Reservation URL : {}", vaccinationCenter.getReservationUrl());
            log.info("API URL : {}", finalUrl);

            DoctolibResponse result = new RestTemplate().getForObject(finalUrl, DoctolibResponse.class);
            log.info("Availabilities : {}", result.getAvailabilities() != null ? result.getAvailabilities().size() : "NONE");

            // Only if free slots
            if (!CollectionUtils.isEmpty(result.getAvailabilities())) {
                List<VaccinationSlot> slots = vaccinationSlotRepository.findVaccinationSlotByAlreadyTakenFalseAndVaccinationCenter(
                    vaccinationCenter
                );

                // Loop over availibilitires
                for (DoctolibResponse.Availability availability : result
                    .getAvailabilities()
                    .stream()
                    .filter(a -> !CollectionUtils.isEmpty(a.getSlots()))
                    .collect(Collectors.toList())) {
                    List<VaccinationSlot> newSlotsForDay = new ArrayList<>();

                    String formattedDate = WordUtils.capitalize(
                        new SimpleDateFormat("EEEE dd MMM").format(dayFormat.parse(availability.getDate()))
                    );

                    log.info("Date : {}", formattedDate);

                    for (DoctolibResponse.Slot s : availability.getSlots()) {
                        VaccinationSlot slot = new VaccinationSlot();
                        slot.setVaccinationCenter(vaccinationCenter);
                        slot.setAlreadyTaken(false);
                        slot.setDate(s.getStartDate().toInstant());
                        slot.setCreationDate(creationDate);

                        Optional<VaccinationSlot> alreadyExistingSlot = slots
                            .stream()
                            .filter(
                                bddSLot ->
                                    bddSLot.getVaccinationCenter().equals(vaccinationCenter) && bddSLot.getDate().equals(slot.getDate())
                            )
                            .findFirst();

                        if (!alreadyExistingSlot.isPresent()) {
                            log.info("- {}", dateHourFormat.format(s.getStartDate()));
                            vaccinationSlotRepository.save(slot);
                            newSlotForCenter = true;
                            notifyEmail = true;

                            newSlotsForDay.add(slot);
                        }

                        if (!CollectionUtils.isEmpty(newSlotsForDay)) {
                            notifyFreeSlotDTO.getSlots().put(formattedDate, newSlotsForDay);
                        }
                    }
                }
            }

            if (newSlotForCenter) {
                newFreeSlotsList.add(notifyFreeSlotDTO);
            }
        }

        // 3 - Notify if new slots
        if (notifyEmail) {
            Optional<User> erwan = userRepository.findOneByLogin("erwan");
            if (erwan.isPresent()) {
                mailService.sendNotifyFreeSlotMail(erwan.get(), newFreeSlotsList);
            }
        }
    }

    /**
     * Replace wanted parameter in URL
     *
     * @param apiUrl API URL
     * @return URL with desired params
     */
    private String replaceParamsUrl(String apiUrl) {
        Param minDateSlot = paramRepository.findParamByNameEquals("min_date_slot");
        Param maxDaysSearch = paramRepository.findParamByNameEquals("max_days_search");

        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromUriString(apiUrl);
        urlBuilder.replaceQueryParam(DoctolibApiParam.START_DATE.param, minDateSlot.getValue());
        urlBuilder.replaceQueryParam(DoctolibApiParam.LIMIT.param, maxDaysSearch.getValue());

        return urlBuilder.build().toUriString();
    }
}
