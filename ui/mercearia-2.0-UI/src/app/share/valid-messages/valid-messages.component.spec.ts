import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ValidMessagesComponent } from './valid-messages.component';

describe('ValidMessagesComponent', () => {
  let component: ValidMessagesComponent;
  let fixture: ComponentFixture<ValidMessagesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ValidMessagesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ValidMessagesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
