import {Injectable} from '@angular/core';
import {map, Observable, pipe, tap} from "rxjs";
import {Product} from "../model/product";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private baseUrl = 'http://localhost:8080/api/v1/products';

  constructor(private httpClient: HttpClient) {
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
    const searchUrl = `${this.baseUrl}/search?keywords=${searchQuery}

    `;
    console.log("search query is: " + searchQuery);
    return this.httpClient.get<Product[]>(searchUrl).pipe(
      // tap(response => console.log("getproductsbyquery:  ", response)),
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
      // .pipe(
      // tap(response => console.log(response))
  }

  // Function to get filtered products from the backend
  getFilteredProducts(filterCriteria: any,
                      categoryId: string,
                      page: number,
                      pageSize: number): Observable<GetResponseProducts> {
    // Convert selected name filters (those set to true) to an array of keys
    const selectedNameFilters = Object.keys(filterCriteria.nameFilters)
      .filter(key => filterCriteria.nameFilters[key])
      .join(','); // Convert the keys to a comma-separated string

    return this.httpClient.get<GetResponseProducts>(`${this.baseUrl}/filter`, {
      params: {
        categoryId: categoryId,
        min_price: filterCriteria.priceFrom,
        max_price: filterCriteria.priceTo,
        price_range: filterCriteria.selectedPriceRange,
        name_filters: selectedNameFilters, // Include selected name filters in the request
        page: page,
        size: pageSize
      }
    }).pipe(
      // tap(data => console.log(`filtered products are ${JSON.stringify(data.content)}`))
    );
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

