import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { environment } from '@environments/environment';
import { SurveyAnswer } from '@app/models/survey-answer';
import { AuthenticationService } from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class SurveyAnswerService {
  params: any;
  constructor(private http: HttpClient) { }

  getAll(params?) {
    this.params = params;
    return this.http.get<SurveyAnswer[]>(`${environment.apiUrl}/surveyAnswer`, { params: this.params });
  }

  getSurveyAnswerByIdentifiant(identifiant) {
    return this.http.get<SurveyAnswer>(`${environment.apiUrl}/surveyAnswer/` + identifiant);
  }

  getUserSurveyAnswers() {
    return this.http.get<SurveyAnswer[]>(`${environment.apiUrl}/surveyAnswer/user`);
  }

  createSurveyAnswer(params) {
    this.params = params;
    return this.http.post<SurveyAnswer>(`${environment.apiUrl}/surveyAnswer/`, params);
  }

  editSurveyAnswer(params, id) {
    this.params = params;
    return this.http.put<SurveyAnswer>(`${environment.apiUrl}/surveyAnswer/` + id, params);
  }

  deleteSurveyAnswer(id) {
    return this.http.delete<SurveyAnswer>(`${environment.apiUrl}/surveyAnswer/` + id);
  }
}
