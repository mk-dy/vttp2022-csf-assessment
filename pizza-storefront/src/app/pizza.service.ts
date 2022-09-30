// Implement the methods in PizzaService for Task 3
// Add appropriate parameter and return type 

import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { firstValueFrom, Subject } from "rxjs";
import { Order, OrderSummary } from "./models";

@Injectable()
export class PizzaService {

  onOrderSummaries = new Subject<OrderSummary[]>()

  constructor(private http: HttpClient) { }

  // POST /api/order
  // Add any required parameters or return type
  createOrder(order: Order) { 

    const headers = new HttpHeaders()
                .set('Content-Type', 'application/json')
                .set('Accept', 'application/json')
    
    return firstValueFrom(
        this.http.post<any>(`/api/order`, order, { headers } )
    ).then(result => {
        this.getOrders(order.email)
        return result
    })            

  }

  // GET /api/order/<email>/all
  // Add any required parameters or return type
  getOrders(email: string) { 

    return firstValueFrom(
      this.http.get<any>(`/api/order/${email}/all`)
    ).then((result: OrderSummary[]) => {
      this.onOrderSummaries.next(result)
      return result
    } 
      )

  }

}
