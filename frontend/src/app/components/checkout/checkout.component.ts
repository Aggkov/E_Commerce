import {AfterViewInit, Component, input, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {CurrencyPipe, NgForOf, NgIf} from "@angular/common";
import {FormService} from "../../services/form.service";
import {Country} from "../../interfaces/country";
import {State} from "../../interfaces/state";
import {CartService} from "../../services/cart.service";
import {WhitespaceValidator} from "../../validators/whitespace-validator";
import {CartItem} from "../../interfaces/cart-item";
import {CreditCardFormatDirective} from "../../directives/credit-card-format.directive";
import {OrderService, OrderSuccess} from "../../services/order.service";
import {Event, Router} from "@angular/router";
import {Order} from "../../interfaces/order/order";
import {OrderItem} from "../../interfaces/order/order-item";
import {CreditCardExpirationDateFormatDirective} from "../../directives/credit-card-expiration-date-format.directive";
import {window} from "rxjs";
import {ICreateOrderRequest, IPayPalConfig, NgxPayPalModule} from "ngx-paypal";


@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CurrencyPipe,
    NgForOf,
    NgIf,
    CreditCardFormatDirective,
    CreditCardExpirationDateFormatDirective,
    NgxPayPalModule
  ],
  templateUrl: './checkout.component.html',
  styleUrl: './checkout.component.css'
})
export class CheckoutComponent implements OnInit, AfterViewInit {

  checkoutFormGroup!: FormGroup;
  totalPrice: number = 0;
  totalQuantity: number = 0;
  showBillingInfo: boolean = true;
  countries: Country[] = [];
  shippingAddressStates: State[] = [];
  billingAddressStates: State[] = [];
  cartItems : CartItem[] = [];
  orderTrackingNumber : string = "";
  public payPalConfig?: IPayPalConfig;

