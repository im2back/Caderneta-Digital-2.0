import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PurchaseMainPageComponent } from './purchase-main-page.component';

describe('PurchaseMainPageComponent', () => {
  let component: PurchaseMainPageComponent;
  let fixture: ComponentFixture<PurchaseMainPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PurchaseMainPageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PurchaseMainPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
