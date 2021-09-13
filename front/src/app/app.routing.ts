import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './home/home.component';
import { LoginComponent } from './authentication/login/login.component';
import { AuthGuard } from './helpers/auth.guard';
import { UserComponent } from './user/user.component';
import { SurveyComponent } from './survey/survey.component';
import { WorkshopComponent } from './workshop/workshop.component';
import { CourseComponent } from './course/course.component';
import { CourseDetailsComponent } from './course/course-details/course-details.component';
import { QuestionnaireDetailsComponent } from './questionnaire/questionnaire-details/questionnaire-details.component';
import { BookComponent } from './book/book.component';
import { BookDetailsComponent } from './book/book-details/book-details.component';
import { PurchaseComponent } from './book/purchase/purchase.component';
import { UserDetailsComponent } from './user/user-details/user-details.component';
import { SettingsComponent } from './user/settings/settings.component';
import { Error404Component } from './shared/error404/error404.component';
import { AddressComponent } from './address/address.component';
import { AddressDetailsComponent } from './address/address-details/address-details.component';
import { AddressCreateComponent } from './address/address-create/address-create.component';
import { RegisterComponent } from './authentication/register/register.component';
import { SubscriptionComponent } from './subscription/subscription.component';
import { CourseListComponent } from './admin/course-list/course-list.component';
import { CourseEditComponent } from './admin/course/course-edit/course-edit.component';
import { CourseCreateComponent } from './admin/course/course-create/course-create.component';
import { QuestionsListComponent } from './admin/questions-list/questions-list.component';
import { AnswerListComponent } from './admin/answer-list/answer-list.component';
import { QuestionnaireListComponent } from './admin/questionnaire-list/questionnaire-list.component';
import { BookListComponent } from './admin/book-list/book-list.component';
import { BookCreateComponent } from './admin/book/book-create/book-create.component';
import { BookEditComponent } from './admin/book/book-edit/book-edit.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'address', component: AddressComponent, canActivate: [AuthGuard] },
  { path: 'user/:id/create', component: AddressCreateComponent, canActivate: [AuthGuard] },
  { path: 'user', component: UserComponent, canActivate: [AuthGuard] },
  { path: 'user/:id', component: UserDetailsComponent, canActivate: [AuthGuard] },
  { path: 'user/:id/settings', component: SettingsComponent, canActivate: [AuthGuard] },
  { path: 'user/:id/address/:id', component: AddressDetailsComponent, canActivate: [AuthGuard] },
  { path: 'surveys', component: SurveyComponent, canActivate: [AuthGuard] },
  { path: 'workshops', component: WorkshopComponent, canActivate: [AuthGuard] },
  { path: 'courses', component: CourseComponent, canActivate: [AuthGuard] },
  { path: 'courses/:id', component: CourseDetailsComponent, canActivate: [AuthGuard] },
  { path: 'courses/:id/questionnaire/:id', component: QuestionnaireDetailsComponent, canActivate: [AuthGuard] },
  { path: 'books', component: BookComponent, canActivate: [AuthGuard] },
  { path: 'books/:id', component: BookDetailsComponent, canActivate: [AuthGuard] },
  { path: 'books/:id/purchase', component: PurchaseComponent, canActivate: [AuthGuard] },
  { path: 'subscription', component: SubscriptionComponent, canActivate: [AuthGuard] },
  { path: 'admin/books', component: BookListComponent, canActivate: [AuthGuard] },
  { path: 'admin/books/create', component: BookCreateComponent, canActivate: [AuthGuard] },
  { path: 'admin/books/:id', component: BookEditComponent, canActivate: [AuthGuard] },
  { path: 'admin/courses', component: CourseListComponent, canActivate: [AuthGuard] },
  { path: 'admin/courses/create', component: CourseCreateComponent, canActivate: [AuthGuard] },
  { path: 'admin/courses/:id', component: CourseEditComponent, canActivate: [AuthGuard] },
  { path: 'admin/questionnaires', component: QuestionnaireListComponent, canActivate: [AuthGuard] },
  { path: 'admin/answers', component: AnswerListComponent, canActivate: [AuthGuard] },
  { path: 'admin/questions', component: QuestionsListComponent, canActivate: [AuthGuard] },

  // otherwise redirect to home
  { path: '**', component: Error404Component}
];

export const appRoutingModule = RouterModule.forRoot(routes);
