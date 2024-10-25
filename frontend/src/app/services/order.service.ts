import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Order} from "../interfaces/order/order";
import {OrderItem} from "../interfaces/order/order-item";
import {User} from "../interfaces/order/user";
import {environment} from "../../enviroments/enviroment";
import {OrderInfo} from "../interfaces/order/order-info";


@Injectable({
  providedIn: 'root'
})
export class OrderService {

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
