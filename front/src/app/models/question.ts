import { Questionnaire } from './questionnaire';
import { Answer } from './answer';

export class Question {
  id: number;
  content: string;
  multiple_answers: boolean;
  placement: number;
  questionnaire: Questionnaire;
  answers: Answer[];
}
