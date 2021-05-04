import dayjs from 'dayjs';
import { IVaccinationCenter } from 'app/shared/model/vaccination-center.model';

export interface IVaccinationSlot {
  id?: number;
  date?: string;
  alreadyTaken?: boolean | null;
  creationDate?: string | null;
  vaccinationCenter?: IVaccinationCenter | null;
}

export const defaultValue: Readonly<IVaccinationSlot> = {
  alreadyTaken: false,
};
