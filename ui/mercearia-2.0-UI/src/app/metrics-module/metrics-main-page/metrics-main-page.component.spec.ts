import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MetricsMainPageComponent } from './metrics-main-page.component';

describe('MetricsMainPageComponent', () => {
  let component: MetricsMainPageComponent;
  let fixture: ComponentFixture<MetricsMainPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MetricsMainPageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MetricsMainPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
