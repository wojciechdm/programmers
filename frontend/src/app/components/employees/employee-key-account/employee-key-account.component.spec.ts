import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeKeyAccountComponent } from './employee-key-account.component';

describe('EmployeeKeyAccountComponent', () => {
  let component: EmployeeKeyAccountComponent;
  let fixture: ComponentFixture<EmployeeKeyAccountComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EmployeeKeyAccountComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EmployeeKeyAccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
