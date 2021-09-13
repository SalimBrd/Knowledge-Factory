import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { environment } from '@environments/environment';
import { Diploma } from '@app/models/diploma';
import { AuthenticationService } from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class DiplomaService {
  params: any;
  constructor(private http: HttpClient) { }

  getAll(params?) {
    this.params = params;
    return this.http.get<Diploma[]>(`${environment.apiUrl}/diploma`, { params: this.params });
  }

  getDiplomaByIdentifiant(identifiant) {
    return this.http.get<Diploma>(`${environment.apiUrl}/diploma/` + identifiant);
  }

  createDiploma(params) {
    this.params = params;
    return this.http.post<Diploma>(`${environment.apiUrl}/diploma/`, params);
  }

  editDiploma(params, id) {
    this.params = params;
    return this.http.put<Diploma>(`${environment.apiUrl}/diploma/` + id, params);
  }

  deleteDiploma(id) {
    return this.http.delete<Diploma>(`${environment.apiUrl}/diploma/` + id);
  }
}
