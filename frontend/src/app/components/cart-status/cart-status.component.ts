import {Component, OnInit} from '@angular/core';
import {CartService} from "../../services/cart.service";
import {CurrencyPipe} from "@angular/common";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-cart-status',
  standalone: true,
  imports: [
    CurrencyPipe,
    RouterLink
  ],
  templateUrl: './cart-status.component.html',
  styleUrl: './cart-status.component.css'
})
export class CartStatusComponent implements OnInit {

  totalPrice: number = 0.00;
  totalQuantity: number = 0;

  constructor(private cartService: CartService) { }

  ngOnInit(): void {
    // console.log(`CartStatusComponent started`)
    this.updateCartStatus();
  }

  private updateCartStatus() {
    // subscribe to the cart totalPrice
    this.cartService.totalPrice.subscribe({
      next: data => this.totalPrice = data,
      error: err => console.log(err)
      }
    );

    // subscribe to the cart totalQuantity
    this.cartService.totalQuantity.subscribe({
        next: data => this.totalQuantity = data,
        error: err => console.log(err)
      }
    );
  }
}
