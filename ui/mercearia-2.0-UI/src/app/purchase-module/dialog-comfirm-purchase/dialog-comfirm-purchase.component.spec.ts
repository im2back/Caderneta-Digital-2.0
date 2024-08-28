import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogComfirmPurchaseComponent } from './dialog-comfirm-purchase.component';

describe('DialogComfirmPurchaseComponent', () => {
  let component: DialogComfirmPurchaseComponent;
  let fixture: ComponentFixture<DialogComfirmPurchaseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DialogComfirmPurchaseComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DialogComfirmPurchaseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
