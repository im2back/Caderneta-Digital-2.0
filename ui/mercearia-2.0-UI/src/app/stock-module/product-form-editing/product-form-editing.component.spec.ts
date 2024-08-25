import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductFormEditingComponent } from './product-form-editing.component';

describe('ProductFormEditingComponent', () => {
  let component: ProductFormEditingComponent;
  let fixture: ComponentFixture<ProductFormEditingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductFormEditingComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ProductFormEditingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
