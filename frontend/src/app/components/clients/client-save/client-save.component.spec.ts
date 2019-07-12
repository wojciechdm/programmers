import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientSaveComponent } from './client-save.component';

describe('ClientSaveComponent', () => {
  let component: ClientSaveComponent;
  let fixture: ComponentFixture<ClientSaveComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ClientSaveComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientSaveComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
