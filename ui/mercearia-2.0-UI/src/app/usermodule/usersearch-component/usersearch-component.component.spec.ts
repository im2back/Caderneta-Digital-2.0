import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UsersearchComponentComponent } from './usersearch-component.component';

describe('UsersearchComponentComponent', () => {
  let component: UsersearchComponentComponent;
  let fixture: ComponentFixture<UsersearchComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UsersearchComponentComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UsersearchComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
