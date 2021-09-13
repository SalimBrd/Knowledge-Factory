import { Component } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';

import { AuthenticationService } from '@app/services/authentication.service';
import { User } from '@app//models/user';
import { UserService } from '@app/services/user.service';
import { first } from 'rxjs/operators';

@Component({ selector: 'app-home', templateUrl: 'home.component.html', styleUrls: ['home.component.css'] })
export class HomeComponent {
  currentUser: User;
  user: User;

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService,
    private userService: UserService
  ) {
    this.authenticationService.currentUser.subscribe(x => {
      this.currentUser = x;
      this.getUser();
    });
  }

  getUser() {
    if (this.currentUser != null) {
      this.userService.me().pipe(first()).subscribe(user => {
        this.user = user;
      })
    }
  }

  ngOnInit() {
  }
}
