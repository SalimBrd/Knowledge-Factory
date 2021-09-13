import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Course } from '@app/models/course';
import { User } from '@app/models/user';
import { PageEvent } from '@angular/material/paginator';
import { first } from 'rxjs/operators';
import { UserService } from '@app/services/user.service';
import { CourseService } from '@app/services/course.service';
import { AuthenticationService } from '@app/services/authentication.service';
import * as $ from 'jquery/dist/jquery.min.js';

@Component({
  selector: 'app-course-list',
  templateUrl: './course-list.component.html',
  styleUrls: ['./course-list.component.css']
})
export class CourseListComponent implements OnInit {
  courses: Course[];
  currentUser: User;
  length = 0;
  pageSize = 25;
  pageIndex = 0;
  pageEvent: PageEvent;
  displayedColumns: string[] = ['Id', 'Title', 'Difficulty', 'Suggested_Hours', 'Premium', 'Edit', 'Delete'];


  constructor(
    private authenticationService: AuthenticationService,
    private userService: UserService,
    private router: Router,
    private courseService: CourseService
  ) { }

  ngOnInit(): void {
    this.getCurrentUser();
    this.getCourses();
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

  getCourses() {
    let params = { "page": this.pageIndex + 1, "limit": 10000 }
    this.courseService.getAll(params).pipe(first()).subscribe(courses => {
      this.courses = courses;
      this.length = courses.length;
      this.getCoursesByPage();
    })
  }

  getCoursesByPage() {
    let params = { "page": this.pageIndex + 1, "limit": this.pageSize }
    this.courseService.getAll(params).pipe(first()).subscribe(courses => {
      this.courses = courses;
    })
  }

  handlePageEvent(event: PageEvent): PageEvent {
    if (!event) return;
    this.pageIndex = event.pageIndex;
    this.getCoursesByPage();
    $('html, body').animate({ scrollTop: '0px' }, 50);
    return event;
  }

  deleteConfirmation(id: number) {
    const retVal = confirm('Do you want to continue ?');
    if (retVal == true) {
      this.deleteCourse(id);
      return true;
    } else {
      return false;
    }
  }

  deleteCourse(id) {
    this.courseService.deleteCourse(id).pipe(first()).subscribe(response => {
      console.log(response);
    })

  }

}
