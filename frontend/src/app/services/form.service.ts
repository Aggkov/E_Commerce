import { Injectable } from '@angular/core';
import {Observable, of} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class FormService {

  constructor() { }

  getCreditCardMonths(startMonth: number): Observable<number[]> {

    let data: number[] = [];

    // build an array for "Month" dropdown list
    // - start at current month and loop until

    for (let theMonth = startMonth; theMonth <= 12; theMonth++) {
      data.push(theMonth);
    }

    return of(data);
  }

  getCreditCardYears(): Observable<number[]> {

    let data: number[] = [];

    // build an array for "Year" downlist list
    // - start at current year and loop for next 10 years

    const startYear: number = new Date().getFullYear();
    const endYear: number = startYear + 10;

    for (let theYear = startYear; theYear <= endYear; theYear++) {
      data.push(theYear);
    }
    // wrap array into an observable
    return of(data);
  }

  getExpirationDate(dateEntered: string): Observable<string> {
    let monthInput: string = dateEntered.substring(0,2);
    let monthInputNum = parseInt(monthInput);

    let yearInput: string = dateEntered.substring(2);
    let yearInputNum = parseInt(yearInput);
    // get last 2 digits of year ex. 2024 => 24
    let currentYear: string = new Date().getFullYear().toString().substring(2);
    let lastTwoDigitsYearCurrentYear = parseInt(currentYear);
    let result: string = "";

    if(monthInputNum && yearInputNum) {
      if(monthInputNum > 12) {
        monthInput = monthInput.replace(monthInput.charAt(0), '0');
      }
      else {
        result = result + monthInput + '/';
      }
      if(yearInputNum < lastTwoDigitsYearCurrentYear || yearInputNum > lastTwoDigitsYearCurrentYear + 10) {
        return of("")
      } else {
        result = result + yearInput;
      }
    }
    // if() {
    //   if() {
    //     return of("")
    //   }
    // }


    return of();
  }
}
