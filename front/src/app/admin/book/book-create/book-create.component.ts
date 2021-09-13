import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { first } from 'rxjs/operators';
import { Book } from '@app/models/book';
import { BookService } from '@app/services/book.service';
import { UserService } from '@app/services/user.service';
import { NgForm } from '@angular/forms';
import { User } from '@app/models/user';
import { AuthenticationService } from '@app/services/authentication.service';

@Component({
  selector: 'app-book-create',
  templateUrl: './book-create.component.html',
  styleUrls: ['./book-create.component.css']
})
export class BookCreateComponent implements OnInit {

  identifiant: string;
  book: Book = new Book();
  user: User;
  currentUser: User;
  isAdmin = false;
  profile = false;
  loading = false;
  submitted = false;
  error = '';

  constructor(
    private activatedRoute: ActivatedRoute,
    private bookService: BookService,
    private authenticationService: AuthenticationService,
    private router: Router,
    private userService: UserService
  ) { }

  ngOnInit(): void {
    this.getCurrentUser();
  }

  onSubmit(f: NgForm) {
    this.submitted = true;

    // stop here if form is invalid
    if (f.invalid) {
      return;
    }

    this.loading = true;

    this.book = f.value;
    this.bookService.createBook(this.book)
      .pipe(first())
      .subscribe(
        data => {
          this.router.navigate(['..'], { relativeTo: this.activatedRoute })
        },
        error => {
          this.error = error;
          this.loading = false;
        });
  }

  getCurrentUser(): void {
    this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
    if (this.currentUser != null) {
      this.userService.me().pipe(first()).subscribe(user => {
        this.currentUser = user;
        if (this.currentUser.role != 'ROLE_ADMIN')
          this.router.navigate(['/']);
      })
    }
  }

}
