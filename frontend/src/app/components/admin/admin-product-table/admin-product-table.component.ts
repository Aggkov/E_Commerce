import {Component, OnInit, ViewChild} from '@angular/core';
import {Product} from "../../../model/product";
import {ProductService} from "../../../services/product.service";
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow, MatHeaderRowDef,
  MatRow, MatRowDef, MatTable,
  MatTableDataSource
} from "@angular/material/table";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {CurrencyPipe, NgIf} from "@angular/common";

@Component({
  selector: 'app-admin-product-table',
  standalone: true,
  imports: [
    MatPaginator,
    MatHeaderRow,
    MatRow,
    MatColumnDef,
    MatHeaderCell,
    MatHeaderCellDef,
    MatCellDef,
    MatCell,
    MatHeaderRowDef,
    MatRowDef,
    MatTable,
    NgIf,
    CurrencyPipe,
    MatSort
  ],
  templateUrl: './admin-product-table.component.html',
  styleUrl: './admin-product-table.component.css'
})
export class AdminProductTableComponent implements OnInit {
  // products: Product[] = [];
  displayedColumns: string[] = ['sku', 'name', 'unitPrice', 'active', 'unitsInStock', 'unitsSold'];
  dataSource = new MatTableDataSource<Product>();
  pageNumber: number = 0;
  pageSize: number = 10;
  totalElements: number = 0;
  pageSizes: number[] = [2, 5, 10, 20, 50];  // To hold the total number of elements from the server

  // @ViewChild: This captures the reference to the <mat-paginator> element from your template
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  // This captures the reference to the <mat-sort> directive
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private productService: ProductService) {
  }

  ngOnInit(): void {
    // this.dataSource.paginator = this.paginator;
    this.listProducts();
    // Load first page with default size of 10
  }

  listProducts() {
    this.productService.getAllProducts(this.pageNumber, this.pageSize)
      .subscribe({
        next: response => {
          this.dataSource.data = response.content;
          this.pageNumber = response.page;
          this.pageSize = response.size;
          // this.paginator.pageIndex = response.page;  // Ensure paginator index sync
          this.totalElements = response.totalElements;
          this.paginator.length = response.totalElements;  // Update paginator with total products

        },
        error: error => {
          console.error('Error finding products:', error);
        }
      });
  }

  // ngb pagination needs [()] for pageNumber and pageSize to send and receive values (html - ts)
  // mat pagination has [] only but (page): pageEvent sends new data to ts via event to populate new values inside function
  onPageChange(event: PageEvent) {
    // if pageSize changes and user is not at first page, turn back to page 0 to show products from start
    if(event.pageSize !== this.pageSize && this.pageNumber !== 0) {
      this.pageNumber = 0;
      this.pageSize = event.pageSize;
      this.listProducts();
    }
    else {
      this.pageNumber = event.pageIndex;   // Update current page index
      this.pageSize = event.pageSize;      // Update current page size
      // Fetch data with updated page info
      this.listProducts();
    }
  }

  /*
  ngAfterViewInit() is a lifecycle hook in Angular. It's called after Angular
  has fully initialized the view of the component, including its child components
  and directives. This is especially important when working with components like MatPaginator or MatSort
  because they might not be available until the view has been fully rendered.
   */
  // ngAfterViewInit() {
  //   // subscribing to the page event of the MatPaginator
  //   // This event is emitted when the user interacts with the paginator
  //   this.paginator.page.subscribe(() => {
  //     this.pageNumber = this.paginator.pageIndex;  // Update page number (0-based)
  //     this.pageSize = this.paginator.pageSize;     // Update page size
  //     // fetch products after event emitted
  //     this.getAllProducts(this.pageNumber, this.pageSize); // Fetch new page
  //   });
  // }
}
