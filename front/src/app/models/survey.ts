import { SurveyChoice } from "./survey-choice";

export class Survey {
  id: number;
  title: string;
  description: string;
  premium: boolean;
  activeDate: Date;
  surveyChoices: SurveyChoice[];
}
