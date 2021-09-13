import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { environment } from '@environments/environment';
import { SurveyChoice } from '@app/models/survey-choice';
import { AuthenticationService } from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class SurveyChoiceService {
  params: any;
  constructor(private http: HttpClient) { }

  getAll(params?) {
    this.params = params;
    return this.http.get<SurveyChoice[]>(`${environment.apiUrl}/surveyChoice`, { params: this.params });
  }

  getSurveyChoiceByIdentifiant(identifiant) {
    return this.http.get<SurveyChoice>(`${environment.apiUrl}/surveyChoice/` + identifiant);
  }

  createSurveyChoice(params) {
    this.params = params;
    return this.http.post<SurveyChoice>(`${environment.apiUrl}/surveyChoice/`, params);
  }

  editSurveyChoice(params, id) {
    this.params = params;
    return this.http.put<SurveyChoice>(`${environment.apiUrl}/surveyChoice/` + id, params);
  }

  deleteSurveyChoice(id) {
    return this.http.delete<SurveyChoice>(`${environment.apiUrl}/surveyChoice/` + id);
  }
}
