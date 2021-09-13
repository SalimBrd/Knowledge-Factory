import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { environment } from '@environments/environment';
import { Questionnaire } from '@app/models/questionnaire';
import { AuthenticationService } from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class QuestionnaireService {
  params: any;
  constructor(private http: HttpClient) { }

  getAll(params?) {
    this.params = params;
    return this.http.get<Questionnaire[]>(`${environment.apiUrl}/questionnaire`, { params: this.params });
  }

  getQuestionnaireByIdentifiant(identifiant) {
    return this.http.get<Questionnaire>(`${environment.apiUrl}/questionnaire/` + identifiant);
  }

  createQuestionnaire(params) {
    this.params = params;
    return this.http.post<Questionnaire>(`${environment.apiUrl}/questionnaire/`, params);
  }

  editQuestionnaire(params, id) {
    this.params = params;
    return this.http.put<Questionnaire>(`${environment.apiUrl}/questionnaire/` + id, params);
  }

  deleteQuestionnaire(id) {
    return this.http.delete<Questionnaire>(`${environment.apiUrl}/questionnaire/` + id);
  }
}
