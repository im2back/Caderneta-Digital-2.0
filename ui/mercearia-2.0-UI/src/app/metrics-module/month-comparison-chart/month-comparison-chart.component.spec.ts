import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MonthComparisonChartComponent } from './month-comparison-chart.component';

describe('MonthComparisonChartComponent', () => {
  let component: MonthComparisonChartComponent;
  let fixture: ComponentFixture<MonthComparisonChartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MonthComparisonChartComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MonthComparisonChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
