import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {ProductCategory} from "../model/product-category";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class ProductCategoryService {

  private baseUrl = 'http://localhost:8080/api/product-category'

  constructor(private httpClient: HttpClient) { }

  getProductCategories(): Observable<ProductCategory[]> {
    return this.httpClient.get<ProductCategory[]>(this.baseUrl);
    // .pipe(
    // map(response => response)
    // );
  }
}

interface GetResponseProductCategory {
  content: ProductCategory[];
}

