import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PurchaseRandomComponent } from './purchase-random.component';

describe('PurchaseRandomComponent', () => {
  let component: PurchaseRandomComponent;
  let fixture: ComponentFixture<PurchaseRandomComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PurchaseRandomComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PurchaseRandomComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
