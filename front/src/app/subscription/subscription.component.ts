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
  selector: 'app-subscription',
  templateUrl: './subscription.component.html',
  styleUrls: ['./subscription.component.css']
})
export class SubscriptionComponent implements OnInit {
  currentUser: User;
  isUser = false;
  isSub = false;
  isAdmin = false;
  success = false;

  constructor(
    private authenticationService: AuthenticationService,
    private userService: UserService
  ) { }

  ngOnInit(): void {
    this.getCurrentUser();
  }

  getCurrentUser() {
    this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
    if (this.currentUser != null) {
      this.userService.me().pipe(first()).subscribe(user => {
        this.currentUser = user;
        this.isUser = this.currentUser.role == 'ROLE_USER';
        this.isSub = this.currentUser.role == 'ROLE_SUBSCRIBER';
        this.isAdmin = this.currentUser.role == 'ROLE_ADMIN';
      })
    }
  }

  subscribe() {
    if (this.isSub) this.currentUser.role = "ROLE_USER";
    else if (this.isUser) this.currentUser.role = "ROLE_SUBSCRIBER";
    this.userService.editProfile(this.currentUser.id, this.currentUser).pipe(first()).subscribe(user => {
      this.success = true;
    })
  }

}
