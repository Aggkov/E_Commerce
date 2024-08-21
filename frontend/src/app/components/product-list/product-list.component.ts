import {Component, OnInit} from '@angular/core';
import {Product} from "../../model/product";
import {ProductService} from "../../services/product.service";
import {CurrencyPipe, NgForOf, NgIf, NgOptimizedImage} from "@angular/common";
import {ActivatedRoute, RouterLink, RouterLinkActive} from "@angular/router";
import {NgbPagination} from "@ng-bootstrap/ng-bootstrap";
import {FormsModule} from "@angular/forms";
import {CartItem} from "../../model/cart-item";
import {CartService} from "../../services/cart.service";

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [
    NgIf,
    NgForOf,
    NgOptimizedImage,
    CurrencyPipe,
    RouterLink,
    RouterLinkActive,
    NgbPagination,
    FormsModule
  ],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.css'
})
export class ProductListComponent implements OnInit {

  products: Product[] = [];
  currentCategoryId: number = 1;
  previousCategoryId: number = 1;
  currentCategoryName: string = "";
  previousSearchQuery: string = "";
  searchQuery: string = "";
  // new properties for pagination
  pageNumber: number = 1;
  pageSize: number = 5;
  totalElements: number = 0;
  totalPages: number = 0;
  pageSizes : number[] = [2,5,10,20,50];

  constructor(private productService: ProductService,
              // current active route that loaded the component
              // need it to access route params
              private cartService : CartService,
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    // console.log(`ProductListComponent started`)
    this.route.paramMap.subscribe(() => {
      this.listProducts();
    });
  }

  // Callbacks are executed based on observable events: When the observable emits a new value,
  // the next callback is called. If there's an error, the error callback is called.
  // When the observable completes, if a complete callback is provided, it is executed.
  listProducts() {
    const queryValue = this.route.snapshot.paramMap.get('query');
    const hasSearchQuery = queryValue !== null && queryValue.trim() !== '';
    if (hasSearchQuery) {
      this.showSearchedResults()
    } else {
      this.showProductListByCategory();
    }
  }

  private showSearchedResults() {
    this.searchQuery = this.route.snapshot.paramMap.get('query')!;

    // if we have a different keyword than previous
    // then set thePageNumber to 1
    if (this.previousSearchQuery != this.searchQuery) {
      this.pageNumber = 1;
    }
    this.previousSearchQuery = this.searchQuery;

    this.productService.searchProductByKeywordsPaginated(
      this.pageNumber - 1,
      this.pageSize,
      this.searchQuery).subscribe({
        next: data => {
          this.products = data.content;
          this.pageNumber = data.page + 1;
          this.pageSize = data.size;
          this.totalElements = data.totalElements;
        },
        error: error => {
          console.error('Error finding products:', error);
        }
      }
    );
  }

  private showProductListByCategory() {
    // check if "id" parameter is available
    const hasCategoryId: boolean = this.route.snapshot.paramMap.has('id');

    if (hasCategoryId) {
      // get the "id" param string. convert string to a number using the "+" symbol
      this.currentCategoryId = +this.route.snapshot.paramMap.get('id')!;
      this.currentCategoryName = this.route.snapshot.paramMap.get('name')!;

    } else {
      // not category id available ... default to category id 1
      this.currentCategoryId = 1;
      this.currentCategoryName = 'Books';
    }

    // if we have a different category id than previous
    // then set thePageNumber back to 1
    if (this.previousCategoryId != this.currentCategoryId) {
      this.pageNumber = 1;
    }

    this.previousCategoryId = this.currentCategoryId;

    // console.log(`currentCategoryId=${this.currentCategoryId}, thePageNumber=${this.pageNumber}`);

    // object is passed to subscribe with next and error properties
    /*
    When you call subscribe:
    The observable begins its process, such
    as making an HTTP request or
    listening for user events.
    Event Handling: As the observable emits values, errors,
    or completes, the corresponding callback functions (next, error, and complete)
    are called based on what happens.
    Subscription Management: The subscribe method returns a Subscription
    object that you can use to manage the subscription (e.g., to unsubscribe if necessary).
     */
    // angular pages are 1 based, spring 0 based
    this.productService.getProductsByCategoryPaginated(this.pageNumber - 1, this.pageSize,
      this.currentCategoryId).subscribe({
      next: data => {
        this.products = data.content;
        this.pageNumber = data.page + 1;
        this.pageSize = data.size;
        this.totalElements = data.totalElements;
      },
      error: error => {
        console.error('Error fetching products:', error);
      },
    })
  }

  onPageSizeChange() {
    this.pageNumber = 1;  // Reset to the first page
    this.listProducts();  // Reload the products, refresh page
  }

  addToCart(product: Product) {
    // console.log(`adding to cart ${product.name} ${product.unitPrice}`);
    const cartItem = new CartItem(product);
    this.cartService.addToCartOrIncrementQuantity(cartItem)
  }
}
