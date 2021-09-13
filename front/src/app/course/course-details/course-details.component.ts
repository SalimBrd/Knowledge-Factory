import { Component, OnInit } from '@angular/core';
import { Course } from '@app/models/course';
import { Book } from '@app/models/book';
import { Questionnaire } from '@app/models/questionnaire';
import { User } from '@app/models/user';
import { ActivatedRoute, Router } from '@angular/router';
import { first } from 'rxjs/operators';
import { UserService } from '@app/services/user.service';
import { CourseService } from '@app/services/course.service';
import { BookService } from '@app/services/book.service';
import { AuthenticationService } from '@app/services/authentication.service';
import * as $ from 'jquery/dist/jquery.min.js';

@Component({
  selector: 'app-course-details',
  templateUrl: './course-details.component.html',
  styleUrls: ['./course-details.component.css']
})
export class CourseDetailsComponent implements OnInit {
  identifiant: any;
  courses: Course[];
  course: Course;
  books: Book[];
  book: Book;
  questionnaire: Questionnaire;
  currentUser: User;
  isUser = false;
  isAdmin = false;

  constructor(
    private activatedRoute: ActivatedRoute,
    private authenticationService: AuthenticationService,
    private router: Router,
    private userService: UserService,
    private courseService: CourseService,
private bookService: BookService  ) { }

  ngOnInit(): void {
    this.getCurrentUser();
    this.activatedRoute.url.subscribe(u => this.identifiant = u[1].path);
    this.getCourseById();
  }

  getCurrentUser() {
    this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
    if (this.currentUser != null) {
      this.userService.me().pipe(first()).subscribe(user => {
        this.currentUser = user;
        this.isAdmin = this.currentUser.role == 'ROLE_ADMIN';
        this.isUser = this.currentUser.role == 'ROLE_USER';
        this.getBooks();
      })
    }
  }

  getCourseById() {
    this.courseService.getCourseByIdentifiant(this.identifiant).pipe(first()).subscribe(course => {
      this.course = course;
      this.questionnaire = course.questionnaires[0];
      if (this.isUser && this.course.premium) this.router.navigate(['/courses']);
    })
  }

  getBooks() {
    this.bookService.getAll().pipe(first()).subscribe(books => {
      this.books = books;
      this.book = books[0];
    })
  }

}
