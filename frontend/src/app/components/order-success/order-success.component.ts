import {Component, OnInit} from '@angular/core';
import {OrderResponse} from "../../services/checkout.service";
import {Router} from "@angular/router";
import {NgForOf, NgIf, NgOptimizedImage} from "@angular/common";
import {OrderItem} from "../../model/order-item";

@Component({
  selector: 'app-order-success',
  standalone: true,
  imports: [
    NgIf,
    NgForOf,
    NgOptimizedImage
  ],
  templateUrl: './order-success.component.html',
  styleUrl: './order-success.component.css'
})
export class OrderSuccessComponent implements OnInit {
  orderResponse: OrderResponse | undefined;
  customerEmail: string | undefined;
  orderTrackingNumber: string | undefined;
  orderItems: OrderItem[] = [];
  imageUrl: string | undefined;
  shippingStreet: string | undefined;
  shippingCity: string | undefined;
  shippingZipCode: string | undefined;
  billingStreet: string | undefined;
  billingCity: string | undefined;
  billingZipCode: string | undefined;

  constructor(private router: Router) {}

  ngOnInit(): void {
    // const navigation = this.router.getCurrentNavigation();
    // Access state using history.state instead of getCurrentNavigation
    this.orderResponse = history.state.orderResponse;
    if (this.orderResponse) {
      // this.orderResponse = navigation.extras.state['orderResponse'];
      console.log('Order Response:', this.orderResponse); // Debugging line
    } else {
      console.log('History state is undefined');
    }

    if (this.orderResponse) {
      this.customerEmail = this.orderResponse?.user.email;
      this.orderTrackingNumber = this.orderResponse?.orderTrackingNumber;
      this.orderItems = this.orderResponse?.orderItems || [];
      this.shippingStreet = this.orderResponse?.user.shippingAddress.street;
      this.shippingCity = this.orderResponse?.user.shippingAddress.city;
      this.shippingZipCode = this.orderResponse?.user.shippingAddress.zipCode;
      this.billingStreet = this.orderResponse?.user.billingAddress.street;
      this.billingCity = this.orderResponse?.user.billingAddress.city;
      this.billingZipCode = this.orderResponse?.user.billingAddress.zipCode;
    }
  }
}
