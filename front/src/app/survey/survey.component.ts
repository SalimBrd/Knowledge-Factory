import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { Survey } from '../models/survey';
import { SurveyChoice } from '../models/survey-choice';
import { SurveyAnswer } from '../models/survey-answer';
import { User } from '../models/user';
import { first } from 'rxjs/operators';
import { UserService } from '@app/services/user.service';
import { SurveyService } from '@app/services/survey.service';
import { SurveyChoiceService } from '@app/services/survey-choice.service';
import { SurveyAnswerService } from '@app/services/survey-answer.service';
import { AuthenticationService } from '@app/services/authentication.service';
import * as $ from 'jquery/dist/jquery.min.js';

@Component({
  selector: 'app-survey',
  templateUrl: './survey.component.html',
  styleUrls: ['./survey.component.css']
})
export class SurveyComponent implements OnInit {
  voted: number = 0;
  votedPremium: number = 0;
  surveyChoices: SurveyChoice[] = [];
  survey: Survey = new Survey();
  surveys: Survey[];
  premiumSurveys: Survey[];
  surveyAnswers: SurveyAnswer[];
  surveyAnswer: SurveyAnswer;
  surveyAnswerPremium: SurveyAnswer;
  currentUser: User;
  isAdmin = false;
  isSub = false;
  isUser = false;
  loading = false;
  resultShown = false;
  resultShownPremium = false;

  constructor(
    private authenticationService: AuthenticationService,
    private userService: UserService,
    private surveyService: SurveyService,
    private surveyChoiceService: SurveyChoiceService,
    private surveyAnswerService: SurveyAnswerService
  ) { }

  ngOnInit(): void {
    this.getCurrentUser();
    this.initSurveyChoices();
  }

  vote(id, premium) {
    var params = { "user": { "id": this.currentUser.id }, "surveyChoice": { "id": id } };
    this.surveyAnswerService.createSurveyAnswer(params).pipe(first()).subscribe(surveyAnswer => {
      if (this.voted > 0 && !premium || this.votedPremium > 0 && premium) this.removeOldAnswer(premium);
      if (!premium) {
        this.surveyAnswer = surveyAnswer;
        this.voted = this.surveyAnswer.surveyChoiceId;
      } else {
        this.surveyAnswerPremium = surveyAnswer;
        this.votedPremium = this.surveyAnswerPremium.surveyChoiceId;
      }
    })
  }

  getCurrentUser(): void {
    this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
    if (this.currentUser != null) {
      this.userService.me().pipe(first()).subscribe(user => {
        this.currentUser = user
        this.isAdmin = this.currentUser.role == 'ROLE_ADMIN';
        this.isSub = this.currentUser.role == 'ROLE_SUBSCRIBER';
        this.isUser = this.currentUser.role == 'ROLE_USER';
        this.getSurveys();
      })
    }
  }

  getSurveys(): void {
    this.surveyService.getCurrentSurveys().pipe(first()).subscribe(surveys => {
      this.surveys = surveys;
      this.getPremiumSurveys();
      this.hoverSquare();
    })
  }

  getPremiumSurveys(): void {
    this.surveyService.getCurrentPremiumSurveys().pipe(first()).subscribe(surveys => {
      this.premiumSurveys = surveys;
      this.getUserSurveyAnswers();
    })
  }

  getUserSurveyAnswers() {
    this.surveyAnswerService.getUserSurveyAnswers().pipe(first()).subscribe(surveyAnswers => {
      this.surveyAnswers = surveyAnswers;
      this.getVoted(this.surveys[0]);
      this.getVoted(this.premiumSurveys[0]);
    })
  }

  removeOldAnswer(premium) {
    let id = 0;
    if (!premium) id = this.surveyAnswer.id;
    else id = this.surveyAnswerPremium.id;
    this.surveyAnswerService.deleteSurveyAnswer(id).pipe(first()).subscribe(surveyAnswer => {
    })
  }

  hoverSquare() {
    $(".survey-choices").ready(function () {
      $(".square").hover(function () {
        $(this).children("button").css("display", "block");
        $(this).children(".mask").css("background", "rgba(0,0,0,0.5)")
      }, function () {
        $(this).children("button").css("display", "none");
        $(this).children(".mask").css("background", "rgba(0,0,0,0)")
      })
    })
  }

  getVoted(surveys: Survey) {
    this.surveyAnswers.forEach(sa => {
      surveys.surveyChoices.forEach(sc => {
        if (sa.surveyChoiceId == sc.id) {
          if (surveys.premium) {
            this.votedPremium = sc.id;
            this.surveyAnswerPremium = sa;
          } else {
            this.voted = sc.id;
            this.surveyAnswer = sa;
          }
        }
      })
    })
  }

  toggleResults(premium) {

    if (this.resultShown && !premium) {
      $(".default .result-mask").empty();
      $(".default .result-mask").css("border-bottom", "none");
      this.resultShown = false;
      return;
    }

    if (this.resultShownPremium && premium) {
      $(".premium .result-mask").empty();
      $(".premium .result-mask").css("border-bottom", "none");
      this.resultShownPremium = false;
      return;
    }

    let percentage = 0;
    let results: number[];
    results = [0, 0, 0, 0];
    let total = 0;
    for (let i = 0; i < 4; i++) {
      if (premium) results[i] = this.premiumSurveys[1].surveyChoices[i].surveyAnswers.length;
      else results[i] = this.surveys[1].surveyChoices[i].surveyAnswers.length;
      total += results[i];
    }

    for (let i = 0; i < 4; i++) {
      total > 0 ? percentage = this.precise(results[i] / total) : percentage = 0;
      if (premium) {
        this.resultShownPremium = true;
        $(".premium .result-" + (i + 1)).css("border-bottom", percentage * 125 + "px solid rgba(255,255,255,0.4)").append(percentage * 100 + "%");
      } else {
        this.resultShown = true;
        $(".default .result-" + (i + 1)).css("border-bottom", percentage * 125 + "px solid rgba(255,255,255,0.4)").append(percentage * 100 + "%");
      }
    }
  }

  precise(x) {
    return parseFloat(x.toPrecision(2));
  }

  initSurveyChoices() {
    for (let i = 1; i <= 4; i++) {
      let surveyChoice = new SurveyChoice;
      surveyChoice.content = '';
      surveyChoice.placement = i;
      this.surveyChoices.push(surveyChoice);
    }
  }

  createSurvey(premium) {
    this.survey.premium = premium;
    this.surveyService.createSurvey(this.survey).pipe(first()).subscribe(survey => {
      for (let choice of this.surveyChoices) {
        choice.survey = survey;
        this.createSurveyChoice(choice);
      }
    })
  }

  createSurveyChoice(choice) {
    this.surveyChoiceService.createSurveyChoice(choice).pipe(first()).subscribe(surveyChoice => {
    })
  }
}
