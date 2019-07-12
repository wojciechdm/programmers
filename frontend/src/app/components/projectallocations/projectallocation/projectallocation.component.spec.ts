import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectallocationComponent } from './projectallocation.component';

describe('ProjectallocationComponent', () => {
  let component: ProjectallocationComponent;
  let fixture: ComponentFixture<ProjectallocationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProjectallocationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectallocationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
