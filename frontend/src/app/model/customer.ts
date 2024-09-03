import {Address} from "./address";

export class Customer {
  firstName: string;
  lastName: string;
  email: string;
  shippingAddress: Address;
  billingAddress: Address;

  constructor(firstName: string, lastName: string, email: string, shippingAddress: Address, billingAddress: Address) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.shippingAddress = shippingAddress;
    this.billingAddress = billingAddress;
  }
}
