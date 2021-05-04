import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IParam, defaultValue } from 'app/shared/model/param.model';

export const ACTION_TYPES = {
  FETCH_PARAM_LIST: 'param/FETCH_PARAM_LIST',
  FETCH_PARAM: 'param/FETCH_PARAM',
  CREATE_PARAM: 'param/CREATE_PARAM',
  UPDATE_PARAM: 'param/UPDATE_PARAM',
  PARTIAL_UPDATE_PARAM: 'param/PARTIAL_UPDATE_PARAM',
  DELETE_PARAM: 'param/DELETE_PARAM',
  RESET: 'param/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IParam>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type ParamState = Readonly<typeof initialState>;

// Reducer

export default (state: ParamState = initialState, action): ParamState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PARAM_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PARAM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_PARAM):
    case REQUEST(ACTION_TYPES.UPDATE_PARAM):
    case REQUEST(ACTION_TYPES.DELETE_PARAM):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_PARAM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_PARAM_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PARAM):
    case FAILURE(ACTION_TYPES.CREATE_PARAM):
    case FAILURE(ACTION_TYPES.UPDATE_PARAM):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_PARAM):
    case FAILURE(ACTION_TYPES.DELETE_PARAM):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_PARAM_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_PARAM):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_PARAM):
    case SUCCESS(ACTION_TYPES.UPDATE_PARAM):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_PARAM):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_PARAM):
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

const apiUrl = 'api/params';

// Actions

export const getEntities: ICrudGetAllAction<IParam> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_PARAM_LIST,
  payload: axios.get<IParam>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IParam> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PARAM,
    payload: axios.get<IParam>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IParam> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PARAM,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IParam> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PARAM,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IParam> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_PARAM,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IParam> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PARAM,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
