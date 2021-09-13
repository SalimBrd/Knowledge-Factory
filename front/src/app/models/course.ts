import { Questionnaire } from './questionnaire'

export class Course {
  id: number;
  title: string;
  difficulty: string;
  premium: number;
  suggestedHours: number;
  questionnaires: Questionnaire[];
}
