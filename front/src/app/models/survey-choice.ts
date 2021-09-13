import { Survey } from './survey';
import { SurveyAnswer } from './survey-answer';

export class SurveyChoice {
  id: number;
  content: string;
  placement: number;
  survey: Survey;
  surveyAnswers: SurveyAnswer[];
}
