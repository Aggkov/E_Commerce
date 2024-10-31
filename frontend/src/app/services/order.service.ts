import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable, tap} from "rxjs";
import {Order} from "../interfaces/order/order";
import {OrderItem} from "../interfaces/order/order-item";
import {User} from "../interfaces/order/user";
import {environment} from "../../enviroments/enviroment";
import {OrderInfo} from "../interfaces/order/order-info";
import {Address} from "../interfaces/order/address";
import {Product} from "../interfaces/product";


@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private orderUrl = environment.gatewayUrl + environment.coreContextPath +  '/order';

  constructor(private httpClient: HttpClient) { }

  createOrder(order: Order): Observable<OrderSuccess> {
    return this.httpClient.post<OrderSuccess>(this.orderUrl, order);
  }

  getOrdersByUser(page: number, pageSize: number):Observable<GetResponseOrders> {
    return this.httpClient.get<GetResponseOrders>(
      `${this.orderUrl}?page=${page}&size=${pageSize}`
    ).pipe(
      tap(response =>
        console.log('orders response: ', response))
    );
  }

  getOrderByOrderTrackingNumber(orderTrackingNumber: string): Observable<Order> {
    return this.httpClient.get<Order>(`${this.orderUrl}/${orderTrackingNumber}`)
      .pipe(
        tap(response => console.log('order: ', response))
      );
  }
}

export interface OrderSuccess {
  orderTrackingNumber: string;
  orderInfo: OrderInfo;
  orderItems: OrderItem [];
  user: User;
}

export interface GetResponseOrders {
  content: Order[];
  size: number,
  page: number,
  totalElements: number,
  totalPages: number
}


