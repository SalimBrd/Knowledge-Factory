import { Injectable } from '@angular/core';
import { HttpClient, HttpParams  } from '@angular/common/http';

import { environment } from '@environments/environment';
import { Course } from '@app/models/course';
import { AuthenticationService } from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class CourseService {
  params: any;
  constructor(private http: HttpClient) { }

  getAll(params?) {
    this.params = params;
    return this.http.get<Course[]>(`${environment.apiUrl}/course`, { params: this.params });
  }

  getCourseByIdentifiant(identifiant) {
    return this.http.get<Course>(`${environment.apiUrl}/course/` + identifiant);
  }

  getDefaultCourseList(params?) {
    return this.http.get<Course[]>(`${environment.apiUrl}/course/default`, { params });
  }

  getPremiumCourseList(params?) {
    return this.http.get<Course[]>(`${environment.apiUrl}/course/premium`, { params });
  }

  createCourse(params) {
    this.params = params;
    return this.http.post<Course>(`${environment.apiUrl}/course/`, params);
  }

  editCourse(params, id) {
    this.params = params;
    return this.http.put<Course>(`${environment.apiUrl}/course/` + id, params);
  }

  deleteCourse(id) {
    return this.http.delete<Course>(`${environment.apiUrl}/course/` + id);
  }
}
