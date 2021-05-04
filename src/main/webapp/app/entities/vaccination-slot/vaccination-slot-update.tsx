import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IVaccinationCenter } from 'app/shared/model/vaccination-center.model';
import { getEntities as getVaccinationCenters } from 'app/entities/vaccination-center/vaccination-center.reducer';
import { getEntity, updateEntity, createEntity, reset } from './vaccination-slot.reducer';
import { IVaccinationSlot } from 'app/shared/model/vaccination-slot.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IVaccinationSlotUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const VaccinationSlotUpdate = (props: IVaccinationSlotUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { vaccinationSlotEntity, vaccinationCenters, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/vaccination-slot');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }

    props.getVaccinationCenters();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.date = convertDateTimeToServer(values.date);
    values.creationDate = convertDateTimeToServer(values.creationDate);

    if (errors.length === 0) {
      const entity = {
        ...vaccinationSlotEntity,
        ...values,
        vaccinationCenter: vaccinationCenters.find(it => it.id.toString() === values.vaccinationCenterId.toString()),
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="vaccineSlotFinderApp.vaccinationSlot.home.createOrEditLabel" data-cy="VaccinationSlotCreateUpdateHeading">
            Create or edit a VaccinationSlot
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : vaccinationSlotEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="vaccination-slot-id">ID</Label>
                  <AvInput id="vaccination-slot-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="dateLabel" for="vaccination-slot-date">
                  Date
                </Label>
                <AvInput
                  id="vaccination-slot-date"
                  data-cy="date"
                  type="datetime-local"
                  className="form-control"
                  name="date"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.vaccinationSlotEntity.date)}
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="alreadyTakenLabel">
                  <AvInput
                    id="vaccination-slot-alreadyTaken"
                    data-cy="alreadyTaken"
                    type="checkbox"
                    className="form-check-input"
                    name="alreadyTaken"
                  />
                  Already Taken
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="creationDateLabel" for="vaccination-slot-creationDate">
                  Creation Date
                </Label>
                <AvInput
                  id="vaccination-slot-creationDate"
                  data-cy="creationDate"
                  type="datetime-local"
                  className="form-control"
                  name="creationDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.vaccinationSlotEntity.creationDate)}
                />
              </AvGroup>
              <AvGroup>
                <Label for="vaccination-slot-vaccinationCenter">Vaccination Center</Label>
                <AvInput
                  id="vaccination-slot-vaccinationCenter"
                  data-cy="vaccinationCenter"
                  type="select"
                  className="form-control"
                  name="vaccinationCenterId"
                >
                  <option value="" key="0" />
                  {vaccinationCenters
                    ? vaccinationCenters.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.name}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/vaccination-slot" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  vaccinationCenters: storeState.vaccinationCenter.entities,
  vaccinationSlotEntity: storeState.vaccinationSlot.entity,
  loading: storeState.vaccinationSlot.loading,
  updating: storeState.vaccinationSlot.updating,
  updateSuccess: storeState.vaccinationSlot.updateSuccess,
});

const mapDispatchToProps = {
  getVaccinationCenters,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(VaccinationSlotUpdate);
