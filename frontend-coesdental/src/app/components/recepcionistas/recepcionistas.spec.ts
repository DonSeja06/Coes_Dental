import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Recepcionistas } from './recepcionistas';

describe('Recepcionistas', () => {
  let component: Recepcionistas;
  let fixture: ComponentFixture<Recepcionistas>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Recepcionistas],
    }).compileComponents();

    fixture = TestBed.createComponent(Recepcionistas);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
