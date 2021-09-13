import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { Book } from '@app/models/book';
import { User } from '@app/models/user';
import { ActivatedRoute, Router } from '@angular/router';
import { first } from 'rxjs/operators';
import { UserService } from '@app/services/user.service';
import { BookService } from '@app/services/book.service';
import { CartService } from '@app/services/cart.service';
import { AuthenticationService } from '@app/services/authentication.service';
import * as $ from 'jquery/dist/jquery.min.js';

@Component({
  selector: 'app-book',
  templateUrl: './book.component.html',
  styleUrls: ['./book.component.css']
})
export class BookComponent implements OnInit {
  identifiant: any;
  books: Book[];
  book: Book;
  currentUser: User;
  isAdmin = false;
  cartLength: number;

  constructor(
    private authenticationService: AuthenticationService,
    private userService: UserService,
    private bookService: BookService,
    public cartService: CartService
  ) { }

  ngOnInit(): void {
    this.getCurrentUser();
    this.cartService.currentCartLength.subscribe(cartLength => this.cartLength = cartLength)
  }

  getCurrentUser() {
    this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
    if (this.currentUser != null) {
      this.userService.me().pipe(first()).subscribe(user => {
        this.currentUser = user;
        this.isAdmin = this.currentUser.role == 'ROLE_ADMIN';
        this.getBooks();
      })
    }
  }

  getBooks() {
    this.bookService.getAll().pipe(first()).subscribe(books => {
      this.books = books;
      this.book = books[0];
    })
  }

  addToCart(book) {
    this.cartService.addToCart(book);
    this.cartService.changeCartLength(this.cartService.getBooks().length);
  }

  removeFromCart(id) {
    this.cartService.removeBook(id);
    this.cartService.changeCartLength(this.cartService.getBooks().length);
  }

}
