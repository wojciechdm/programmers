import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientForProjectSaveComponent } from './client-for-project-save.component';

describe('ClientForProjectSaveComponent', () => {
  let component: ClientForProjectSaveComponent;
  let fixture: ComponentFixture<ClientForProjectSaveComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ClientForProjectSaveComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientForProjectSaveComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
