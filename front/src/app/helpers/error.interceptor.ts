import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { AuthenticationService } from '@app/services/authentication.service';

import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Router } from '@angular/router';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

  constructor(private authenticationService: AuthenticationService, private router: Router) { }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (request)
      return next.handle(request).pipe(catchError(err => {

        let errorMsg = '';
        if (err.status === 0) {
          errorMsg = `Error: Unable to connect to server`;
          return throwError(errorMsg);
        }
        if (err.status === 401) {
          this.authenticationService.logout();
        }
        if (err.error instanceof ErrorEvent) {
          errorMsg = `Error: ${err.error.message}`;
        }
        else {
          if (err.error)
            errorMsg = `${err.error[Object.keys(err.error)[0]]}`;
        }

        if (err.status === 404) {
          this.router.navigate(['404']);
        }
        return throwError(errorMsg);
      })
      )
  }
}
