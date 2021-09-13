import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  books = [];
  private cartLengthSource = new BehaviorSubject(0);
  currentCartLength = this.cartLengthSource.asObservable();

  constructor() { }

  changeCartLength(cartLength: number) {
    this.cartLengthSource.next(cartLength)
  }

  addToCart(book) {
    if (this.getBook(book.id) !== undefined) return;
    this.getBooks();
    this.books.push(book);
    localStorage.setItem("book", JSON.stringify(this.books));
  }

  getBook(id) {
    let books = [];
    books = JSON.parse(localStorage.getItem("book"));
    for (let book of books) {
      if (book.id == id)
        return book;
    }
  }

  getBooks() {
    if (localStorage.getItem("book"))
      this.books = JSON.parse(localStorage.getItem("book"));
    return this.books;
  }

  removeBook(id) {
    this.getBooks();
    this.books = this.books.filter((book) => {
      return book.id !== id;
    });
    localStorage.setItem("book", JSON.stringify(this.books));
  }

  clearCart() {
    this.books = [];
    localStorage.removeItem("book");
  }
}
