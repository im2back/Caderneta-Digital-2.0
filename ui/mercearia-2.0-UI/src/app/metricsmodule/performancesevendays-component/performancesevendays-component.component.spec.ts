import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PerformancesevendaysComponentComponent } from './performancesevendays-component.component';

describe('PerformancesevendaysComponentComponent', () => {
  let component: PerformancesevendaysComponentComponent;
  let fixture: ComponentFixture<PerformancesevendaysComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PerformancesevendaysComponentComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PerformancesevendaysComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
