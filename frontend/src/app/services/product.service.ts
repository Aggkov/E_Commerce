import {Injectable} from '@angular/core';
import {map, Observable, tap} from "rxjs";
import {Product} from "../model/product";
import {HttpClient} from "@angular/common/http";
import {ProductCategory} from "../model/product-category";

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private baseUrl = 'http://localhost:8080/api/products';

  constructor(private httpClient: HttpClient) {
  }

  getProductsByCategoryPaginated(page: number,
                                 pageSize: number,
                                 theCategoryId: number): Observable<GetResponseProducts> {
    const searchUrl = `${this.baseUrl}/category/${theCategoryId}?`
      + `&page=${page}&size=${pageSize}`;
    // return this.httpClient.get<Product[]>(this.baseUrl); // THIS IS SUFFICIENT IF JSON RETURNED IS SIMPLE

    return this.httpClient.get<GetResponseProducts>(searchUrl);
    // .pipe(
    //   // Log the response
    // tap(response => console.log('API response:', response)),
    // map(response => response.content)
    // );
  }

  searchProductByKeywords(searchQuery: string,
                          // limit: number
  ): Observable<Product[]> {
    const searchUrl = `${this.baseUrl}/search?keywords=${searchQuery}

    `;
    console.log("search query is: " + searchQuery);
    return this.httpClient.get<Product[]>(searchUrl).pipe(
      tap(response => console.log("getproductsbyquery:  ", response)),
      map(response => response)
    )
  }

  searchProductByKeywordsPaginated(page: number,
                                   pageSize: number,
                                   searchQuery: string): Observable<GetResponseProducts> {
    const searchUrl = `${this.baseUrl}/search/paginated?keywords=${searchQuery}`
      + `&page=${page}&size=${pageSize}`;
    return this.httpClient.get<GetResponseProducts>(searchUrl)
    // .pipe(
    // tap(response => console.log("getproductsbyquery:  ", response)),
    // map(response => response.content)
    // )
  }

  getProductById(theProductId: number): Observable<Product> {
    // need to build URL based on product id
    const productUrl = `${this.baseUrl}/${theProductId}`;

    return this.httpClient.get<Product>(productUrl).pipe(
      tap(response => console.log(response))
    )
  }
}

// defines the structure of the response data expected from the backend API.
// json will be parsed to an embedded JavaScript object
interface GetResponseProducts {
  content: Product[];
  size: number,
  page: number,
  totalElements: number,
  totalPages: number
}

// interface ProductSuggestions {
//   name: string;
// }
