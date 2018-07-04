import { TestBed, inject } from '@angular/core/testing';

import { GraphQlService } from './graph-ql.service';

describe('GraphQlService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [GraphQlService]
    });
  });

  it('should be created', inject([GraphQlService], (service: GraphQlService) => {
    expect(service).toBeTruthy();
  }));
});
