import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ProductService} from "../../services/product.service";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-filter',
  standalone: true,
  imports: [
    FormsModule,
    NgIf,
    ReactiveFormsModule
  ],
  templateUrl: './filter.component.html',
  styleUrl: './filter.component.css'
})
export class FilterComponent implements OnInit {

  @Input() selectedCategory!: string
  @Output() filterCriteria = new EventEmitter<any>();

  priceFrom!: string;
  priceTo!: string;
  selectedPriceRange: string = '';
  // Checkbox filters for book names
  nameFilters = {
    java: false,
    javascript: false,
    python: false,
    vue: false,
    csharp: false,
    sql: false
  };

  constructor() {
  }

  ngOnInit(): void {
    // this.priceForm = new FormGroup({
    //   priceFrom: new FormControl(''),
    //   priceTo: new FormControl('')
    // });

    // // Listen for changes on the form to check and swap the values if necessary
    // this.priceForm.valueChanges.subscribe((values) => {
    //   const priceFrom = parseFloat(values.priceFrom);
    //   const priceTo = parseFloat(values.priceTo);
    //
    //   // Check if both values are numbers and priceTo is less than priceFrom
    //   if (!isNaN(priceFrom) && !isNaN(priceTo) && priceTo < priceFrom) {
    //     // Swap the values
    //     this.priceForm.setValue({
    //       priceFrom: priceTo.toString(),
    //       priceTo: priceFrom.toString()
    //     }, { emitEvent: false }); // Prevent infinite loop of valueChanges
    //   }
    // });
  }

  // Mock method to handle filtering based on user input
  applyFilter() {
    this.priceFrom = this.priceFrom.trim();
    this.priceTo = this.priceTo.trim();
    let finalNumberFrom = "";
    let finalNumberTo = "";
    // find valid digits inside string
    for (let i = 0; i < this.priceFrom.length; i++) {
      let digit = this.priceFrom[i];
      if (!isNaN(Number(digit)) && digit !== " ") { // Ensure it's not NaN and not a space
        finalNumberFrom += digit;
      }
    }
    // if not found set to default
    if (finalNumberFrom === "" || isNaN(Number(finalNumberFrom))) this.priceFrom = "10";
    else this.priceFrom = finalNumberFrom;

    for (let i = 0; i < this.priceTo.length; i++) {
      let digit = this.priceTo[i];
      if (!isNaN(Number(digit)) && digit !== " ") { // Ensure it's not NaN and not a space
        finalNumberTo += digit;
      }
    }
    if (finalNumberTo === "" || isNaN(Number(finalNumberTo))) this.priceTo = "15";
    else this.priceTo = finalNumberTo;

    // check if priceTo is less than priceFrom
    if (!isNaN(Number(this.priceFrom)) &&
      !isNaN(Number(this.priceTo)) &&
      this.priceFrom < this.priceFrom
    ) {
      // Swap the values
      let tmp = "";
      tmp = this.priceFrom;
      this.priceFrom = this.priceTo;
      this.priceTo = tmp;
    }

    // this.selectedPriceRange.

    const filterCriteria : FilterCriteria = {
      priceFrom: this.priceFrom,
      priceTo: this.priceTo,
      selectedPriceRange: this.selectedPriceRange,
      nameFilters: this.nameFilters
    };
    this.filterCriteria.emit(filterCriteria);
  }
}

export interface NameFilters {
  java: boolean;
  javascript: boolean;
  python: boolean;
  vue: boolean;
  csharp: boolean;
  sql: boolean;
}

export interface FilterCriteria {
  priceFrom: string;
  priceTo: string;
  selectedPriceRange: string;
  nameFilters: NameFilters;
}


