import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { User } from '@app/models/user';
import { AuthenticationService } from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  params: any;
  constructor(private http: HttpClient) { }

  create(params) {
    this.params = params;
    return this.http.post(`${environment.apiUrl}/register`, this.params );
  }

  editProfile(id, params) {
    this.params = params;
    return this.http.put<User>(`${environment.apiUrl}/user/` + id, this.params);
  }

  deleteUser(id) {
    return this.http.delete<User>(`${environment.apiUrl}/user/` + id);
  }

  getAll(params?) {
    this.params = params;
    return this.http.get<User[]>(`${environment.apiUrl}/user`, { params: this.params });
  }

  me() {
    return this.http.get<User>(`${environment.apiUrl}/me`);
  }

  getUserByIdentifiant(identifiant) {
    return this.http.get<User>(`${environment.apiUrl}/user/` + identifiant);
  }
}
