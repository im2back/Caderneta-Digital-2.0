
import {  Routes } from '@angular/router';
import { UserpageComponentComponent } from './usermodule/userpage-component/userpage-component.component';
import { UserdetailComponentComponent } from './usermodule/userdetail-component/userdetail-component.component';
import { StockMainPageComponent } from './stock-module/stock-main-page/stock-main-page.component';
import { MetricsMainPageComponent } from './metrics-module/metrics-main-page/metrics-main-page.component';
import { PurchaseMainPageComponent } from './purchase-module/purchase-main-page/purchase-main-page.component';





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
    component : MetricsMainPageComponent
  },
  {
    path : 'purchase',
    component : PurchaseMainPageComponent
  },
  {
    path : 'stock',
    component : StockMainPageComponent
  },
  {
    path : '**',
    component : MetricsMainPageComponent
  },

];
