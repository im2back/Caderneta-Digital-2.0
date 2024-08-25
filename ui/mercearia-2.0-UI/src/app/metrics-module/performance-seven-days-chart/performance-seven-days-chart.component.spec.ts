import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PerformanceSevenDaysChartComponent } from './performance-seven-days-chart.component';

describe('PerformanceSevenDaysChartComponent', () => {
  let component: PerformanceSevenDaysChartComponent;
  let fixture: ComponentFixture<PerformanceSevenDaysChartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PerformanceSevenDaysChartComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PerformanceSevenDaysChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
