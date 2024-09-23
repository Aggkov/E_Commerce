import {Directive, HostListener, Input} from '@angular/core';
import {FormGroup} from "@angular/forms";

@Directive({
  selector: '[appCreditCardExpirationDateFormat]',
  standalone: true
})
export class CreditCardExpirationDateFormatDirective {

  @Input() checkoutFormGroup!: FormGroup;
  invalidYear: boolean = false;
  invalidMonth: boolean = false;
  currentYear: number = new Date().getFullYear() % 100;
  currentMonth: number = new Date().getMonth() + 1;
  maxYear: number = this.currentYear + 20;
  private _isBackspacePressed = false;

  constructor() {
  }

  @HostListener('input', ['$event']) onExpirationDateInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    // allow only digits
    let userInput = input.value.replace(/\D/g, '');
    let formattedValue = '';
    // Handle backspace
    if(this._isBackspacePressed) return;

    // Skip formatting if backspace was pressed
    if (userInput.length === 1) {
      if (parseInt(userInput[0], 10) > 1) {
        formattedValue = '0' + userInput[0] + '/'; // Pad single digits with '0' if > 1
      } else {
        formattedValue = userInput[0]; // Keep the digit if it's '1'
      }
    }

    if (userInput.length === 2) {
      let month = parseInt(userInput.slice(0, 2), 10);

      if (month > 12) {
        formattedValue = userInput[0]; // Revert to the first digit
      } else {
        formattedValue = userInput.slice(0, 2) + '/'; // Add the slash after a valid month
      }
    }

    if (userInput.length === 3) {
      let month = parseInt(userInput.slice(0, 2), 10);
      let yearFirstDigit = userInput[2];
      if (month > 12) {
        formattedValue = userInput[0]; // Revert to the first digit
      } else {
        formattedValue = userInput.slice(0, 2) + '/' + yearFirstDigit; // Add the first digit of the year
      }
    }

    if (userInput.length === 4) {
      let month = parseInt(userInput.slice(0, 2), 10);
      let year = parseInt(userInput.slice(2, 4), 10);

      if (month > 12) {
        formattedValue = userInput[0]; // Revert to the first digit
      } else if (year < this.currentYear || year > this.maxYear) {
        // this.invalidYear = true; // Set an error or display a message
        this.setExpirationDateError('invalidYear');
        return;
      } else if (year === this.currentYear && month < this.currentMonth) {
        // this.invalidMonth = true;
        this.setExpirationDateError('invalidMonth');
        return;
      } else {
        formattedValue = userInput.slice(0, 2) + '/' + userInput.slice(2, 4); // Complete MM/YY format
        this.clearExpirationDateError();
      }
    }
    // Update the input value and form control
    input.value = formattedValue;
    // this.checkoutFormGroup.controls['creditCard'].patchValue({expirationDate: formattedValue}, {emitEvent: false});
    this.checkoutFormGroup.controls['creditCard'].patchValue(
      {expirationDate: formattedValue},
      {emitEvent: false});

    console.log(`Formatted value: ${formattedValue}`);
  }

  // Method to set form control errors
  private setExpirationDateError(errorType: string) {
    const expirationDateControl = this.checkoutFormGroup.get('creditCard.expirationDate');
    if (expirationDateControl) {
      expirationDateControl.setErrors({ [errorType]: true });
    }
  }

  // Method to clear form control errors
  private clearExpirationDateError() {
    const expirationDateControl = this.checkoutFormGroup.get('creditCard.expirationDate');
    if (expirationDateControl) {
      expirationDateControl.setErrors(null);
    }
  }

  @HostListener('keydown', ['$event']) onKeyDown(event: KeyboardEvent) {
    this._isBackspacePressed = event.key === 'Backspace';
  }
}
