import {Product} from "../product";
import {CartItem} from "../cart-item";

export interface OrderItem {
  quantity: number;
  product: Product;

  // constructor(cartItem: CartItem) {
  //   this.quantity = cartItem.quantity;
  //   this.product = {};
  //   this.product.id = cartItem.product.id;
  // }
}
