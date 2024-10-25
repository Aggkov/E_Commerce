import {OrderInfo} from "./order-info";
import {User} from "./user";
import {OrderItem} from "./order-item";

export interface Order {
  orderInfo: OrderInfo;
  user: User;
  orderItems: OrderItem[];
}
