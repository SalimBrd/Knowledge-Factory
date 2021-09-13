import { Component } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';

import { AuthenticationService } from './services/authentication.service';
import { User } from './models/user';
import { UserService } from './services/user.service';
import { first } from 'rxjs/operators';

@Component({ selector: 'app', templateUrl: 'app.component.html', styleUrls: ['app.component.css'] })
export class AppComponent {
  currentUser: User;
  user: User;
  isSub = false;

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService,
    private userService: UserService
  ) {
    this.authenticationService.currentUser.subscribe(x => {
      this.currentUser = x;
      this.getUser();
    });

    this.router.routeReuseStrategy.shouldReuseRoute = function () {
      return false;
    }

    this.router.events.subscribe((evt) => {
      if (evt instanceof NavigationEnd) {
        // trick the Router into believing it's last link wasn't previously loaded
        this.router.navigated = false;
        // if you need to scroll back to top, here is the right place
        window.scrollTo(0, 0);
      }
    });
  }

  getUser() {
    if (this.currentUser != null) {
      this.userService.me().pipe(first()).subscribe(user => {
        this.user = user;
        if (this.user.role == "ROLE_SUBSCRIBER") this.isSub = true;
      })
    }
  }

  logout() {
    this.authenticationService.logout();
  }
}
