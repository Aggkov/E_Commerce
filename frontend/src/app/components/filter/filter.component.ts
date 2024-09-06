import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {ProductService} from "../../services/product.service";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-filter',
  standalone: true,
  imports: [
    FormsModule,
    NgIf
  ],
  templateUrl: './filter.component.html',
  styleUrl: './filter.component.css'
})
export class FilterComponent implements OnInit {

  priceFrom!: string;
  priceTo!: string;
  selectedPriceRange: string = '';
  @Input() selectedCategory!: string
  @Input() categoryId!: string;

  // Checkbox filters for book names
  nameFilters = {
    java: false,
    javascript: false,
    python: false,
    vue: false,
    csharp: false,
    sql: false
  };

  // Emit filtered products to parent (Product List)
  @Output() filteredProducts = new EventEmitter<any[]>();

  constructor(private productService: ProductService,) {}

  ngOnInit(): void {

  }

  // Mock method to handle filtering based on user input
  applyFilter() {
    const filterCriteria = {
      priceFrom: this.priceFrom,
      priceTo: this.priceTo,
      priceRange: this.selectedPriceRange,
      nameFilters: this.nameFilters
    };
    console.log('Applied Filters:', filterCriteria);
    // Fetch filtered products from backend
    this.productService.getFilteredProducts(filterCriteria, this.categoryId)
      .subscribe({
        next: data => this.filteredProducts.emit(data.content),
        error: err => console.log(err)
      });

      // Emit the filtered products back to the Product List component
      // this.filteredProducts.emit(products);
    // });
  }
}
