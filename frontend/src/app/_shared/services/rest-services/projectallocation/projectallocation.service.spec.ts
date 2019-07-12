import { TestBed } from '@angular/core/testing';

import { ProjectallocationService } from './projectallocation.service';

describe('ProjectallocationService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ProjectallocationService = TestBed.get(ProjectallocationService);
    expect(service).toBeTruthy();
  });
});
