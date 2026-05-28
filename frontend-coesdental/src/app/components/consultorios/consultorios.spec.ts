import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Consultorios } from './consultorios';

describe('Consultorios', () => {
  let component: Consultorios;
  let fixture: ComponentFixture<Consultorios>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Consultorios],
    }).compileComponents();

    fixture = TestBed.createComponent(Consultorios);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
