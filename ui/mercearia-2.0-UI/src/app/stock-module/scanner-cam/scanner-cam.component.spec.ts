import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScannerCamComponent } from './scanner-cam.component';

describe('ScannerCamComponent', () => {
  let component: ScannerCamComponent;
  let fixture: ComponentFixture<ScannerCamComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ScannerCamComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ScannerCamComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
