import { Component, OnInit } from '@angular/core';
import { first } from 'rxjs/operators';
import { User } from '@app/models/user';
import { Course } from '@app/models/course';
import { Diploma } from '@app/models/diploma';
import { UserService } from '@app/services/user.service';
import { CourseService } from '@app/services/course.service';
import { DiplomaService } from '@app/services/diploma.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AddressService } from '@app/services/address.service';
import { AuthenticationService } from '../../services/authentication.service';

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.css']
})
export class UserDetailsComponent implements OnInit {
  private identifiant: string;
  loading = false;
  profile = false;
  isAdmin = false;
  user: User;
  currentUser: User;
  courses: Course[] = [];
  diplomas: Diploma[];

  constructor(
    private authenticationService: AuthenticationService,
    private userService: UserService,
    private courseService: CourseService,
    private diplomaService: DiplomaService,
    private addressService: AddressService,
    private activatedRoute: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loading = true;
    this.activatedRoute.url.subscribe(u => this.identifiant = u[1].path);
    this.getCurrentUser();
    this.addressService.getAll().pipe(first()).subscribe(address => {
      this.loading = false;
    })
  }

  getCurrentUser(): void {
    this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
    if (this.currentUser != null) {
      this.userService.me().pipe(first()).subscribe(user => {
        this.currentUser = user
        this.isAdmin = this.currentUser.role == 'ROLE_ADMIN';
        this.getProfileUser();
      })
    }
  }

  getProfileUser(): void {
    this.userService.getUserByIdentifiant(this.identifiant).pipe(first()).subscribe(user => {
      this.user = user;
      if (this.user.username == this.currentUser.username) {
        this.profile = true;
        this.getDiplomasForUser();
      }
    })
  }

  getDiplomasForUser() {
    this.diplomaService.getAll().pipe(first()).subscribe(diplomas => {
      this.diplomas = diplomas;
      this.getCoursesByDiplomas();
      
    })
  }

  getCoursesByDiplomas() {
    this.diplomas.forEach(d => {
      this.courseService.getCourseByIdentifiant(d.courseId).pipe(first()).subscribe(course => {
        this.courses.push(course);
      })
    })
  }
}
