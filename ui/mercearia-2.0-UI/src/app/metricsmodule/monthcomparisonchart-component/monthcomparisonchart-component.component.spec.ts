import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MonthcomparisonchartComponentComponent } from './monthcomparisonchart-component.component';

describe('MonthcomparisonchartComponentComponent', () => {
  let component: MonthcomparisonchartComponentComponent;
  let fixture: ComponentFixture<MonthcomparisonchartComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MonthcomparisonchartComponentComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MonthcomparisonchartComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
