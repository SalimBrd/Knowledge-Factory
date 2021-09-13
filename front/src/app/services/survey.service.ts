import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { environment } from '@environments/environment';
import { Survey } from '@app/models/survey';
import { AuthenticationService } from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class SurveyService {
  params: any;
  constructor(private http: HttpClient) { }

  getAll(params?) {
    this.params = params;
    return this.http.get<Survey[]>(`${environment.apiUrl}/survey`, { params: this.params });
  }

  getCurrentSurveys() {
    return this.http.get<Survey[]>(`${environment.apiUrl}/survey/default`);
  }

  getCurrentPremiumSurveys() {
    return this.http.get<Survey[]>(`${environment.apiUrl}/survey/premium`);
  }

  getSurveyByIdentifiant(identifiant) {
    return this.http.get<Survey>(`${environment.apiUrl}/survey/` + identifiant);
  }

  createSurvey(params) {
    this.params = params;
    return this.http.post<Survey>(`${environment.apiUrl}/survey/`, params);
  }

  editSurvey(params, id) {
    this.params = params;
    return this.http.put<Survey>(`${environment.apiUrl}/survey/` + id, params);
  }

  deleteSurvey(id) {
    return this.http.delete<Survey>(`${environment.apiUrl}/survey/` + id);
  }
}
