import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatStepperModule } from '@angular/material/stepper';
import { MatInputModule } from '@angular/material/input';
import { MatTableModule } from '@angular/material/table';

import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppComponent } from './app.component';
import { LoginComponent } from './authentication/login/login.component';
import { RegisterComponent } from './authentication/register/register.component';
import { UserComponent } from './user/user.component';
import { UserDetailsComponent } from './user/user-details/user-details.component';
import { HomeComponent } from './home/home.component';

import { appRoutingModule } from './app.routing';

import { JwtInterceptor} from './helpers/jwt.interceptor';
import { ErrorInterceptor } from './helpers/error.interceptor';
import { Error404Component } from './shared/error404/error404.component';
import { RouterModule } from '@angular/router';
import { AddressComponent } from './address/address.component';
import { AddressDetailsComponent } from './address/address-details/address-details.component';
import { AddressCreateComponent } from './address/address-create/address-create.component';
import { SettingsComponent } from './user/settings/settings.component';
import { FooterComponent } from './shared/footer/footer.component';
import { WorkshopComponent } from './workshop/workshop.component';
import { SurveyComponent } from './survey/survey.component';
import { CourseComponent } from './course/course.component';
import { BookComponent } from './book/book.component';
import { CourseDetailsComponent } from './course/course-details/course-details.component';
import { QuestionnaireComponent } from './questionnaire/questionnaire.component';
import { QuestionnaireDetailsComponent } from './questionnaire/questionnaire-details/questionnaire-details.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BookDetailsComponent } from './book/book-details/book-details.component';
import { CartComponent } from './shared/cart/cart.component';
import { PurchaseComponent } from './book/purchase/purchase.component';
import { SubscriptionComponent } from './subscription/subscription.component';
import { BookListComponent } from './admin/book-list/book-list.component';
import { CourseListComponent } from './admin/course-list/course-list.component';
import { QuestionnaireListComponent } from './admin/questionnaire-list/questionnaire-list.component';
import { QuestionsListComponent } from './admin/questions-list/questions-list.component';
import { AnswerListComponent } from './admin/answer-list/answer-list.component';
import { CourseCreateComponent } from './admin/course/course-create/course-create.component';
import { CourseEditComponent } from './admin/course/course-edit/course-edit.component';
import { BookEditComponent } from './admin/book/book-edit/book-edit.component';
import { BookCreateComponent } from './admin/book/book-create/book-create.component';

@NgModule({
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    HttpClientModule,
    appRoutingModule,
    RouterModule,
    MatPaginatorModule,
    MatStepperModule,
    MatInputModule,
    MatTableModule,
    FormsModule,
    BrowserAnimationsModule
  ],
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    RegisterComponent,
    UserComponent,
    UserDetailsComponent,
    Error404Component,
    AddressComponent,
    AddressDetailsComponent,
    AddressCreateComponent,
    SettingsComponent,
    FooterComponent,
    WorkshopComponent,
    SurveyComponent,
    CourseComponent,
    BookComponent,
    CourseDetailsComponent,
    QuestionnaireComponent,
    QuestionnaireDetailsComponent,
    BookDetailsComponent,
    CartComponent,
    PurchaseComponent,
    SubscriptionComponent,
    BookListComponent,
    CourseListComponent,
    QuestionnaireListComponent,
    QuestionsListComponent,
    AnswerListComponent,
    CourseCreateComponent,
    CourseEditComponent,
    BookEditComponent,
    BookCreateComponent
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
