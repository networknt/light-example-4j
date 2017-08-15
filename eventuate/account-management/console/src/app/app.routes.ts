import {Routes} from "@angular/router";


import {ListComponent} from './list';
import {EditComponent} from './edit';


export const rootRouterConfig: Routes = [
  {
    path: "",
    redirectTo: "list",
    pathMatch: 'full'
  },
  {
    path: "list",
    component: ListComponent
  },
  {
    path: "edit",
    component: EditComponent
  },
  {
    path: "edit/:id",
    component: EditComponent
  }

];
