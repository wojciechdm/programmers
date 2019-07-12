import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectallocationSaveComponent } from './projectallocation-save.component';

describe('ProjectallocationSaveComponent', () => {
  let component: ProjectallocationSaveComponent;
  let fixture: ComponentFixture<ProjectallocationSaveComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProjectallocationSaveComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectallocationSaveComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
