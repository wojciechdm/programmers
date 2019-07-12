import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectallocationShowComponent } from './projectallocation-show.component';

describe('ProjectallocationShowComponent', () => {
  let component: ProjectallocationShowComponent;
  let fixture: ComponentFixture<ProjectallocationShowComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProjectallocationShowComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectallocationShowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
