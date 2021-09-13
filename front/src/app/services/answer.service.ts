import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { environment } from 'environments/environment';
import { Answer } from '@app/models/answer';
import { AuthenticationService } from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class AnswerService {

  params: any;
  constructor(private http: HttpClient) { }

  getAll(params?) {
    this.params = params;
    return this.http.get<Answer[]>(`${environment.apiUrl}/answer`, { params: this.params });
  }

  getAnswerByIdentifiant(identifiant) {
    return this.http.get<Answer>(`${environment.apiUrl}/answer/` + identifiant);
  }

  createAnswer(params) {
    this.params = params;
    return this.http.post<Answer>(`${environment.apiUrl}/answer/`, params);
  }

  editAnswer(params, id) {
    this.params = params;
    return this.http.put<Answer>(`${environment.apiUrl}/answer/` + id, params);
  }

  deleteAnswer(id) {
    return this.http.delete<Answer>(`${environment.apiUrl}/answer/` + id);
  }
}
