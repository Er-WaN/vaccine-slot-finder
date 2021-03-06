import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IVaccinationCenter, defaultValue } from 'app/shared/model/vaccination-center.model';

export const ACTION_TYPES = {
  FETCH_VACCINATIONCENTER_LIST: 'vaccinationCenter/FETCH_VACCINATIONCENTER_LIST',
  FETCH_VACCINATIONCENTER: 'vaccinationCenter/FETCH_VACCINATIONCENTER',
  CREATE_VACCINATIONCENTER: 'vaccinationCenter/CREATE_VACCINATIONCENTER',
  UPDATE_VACCINATIONCENTER: 'vaccinationCenter/UPDATE_VACCINATIONCENTER',
  PARTIAL_UPDATE_VACCINATIONCENTER: 'vaccinationCenter/PARTIAL_UPDATE_VACCINATIONCENTER',
  DELETE_VACCINATIONCENTER: 'vaccinationCenter/DELETE_VACCINATIONCENTER',
  RESET: 'vaccinationCenter/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IVaccinationCenter>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type VaccinationCenterState = Readonly<typeof initialState>;

// Reducer

export default (state: VaccinationCenterState = initialState, action): VaccinationCenterState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_VACCINATIONCENTER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_VACCINATIONCENTER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_VACCINATIONCENTER):
    case REQUEST(ACTION_TYPES.UPDATE_VACCINATIONCENTER):
    case REQUEST(ACTION_TYPES.DELETE_VACCINATIONCENTER):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_VACCINATIONCENTER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_VACCINATIONCENTER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_VACCINATIONCENTER):
    case FAILURE(ACTION_TYPES.CREATE_VACCINATIONCENTER):
    case FAILURE(ACTION_TYPES.UPDATE_VACCINATIONCENTER):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_VACCINATIONCENTER):
    case FAILURE(ACTION_TYPES.DELETE_VACCINATIONCENTER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_VACCINATIONCENTER_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_VACCINATIONCENTER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_VACCINATIONCENTER):
    case SUCCESS(ACTION_TYPES.UPDATE_VACCINATIONCENTER):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_VACCINATIONCENTER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_VACCINATIONCENTER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/vaccination-centers';

// Actions

export const getEntities: ICrudGetAllAction<IVaccinationCenter> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_VACCINATIONCENTER_LIST,
  payload: axios.get<IVaccinationCenter>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IVaccinationCenter> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_VACCINATIONCENTER,
    payload: axios.get<IVaccinationCenter>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IVaccinationCenter> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_VACCINATIONCENTER,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IVaccinationCenter> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_VACCINATIONCENTER,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IVaccinationCenter> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_VACCINATIONCENTER,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IVaccinationCenter> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_VACCINATIONCENTER,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
