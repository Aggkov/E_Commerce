import {Component, EventEmitter, HostListener, OnInit, Output} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {AsyncPipe, NgForOf, NgIf} from "@angular/common";
import {debounceTime, distinctUntilChanged, map, Observable, of, switchMap, tap} from "rxjs";
import {Product} from "../../model/product";
import {ProductService} from "../../services/product.service";
import {ActivatedRoute, Router} from "@angular/router";
import {CartStatusComponent} from "../cart-status/cart-status.component";

@Component({
  selector: 'app-search',
  standalone: true,
  imports: [
    FormsModule,
    NgIf,
    ReactiveFormsModule,
    NgForOf,
    AsyncPipe,
    CartStatusComponent
  ],
  templateUrl: './search.component.html',
  styleUrl: './search.component.css'
})
export class SearchComponent implements OnInit {
  searchForm: FormGroup;
  searchQuery: string = '';
  suggestions$: Observable<Product[]> = of([]);
  dropdownVisible = false;
  // limit: number = 10;

  constructor(
    private fb: FormBuilder,
    private productService: ProductService, private router: Router) {
    // this.searchForm = new FormGroup({
    //   searchQuery: new FormControl('', [Validators.required])
    // });
    this.searchForm = this.fb.group({
      searchQuery: ['']
    });
  }

  ngOnInit(): void {
    this.suggestions$ = this.searchForm.get('searchQuery')!.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      // switchMap(query => this.productService.getSuggestions(query))
      switchMap(query => {
        if (query.trim() === "") {
          this.dropdownVisible = false; // Hide dropdown if query is empty
          // Return an observable of an empty array if the query is empty
          return of([]);
        } else {
          this.dropdownVisible = true; // Show dropdown if there is a query
          return this.productService.searchProductByKeywords(query);
        }
      }),
      tap(suggestions => console.log("Suggestions:", suggestions, this.dropdownVisible))
    );

    // this.suggestions$ = this.observeSearchQuery().pipe(
    //   debounceTime(300),
    //   distinctUntilChanged(),
    //   switchMap(query => {
    //     if (query.trim() === "") {
    //       this.dropdownVisible = false; // Hide dropdown if query is empty
    //       return of([]); // Return an observable of an empty array if the query is empty
    //     } else {
    //       this.dropdownVisible = true; // Show dropdown if there is a query
    //       return this.productService.searchProductByKeywords(query
    //         // , this.limit
    //       );
    //     }
    //   }),
    //   // tap(suggestions => console.log("Suggestions:", suggestions, this.dropdownVisible))
    // );
  }

  // observeSearchQuery(): Observable<string> {
  //   return new Observable(observer => {
  //     Object.defineProperty(this, 'searchQuery', {
  //       get: () => this._searchQuery,
  //       set: value => {
  //         this._searchQuery = value;
  //         observer.next(value);
  //       }
  //     });
  //   });
  // }

  onSearch(): void {
    // if (this.searchForm.valid) {
    const query = this.searchForm.get('searchQuery')!.value;
    this.router.navigate(['/search', query]);
    // this.router.navigate(['/search', this.searchQuery]);
    // }
  }

  selectSuggestion(suggestion: Product): void {
    this.searchForm.get('searchQuery')!.setValue(suggestion.name);
    // this.suggestions$ = new Observable<Product[]>(); // Clear suggestions
    // this.searchQuery = suggestion.name;
    this.onSearch(); // Optionally perform search on suggestion select
  }

  @HostListener('document:click', ['$event'])
  onClick(event: Event): void {
    const target = event.target as HTMLElement;
    if (!target.closest('.ul')) {
      this.dropdownVisible = false; // Hide dropdown if click is outside
    }
  }

  // private _searchQuery: string = '';
}
