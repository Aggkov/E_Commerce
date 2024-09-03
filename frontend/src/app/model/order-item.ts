import {Product} from "./product";
import {CartItem} from "./cart-item";

export class OrderItem {
  quantity: number;
  product: Product;

  // constructor(quantity: number, product: Product) {
  //   this.quantity = quantity;
  //   this.product = product;
  // }
  constructor(cartItem: CartItem) {
    this.quantity = cartItem.quantity;
    this.product = cartItem.product;
  }
}
