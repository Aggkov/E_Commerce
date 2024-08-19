import { Injectable } from '@angular/core';
import {CartItem} from "../model/cart-item";
import {BehaviorSubject, Subject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class CartService {

  cartItems: CartItem[] = [];
  //  A Subject does not keep track of any previously emitted values. When a value is emitted using subject.next(value),
  //  it is broadcasted to all current subscribers, but the Subject doesn't retain this value in any way.
  // If a new subscriber subscribes to a Subject,
  // it will not receive any previously emitted values.
  // It will only receive values emitted after the subscription.
  totalPrice: BehaviorSubject<number> = new BehaviorSubject<number>(0.0);
  totalQuantity: BehaviorSubject<number> = new BehaviorSubject<number>(0);

  constructor() {
    // console.log(`CartService started`)

    // const storedCartItems = sessionStorage.getItem('cartItems');
    // this.cartItems = storedCartItems !== null ? JSON.parse(storedCartItems) : [];


    /*
    You can call the computeCartTotals() method in the constructor
    after loading the cart items to ensure that the totals
    reflect the current state of the cart upon page load.
     */
    // this.computeCartTotals();
  }

  addToCartOrIncrementQuantity(cartItem: CartItem) {

    // check if we already have the item in our cart
    let alreadyExistsInCart: boolean = false;
    let existingCartItem: CartItem | undefined = undefined;

    if (this.cartItems.length > 0) {
      // find the item in the cart based on item id
      // if find return item else undefined
      existingCartItem = this.cartItems.find(item => item.id === cartItem.id);
      // check if we found it
      alreadyExistsInCart = (existingCartItem !== undefined);
    }

    if (alreadyExistsInCart) {
      // increment the quantity
      existingCartItem!.quantity++;  // The '!' operator asserts that existingCartItem is not undefined
    } else {
      // just add the item to the array
      this.cartItems.push(cartItem);
    }
    // compute cart total price and total quantity
    this.computeCartTotals();
  }

  computeCartTotals() {
    let totalPriceValue: number = 0;
    let totalQuantityValue: number = 0;

    for (let cartItem of this.cartItems) {
      totalPriceValue += cartItem.quantity * cartItem.unitPrice;
      totalQuantityValue += cartItem.quantity;
    }

    // publish the new values ... all subscribers will receive the new data
    this.totalPrice.next(totalPriceValue);
    this.totalQuantity.next(totalQuantityValue);

    // sessionStorage.setItem('cartItems', JSON.stringify(this.cartItems));

    // log cart data just for debugging purposes
    // this.logCartData(totalPriceValue, totalQuantityValue);
  }

  private logCartData(totalPriceValue: number, totalQuantityValue: number) {
    console.log('Contents of the cart');
    for (let cartItem of this.cartItems) {
      const subTotalPrice = cartItem.quantity * cartItem.unitPrice;
      console.log(`name: ${cartItem.name}, quantity=${cartItem.quantity},
      unitPrice=${cartItem.unitPrice}, subTotalPrice=${subTotalPrice}`);
    }

    console.log(`totalPrice: ${totalPriceValue.toFixed(2)},
    totalQuantity: ${totalQuantityValue}`);
    console.log('----');
  }

  decrementQuantity(cartItem: CartItem) {
    if (cartItem.quantity > 0) {
      cartItem.quantity--;
      this.computeCartTotals();
      // this.remove(cartItem);
    }
    // else {
    // }
    // if(cartItem.quantity > 0) {
    //   cartItem.quantity--;
    //   this.totalQuantity.next(this.totalQuantity.value - cartItem.quantity);
    //   this.totalPrice.next(cartItem.quantity * cartItem.unitPrice);
    // }
  }

  remove(cartItem: CartItem) {
// get index of item in the array
    const itemIndex = this.cartItems.findIndex( item => item.id === cartItem.id );

    // if found, remove the item from the array at the given index
    if (itemIndex > -1) {
      this.cartItems.splice(itemIndex, 1);

      this.computeCartTotals();
    }
  }
}
