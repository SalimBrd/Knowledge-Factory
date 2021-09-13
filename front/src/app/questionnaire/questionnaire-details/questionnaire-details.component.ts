import { Component, OnInit } from '@angular/core';
import { Course } from '@app/models/course';
import { Questionnaire } from '@app/models/questionnaire';
import { QuestionnaireSuccess } from '@app/models/questionnaire-success';
import { Question } from '@app/models/question';
import { Answer } from '@app/models/answer';
import { User } from '@app/models/user';
import { ActivatedRoute, Router } from '@angular/router';
import { first } from 'rxjs/operators';
import { UserService } from '@app/services/user.service';
import { CourseService } from '@app/services/course.service';
import { QuestionnaireService } from '@app/services/questionnaire.service';
import { QuestionnaireSuccessService } from '@app/services/questionnaire-success.service';
import { DiplomaService } from '@app/services/diploma.service';
import { AuthenticationService } from '@app/services/authentication.service';
import * as $ from 'jquery/dist/jquery.min.js';

@Component({
  selector: 'app-questionnaire-details',
  templateUrl: './questionnaire-details.component.html',
  styleUrls: ['./questionnaire-details.component.css']
})
export class QuestionnaireDetailsComponent implements OnInit {
  courses: Course[];
  course: Course;
  questionnaire: Questionnaire;
  questions: Question[];
  questionAnswered = [];
  answers: Answer[];
  questionnaireSuccesses: QuestionnaireSuccess[];
  failedQuestions = [];
  isCheckedMap = new Map();
  successCountRequired = new Map();
  currentUser: User;


  isUser = false;
  isAdmin = false;
  success = false;
  failedMsg = false;
  infoMsg = true;
  radioValue: number;
  questionnaireSucceeded: number = 0;
  courseId: any;
  questionnaireId: any;

  constructor(
    private activatedRoute: ActivatedRoute,
    private authenticationService: AuthenticationService,
    private router: Router,
    private userService: UserService,
    private courseService: CourseService,
    private questionnaireService: QuestionnaireService,
    private questionnaireSuccessService: QuestionnaireSuccessService,
    private diplomaService: DiplomaService
  ) { }

  ngOnInit(): void {
    this.getCurrentUser();
    this.activatedRoute.url.subscribe(u => this.courseId = u[1].path);
    this.activatedRoute.url.subscribe(u => this.questionnaireId = u[3].path);
    this.getCourseById();
    this.getQuestionnaireById();
  }

  getCurrentUser() {
    this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
    if (this.currentUser != null) {
      this.userService.me().pipe(first()).subscribe(user => {
        this.currentUser = user;
        this.isAdmin = this.currentUser.role == 'ROLE_ADMIN';
        this.isUser = this.currentUser.role == 'ROLE_USER';
      })
    }
  }

  getCourseById() {
    this.courseService.getCourseByIdentifiant(this.courseId).pipe(first()).subscribe(course => {
      this.course = course;
      this.getQuestionnaireSuccess();
      if (this.isUser && this.course.premium) this.router.navigate(['/courses']);
    })
  }

  getQuestionnaireById() {
    this.questionnaireService.getQuestionnaireByIdentifiant(this.questionnaireId).pipe(first()).subscribe(questionnaire => {
      this.questionnaire = questionnaire;
      this.questions = questionnaire.questions;
      this.getSuccessCountRequired();
    })
  }

  getQuestionnaireSuccess() {
    this.questionnaireSuccessService.getAll().pipe(first()).subscribe(questionnaireSuccess => {
      this.questionnaireSuccesses = questionnaireSuccess;
      this.checkIfSucceeded();
    })
  }

  checkIfSucceeded() {
    for (let questionnaireSuccess of this.questionnaireSuccesses) {
      if (questionnaireSuccess.questionnaireId == this.questionnaireId) {
        this.success = true;
        this.infoMsg = false;
      }
      for (let questionnaire of this.course.questionnaires) {
        if (questionnaire.id == questionnaireSuccess.questionnaireId) this.questionnaireSucceeded++;
      }
    }
  }

