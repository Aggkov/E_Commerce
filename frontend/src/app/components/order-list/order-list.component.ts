import {Component, OnInit} from '@angular/core';
import {Order} from "../../interfaces/order/order";
import {OrderService} from "../../services/order.service";
import {MatPaginator, PageEvent} from "@angular/material/paginator";

@Component({
  selector: 'app-order-list',
  standalone: true,
  imports: [
    MatPaginator
  ],
  templateUrl: './order-list.component.html',
  styleUrl: './order-list.component.css'
})
export class OrderListComponent implements OnInit {

  orders: Order[] = [];
  pageNumber: number = 0;
  pageSize: number = 10;
  totalElements: number = 0;
  pageSizes: number[] = [2, 5, 10, 20, 50];

  constructor(private orderService: OrderService) {
  }

  ngOnInit(): void {
    this.listOrders();
  }

  private listOrders() {
    this.orderService.getOrdersByUser(this.pageNumber, this.pageSize)
      .subscribe({
      next: data => {
        this.orders = data.content;
        this.pageNumber = data.page;
        this.pageSize = data.size;
        this.totalElements = data.totalElements;
      },
        error: err => console.log(err)
    });
  }

  onPageChange(event: PageEvent) {
    if(event.pageSize !== this.pageSize && this.pageNumber !== 0) {
      this.pageNumber = 0;
      this.pageSize = event.pageSize;
      this.listOrders();
    }
    else {
      this.pageNumber = event.pageIndex;   // Update current page index
      this.pageSize = event.pageSize;      // Update current page size
      // Fetch data with updated page info
      this.listOrders();
    }
  }
}
