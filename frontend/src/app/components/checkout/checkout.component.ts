import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {CurrencyPipe, NgForOf, NgIf} from "@angular/common";
import {FormService} from "../../services/form.service";
import {Country} from "../../model/country";
import {State} from "../../model/state";
import {CartService} from "../../services/cart.service";
import {WhitespaceValidator} from "../../validators/whitespace-validator";
import {CartItem} from "../../model/cart-item";
import {LuhnCheckValidator} from "../../validators/luhn-check-validator";
import {CreditCardFormatDirective} from "../../directives/credit-card-format.directive";
import {CheckoutService} from "../../services/checkout.service";
import {Router} from "@angular/router";
import {Order} from "../../model/order";
import {Customer} from "../../model/customer";
import {OrderInfo} from "../../model/order-info";
import {Address} from "../../model/address";
import {OrderItem} from "../../model/order-item";

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CurrencyPipe,
    NgForOf,
    NgIf,
    CreditCardFormatDirective
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
  cartItems : CartItem[] = [];
  orderTrackingNumber : string = "";
  // creditCardYears: number[] = [];
  // creditCardMonths: number[] = [];

  constructor(private formBuilder: FormBuilder,
              private formService: FormService,
              private cartService: CartService,
              private checkoutService: CheckoutService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.cartItems = this.cartService.cartItems;
    this.checkoutFormGroup = this.formBuilder.group({
      customer: this.formBuilder.group({
        firstName: new FormControl('',
          [Validators.required,
            Validators.minLength(2), WhitespaceValidator.onlyWhitespace]),
        lastName:  new FormControl('',
          [Validators.required,
            Validators.minLength(2), WhitespaceValidator.onlyWhitespace]),
        email: new FormControl('',
          [Validators.required,
            Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$')])
      }),
      shippingAddress: this.formBuilder.group({
        street: new FormControl('', [Validators.required, Validators.minLength(2),
          WhitespaceValidator.onlyWhitespace]),
        city: new FormControl('', [Validators.required, Validators.minLength(2),
          WhitespaceValidator.onlyWhitespace]),
        state: new FormControl('', [Validators.required]),
        country: new FormControl('', [Validators.required]),
        zipCode: new FormControl('', [Validators.required, Validators.minLength(5),
          Validators.maxLength(5),
          WhitespaceValidator.onlyWhitespace])
      }),
      billingAddress: this.formBuilder.group({
        street: new FormControl('', [Validators.required, Validators.minLength(2),
          WhitespaceValidator.onlyWhitespace]),
        city: new FormControl('', [Validators.required, Validators.minLength(2),
          WhitespaceValidator.onlyWhitespace]),
        state: new FormControl('', [Validators.required]),
        country: new FormControl('', [Validators.required]),
        zipCode: new FormControl('', [Validators.required, Validators.minLength(5),
          Validators.maxLength(5),
          WhitespaceValidator.onlyWhitespace])
      }),
      creditCard: this.formBuilder.group({
        cardType: new FormControl('',
          [Validators.required]),
        nameOnCard:  new FormControl('',
          [Validators.required,
            Validators.minLength(2),
          WhitespaceValidator.onlyWhitespace]),
        cardNumber: new FormControl('',
          [Validators.required,
            // Validators.pattern('^[0-9]+$'),
            Validators.minLength(13),
            Validators.maxLength(19),
            LuhnCheckValidator.luhnCheck]),
        securityCode: new FormControl('',
          [Validators.required,
            Validators.pattern('[0-9]{3}')
          ]),
        expirationDate: [''],
        // expirationMonth: [''],
        // expirationYear: ['']
      })
    });

    this.reviewCartTotals();

    this.formService.getAllCountries().subscribe({
        next: data => {
          // console.log(JSON.stringify(data))
          this.countries = data
        },
        error: error => console.log(error)
      }
    );
  }

  reviewCartTotals() {
    this.cartService.totalPrice.subscribe({
      next: data => this.totalPrice = data
    });

    this.cartService.totalQuantity.subscribe({
      next: data => this.totalQuantity = data
    });
  }

  get firstName() { return this.checkoutFormGroup.get('customer.firstName'); }
  get lastName() { return this.checkoutFormGroup.get('customer.lastName'); }
  get email() { return this.checkoutFormGroup.get('customer.email'); }

  get shippingAddressStreet() { return this.checkoutFormGroup.get('shippingAddress.street'); }
  get shippingAddressCity() { return this.checkoutFormGroup.get('shippingAddress.city'); }
  get shippingAddressState() { return this.checkoutFormGroup.get('shippingAddress.state'); }
  get shippingAddressZipCode() { return this.checkoutFormGroup.get('shippingAddress.zipCode'); }
  get shippingAddressCountry() { return this.checkoutFormGroup.get('shippingAddress.country'); }

  get billingAddressStreet() { return this.checkoutFormGroup.get('billingAddress.street'); }
  get billingAddressCity() { return this.checkoutFormGroup.get('billingAddress.city'); }
  get billingAddressState() { return this.checkoutFormGroup.get('billingAddress.state'); }
  get billingAddressZipCode() { return this.checkoutFormGroup.get('billingAddress.zipCode'); }
  get billingAddressCountry() { return this.checkoutFormGroup.get('billingAddress.country'); }

  get creditCardType() { return this.checkoutFormGroup.get('creditCard.cardType'); }
  get creditCardNameOnCard() { return this.checkoutFormGroup.get('creditCard.nameOnCard'); }
  get creditCardNumber() { return this.checkoutFormGroup.get('creditCard.cardNumber'); }
  get creditCardSecurityCode() { return this.checkoutFormGroup.get('creditCard.securityCode'); }
  get creditCardExpDate() { return this.checkoutFormGroup.get('creditCard.expirationDate'); }

  onSubmit() {
    console.log("Handling the submit button");

    // if (this.checkoutFormGroup.invalid) {
    //   this.checkoutFormGroup.markAllAsTouched();
    //   return;
    // }

    let shippingState: State = this.checkoutFormGroup.controls['shippingAddress'].value.state;
    let billingState: State = this.checkoutFormGroup.controls['billingAddress'].value.state;


    let shippingAddress = new Address(
      this.shippingAddressCity?.value,
      this.shippingAddressStreet?.value,
      this.shippingAddressZipCode?.value,
      shippingState);

    let billingAddress = new Address(
      this.billingAddressCity?.value,
      this.billingAddressStreet?.value,
      this.billingAddressZipCode?.value,
      billingState);

    let newCustomer = new Customer(
      this.firstName?.value,
      this.lastName?.value,
      this.email?.value,
      shippingAddress,
      billingAddress
    );

    let orderItems: OrderItem[] = this.cartItems.map(
      item => new OrderItem(item)
    );

    let newOrder: Order = new Order(
      new OrderInfo(this.totalPrice, this.totalQuantity,"PENDING"),
      newCustomer,
      orderItems
    );
    // console.log("order is: ",JSON.stringify(newOrder));

    this.checkoutService.createOrder(newOrder).subscribe({
        next: response => {
          this.orderTrackingNumber = response.orderTrackingNumber;
          alert(`Your order has been received.\nOrder tracking number:
          ${response.orderTrackingNumber}`);

          // reset cart
          // this.resetCart();
        },

        error: err => alert(`There was an error: ${err.message}`)
    });
    // ?. === safely access object's properties
    console.log("Entire formgroup object:", this.checkoutFormGroup?.value);
    // console.log(`Entire formgroup object: ${JSON.stringify(this.checkoutFormGroup?.value)}`);

    console.log("firstName formcontrol object:", this.checkoutFormGroup.controls['customer'].value.firstName);
    // console.log("The email address is " + this.checkoutFormGroup?.get('customer')?.value.email);
    // console.log(this.checkoutFormGroup?.get('customer')?.value);

    // console.log(`with backticks The email address is ${this.checkoutFormGroup?.get('customer')?.value.email}`);
    // console.log(`with backticks The email address is ${this.checkoutFormGroup.controls['customer'].value.email}`);

    // console.log("The shipping address country is " + this.checkoutFormGroup?.get('shippingAddress')?.value.country.name);
    // console.log("The shipping address country is " + this.checkoutFormGroup.controls['shippingAddress'].value.country.name);
    // console.log("The shipping address country is " + this.checkoutFormGroup.get('shippingAddress.country')?.value.name);
    // console.log("The shipping address state is " + this.checkoutFormGroup?.get('shippingAddress')?.value.state.name);
  }

  resetCart() {
    // reset cart data
    this.cartService.cartItems = [];
    this.cartService.totalPrice.next(0);
    this.cartService.totalQuantity.next(0);

    // reset the form
    this.checkoutFormGroup.reset();

    // navigate back to the products page
    this.router.navigateByUrl("/products");
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
      // this.billingAddressStates = this.shippingAddressStates;
      this.getStatesByCountryCode('billingAddress');

      console.log("shipping address info: ",this.checkoutFormGroup.controls['shippingAddress'].value);
      console.log("billing address info: ", this.checkoutFormGroup.controls['billingAddress'].value);
    } else {
      this.showBillingInfo = true;
      this.checkoutFormGroup.controls['billingAddress'].reset();
      // bug fix for states
      // this.billingAddressStates = [];
      console.log(this.checkoutFormGroup.controls['shippingAddress'].value);
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
      // this.checkoutFormGroup.controls['creditCard'].patchValue({expirationDate: formattedValue}, {emitEvent: false});
      this.checkoutFormGroup.controls['creditCard'].patchValue(
        {expirationDate: formattedValue},
        {emitEvent: false});

      console.log(`Formatted value: ${formattedValue}`);
    }
  }

  onCardNameInput(event: Event) {
    const input = event.target as HTMLInputElement;
    let userInput = input.value.replace(/\D/g, ''); // Remove non-digit characters
    let formattedValue = '';
  }
// 5425233430109903
  onKeyDown(event: KeyboardEvent) {
    this.isBackspacePressed = event?.key === 'Backspace';
  }

  getStatesByCountryCode(formGroupName: string) {
    const formGroup = this.checkoutFormGroup
      .controls[formGroupName];

    const country = formGroup.value.country;
    const countryCode = formGroup.value.country.code;
    const countryName = formGroup.value.country.name;

    console.log(`${formGroupName} country: ${JSON.stringify(country)}`);
    console.log(`${formGroupName} country code: ${countryCode}`);
    console.log(`${formGroupName} country name: ${countryName}`);

    this.formService.getStatesByCountryCode(countryCode).subscribe({
      next: data => {
        if (formGroupName === 'shippingAddress') {
          this.shippingAddressStates = data;
        } else {
          this.billingAddressStates = data;
        }
        // select first item by default
        formGroup?.get('state')?.setValue(data[0]);
        // formGroup.value.setValue().setValue(data[0]);
      }
    })
  }
}

