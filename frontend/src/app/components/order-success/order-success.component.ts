import {Component, OnInit} from '@angular/core';
import {OrderSuccess} from "../../services/order.service";
import {Router} from "@angular/router";
import {AsyncPipe, NgForOf, NgIf, NgOptimizedImage} from "@angular/common";
import {OrderItem} from "../../interfaces/order/order-item";
import {environment} from "../../../enviroments/enviroment";
import {Product} from "../../interfaces/product";
import {Observable, of} from "rxjs";

@Component({
  selector: 'app-order-success',
  standalone: true,
  imports: [
    NgIf,
    NgForOf,
    NgOptimizedImage,
    AsyncPipe
  ],
  templateUrl: './order-success.component.html',
  styleUrl: './order-success.component.css'
})
export class OrderSuccessComponent implements OnInit {
  orderSuccess: OrderSuccess | undefined;
  customerEmail: string | undefined;
  orderTrackingNumber: string | undefined;
  orderItems: OrderItem[] = [];
  shippingStreet: string | undefined;
  shippingCity: string | undefined;
  shippingZipCode: string | undefined;
  billingStreet: string | undefined;
  billingCity: string | undefined;
  billingZipCode: string | undefined;
  totalQuantity$: Observable<number> = of(0); // Default value
  totalPrice$: Observable<number> = of(0);    // Default value
  // status$: Observable<string> = of('');

  constructor(private router: Router) {}

  ngOnInit(): void {
    // const navigation = this.router.getCurrentNavigation();
    // Access state using history.state instead of getCurrentNavigation
    this.orderSuccess = history.state.orderSuccess;
    // if (this.orderSuccess) {
    //   this.orderSuccess = history.state.orderSuccess;
      // console.log('Full OrderSuccess from History State:', history.state);
      // console.log('OrderInfo:', this.orderSuccess?.orderInfo);
    // } else {
    //   console.log('History state is undefined');
    // }

    if (this.orderSuccess) {
      this.customerEmail = this.orderSuccess?.user.email;
      this.orderTrackingNumber = this.orderSuccess?.orderTrackingNumber;
      this.orderItems = this.orderSuccess?.orderItems || [];
      this.totalQuantity$ = of(this.orderSuccess?.orderInfo?.totalQuantity);
      this.totalPrice$ = of(this.orderSuccess?.orderInfo?.totalPrice);
      // this.status$ = of(this.orderSuccess?.orderInfo.status);
      this.shippingStreet = this.orderSuccess?.user.shippingAddress.street;
      this.shippingCity = this.orderSuccess?.user.shippingAddress.city;
      this.shippingZipCode = this.orderSuccess?.user.shippingAddress.zipCode;
      this.billingStreet = this.orderSuccess?.user.billingAddress.street;
      this.billingCity = this.orderSuccess?.user.billingAddress.city;
      this.billingZipCode = this.orderSuccess?.user.billingAddress.zipCode;
    }
  }

  getImage(product: Product): string | undefined {
    if (product.imageUrl) {
      return 'data:image/png;base64,' + product.imageUrl
    }
    return 'https://upload.wikimedia.org/wikipedia/en/3/30/Java_programming_language_logo.svg';
  }
}
