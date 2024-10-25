import {OrderInfo} from "./order-info";
import {User} from "./user";
import {OrderItem} from "./order-item";

export class Order {
  orderInfo: OrderInfo;
  user: User;
  orderItems: OrderItem[];

  constructor(orderInfo: OrderInfo, user: User, orderItems: OrderItem[]) {
    this.orderInfo = orderInfo;
    this.user = user;
    this.orderItems = orderItems;
  }
}
