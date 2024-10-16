import {Component, OnInit} from '@angular/core';
import {Product} from "../../model/product";
import {ProductService} from "../../services/product.service";
import {CurrencyPipe, NgForOf, NgIf, NgOptimizedImage} from "@angular/common";
import {ActivatedRoute, NavigationEnd, Router, RouterLink, RouterLinkActive} from "@angular/router";
import {NgbPagination} from "@ng-bootstrap/ng-bootstrap";
import {FormsModule} from "@angular/forms";
import {CartItem} from "../../model/cart-item";
import {CartService} from "../../services/cart.service";
import {ProductCategoryService} from "../../services/product-category.service";
import {map} from "rxjs";
import {ProductCategoryMenuComponent} from "../product-category-menu/product-category-menu.component";
import {FilterComponent, FilterCriteria} from "../filter/filter.component";
import {environment} from "../../../enviroments/enviroment";

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
    FormsModule,
    ProductCategoryMenuComponent,
    FilterComponent
  ],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.css'
})
export class ProductListComponent implements OnInit {

  products: Product[] = [];
  currentCategoryName: string = "";
  currentCategoryId: string = '';
  previousCategoryId: string = '';
  previousSearchQuery: string = "";
  currentSearchQuery: string = "";

  // new properties for pagination
  pageNumber: number = 1;
  pageSize: number = 5;
  totalElements: number = 0;
  // totalPages: number = 0;
  pageSizes: number[] = [2, 5, 10, 20, 50];

  productCategoryIds: string[] = [];
  showCategoryMenu: boolean = true;
  showFilterMenu: boolean = false;
  isFiltered: boolean = false; // Flag to track whether filtering is applied

  filterCriteria: FilterCriteria | null = null; // Use null when no filter is applied

  // getImageUrl(product: any): string {
  //   return `${environment.backendUrl}${product.imageUrl}`;
  // }

  constructor(private productService: ProductService,
              private productCategoryService: ProductCategoryService,
              // current active route that loaded the component
              // need it to access route params
              private cartService: CartService,
              private activeRoute: ActivatedRoute,
              private router: Router) {
  }

  ngOnInit(): void {
    // Subscribe to NavigationEnd to trigger refresh when navigating to the same route
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd && event.urlAfterRedirects === '/products') {
        this.listProducts();
      }
    });
    this.productCategoryService.getProductCategories().pipe(
      // tap(response => console.log('Response:', response)),
      map(response => {
        this.productCategoryIds = response.map(category => category.id);
        // console.log('Category IDs:', this.productCategoryIds);
        return this.productCategoryIds;
      })
    ).subscribe({
        next: () => {
          this.activeRoute.paramMap.subscribe(() => {
            this.listProducts();
          });
        },
        error: err => {
          console.error('Error fetching product categories', err);
        }
      }
    )
  }

  // Callbacks are executed based on observable events: When the observable emits a new value,
  // the next callback is called. If there's an error, the error callback is called.
  // When the observable completes, if a complete callback is provided, it is executed.
  listProducts() {
    const queryValue = this.activeRoute.snapshot.paramMap.get('query');
    const hasSearchQuery = queryValue !== null && queryValue.trim() !== '';

    if (hasSearchQuery) {
      this.isFiltered = false;  // Reset isFiltered if a search is done
      this.showSearchedResults();
    } else if (this.isFiltered && this.filterCriteria) {
      this.showFilteredProducts();
    } else {
      this.isFiltered = false;  // Reset isFiltered if a search is done
      this.showProductListByCategory();
    }
  }

  private showSearchedResults() {
    this.currentSearchQuery = this.activeRoute.snapshot.paramMap.get('query')!;

    if (this.previousSearchQuery != this.currentSearchQuery) {
      this.pageNumber = 1;
    }
    this.previousSearchQuery = this.currentSearchQuery;

    this.productService.searchProductByKeywordsPaginated(
      this.pageNumber - 1,
      this.pageSize,
      this.currentSearchQuery).subscribe({
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
    const hasCategoryId: boolean = this.activeRoute.snapshot.paramMap.has('id');
    // console.log('inside showProductListByCategory Category IDs:', this.productCategoryIds);
    if (hasCategoryId) {
      this.currentCategoryId = this.activeRoute.snapshot.paramMap.get('id')!;
      this.currentCategoryName = this.activeRoute.snapshot.paramMap.get('name')!;
    } else {
      // not category id available ... default to first category id from array
      this.currentCategoryId = this.productCategoryIds[0];
      this.currentCategoryName = 'Books';
    }

    // if we have a different category id than previous
    // then set thePageNumber back to 1
    if (this.previousCategoryId != this.currentCategoryId) {
      this.pageNumber = 1;
    }
    this.previousCategoryId = this.currentCategoryId;

    // object is passed to subscribe with next and error properties
    /*
    When you call subscribe: The observable begins its process, such as making an HTTP request or listening for user events.
    Event Handling: As the observable emits values, errors, or completes, the corresponding callback functions (next, error, and complete)
    are called based on what happens. Subscription Management: The subscribe method returns a Subscription object that you can use to manage the subscription (e.g., to unsubscribe if necessary).
    */
    // angular pages are 1 based, spring 0 based
    this.productService.getProductsByCategoryIdPaginated(
      this.pageNumber - 1,
      this.pageSize,
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

  handleFilteredProducts(filterCriteria: any) {
    // Set the filter criteria and flag that we are in filtered mode
    this.filterCriteria = filterCriteria;
    this.isFiltered = true;
    this.pageNumber = 1; // Reset pagination
    this.listProducts();
  }

  private showFilteredProducts() {
    this.productService.getFilteredProducts(
      this.filterCriteria,
      this.currentCategoryId,
      this.pageNumber - 1,
      this.pageSize)
      .subscribe({
        next: data => {
          this.products = data.content;
          this.pageNumber = data.page + 1;
          this.pageSize = data.size;
          this.totalElements = data.totalElements;
        },
        error: err => console.log(err)
      });
    this.isFiltered = false;
  }

  onPageSizeChange() {
    this.pageNumber = 1;
    this.listProducts();  // Reload the products, refresh page
  }

  addToCart(product: Product) {
    // console.log(`adding to cart ${product.name} ${product.unitPrice}`);
    const cartItem = new CartItem(product);
    this.cartService.addToCartOrIncrementQuantity(cartItem)
  }

  toggleCategoryMenu() {
    this.showCategoryMenu = !this.showCategoryMenu;
    if (this.showCategoryMenu) {
      this.showFilterMenu = false;
      this.isFiltered = false;  // Reset filtering
      this.filterCriteria = null; // Clear filter criteria
      this.listProducts();
    }
  }

  // Function to toggle filter menu visibility
  toggleFilterMenu() {
    this.showFilterMenu = !this.showFilterMenu;

    // Optionally, close category menu if open
    if (this.showFilterMenu) {
      this.showCategoryMenu = false;
    }
  }

  getImage(product: Product): string | undefined {
    if (product.imageUrl) {
      return 'data:image/png;base64,' + product.imageUrl;
    }
    return 'https://upload.wikimedia.org/wikipedia/en/3/30/Java_programming_language_logo.svg';
  }
}
