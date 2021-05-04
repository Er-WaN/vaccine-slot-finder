import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import VaccinationCenter from './vaccination-center';
import Param from './param';
import VaccinationSlot from './vaccination-slot';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}vaccination-center`} component={VaccinationCenter} />
      <ErrorBoundaryRoute path={`${match.url}param`} component={Param} />
      <ErrorBoundaryRoute path={`${match.url}vaccination-slot`} component={VaccinationSlot} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