  constructor(private formBuilder: FormBuilder,
              private formService: FormService,
              private cartService: CartService,
              private orderService: OrderService,
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
      })
      // creditCard: this.formBuilder.group({
      //   cardType: new FormControl('',
      //     [Validators.required]),
      //   nameOnCard:  new FormControl('',
      //     [Validators.required,
      //       Validators.minLength(2),
      //     WhitespaceValidator.onlyWhitespace]),
      //   cardNumber: new FormControl('',
      //     [Validators.required,
      //       // Validators.pattern('^[0-9]+$'),
      //       Validators.minLength(13),
      //       Validators.maxLength(19)]
      //       // LuhnCheckValidator.luhnCheck]
      //   ),
      //   securityCode: new FormControl('',
      //     [Validators.required,
      //       Validators.pattern('[0-9]{3}')
      //     ]),
      //   expirationDate: new FormControl('',
      //     [Validators.required])
      //   // (keydown)="onKeyDown($event)"
      //   // expirationMonth: [''],
      //   // expirationYear: ['']
      // })
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

    this.initPaypalConfig();
  }

  ngAfterViewInit(): void {
    // this.loadPayPalScript()
    //   .then(() => this.renderPayPalButtons())
    //   .catch(err => console.error('PayPal SDK failed to load:', err));
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

  onSubmit(orderId?: string) {

    // if (this.checkoutFormGroup.invalid) {
    //   this.checkoutFormGroup.markAllAsTouched();
    //   return;
    // }

    let shippingState: State = this.checkoutFormGroup.controls['shippingAddress'].value.state;
    let billingState: State = this.checkoutFormGroup.controls['billingAddress'].value.state;


    let shippingAddress = {
      city: this.shippingAddressCity?.value,
      street: this.shippingAddressStreet?.value,
      zipCode: this.shippingAddressZipCode?.value,
      state: shippingState
    };

    let billingAddress = {
      city: this.billingAddressCity?.value,
      street: this.billingAddressStreet?.value,
      zipCode: this.billingAddressZipCode?.value,
      state: billingState
    };

    let newUser = {
      firstName: this.firstName?.value,
      lastName: this.lastName?.value,
      email: this.email?.value,
      shippingAddress: shippingAddress,
      billingAddress: billingAddress
    };

    let orderItems: OrderItem[] = this.cartItems.map(
      // explicit with return
      cartItem => {
        return {
          product: {
            id: cartItem.product.id
          },
          quantity: cartItem.quantity
        };
      }
      // implicit without return of object ({object})
      // cartItem => ({
      //   product: {
      //     id: cartItem.product.id
      //   },
      //   quantity: cartItem.quantity
      // })
    );

    let newOrder: Order = {
      orderInfo: { totalPrice: this.totalPrice, totalQuantity: this.totalQuantity, status: "PENDING" },
      user: newUser,
      orderItems: orderItems,
      paypalOrderId: orderId
    };
    // console.log("order is: ",JSON.stringify(newOrder));

    this.orderService.createOrder(newOrder).subscribe({
        next: response => {
          this.orderTrackingNumber = response.orderTrackingNumber;
          alert(`Your order has been received.\nOrder tracking number:
          ${response.orderTrackingNumber}`);

          this.resetCart(response);
        },
        error: err => {
          if (err.status === 400) {
            console.log('Validation errors:', err.error);
          }
        }
    });
  }

  private initPaypalConfig(): void {
    this.payPalConfig = {
      currency: 'USD',
      clientId: 'AZOIrGdz55roalSkzWF9qbUFBaxrO2uVq3VVWTDhGh9cGf2YIEyB7NbzO9GZp8CHWDi8Iojoe146m1Ja',
      createOrderOnClient: (data) => <ICreateOrderRequest> {
        intent: 'CAPTURE',
        purchase_units: [{
          amount: {
            currency_code: 'USD',
            value: this.totalPrice.toFixed(2),
            breakdown: {
              item_total: {
                currency_code: 'USD',
                value: this.totalPrice.toFixed(2)
              }
            }
          }
        }]
      },
      onApprove: (data, actions) => {
        return actions.order.capture().then((details: any) => {
          console.log('Order captured:', details);
          this.onSubmit(details.id);
        });
      },
      onError: err => {
        console.error('PayPal error:', err);
      }
    };
  }

  resetCart(orderSuccess: OrderSuccess) {
    // reset cart data
    this.cartService.cartItems = [];
    this.cartService.totalPrice.next(0);
    this.cartService.totalQuantity.next(0);
    sessionStorage.removeItem('cartItems');

    this.checkoutFormGroup.reset();
    // navigate back to the products page
    this.router.navigateByUrl("/success", { state: { orderSuccess: orderSuccess } });
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
      // this.getStatesByCountryCode('billingAddress');

      // console.log("shipping address info: ",this.checkoutFormGroup.controls['shippingAddress'].value);
      // console.log("billing address info: ", this.checkoutFormGroup.controls['billingAddress'].value);
      // console.log("billing array as json " + JSON.stringify(this.billingAddressStates));
      // console.log("shipping array as json " + JSON.stringify(this.shippingAddressStates));
      // console.log("billing array " + this.billingAddressStates)
      // console.log("shipping array " + this.shippingAddressStates)
    } else {
      this.showBillingInfo = true;
      this.checkoutFormGroup.controls['billingAddress'].reset();
      // bug fix for states
      // this.billingAddressStates = [];
      // console.log(this.checkoutFormGroup.controls['shippingAddress'].value);
      // console.log(this.checkoutFormGroup.controls['billingAddress'].value);
      // console.log("billing array as json " + JSON.stringify(this.billingAddressStates));
      // console.log("shipping array as json " + JSON.stringify(this.shippingAddressStates));
    }
  }

  getStatesByCountryCode(formGroupName: string) {
    const formGroup = this.checkoutFormGroup
      .controls[formGroupName];

    const country = formGroup.value.country;
    const countryCode = formGroup.value.country.code;
    const countryName = formGroup.value.country.name;
    // console.log(`${formGroupName} country: ${JSON.stringify(country)}`);
    // console.log(`${formGroupName} country code: ${countryCode}`);
    // console.log(`${formGroupName} country name: ${countryName}`);

    this.formService.getStatesByCountryCode(countryCode).subscribe({
      next: data => {
        if (formGroupName === 'shippingAddress') {
          this.shippingAddressStates = data;
        } else {
          this.billingAddressStates = data;
        }
        // select first item by default
        formGroup?.get('state')?.setValue(data[0]);
        // console.log(`on select country country is: ${JSON.stringify(formGroup.value.country)}`);
        // console.log(`on select state state is: ${JSON.stringify(formGroup.value.state)}`);
      },
      error: err => console.log(err)
    });
  }

  // onCardNameInput(event: Event) {
  //   const input = event.target as HTMLInputElement;
  //   // allow only characters, remove digits
  //   let userInput = input.value.replace(/\d/g, '');
  //   input.value = userInput;
  //   this.creditCardNameOnCard?.patchValue(
  //     userInput,
  //     {emitEvent: false});
  // }
}

