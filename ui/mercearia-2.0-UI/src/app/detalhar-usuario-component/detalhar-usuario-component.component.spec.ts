import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetalharUsuarioComponentComponent } from './detalhar-usuario-component.component';

describe('DetalharUsuarioComponentComponent', () => {
  let component: DetalharUsuarioComponentComponent;
  let fixture: ComponentFixture<DetalharUsuarioComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetalharUsuarioComponentComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DetalharUsuarioComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
