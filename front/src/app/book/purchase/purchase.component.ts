import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { first } from 'rxjs/operators';
import { Address } from '@app/models/address';
import { AddressService } from '@app/services/address.service';
import { UserService } from '@app/services/user.service';
import { NgForm } from '@angular/forms';
import { User } from '../../models/user';
import { AuthenticationService } from '../../services/authentication.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-purchase',
  templateUrl: './purchase.component.html',
  styleUrls: ['./purchase.component.css']
})
export class PurchaseComponent implements OnInit {

  identifiant: string;
  addresses: Address[];
  address: Address = new Address();
  currentUser: User;
  loading = false;
  submitted = false;
  error = false;
  success = false;
  errorMsg = '';

  isLinear = false;
  addressFormGroup: FormGroup;
  payementFormGroup: FormGroup;

  constructor(private activatedRoute: ActivatedRoute,
    private addressService: AddressService,
    private authenticationService: AuthenticationService,
    private router: Router,
    private userService: UserService,
    private _formBuilder: FormBuilder
  ) { }

  ngOnInit() {
    this.activatedRoute.url.subscribe(u => this.identifiant = u[1].path);
    this.getCurrentUser();

    this.addressFormGroup = this._formBuilder.group({
      city: [''],
      street: [''],
      postalCode: [''],
      country: [''],
      user: ['']
    });
    this.payementFormGroup = this._formBuilder.group({
      secondCtrl: ['', Validators.required]
    });
  }

  onSubmit() {
    this.submitted = true;
    this.error = false;
    this.success = false;
    // stop here if form is invalid
    if (this.addressFormGroup.status == "INVALID") {
      this.error = true;
      this.errorMsg = "Le formulaire est invalide";
      return;
    }

    this.loading = true;
    this.addressFormGroup.value.user = this.currentUser;
    this.address = this.addressFormGroup.value;
    this.addressService.createAddress(this.address)
      .pipe(first())
      .subscribe(
        data => {
          this.currentUser.addresses.push(data);
          this.success = true;
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
      })
    }
  }
}