  // Onclick function to store checked values
  checkAnswer(event, answerId, questionId) {
    if (this.failedMsg) {
      this.clearUI();
    }
    if (event.target.type == "radio") return;

    if (event.target.checked) {
      this.isCheckedMap.set(answerId, questionId);
      this.addItem(questionId);
    }
    else {
      this.removeItem(questionId);
      this.isCheckedMap.delete(answerId);
    }
  }

  // Increment questionAnswered length if questionId is not in the array
  addItem(item) {
    if (this.questionAnswered.indexOf(item) === -1) {
      this.questionAnswered.push(item);
    }
  }

  // Lower questionAnswered length if every checkbox of a question are unchecked
  removeItem(item) {
    let count = 0;
    for (let v of this.isCheckedMap.values()) {
      if (v === item) {
        count++;
      }
    }
    if (count > 1) return;
    let index = this.questionAnswered.indexOf(item);
    this.questionAnswered.splice(index, 1);
  }

  // Function to gather the last value from a radio and then remove it from isCheckedMap
  getLastRadioValue(answerId, questionId) {
    this.radioValue = parseInt($('input[name=question-' + questionId + ']:checked').val());
    if (this.radioValue == answerId) return;
    this.isCheckedMap.set(answerId, questionId);
    this.isCheckedMap.delete(this.radioValue);
    this.addItem(questionId);
  }

  // Count how many answers required to validate a question
  getSuccessCountRequired() {
    let success;
    for (let question of this.questions) {
      success = 0;
      for (let answer of question.answers) {
        if (answer.success) success++
      }
      this.successCountRequired.set(question.id, success);
    }
  }

  // Onclick function to submit results
  submitResults() {
    //if (this.questionAnswered.length < this.questions.length) return;
    this.clearUI();
    let successCount = this.verifyResults();
    this.showFeedback();
    if (successCount > 8) {
      this.success = true;
      this.sendQuestionnaire();
    } else {
      this.failedMsg = true;
    }
  }

  // Count how many questions have been answered correctly
  verifyResults() {
    let successCount = 0;
    let count;
    this.failedQuestions = [];

    for (let question of this.questions) {
      count = 0;
      for (let answer of question.answers) {
        if (this.isCheckedMap.has(answer.id) && answer.success) count++;
        if (this.isCheckedMap.has(answer.id) && !answer.success) count--;
      }
      if (count == this.successCountRequired.get(question.id)) successCount++;
      else this.failedQuestions.push(question.id);
    }
    return successCount;
  }

  sendQuestionnaire() {
    var params = { "user": { "id": this.currentUser.id }, "questionnaire": { "id": this.questionnaireId } };
    this.questionnaireSuccessService.createQuestionnaireSuccess(params).pipe(first()).subscribe(questionnaireSuccess => {
      this.questionnaireSucceeded++;
      if (this.questionnaireSucceeded == this.course.questionnaires.length) this.createDiploma();
    })
  }

  createDiploma() {
    var params = { "user": { "id": this.currentUser.id }, "course": { "id": this.courseId } };
    this.diplomaService.createDiploma(params).pipe(first()).subscribe(diploma => {
    })
  }

  // Clear UI messages/colors
  clearUI() {
    $(".question").removeClass("failed");
    $(".question").removeClass("success");
    this.failedMsg = false;
    this.infoMsg = true;
  }

  // Update UI to show failed/successed questions and uncheck all
  showFeedback() {
    this.infoMsg = false;
    this.failedQuestions.forEach(fq => {
      $(".question-" + fq).addClass("failed");
    })
    $(".question").not(".failed").addClass("success");
    this.questionAnswered = [];
    this.isCheckedMap.clear();
    $(":checkbox, :radio").prop("checked", false);
  }

}
