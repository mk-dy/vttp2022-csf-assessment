import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { Routes, RouterModule } from '@angular/router';

import { AppComponent } from './app.component';
import { MainComponent } from './components/main.component';
import { HttpClientModule } from '@angular/common/http';
import { OrdersComponent } from './components/orders.component';
import { PizzaService } from './pizza.service';

const appRoutes: Routes = [
  { path: '', component: MainComponent }, 
  { path: 'orders/:email', component: OrdersComponent},
  { path: '**', redirectTo: '/', pathMatch: 'full'}

]

@NgModule({
  declarations: [
    AppComponent, MainComponent, OrdersComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(appRoutes, {useHash: true}),
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule
  ],

  providers: [ PizzaService ],
  bootstrap: [AppComponent]
})
export class AppModule { }
