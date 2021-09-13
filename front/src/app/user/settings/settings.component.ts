import { Component, OnInit } from '@angular/core';
import { User } from '@app/models/user';
import { NgForm } from '@angular/forms';
import { UserService } from '@app/services/user.service';
import { ActivatedRoute, Router } from '@angular/router';
import { first } from 'rxjs/operators';
import { AuthenticationService } from '../../services/authentication.service';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {
  identifiant: string;
  profile: boolean = false;
  isAdmin: boolean = false;
  user: User;
  currentUser: User;

  constructor(
    private userService: UserService,
    private authenticationService: AuthenticationService,
    private activatedRoute: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.activatedRoute.url.subscribe(u => this.identifiant = u[1].path);
    this.getCurrentUser();
  }

  onSubmit(f: NgForm) {
    this.userService.editProfile(this.identifiant, f.value).subscribe(
      data => {
      });
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
      if (this.user.username == this.currentUser.username)
        this.profile = true;
      if (!this.isAdmin && !this.profile)
        this.router.navigate(['/user']);
    })
  }

  deleteConfirmation() {
    if (confirm("Êtes vous sur de vouloir supprimer votre compte ?")) {
      this.deleteUser();
    }
  }

  deleteUser() {
    this.userService.deleteUser(this.identifiant)
      .pipe(first())
      .subscribe(
        data => {
          if (this.profile)
            this.authenticationService.logout();
          else
            this.router.navigate(['/user'])
        },
        error => {
          console.error(error);
        });
  }

}
