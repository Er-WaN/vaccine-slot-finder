import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './param.reducer';
import { IParam } from 'app/shared/model/param.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IParamUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ParamUpdate = (props: IParamUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { paramEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/param');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...paramEntity,
        ...values,
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
          <h2 id="vaccineSlotFinderApp.param.home.createOrEditLabel" data-cy="ParamCreateUpdateHeading">
            Create or edit a Param
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : paramEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="param-id">ID</Label>
                  <AvInput id="param-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nameLabel" for="param-name">
                  Name
                </Label>
                <AvField
                  id="param-name"
                  data-cy="name"
                  type="text"
                  name="name"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="valueLabel" for="param-value">
                  Value
                </Label>
                <AvField id="param-value" data-cy="value" type="text" name="value" />
              </AvGroup>
              <AvGroup>
                <Label id="descriptionLabel" for="param-description">
                  Description
                </Label>
                <AvField id="param-description" data-cy="description" type="text" name="description" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/param" replace color="info">
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
  paramEntity: storeState.param.entity,
  loading: storeState.param.loading,
  updating: storeState.param.updating,
  updateSuccess: storeState.param.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ParamUpdate);
