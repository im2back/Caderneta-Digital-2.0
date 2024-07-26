import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserformregisterComponentComponent } from './userformregister-component.component';

describe('UserformregisterComponentComponent', () => {
  let component: UserformregisterComponentComponent;
  let fixture: ComponentFixture<UserformregisterComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserformregisterComponentComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UserformregisterComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
