import { TestBed } from '@angular/core/testing';

import { MetricsServiceService } from './metrics-service.service';

describe('MetricsServiceService', () => {
  let service: MetricsServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MetricsServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
