import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalAddPersonComponent } from './modal-add-person.component';

describe('ModalAddPersonComponent', () => {
  let component: ModalAddPersonComponent;
  let fixture: ComponentFixture<ModalAddPersonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModalAddPersonComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ModalAddPersonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
