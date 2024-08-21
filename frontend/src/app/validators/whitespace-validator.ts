import {FormControl, ValidationErrors} from "@angular/forms";

export class WhitespaceValidator {
  // whitespace validation
  static onlyWhitespace(control: FormControl) : ValidationErrors | null {

    // check if string only contains whitespace
    if ((control.value != null) && (control.value.trim().length === 0)) {

      // invalid, return error object
      return { 'onlyWhitespace': true };
    }
    else {
      // valid, return null
      return null;
    }
  }
}
