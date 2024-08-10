import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MetricspageComponentComponent } from './metricspage-component.component';

describe('MetricspageComponentComponent', () => {
  let component: MetricspageComponentComponent;
  let fixture: ComponentFixture<MetricspageComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MetricspageComponentComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MetricspageComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
