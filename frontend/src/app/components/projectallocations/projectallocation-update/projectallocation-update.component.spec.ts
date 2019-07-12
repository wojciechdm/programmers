import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectallocationUpdateComponent } from './projectallocation-update.component';

describe('ProjectallocationUpdateComponent', () => {
  let component: ProjectallocationUpdateComponent;
  let fixture: ComponentFixture<ProjectallocationUpdateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProjectallocationUpdateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectallocationUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
