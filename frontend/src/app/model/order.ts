import {OrderInfo} from "./order-info";
import {Customer} from "./customer";
import {OrderItem} from "./order-item";

export class Order {
  orderInfo: OrderInfo;
  customer: Customer;
  orderItemList: OrderItem[];

  constructor(orderInfo: OrderInfo, customer: Customer, orderItemList: OrderItem[]) {
    this.orderInfo = orderInfo;
    this.customer = customer;
    this.orderItemList = orderItemList;
  }
}
