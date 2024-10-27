import {Component, OnInit} from '@angular/core';
import {Order} from "../../interfaces/order/order";
import {ActivatedRoute} from "@angular/router";
import {OrderService} from "../../services/order.service";
import {Product} from "../../interfaces/product";
import {AsyncPipe, DatePipe, DecimalPipe, NgForOf, NgIf} from "@angular/common";
import {Observable, of} from "rxjs";

@Component({
  selector: 'app-order-details',
  standalone: true,
  imports: [
    DecimalPipe,
    NgForOf,
    NgIf,
    DatePipe,
    AsyncPipe
  ],
  templateUrl: './order-details.component.html',
  styleUrl: './order-details.component.css'
})
export class OrderDetailsComponent implements OnInit {

  order$!: Observable<Order>;

  constructor(private orderService: OrderService,
              private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(() => {
      this.handleOrderDetails();
    })
  }

  private handleOrderDetails() {
    const orderTrackingNumber: string = this.route.snapshot.paramMap.get('orderTracking')!;
    if (orderTrackingNumber) {
      this.order$ = this.orderService.getOrderByOrderTrackingNumber(orderTrackingNumber);
    }
  }

  getImage(product: Product): string {
    if (product.imageUrl) {
      return 'data:image/png;base64,' + product.imageUrl
    }
    return 'https://upload.wikimedia.org/wikipedia/en/3/30/Java_programming_language_logo.svg';
  }
}
