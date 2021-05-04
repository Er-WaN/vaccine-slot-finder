import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './vaccination-center.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IVaccinationCenterDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const VaccinationCenterDetail = (props: IVaccinationCenterDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { vaccinationCenterEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="vaccinationCenterDetailsHeading">VaccinationCenter</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{vaccinationCenterEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{vaccinationCenterEntity.name}</dd>
          <dt>
            <span id="address">Address</span>
          </dt>
          <dd>{vaccinationCenterEntity.address}</dd>
          <dt>
            <span id="apiUrl">Api Url</span>
          </dt>
          <dd>{vaccinationCenterEntity.apiUrl}</dd>
          <dt>
            <span id="reservationUrl">Reservation Url</span>
          </dt>
          <dd>{vaccinationCenterEntity.reservationUrl}</dd>
          <dt>
            <span id="enabled">Enabled</span>
          </dt>
          <dd>{vaccinationCenterEntity.enabled ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/vaccination-center" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/vaccination-center/${vaccinationCenterEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ vaccinationCenter }: IRootState) => ({
  vaccinationCenterEntity: vaccinationCenter.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(VaccinationCenterDetail);
