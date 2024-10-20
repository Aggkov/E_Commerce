import {Product} from "./product";

export class CartItem {
  quantity: number;
  product: Product;

  constructor(product: Product) {
    // this.id = product.id;
    // this.name = product.name;
    // this.imageUrl = product.imageUrl;
    // this.unitPrice = product.unitPrice;
    this.product = product;
    this.quantity = 1;
  }
}
