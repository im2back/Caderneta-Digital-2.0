import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ValuescomponentComponent } from './valuescomponent.component';

describe('ValuescomponentComponent', () => {
  let component: ValuescomponentComponent;
  let fixture: ComponentFixture<ValuescomponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ValuescomponentComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ValuescomponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
