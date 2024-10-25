import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Order} from "../model/order/order";
import {OrderItem} from "../model/order/order-item";
import {User} from "../model/order/user";
import {environment} from "../../enviroments/enviroment";
import {OrderInfo} from "../model/order/order-info";


@Injectable({
  providedIn: 'root'
})
export class CheckoutService {

  private purchaseUrl = environment.coreServiceUrl + '/order';

  constructor(private httpClient: HttpClient) { }

  createOrder(order: Order): Observable<OrderSuccess> {
    return this.httpClient.post<OrderSuccess>(this.purchaseUrl, order);
  }
}

export interface OrderSuccess {
  orderTrackingNumber: string;
  orderInfo: OrderInfo;
  orderItems: OrderItem [];
  user: User;
}
