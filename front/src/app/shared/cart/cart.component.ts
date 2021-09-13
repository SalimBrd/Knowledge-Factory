import { Component, OnInit } from '@angular/core';
import { CartService } from '@app/services/cart.service'

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  books: any  = [];
  cartLength: number;

  constructor(public cartService: CartService) { }

  ngOnInit(): void {
    this.cartService.currentCartLength.subscribe(cartLength => this.cartLength = cartLength);
    this.getBooks();
  }

  getBooks() {
    this.books = this.cartService.getBooks();
    this.cartLength = this.books.length;
  }
}
