import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { environment } from '@environments/environment';
import { Book } from '@app/models/book';
import { AuthenticationService } from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class BookService {
  params: any;
  constructor(private http: HttpClient) { }

  getAll(params?) {
    this.params = params;
    return this.http.get<Book[]>(`${environment.apiUrl}/book`, { params: this.params });
  }

  getBookByIdentifiant(identifiant) {
    return this.http.get<Book>(`${environment.apiUrl}/book/` + identifiant);
  }

  createBook(params) {
    this.params = params;
    return this.http.post<Book>(`${environment.apiUrl}/book/`, params);
  }

  editBook(params, id) {
    this.params = params;
    return this.http.put<Book>(`${environment.apiUrl}/book/` + id, params);
  }

  deleteBook(id) {
    return this.http.delete<Book>(`${environment.apiUrl}/book/` + id);
  }
}
