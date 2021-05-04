export interface IVaccinationCenter {
  id?: number;
  name?: string;
  address?: string;
  apiUrl?: string;
  reservationUrl?: string | null;
  enabled?: boolean;
}

export const defaultValue: Readonly<IVaccinationCenter> = {
  enabled: false,
};
