import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { first } from 'rxjs/operators';
import { Address } from '@app/models/address';
import { AddressService } from '@app/services/address.service';
import { UserService } from '@app/services/user.service';
import { NgForm } from '@angular/forms';
import { User } from '@app/models/user';
import { AuthenticationService } from '@app/services/authentication.service';

@Component({
  selector: 'app-address-create',
  templateUrl: './address-create.component.html',
  styleUrls: ['./address-create.component.css']
})
export class AddressCreateComponent implements OnInit {

  identifiant: string;
  address: Address = new Address();
  user: User;
  currentUser: User;
  isAdmin = false;
  profile = false;
  loading = false;
  submitted = false;
  error = '';

  constructor(
    private activatedRoute: ActivatedRoute,
    private addressService: AddressService,
    private authenticationService: AuthenticationService,
    private router: Router,
    private userService: UserService
  ) { }

  ngOnInit(): void {
    this.activatedRoute.url.subscribe(u => this.identifiant = u[1].path);
    this.getCurrentUser();
  }

  onSubmit(f: NgForm) {
    this.submitted = true;

    // stop here if form is invalid
    if (f.invalid) {
      return;
    }

    this.loading = true;
    f.value.user = this.user;
    this.address = f.value;
    this.addressService.createAddress(this.address)
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

}
