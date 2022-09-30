import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { Order, OrderSummary } from '../models';
import { PizzaService } from '../pizza.service';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css']
})
export class OrdersComponent implements OnInit, OnDestroy {

  email!: string
  orders: OrderSummary[] = []
  sub$!: Subscription

  constructor(private pizzaSvc: PizzaService,private activatedRoute: ActivatedRoute) { }
  

  ngOnInit(): void {
    
    this.email = this.activatedRoute.snapshot.params['email']
    this.pizzaSvc.getOrders(this.email).then(result => {
      this.orders = result 
    }
      )
    // this.sub$ = this.pizzaSvc.onOrderSummaries.subscribe( (result:OrderSummary[]) => {
    //   this.orders = result
    // })
  }
  ngOnDestroy(): void {
    // this.sub$.unsubscribe()
  }

}
