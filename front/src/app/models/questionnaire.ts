import { Course } from './course';
import { Question } from './question';

export class Questionnaire {
  id: number;
  title: string;
  description: string;
  placement: string;
  course: Course;
  questions: Question[];
}
