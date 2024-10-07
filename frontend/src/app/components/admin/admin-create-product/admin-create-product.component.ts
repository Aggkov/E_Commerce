import { Component } from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ProductService} from "../../../services/product.service";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-admin-create-product',
  standalone: true,
  imports: [
    NgIf,
    FormsModule,
    RouterLink,
    NgForOf,
    ReactiveFormsModule
  ],
  templateUrl: './admin-create-product.component.html',
  styleUrl: './admin-create-product.component.css'
})
export class AdminCreateProductComponent {

  product = {
    sku: '',
    name: '',
    unitPrice: '',
    unitsInStock: '',
    description: '',
    categoryName: '',
    imageUrl: ''
  };

  categories = ['Books', 'Coffee Mugs','Mouse Pads', 'Luggage Tags'];
  imageFile: File | null = null;

  constructor(private productService: ProductService) {
  }

  onFileSelected(event: any) {
    const file: any = event?.target?.files[0];
    if (file) {
      this.imageFile = file;
      // You can also set the image URL to send it to the backend if needed
      // this.product.imageUrl = `assets/images/products/${file.name}`;
    }
  }

  onSubmit() {
    if (this.product) {
      // console.log('Form Submitted:', this.product);
      const formData = new FormData();

      // Append product details as JSON
      const newProduct = {
        sku: this.product.sku,
        name: this.product.name,
        unitPrice: this.product.unitPrice,
        unitsInStock: this.product.unitsInStock,
        description: this.product.description,
        categoryName: this.product.categoryName,
      };
      formData.append('product', new Blob([JSON.stringify(newProduct)], { type: 'application/json' }));

      // Append image file
      if (this.imageFile) {
        formData.append('image', this.imageFile);
      }

      // Send the FormData to the backend
      this.productService.createProduct(formData)
        .subscribe(response => {
          console.log('Product created:', response);
        });

    }
  }
}
