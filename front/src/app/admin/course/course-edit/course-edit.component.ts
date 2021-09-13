import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { first } from 'rxjs/operators';
import { Course } from '@app/models/course';
import { CourseService } from '@app/services/course.service';
import { UserService } from '@app/services/user.service';
import { NgForm } from '@angular/forms';
import { User } from '@app/models/user';
import { AuthenticationService } from '@app/services/authentication.service';

@Component({
  selector: 'app-course-edit',
  templateUrl: './course-edit.component.html',
  styleUrls: ['./course-edit.component.css']
})
export class CourseEditComponent implements OnInit {

  identifiant: string;
  course: Course = new Course();
  user: User;
  currentUser: User;
  isAdmin = false;
  profile = false;
  loading = false;
  submitted = false;
  error = '';

  constructor(
    private activatedRoute: ActivatedRoute,
    private courseService: CourseService,
    private authenticationService: AuthenticationService,
    private router: Router,
    private userService: UserService
  ) { }

  ngOnInit(): void {
    this.getCurrentUser();
    this.activatedRoute.url.subscribe(u => this.identifiant = u[2].path);
    this.getCourse();
  }

  getCourse() {
    this.courseService.getCourseByIdentifiant(this.identifiant).pipe(first()).subscribe(course => {
      this.course = course;
      console.log(course);
    })
  }

  onSubmit(f: NgForm) {
    this.submitted = true;

    // stop here if form is invalid
    if (f.invalid) {
      return;
    }

    this.loading = true;

    this.course = f.value;
    this.courseService.editCourse(this.course, this.identifiant)
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
