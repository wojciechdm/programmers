import { TestBed } from '@angular/core/testing';

import { Loader.InterceptorService } from './loader.interceptor.service';

describe('Loader.InterceptorService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: Loader.InterceptorService = TestBed.get(Loader.InterceptorService);
    expect(service).toBeTruthy();
  });
});
