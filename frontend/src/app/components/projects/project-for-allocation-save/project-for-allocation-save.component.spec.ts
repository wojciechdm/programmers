import { async, ComponentFixture, TestBed } from "@angular/core/testing";

import { ProjectForAllocationSaveComponent } from "./project-for-allocation-save.component";

describe("ProjectForAllocationSaveComponent", () => {
  let component: ProjectForAllocationSaveComponent;
  let fixture: ComponentFixture<ProjectForAllocationSaveComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ProjectForAllocationSaveComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectForAllocationSaveComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });
});
