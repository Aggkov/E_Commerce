import {Injectable} from '@angular/core';
import {map, Observable, pipe, tap} from "rxjs";
import {Product} from "../model/product";
import {HttpClient} from "@angular/common/http";
import {FilterCriteria} from "../components/filter/filter.component";

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private baseUrl = 'http://localhost:8080/api/v1/products';

  constructor(private httpClient: HttpClient) {
  }

  getAllProducts(page:number,
                 pageSize: number) : Observable<GetResponseProducts> {
    const searchUrl = `${this.baseUrl}?page=${page}&size=${pageSize}`;
    return this.httpClient.get<GetResponseProducts>(searchUrl);
  }

  getProductsByCategoryIdPaginated(page: number,
                                   pageSize: number,
                                   categoryId: string): Observable<GetResponseProducts> {
    const searchUrl = `${this.baseUrl}/category/${categoryId}?`
      + `page=${page}&size=${pageSize}`;

    return this.httpClient.get<GetResponseProducts>(searchUrl)
    // .pipe(tap(response =>
    //   console.log('API response:', response)));
  }

  searchProductByKeywords(searchQuery: string,
                          // limit: number
  ): Observable<Product[]> {
    const searchUrl = `${this.baseUrl}/search?keywords=${searchQuery}`;
    return this.httpClient.get<Product[]>(searchUrl).pipe(
      map(response => response)
    )
  }

  searchProductByKeywordsPaginated(page: number,
                                   pageSize: number,
                                   searchQuery: string): Observable<GetResponseProducts> {
    const searchUrl = `${this.baseUrl}/search/paginated?keywords=${searchQuery}`
      + `&page=${page}&size=${pageSize}`;
    return this.httpClient.get<GetResponseProducts>(searchUrl)
  }

  getProductById(productId: string): Observable<Product> {
    // need to build URL based on product id
    const productUrl = `${this.baseUrl}/${productId}`;

    return this.httpClient.get<Product>(productUrl);
  }

  // Function to get filtered products from the backend
  getFilteredProducts(filterCriteria: FilterCriteria | null,
                      categoryId: string,
                      page: number,
                      pageSize: number): Observable<GetResponseProducts> {

    // Construct the payload with filter criteria
    const payload = {
      categoryId: categoryId,
      minPrice: filterCriteria?.priceFrom,
      maxPrice: filterCriteria?.priceTo,
      priceRange: filterCriteria?.priceRange,
      nameFilters: Object.keys(filterCriteria?.nameFilters || {}).filter(
        (key) => filterCriteria?.nameFilters[key]
      ), // Convert nameFilters object to an array of selected filters
      page: page,
      size: pageSize,
    };
    return this.httpClient.post<GetResponseProducts>(`${this.baseUrl}/filter`, payload);
  }
}

// defines the structure of the response data expected from the backend API.
// json will be parsed to an embedded JavaScript object
export interface GetResponseProducts {
  content: Product[];
  size: number,
  page: number,
  totalElements: number,
  totalPages: number
}

