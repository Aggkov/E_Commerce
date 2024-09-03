export class OrderInfo {
  totalPrice: number;
  totalQuantity: number;
  status: string;

  constructor(totalPrice: number, totalQuantity: number, status: string) {
    this.totalPrice = totalPrice;
    this.totalQuantity = totalQuantity;
    this.status = status;
  }
}

