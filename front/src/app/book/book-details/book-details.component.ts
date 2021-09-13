import { Component, OnInit } from '@angular/core';
import { Book } from '@app/models/book';
import { User } from '@app/models/user';
import { ActivatedRoute, Router } from '@angular/router';
import { first } from 'rxjs/operators';
import { UserService } from '@app/services/user.service';
import { BookService } from '@app/services/book.service';
import { AuthenticationService } from '@app/services/authentication.service';
import * as $ from 'jquery/dist/jquery.min.js';

@Component({
  selector: 'app-book-details',
  templateUrl: './book-details.component.html',
  styleUrls: ['./book-details.component.css']
})
export class BookDetailsComponent implements OnInit {
  identifiant: any;
  books: Book[];
  book: Book;
  currentUser: User;
  isAdmin = false;

  constructor(
    private activatedRoute: ActivatedRoute,
    private authenticationService: AuthenticationService,
    private userService: UserService,
    private bookService: BookService
  ) { }

  ngOnInit(): void {
    this.getCurrentUser();
    this.activatedRoute.url.subscribe(u => this.identifiant = u[1].path);
    this.getBookById();
  }

  getCurrentUser() {
    this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
    if (this.currentUser != null) {
      this.userService.me().pipe(first()).subscribe(user => {
        this.currentUser = user;
        this.isAdmin = this.currentUser.role == 'ROLE_ADMIN';
      })
    }
  }

  getBookById() {
    this.bookService.getBookByIdentifiant(this.identifiant).pipe(first()).subscribe(book => {
      this.book = book;
    })
  }


}
