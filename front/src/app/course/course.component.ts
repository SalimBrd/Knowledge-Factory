import { Component, OnInit } from '@angular/core';
import { Course } from '../models/course';
import { User } from '../models/user';
import { PageEvent } from '@angular/material/paginator';
import { first } from 'rxjs/operators';
import { UserService } from '@app/services/user.service';
import { CourseService } from '@app/services/course.service';
import { AuthenticationService } from '@app/services/authentication.service';
import * as $ from 'jquery/dist/jquery.min.js';

@Component({
  selector: 'app-course',
  templateUrl: './course.component.html',
  styleUrls: ['./course.component.css']
})
export class CourseComponent implements OnInit {
  courses: Course[];
  defaultCourses: Course[];
  premiumCourses: Course[];
  currentUser: User;
  premium = false;
  premiumState = false;
  isUser = false;
  isAdmin = false;
  isSub = false;
  length = 0;
  defaultLength = 0;
  premiumLength = 0;
  pageSize = 10;
  pageIndex = 0;
  pageEvent: PageEvent;

  constructor(
    private authenticationService: AuthenticationService,
    private userService: UserService,
    private courseService: CourseService,
  ) { }

  ngOnInit(): void {
    this.getCurrentUser();
    this.loadScript();
    this.getCourses();
    this.handlePageEvent(null);
  }

  loadDefault() {
    this.courses = this.defaultCourses;
    this.length = this.defaultLength;
    this.premium = false;
  }

  loadPremium() {
    this.courses = this.premiumCourses;
    this.length = this.premiumLength;
    this.premium = true;
  }

  getCurrentUser() {
    this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
    if (this.currentUser != null) {
      this.userService.me().pipe(first()).subscribe(user => {
        this.currentUser = user;
        this.isAdmin = this.currentUser.role == 'ROLE_ADMIN';
        this.isSub = this.currentUser.role == 'ROLE_SUBSCRIBER';
        this.isUser = this.currentUser.role == 'ROLE_USER';
      })
    }
  }

  getCourses() {
    let params = { "limit": 10000 }
    this.courseService.getDefaultCourseList(params).pipe(first()).subscribe(courses => {
      this.defaultCourses = courses;
      this.courses = courses;
      this.defaultLength = courses.length;
      this.getPremiumCourses();
    })
  }

  getPremiumCourses() {
    let params = { "limit": 10000 }
    this.courseService.getPremiumCourseList(params).pipe(first()).subscribe(courses => {
      this.premiumCourses = courses;
      this.premiumLength = courses.length;
      this.length = this.defaultLength;
    })
  }

  getCoursesByPage() {
    let params = { "page": this.pageIndex + 1, "limit": this.pageSize }
    this.courseService.getDefaultCourseList(params).pipe(first()).subscribe(courses => {
      this.defaultCourses = courses;
      this.courses = courses;
    })
  }

  getPremiumCoursesByPage() {
    let params = { "page": this.pageIndex + 1, "limit": this.pageSize }
    this.courseService.getPremiumCourseList(params).pipe(first()).subscribe(courses => {
      this.premiumCourses = courses;
      this.courses = courses;
    })
  }

  loadScript() {
    $('.btn-group .btn').on('click', function () {
      $(this).addClass("btn-primary").removeClass("btn-secondary")
        .siblings().removeClass("btn-primary").addClass("btn-secondary");
    });
  }

  handlePageEvent(event: PageEvent): PageEvent {
    if (!event) return;
    this.pageIndex = event.pageIndex;
    if (this.premium) this.getPremiumCoursesByPage();
    else this.getCoursesByPage();
    $('html, body').animate({ scrollTop: '0px' }, 50);
    return event;
  }
}
