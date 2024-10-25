import {Injectable} from '@angular/core';
import {map, Observable, pipe, tap} from "rxjs";
import {Product} from "../interfaces/product";
import {HttpClient} from "@angular/common/http";
import {FilterCriteria} from "../components/filter/filter.component";
import {environment} from "../../enviroments/enviroment";

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private baseUrl = environment.coreServiceUrl + '/products';

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

  getProductByName(productName: string): Observable<Product> {
    // need to build URL based on product id
    const productUrl = `${this.baseUrl}/${productName}`;

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
      nameFilters: Object.keys(filterCriteria?.nameFilters || {})
        .map(key => key === 'csharp' ? 'c#' : key) // Convert 'csharp' to 'c#'
        .filter((key) => filterCriteria?.nameFilters[key] // Only include selected filters
      ),
      page: page,
      size: pageSize,
    };
    return this.httpClient.post<GetResponseProducts>(`${this.baseUrl}/filter`, payload);
  }

  createProduct(formData: FormData) : Observable<Product> {
    return this.httpClient.post<Product>(`${this.baseUrl}/admin`, formData);
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

