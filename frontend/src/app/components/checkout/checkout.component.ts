import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {CurrencyPipe, NgForOf, NgIf} from "@angular/common";
import {FormService} from "../../services/form.service";
import {Country} from "../../model/country";
import {State} from "../../model/state";

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CurrencyPipe,
    NgForOf,
    NgIf
  ],
  templateUrl: './checkout.component.html',
  styleUrl: './checkout.component.css'
})
export class CheckoutComponent implements OnInit {
  checkoutFormGroup!: FormGroup;

  totalPrice: number = 0;
  totalQuantity: number = 0;
  invalidYear: boolean = false;
  invalidMonth: boolean = false;
  currentYear: number = new Date().getFullYear() % 100;
  currentMonth: number = new Date().getMonth() + 1;
  maxYear: number = this.currentYear + 20;
  private isBackspacePressed = false;
  showBillingInfo: boolean = true;
  countries: Country[] = [];
  shippingAddressStates: State[] = [];
  billingAddressStates: State[] = [];

  // creditCardYears: number[] = [];
  // creditCardMonths: number[] = [];

  constructor(private formBuilder: FormBuilder,
              private formService: FormService) {
  }

  ngOnInit(): void {
    this.checkoutFormGroup = this.formBuilder.group({
      customer: this.formBuilder.group({
        firstName: [''],
        lastName: [''],
        email: ['']
      }),
      shippingAddress: this.formBuilder.group({
        country: [''],
        street: [''],
        city: [''],
        state: [''],
        zipCode: ['']
      }),
      billingAddress: this.formBuilder.group({
        country: [''],
        street: [''],
        city: [''],
        state: [''],
        zipCode: ['']
      }),
      creditCard: this.formBuilder.group({
        cardType: [''],
        nameOnCard: [''],
        cardNumber: [''],
        securityCode: [''],

        expirationDate: [''],

        expirationMonth: [''],
        expirationYear: ['']
      })
    });

    this.formService.getAllCountries().subscribe({
      next: data => {
        // console.log(JSON.stringify(data))
        this.countries = data
      },
      error: error => console.log(error)
      }
    );

    // populate credit card months
    //
    // console.log("startMonth: " + startMonth);

    // this.formService.getCreditCardMonths(startMonth).subscribe(
    //   data => {
    //     // console.log("Retrieved credit card months: " + JSON.stringify(data));
    //     this.creditCardMonths = data;
    //   }
    // );
    //
    // // populate credit card years
    // this.formService.getCreditCardYears().subscribe(
    //   data => {
    //     // console.log("Retrieved credit card years: " + JSON.stringify(data));
    //     this.creditCardYears = data;
    //   }
    // );
  }

  onSubmit() {
    console.log("Handling the submit button");
    // ?. === safely access object's properties
    console.log(this.checkoutFormGroup?.value);
    console.log(this.checkoutFormGroup?.get('customer')?.value);
    console.log("The email address is " + this.checkoutFormGroup?.get('customer')?.value.email);
    console.log(`with backticks The email address is ${this.checkoutFormGroup?.get('customer')?.value.email}`);

    console.log("The shipping address country is " + this.checkoutFormGroup?.get('shippingAddress')?.value.country.name);
    console.log("The shipping address state is " + this.checkoutFormGroup?.get('shippingAddress')?.value.state.name);
  }

  /*
  Event object contains details about the event, including information about the event target,
  which is usually the DOM element that triggered the event.
   */
  copyShippingAddressToBillingAddress(checked: boolean) {
    if (checked) {
      this.showBillingInfo = false;
      this.checkoutFormGroup.controls['billingAddress']
        .setValue(this.checkoutFormGroup.controls['shippingAddress'].value);
      // bug fix for states
      this.billingAddressStates = this.shippingAddressStates;
      console.log(this.checkoutFormGroup.controls['billingAddress'].value);
    } else {
      this.showBillingInfo = true;
      this.checkoutFormGroup.controls['billingAddress'].reset();
      // bug fix for states
      this.billingAddressStates = [];
      console.log(this.checkoutFormGroup.controls['billingAddress'].value);
    }
  }

  onExpirationDateInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    let userInput = input.value.replace(/\D/g, ''); // Remove non-digit characters
    let formattedValue = '';

    // Handle backspace: Remove one character at a time
    if (this.isBackspacePressed) {
      if (userInput.endsWith('/')) {
      //   // If the last character is '/', remove it
      formattedValue = userInput.slice(0, -1);
      }
      else {
        // Otherwise, just remove the last character
        formattedValue = userInput;
      }
      this.invalidYear = false;
      this.invalidMonth = false;
      this.isBackspacePressed = false; // Reset backspace flag
    } else {
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
          this.invalidYear = true; // Set an error or display a message
          return;
        } else if (year === this.currentYear && month < this.currentMonth) {
            this.invalidMonth = true;
            return;
        } else {
          formattedValue = userInput.slice(0, 2) + '/' + userInput.slice(2, 4); // Complete MM/YY format
        }
      }
      // Update the input value and form control
      input.value = formattedValue;
      this.checkoutFormGroup.controls['creditCard'].patchValue({expirationDate: formattedValue}, {emitEvent: false});

      console.log(`Formatted value: ${formattedValue}`);
    }
  }

  onKeyDown(event:KeyboardEvent) {
    this.isBackspacePressed = event?.key === 'Backspace';
  }

  getStatesByCountryCode(formGroupName: string) {
    const formGroup = this.checkoutFormGroup
      .controls[formGroupName];

    const country = formGroup.value.country;
    const countryCode = formGroup.value.country.code;
    const countryName = formGroup.value.country.name;

    console.log(`${formGroupName} country: ${country}`);
    console.log(`${formGroupName} country code: ${countryCode}`);
    console.log(`${formGroupName} country name: ${countryName}`);

    this.formService.getStatesByCountryCode(countryCode).subscribe({
      next: data => {
        if (formGroupName === 'shippingAddress') {
          this.shippingAddressStates = data;
        }
        else {
          this.billingAddressStates = data;
        }
        // select first item by default
        formGroup?.get('state')?.setValue(data[0]);
      }
    })
  }
}

