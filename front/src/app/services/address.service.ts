import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { environment } from '@environments/environment';
import { Address } from '@app/models/address';
import { AuthenticationService } from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class AddressService {
  params: any;
  constructor(private http: HttpClient) { }

  getAll(params?) {
    this.params = params;
    return this.http.get<Address[]>(`${environment.apiUrl}/address`, { params: this.params });
  }

  getAddressByIdentifiant(identifiant) {
    return this.http.get<Address>(`${environment.apiUrl}/address/` + identifiant);
  }

  createAddress(params) {
    this.params = params;
    return this.http.post<Address>(`${environment.apiUrl}/address/`, params);
  }

  editAddress(params, id) {
    this.params = params;
    return this.http.put<Address>(`${environment.apiUrl}/address/` + id, params);
  }

  deleteAddress(id) {
    return this.http.delete<Address>(`${environment.apiUrl}/address/` + id);
  }
}
