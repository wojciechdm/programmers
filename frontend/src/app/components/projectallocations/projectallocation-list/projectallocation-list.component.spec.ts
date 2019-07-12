import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectallocationListComponent } from './projectallocation-list.component';

describe('ProjectallocationListComponent', () => {
  let component: ProjectallocationListComponent;
  let fixture: ComponentFixture<ProjectallocationListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProjectallocationListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectallocationListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
