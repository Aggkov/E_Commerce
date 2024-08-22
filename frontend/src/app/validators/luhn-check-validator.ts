import {FormControl, ValidationErrors} from "@angular/forms";

export class LuhnCheckValidator {

  static luhnCheck(control: FormControl): ValidationErrors | null {
    const value = control.value;

    // If the control is empty, no validation error (other validators can handle required fields)
    if (!value) {
      return null;
    }

    // Remove all spaces from the card number
    const sanitizedValue = value.replace(/\s+/g, '');

    // Convert the card number to a string and reverse it to process from right to left
    let digits = sanitizedValue.split('').reverse();
    let sum = 0;

    // Loop through each digit
    for (let i = 0; i < digits.length; i++) {
      let digit = parseInt(digits[i]);

      // Double every second digit
      if (i % 2 === 1) {
        digit *= 2;
        // If doubling the digit results in a number greater than 9, subtract 9
        if (digit > 9) {
          digit -= 9;
        }
      }

      // Sum the digits
      sum += digit;
    }

    // The card is valid if the sum modulo 10 is 0
    const isValid = sum % 10 === 0;

    // Return null if valid, otherwise return an error object
    return isValid ? null : { luhnCheck: true };
    }
}
