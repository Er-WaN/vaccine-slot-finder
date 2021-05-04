import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Param from './param';
import ParamDetail from './param-detail';
import ParamUpdate from './param-update';
import ParamDeleteDialog from './param-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ParamUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ParamUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ParamDetail} />
      <ErrorBoundaryRoute path={match.url} component={Param} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ParamDeleteDialog} />
  </>
);

export default Routes;
