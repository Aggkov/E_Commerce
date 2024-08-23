import {Directive, ElementRef, HostListener} from '@angular/core';

@Directive({
  selector: '[appCreditCardFormat]',
  standalone: true
})
export class CreditCardFormatDirective {

  constructor(private el: ElementRef) {
  }

  @HostListener('input', ['$event']) onInputChange(event: Event) {
    const input = event.target as HTMLInputElement;
    let value = input.value.replace(/\s+/g, ''); // Remove all existing spaces

    // Split value into chunks of 4 digits
    let formattedValue = '';
    for (let i = 0; i < value.length; i++) {
      formattedValue += value[i];
      // Add a space after every 4 digits
      if ((i + 1) % 4 === 0 && i + 1 <= 12) {
        formattedValue += ' ';
      }
    }
    // 5425 2334 3010 9903
    // Update the input value
    input.value = formattedValue;
  }
}