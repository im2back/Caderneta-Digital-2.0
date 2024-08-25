import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogAddToCarComponent } from './dialog-add-to-car.component';

describe('DialogAddToCarComponent', () => {
  let component: DialogAddToCarComponent;
  let fixture: ComponentFixture<DialogAddToCarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DialogAddToCarComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DialogAddToCarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
