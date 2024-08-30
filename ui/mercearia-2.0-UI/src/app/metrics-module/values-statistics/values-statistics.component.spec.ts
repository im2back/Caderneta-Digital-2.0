import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ValuesStatisticsComponent } from './values-statistics.component';

describe('ValuesStatisticsComponent', () => {
  let component: ValuesStatisticsComponent;
  let fixture: ComponentFixture<ValuesStatisticsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ValuesStatisticsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ValuesStatisticsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
