import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction,
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IVaccinationSlot, defaultValue } from 'app/shared/model/vaccination-slot.model';

export const ACTION_TYPES = {
  FETCH_VACCINATIONSLOT_LIST: 'vaccinationSlot/FETCH_VACCINATIONSLOT_LIST',
  FETCH_VACCINATIONSLOT: 'vaccinationSlot/FETCH_VACCINATIONSLOT',
  CREATE_VACCINATIONSLOT: 'vaccinationSlot/CREATE_VACCINATIONSLOT',
  UPDATE_VACCINATIONSLOT: 'vaccinationSlot/UPDATE_VACCINATIONSLOT',
  PARTIAL_UPDATE_VACCINATIONSLOT: 'vaccinationSlot/PARTIAL_UPDATE_VACCINATIONSLOT',
  DELETE_VACCINATIONSLOT: 'vaccinationSlot/DELETE_VACCINATIONSLOT',
  RESET: 'vaccinationSlot/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IVaccinationSlot>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type VaccinationSlotState = Readonly<typeof initialState>;

// Reducer

export default (state: VaccinationSlotState = initialState, action): VaccinationSlotState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_VACCINATIONSLOT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_VACCINATIONSLOT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_VACCINATIONSLOT):
    case REQUEST(ACTION_TYPES.UPDATE_VACCINATIONSLOT):
    case REQUEST(ACTION_TYPES.DELETE_VACCINATIONSLOT):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_VACCINATIONSLOT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_VACCINATIONSLOT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_VACCINATIONSLOT):
    case FAILURE(ACTION_TYPES.CREATE_VACCINATIONSLOT):
    case FAILURE(ACTION_TYPES.UPDATE_VACCINATIONSLOT):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_VACCINATIONSLOT):
    case FAILURE(ACTION_TYPES.DELETE_VACCINATIONSLOT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_VACCINATIONSLOT_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_VACCINATIONSLOT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_VACCINATIONSLOT):
    case SUCCESS(ACTION_TYPES.UPDATE_VACCINATIONSLOT):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_VACCINATIONSLOT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_VACCINATIONSLOT):
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

const apiUrl = 'api/vaccination-slots';

// Actions

export const getEntities: ICrudGetAllAction<IVaccinationSlot> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_VACCINATIONSLOT_LIST,
    payload: axios.get<IVaccinationSlot>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IVaccinationSlot> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_VACCINATIONSLOT,
    payload: axios.get<IVaccinationSlot>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IVaccinationSlot> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_VACCINATIONSLOT,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const updateEntity: ICrudPutAction<IVaccinationSlot> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_VACCINATIONSLOT,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IVaccinationSlot> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_VACCINATIONSLOT,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IVaccinationSlot> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_VACCINATIONSLOT,
    payload: axios.delete(requestUrl),
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
