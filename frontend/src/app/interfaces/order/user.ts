import {Address} from "./address";

export interface User {
  firstName: string;
  lastName: string;
  email: string;
  shippingAddress: Address;
  billingAddress: Address;
}
