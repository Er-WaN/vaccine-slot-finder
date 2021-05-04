import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './vaccination-center.reducer';
import { IVaccinationCenter } from 'app/shared/model/vaccination-center.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IVaccinationCenterUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const VaccinationCenterUpdate = (props: IVaccinationCenterUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { vaccinationCenterEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/vaccination-center');
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
        ...vaccinationCenterEntity,
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
          <h2 id="vaccineSlotFinderApp.vaccinationCenter.home.createOrEditLabel" data-cy="VaccinationCenterCreateUpdateHeading">
            Create or edit a VaccinationCenter
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : vaccinationCenterEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="vaccination-center-id">ID</Label>
                  <AvInput id="vaccination-center-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nameLabel" for="vaccination-center-name">
                  Name
                </Label>
                <AvField
                  id="vaccination-center-name"
                  data-cy="name"
                  type="text"
                  name="name"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="addressLabel" for="vaccination-center-address">
                  Address
                </Label>
                <AvField
                  id="vaccination-center-address"
                  data-cy="address"
                  type="text"
                  name="address"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="apiUrlLabel" for="vaccination-center-apiUrl">
                  Api Url
                </Label>
                <AvField
                  id="vaccination-center-apiUrl"
                  data-cy="apiUrl"
                  type="text"
                  name="apiUrl"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="reservationUrlLabel" for="vaccination-center-reservationUrl">
                  Reservation Url
                </Label>
                <AvField id="vaccination-center-reservationUrl" data-cy="reservationUrl" type="text" name="reservationUrl" />
              </AvGroup>
              <AvGroup check>
                <Label id="enabledLabel">
                  <AvInput id="vaccination-center-enabled" data-cy="enabled" type="checkbox" className="form-check-input" name="enabled" />
                  Enabled
                </Label>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/vaccination-center" replace color="info">
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
  vaccinationCenterEntity: storeState.vaccinationCenter.entity,
  loading: storeState.vaccinationCenter.loading,
  updating: storeState.vaccinationCenter.updating,
  updateSuccess: storeState.vaccinationCenter.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(VaccinationCenterUpdate);
