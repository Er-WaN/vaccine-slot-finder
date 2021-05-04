import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './param.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IParamDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ParamDetail = (props: IParamDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { paramEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="paramDetailsHeading">Param</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{paramEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{paramEntity.name}</dd>
          <dt>
            <span id="value">Value</span>
          </dt>
          <dd>{paramEntity.value}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{paramEntity.description}</dd>
        </dl>
        <Button tag={Link} to="/param" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/param/${paramEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ param }: IRootState) => ({
  paramEntity: param.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ParamDetail);
