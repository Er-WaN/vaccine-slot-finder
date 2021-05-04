import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './vaccination-slot.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IVaccinationSlotDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const VaccinationSlotDetail = (props: IVaccinationSlotDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { vaccinationSlotEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="vaccinationSlotDetailsHeading">VaccinationSlot</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{vaccinationSlotEntity.id}</dd>
          <dt>
            <span id="date">Date</span>
          </dt>
          <dd>
            {vaccinationSlotEntity.date ? <TextFormat value={vaccinationSlotEntity.date} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="alreadyTaken">Already Taken</span>
          </dt>
          <dd>{vaccinationSlotEntity.alreadyTaken ? 'true' : 'false'}</dd>
          <dt>
            <span id="creationDate">Creation Date</span>
          </dt>
          <dd>
            {vaccinationSlotEntity.creationDate ? (
              <TextFormat value={vaccinationSlotEntity.creationDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>Vaccination Center</dt>
          <dd>{vaccinationSlotEntity.vaccinationCenter ? vaccinationSlotEntity.vaccinationCenter.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/vaccination-slot" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/vaccination-slot/${vaccinationSlotEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ vaccinationSlot }: IRootState) => ({
  vaccinationSlotEntity: vaccinationSlot.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(VaccinationSlotDetail);
