import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MisCitasPaciente } from './mis-citas-paciente';

describe('MisCitasPaciente', () => {
  let component: MisCitasPaciente;
  let fixture: ComponentFixture<MisCitasPaciente>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MisCitasPaciente],
    }).compileComponents();

    fixture = TestBed.createComponent(MisCitasPaciente);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
