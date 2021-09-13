import { TestBed } from '@angular/core/testing';

import { QuestionnaireSuccessService } from './questionnaire-success.service';

describe('QuestionnaireSuccessService', () => {
  let service: QuestionnaireSuccessService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(QuestionnaireSuccessService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
