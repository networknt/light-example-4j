import {NgModule} from '@angular/core'
import {RouterModule} from "@angular/router";
import {FormsModule} from "@angular/forms";
import {BrowserModule} from "@angular/platform-browser";
import {HttpModule} from "@angular/http";

import {rootRouterConfig} from "./app.routes";
import {AppComponent} from "./app.component";


import {ListComponent} from './list';

import {EditComponent} from './edit';

import {TodoService,
  HeaderComponent} from "./shared";


@NgModule({
  declarations: [
    AppComponent,
    ListComponent, 
    EditComponent,
    HeaderComponent

  ],
  imports     : [BrowserModule, FormsModule, HttpModule, RouterModule.forRoot(rootRouterConfig)],
  providers   : [TodoService],
  bootstrap   : [AppComponent]
})
export class AppModule {

}
