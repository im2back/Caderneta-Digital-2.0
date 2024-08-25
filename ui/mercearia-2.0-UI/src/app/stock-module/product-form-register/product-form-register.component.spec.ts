import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductFormRegisterComponent } from './product-form-register.component';

describe('ProductFormRegisterComponent', () => {
  let component: ProductFormRegisterComponent;
  let fixture: ComponentFixture<ProductFormRegisterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductFormRegisterComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ProductFormRegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
