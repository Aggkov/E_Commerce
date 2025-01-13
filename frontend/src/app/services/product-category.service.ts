import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {ProductCategory} from "../interfaces/product-category";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../enviroments/enviroment";

@Injectable({
  providedIn: 'root'
})
export class ProductCategoryService {

  private baseUrl = environment.coreContextPath + '/product-category'

  constructor(private httpClient: HttpClient) { }

  getProductCategories(): Observable<ProductCategory[]> {
    return this.httpClient.get<ProductCategory[]>(this.baseUrl);
    // .pipe(
    // map(response => response)
    // );
  }
}

export interface GetResponseProductCategory {
  content: ProductCategory[];
}

