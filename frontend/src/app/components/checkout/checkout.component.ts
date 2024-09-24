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
import {CheckoutService, OrderResponse} from "../../services/checkout.service";
import {NavigationExtras, Router} from "@angular/router";
import {Order} from "../../model/order";
import {Customer} from "../../model/customer";
import {OrderInfo} from "../../model/order-info";
import {Address} from "../../model/address";
import {OrderItem} from "../../model/order-item";
import {CreditCardExpirationDateFormatDirective} from "../../directives/credit-card-expiration-date-format.directive";
// import {OrderResponse} from "../../services/checkout.service";

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CurrencyPipe,
    NgForOf,
    NgIf,
    CreditCardFormatDirective,
    CreditCardExpirationDateFormatDirective
  ],
  templateUrl: './checkout.component.html',
  styleUrl: './checkout.component.css'
})
export class CheckoutComponent implements OnInit {

  checkoutFormGroup!: FormGroup;
  totalPrice: number = 0;
  totalQuantity: number = 0;
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
        expirationDate: new FormControl('',
          [Validators.required])
        // (keydown)="onKeyDown($event)"
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
          this.resetCart(response);
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

  resetCart(orderResponse: OrderResponse) {
    // reset cart data
    this.cartService.cartItems = [];
    this.cartService.totalPrice.next(0);
    this.cartService.totalQuantity.next(0);
    // reset the form
    this.checkoutFormGroup.reset();
    // navigate back to the products page
    this.router.navigateByUrl("/success", { state: { orderResponse: orderResponse } });
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
      }
    })
  }

  onCardNameInput(event: Event) {
    const input = event.target as HTMLInputElement;
    // allow only characters, remove digits
    let userInput = input.value.replace(/\d/g, '');
    input.value = userInput;
    this.creditCardNameOnCard?.patchValue(
      userInput,
      {emitEvent: false});
  }
}

