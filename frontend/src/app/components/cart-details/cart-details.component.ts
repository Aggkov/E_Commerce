import {Component, OnInit} from '@angular/core';
import {CartService} from "../../services/cart.service";
import {CartItem} from "../../interfaces/cart-item";
import {CurrencyPipe, NgForOf, NgIf} from "@angular/common";
import {RouterLink} from "@angular/router";
import {Product} from "../../interfaces/product";

@Component({
  selector: 'app-cart-details',
  standalone: true,
  imports: [
    NgForOf,
    CurrencyPipe,
    NgIf,
    RouterLink
  ],
  templateUrl: './cart-details.component.html',
  styleUrl: './cart-details.component.css'
})
export class CartDetailsComponent implements OnInit {
  cartItems: CartItem[] = [];
  totalPrice: number = 0;
  totalQuantity: number = 0;

  // getImageUrl(product: any): string {
  //   return `${environment.backendUrl}${product.imageUrl}`;
  // }

  constructor(private cartService: CartService) { }

  ngOnInit(): void {
    // console.log(`CartDetailsComponent started`)
    this.listCartDetails();
  }

  listCartDetails() {
    // get a handle to the cart items
    this.cartItems = this.cartService.cartItems;

    // subscribe to the cart totalPrice
    this.cartService.totalPrice.subscribe(
      data => this.totalPrice = data
    );

    // subscribe to the cart totalQuantity
    this.cartService.totalQuantity.subscribe(
      data => this.totalQuantity = data
    );

    // compute cart total price and quantity
    // this.cartService.computeCartTotals();
  }

  incrementQuantity(cartItem: any) {
    this.cartService.addToCartOrIncrementQuantity(cartItem);
  }

  decrementQuantity(cartItem: CartItem) {
    this.cartService.decrementQuantity(cartItem);
  }

  remove(theCartItem: CartItem) {
    this.cartService.remove(theCartItem);
  }

  getImage(product: Product): string | undefined {
    if (product.imageUrl) {
      return 'data:image/png;base64,' + product.imageUrl
    }
    return 'https://upload.wikimedia.org/wikipedia/en/3/30/Java_programming_language_logo.svg';
  }
}
