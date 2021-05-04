import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './vaccination-center.reducer';
import { IVaccinationCenter } from 'app/shared/model/vaccination-center.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IVaccinationCenterProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const VaccinationCenter = (props: IVaccinationCenterProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const handleSyncList = () => {
    props.getEntities();
  };

  const { vaccinationCenterList, match, loading } = props;
  return (
    <div>
      <h2 id="vaccination-center-heading" data-cy="VaccinationCenterHeading">
        Vaccination Centers
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Vaccination Center
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {vaccinationCenterList && vaccinationCenterList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Address</th>
                <th>Api Url</th>
                <th>Reservation Url</th>
                <th>Enabled</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {vaccinationCenterList.map((vaccinationCenter, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${vaccinationCenter.id}`} color="link" size="sm">
                      {vaccinationCenter.id}
                    </Button>
                  </td>
                  <td>{vaccinationCenter.name}</td>
                  <td>{vaccinationCenter.address}</td>
                  <td>{vaccinationCenter.apiUrl}</td>
                  <td>{vaccinationCenter.reservationUrl}</td>
                  <td>{vaccinationCenter.enabled ? 'true' : 'false'}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${vaccinationCenter.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${vaccinationCenter.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${vaccinationCenter.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Vaccination Centers found</div>
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ vaccinationCenter }: IRootState) => ({
  vaccinationCenterList: vaccinationCenter.entities,
  loading: vaccinationCenter.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(VaccinationCenter);
