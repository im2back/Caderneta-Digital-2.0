import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StockMainPageComponent } from './stock-main-page.component';

describe('StockMainPageComponent', () => {
  let component: StockMainPageComponent;
  let fixture: ComponentFixture<StockMainPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StockMainPageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(StockMainPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
