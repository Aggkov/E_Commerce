import {State} from "./state";

export class Address {
  city: string;
  street: string;
  zipCode: string;
  state: State;

  constructor(city: string, street: string, zipCode: string, state: State) {
    this.city = city;
    this.street = street;
    this.zipCode = zipCode;
    this.state = state;
  }
}

