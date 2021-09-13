import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { environment } from '@environments/environment';
import { Question } from '@app/models/question';
import { AuthenticationService } from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class QuestionService {
  params: any;
  constructor(private http: HttpClient) { }

  getAll(params?) {
    this.params = params;
    return this.http.get<Question[]>(`${environment.apiUrl}/question`, { params: this.params });
  }

  getQuestionByIdentifiant(identifiant) {
    return this.http.get<Question>(`${environment.apiUrl}/question/` + identifiant);
  }

  createQuestion(params) {
    this.params = params;
    return this.http.post<Question>(`${environment.apiUrl}/question/`, params);
  }

  editQuestion(params, id) {
    this.params = params;
    return this.http.put<Question>(`${environment.apiUrl}/question/` + id, params);
  }

  deleteQuestion(id) {
    return this.http.delete<Question>(`${environment.apiUrl}/question/` + id);
  }
}
