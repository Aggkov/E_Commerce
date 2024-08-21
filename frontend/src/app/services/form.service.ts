import { Injectable } from '@angular/core';
import {map, Observable, of, tap} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {Country} from "../model/country";
import {State} from "../model/state";

@Injectable({
  providedIn: 'root'
})
export class FormService {

  private countriesUrl = 'http://localhost:8080/api/countries';
  private statesUrl = 'http://localhost:8080/api/states';

  constructor(private httpClient: HttpClient) { }

  getAllCountries(): Observable<Country[]> {
    return this.httpClient.get<Country[]>(this.countriesUrl).pipe(
      // tap(response => console.log(response))
    );
  }

  getStatesByCountryCode(theCountryCode: string): Observable<State[]> {
    // search url
    const searchStatesUrl = `${this.statesUrl}/country/${theCountryCode}`;
    return this.httpClient.get<State[]>(searchStatesUrl).pipe(
      // tap(response => console.log(response))
    );
  }

  // getCreditCardMonths(startMonth: number): Observable<number[]> {
  //
  //   let data: number[] = [];
  //
  //   // build an array for "Month" dropdown list
  //   // - start at current month and loop until
  //
  //   for (let theMonth = startMonth; theMonth <= 12; theMonth++) {
  //     data.push(theMonth);
  //   }
  //
  //   return of(data);
  // }
  //
  // getCreditCardYears(): Observable<number[]> {
  //
  //   let data: number[] = [];
  //
  //   // build an array for "Year" downlist list
  //   // - start at current year and loop for next 10 years
  //
  //   const startYear: number = new Date().getFullYear();
  //   const endYear: number = startYear + 10;
  //
  //   for (let theYear = startYear; theYear <= endYear; theYear++) {
  //     data.push(theYear);
  //   }
  //   // wrap array into an observable
  //   return of(data);
  // }
}
