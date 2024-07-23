import { TestBed } from '@angular/core/testing';

import { VisibilityServiceService } from './visibility-service.service';

describe('VisibilityServiceService', () => {
  let service: VisibilityServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VisibilityServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
