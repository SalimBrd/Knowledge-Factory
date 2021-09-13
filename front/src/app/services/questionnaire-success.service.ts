import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { environment } from '@environments/environment';
import { QuestionnaireSuccess } from '@app/models/questionnaire-success';

@Injectable({
  providedIn: 'root'
})
export class QuestionnaireSuccessService {
  params: any;
  constructor(private http: HttpClient) { }

  getAll(params?) {
    this.params = params;
    return this.http.get<QuestionnaireSuccess[]>(`${environment.apiUrl}/questionnaireSuccess`, { params: this.params });
  }

  getQuestionnaireSuccessByIdentifiant(identifiant) {
    return this.http.get<QuestionnaireSuccess>(`${environment.apiUrl}/questionnaireSuccess/` + identifiant);
  }

  createQuestionnaireSuccess(params) {
    this.params = params;
    return this.http.post<QuestionnaireSuccess>(`${environment.apiUrl}/questionnaireSuccess/`, params);
  }

  editQuestionnaireSuccess(params, id) {
    this.params = params;
    return this.http.put<QuestionnaireSuccess>(`${environment.apiUrl}/questionnaireSuccess/` + id, params);
  }

  deleteQuestionnaireSuccess(id) {
    return this.http.delete<QuestionnaireSuccess>(`${environment.apiUrl}/questionnaireSuccess/` + id);
  }
}
