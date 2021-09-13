import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { first } from 'rxjs/operators';
import { Address } from '@app/models/address';
import { AddressService } from '@app/services/address.service';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-address-details',
  templateUrl: './address-details.component.html',
  styleUrls: ['./address-details.component.css']
})
export class AddressDetailsComponent implements OnInit {
  private identifiant: string;
  public address: Address;
  loading = false;
  submitted = false;
  error = '';

  constructor(private activatedRoute: ActivatedRoute, private addressService: AddressService, private router: Router) { }

  ngOnInit(): void {
    this.activatedRoute.url.subscribe(u => this.identifiant = u[3].path);
    this.addressService.getAddressByIdentifiant(this.identifiant).pipe(first()).subscribe(address => {
      this.address = address;
    })
  }

  onSubmit(f: NgForm) {
    this.submitted = true;

    // stop here if form is invalid
    if (f.invalid) {
      return;
    }

    this.loading = true;
    this.address = f.value;

    this.addressService.editAddress(this.address, this.identifiant)
      .pipe(first())
      .subscribe(
        data => {
          this.router.navigate(["../.."], { relativeTo: this.activatedRoute });
        },
        error => {
          this.error = error;
          this.loading = false;
        });
  }

  deleteConfirmation() {
    if (confirm("Êtes vous sur de vouloir supprimer cette addresse ?")) {
      this.deleteAddress();
    }
  }

  deleteAddress() {
    this.addressService.deleteAddress(this.identifiant)
      .pipe(first())
      .subscribe(
        data => {
          this.router.navigate(["../.."], { relativeTo: this.activatedRoute });
        },
        error => {
          this.error = error;
          this.loading = false;
        });
  }
}
