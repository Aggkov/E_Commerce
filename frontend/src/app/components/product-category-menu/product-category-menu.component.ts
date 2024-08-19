import {Component, OnInit} from '@angular/core';
import {ProductCategory} from "../../model/product-category";
import {ProductService} from "../../services/product.service";
import {NgForOf} from "@angular/common";
import {RouterLink, RouterLinkActive} from "@angular/router";
import {ProductCategoryService} from "../../services/product-category.service";

@Component({
  selector: 'app-product-category-menu',
  standalone: true,
  imports: [
    NgForOf,
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './product-category-menu.component.html',
  styleUrl: './product-category-menu.component.css'
})
export class ProductCategoryMenuComponent implements OnInit {

  productCategories: ProductCategory[] = [];

  constructor(private productService: ProductService,
              private productCategoryService: ProductCategoryService) { }

  ngOnInit() {
    this.listProductCategories();
  }

  listProductCategories() {

    this.productCategoryService.getProductCategories().subscribe(
      data => {
        // console.log('Product Categories=' + JSON.stringify(data));
        this.productCategories = data;
      }
    );
  }
}
