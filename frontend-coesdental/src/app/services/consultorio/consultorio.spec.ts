import { TestBed } from '@angular/core/testing';

import { Consultorio } from './consultorio';

describe('Consultorio', () => {
  let service: Consultorio;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Consultorio);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
