import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PurchasecomponentComponent } from './purchasecomponent.component';

describe('PurchasecomponentComponent', () => {
  let component: PurchasecomponentComponent;
  let fixture: ComponentFixture<PurchasecomponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PurchasecomponentComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PurchasecomponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
