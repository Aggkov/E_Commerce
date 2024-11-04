import {OrderInfo} from "./order-info";
import {User} from "./user";
import {OrderItem} from "./order-item";
import {Address} from "./address";

export interface Order {
  orderTrackingNumber?: string;
  orderInfo: OrderInfo;
  user?: User;
  orderItems: OrderItem[];
  createdAt?: Date;
  shippingAddress?: Address;
  billingAddress?: Address;
  paypalOrderId?: string;
}
