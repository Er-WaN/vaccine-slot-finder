import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import VaccinationSlot from './vaccination-slot';
import VaccinationSlotDetail from './vaccination-slot-detail';
import VaccinationSlotUpdate from './vaccination-slot-update';
import VaccinationSlotDeleteDialog from './vaccination-slot-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={VaccinationSlotUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={VaccinationSlotUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={VaccinationSlotDetail} />
      <ErrorBoundaryRoute path={match.url} component={VaccinationSlot} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={VaccinationSlotDeleteDialog} />
  </>
);

export default Routes;
