import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Order} from "../model/order";
import {OrderItem} from "../model/order-item";
import {User} from "../model/user";


@Injectable({
  providedIn: 'root'
})
export class CheckoutService {

  private purchaseUrl = 'http://localhost:8080/api/v1/order';

  constructor(private httpClient: HttpClient) { }

  createOrder(order: Order): Observable<OrderResponse> {
    return this.httpClient.post<OrderResponse>(this.purchaseUrl, order);
  }
}

export interface OrderResponse {
  orderTrackingNumber: string;
  orderItems: OrderItem [];
  user: User;
}
