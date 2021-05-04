import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import VaccinationCenter from './vaccination-center';
import VaccinationCenterDetail from './vaccination-center-detail';
import VaccinationCenterUpdate from './vaccination-center-update';
import VaccinationCenterDeleteDialog from './vaccination-center-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={VaccinationCenterUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={VaccinationCenterUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={VaccinationCenterDetail} />
      <ErrorBoundaryRoute path={match.url} component={VaccinationCenter} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={VaccinationCenterDeleteDialog} />
  </>
);

export default Routes;
