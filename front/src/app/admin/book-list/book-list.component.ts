import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Book } from '@app/models/book';
import { User } from '@app/models/user';
import { PageEvent } from '@angular/material/paginator';
import { first } from 'rxjs/operators';
import { UserService } from '@app/services/user.service';
import { BookService } from '@app/services/book.service';
import { AuthenticationService } from '@app/services/authentication.service';
import * as $ from 'jquery/dist/jquery.min.js';

@Component({
  selector: 'app-book-list',
  templateUrl: './book-list.component.html',
  styleUrls: ['./book-list.component.css']
})
export class BookListComponent implements OnInit {

  books: Book[];
  currentUser: User;
  length = 0;
  pageSize = 25;
  pageIndex = 0;
  pageEvent: PageEvent;
  displayedColumns: string[] = ['Id', 'Title', 'Author', 'Description', 'Price', 'Premium', 'Edit', 'Delete'];


  constructor(
    private authenticationService: AuthenticationService,
    private userService: UserService,
    private router: Router,
    private bookService: BookService
  ) { }

  ngOnInit(): void {
    this.getCurrentUser();
    this.getBooks();
    this.handlePageEvent(null);
  }

  getCurrentUser() {
    this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
    if (this.currentUser != null) {
      this.userService.me().pipe(first()).subscribe(user => {
        this.currentUser = user;
        if (this.currentUser.role != 'ROLE_ADMIN')
          this.router.navigate(['/']);
      })
    }
  }

  getBooks() {
    let params = { "page": this.pageIndex + 1, "limit": 10000 }
    this.bookService.getAll(params).pipe(first()).subscribe(books => {
      this.books = books;
      this.length = books.length;
      this.getBooksByPage();
    })
  }

  getBooksByPage() {
    let params = { "page": this.pageIndex + 1, "limit": this.pageSize }
    this.bookService.getAll(params).pipe(first()).subscribe(books => {
      this.books = books;
    })
  }

  handlePageEvent(event: PageEvent): PageEvent {
    if (!event) return;
    this.pageIndex = event.pageIndex;
    this.getBooksByPage();
    $('html, body').animate({ scrollTop: '0px' }, 50);
    return event;
  }

  deleteConfirmation(id: number) {
    const retVal = confirm('Do you want to continue ?');
    if (retVal == true) {
      this.deleteBook(id);
      return true;
    } else {
      return false;
    }
  }

  deleteBook(id) {
    this.bookService.deleteBook(id).pipe(first()).subscribe(response => {
      console.log(response);
    })

  }

}
