import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserdetailComponentComponent } from './userdetail-component.component';

describe('UserdetailComponentComponent', () => {
  let component: UserdetailComponentComponent;
  let fixture: ComponentFixture<UserdetailComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserdetailComponentComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UserdetailComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
