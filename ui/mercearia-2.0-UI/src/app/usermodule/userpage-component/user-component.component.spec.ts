import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserpageComponentComponent } from './userpage-component.component';

describe('UserpageComponentComponent', () => {
  let component: UserpageComponentComponent;
  let fixture: ComponentFixture<UserpageComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserpageComponentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserpageComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
