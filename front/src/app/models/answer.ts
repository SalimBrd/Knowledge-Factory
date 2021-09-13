import { Question } from './question';

export class Answer {
  id: number;
  content: string;
  placement: number;
  success: boolean;
  question: Question;
}
