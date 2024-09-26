import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
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

  @Input() selectedCategory!: string;
  @Output() filterCriteria = new EventEmitter<FilterCriteria>();

  filterFormGroup: FormGroup; // Reactive Form
  priceFrom!: string;
  priceTo!: string;

  // Checkbox filters for book names as an object
  nameFilters = {
    java: false,
    javascript: false,
    python: false,
    vue: false,
    csharp: false,
    sql: false,
  };

  constructor(private fb: FormBuilder) {
    // Initialize the reactive form
    this.filterFormGroup = this.fb.group({
      priceFrom: [''], // Default empty values
      priceTo: [''],
      selectedPriceRange: [''],
    });
  }

  ngOnInit(): void {
    // Listen for changes to the selected price range and update inputs accordingly
    this.filterFormGroup.get('selectedPriceRange')?.valueChanges.subscribe((value) => {
      this.updatePriceRange(value);
    });
  }

  // Method to update price inputs when a radio button is selected
  updatePriceRange(value: string) {
    if (value) {
      const [min, max] = value.split('-').map(Number);
      this.filterFormGroup.patchValue({
        priceFrom: !isNaN(min) ? min.toString() : '', // Set min value if exists
        priceTo: !isNaN(max) ? max.toString() : '', // Set max value if exists, else leave blank
      });
    }
  }

  // Method to handle filtering based on user input
  applyFilter() {
    // Extract price values from form controls
    this.priceFrom = this.filterFormGroup.controls['priceFrom'].value.trim();
    this.priceTo = this.filterFormGroup.controls['priceTo'].value.trim();

    let finalNumberFrom = '';
    let finalNumberTo = '';

    // Extract valid digits from priceFrom
    for (let i = 0; i < this.priceFrom.length; i++) {
      let digit = this.priceFrom[i];
      if (!isNaN(Number(digit)) && digit !== ' ') {
        finalNumberFrom += digit;
      }
    }
    this.priceFrom = finalNumberFrom === '' || isNaN(Number(finalNumberFrom)) ? '10' : finalNumberFrom;

    // Extract valid digits from priceTo
    for (let i = 0; i < this.priceTo.length; i++) {
      let digit = this.priceTo[i];
      if (!isNaN(Number(digit)) && digit !== ' ') {
        finalNumberTo += digit;
      }
    }
    this.priceTo = finalNumberTo === '' || isNaN(Number(finalNumberTo)) ? '15' : finalNumberTo;

    // Check if priceTo is less than priceFrom and swap if necessary
    if (!isNaN(Number(this.priceFrom)) && !isNaN(Number(this.priceTo)) && Number(this.priceTo) < Number(this.priceFrom)) {
      [this.priceFrom, this.priceTo] = [this.priceTo, this.priceFrom];
    }

    // Update form controls with the potentially swapped values
    this.filterFormGroup.patchValue({
      priceFrom: this.priceFrom,
      priceTo: this.priceTo,
    });

    // Prepare the filter criteria object
    const filterCriteria: FilterCriteria = {
      priceFrom: this.priceFrom,
      priceTo: this.priceTo,
      priceRange: this.filterFormGroup.controls['selectedPriceRange'].value,
      nameFilters: this.nameFilters,
    };
    // Emit the filter criteria
    this.filterCriteria.emit(filterCriteria);
  }
}

export interface NameFilters {
  [key: string]: boolean; // Index signature allows string keys
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
  priceRange: string;
  nameFilters: NameFilters;
}

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


