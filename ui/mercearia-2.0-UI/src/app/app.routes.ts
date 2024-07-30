import { MetricspageComponentComponent } from './metricsmodule/metricspage-component/metricspage-component.component';
import {  Routes } from '@angular/router';
import { UserpageComponentComponent } from './usermodule/userpage-component/userpage-component.component';
import { UserdetailComponentComponent } from './usermodule/userdetail-component/userdetail-component.component';





export const routes: Routes = [
  {
    path : 'user',
    component : UserpageComponentComponent
  },
  {
    path : 'userdetail/:id',
    component : UserdetailComponentComponent
  },
  {
    path : 'metrics',
    component : MetricspageComponentComponent
  },

];
