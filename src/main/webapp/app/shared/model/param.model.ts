export interface IParam {
  id?: number;
  name?: string;
  value?: string | null;
  description?: string | null;
}

export const defaultValue: Readonly<IParam> = {};
