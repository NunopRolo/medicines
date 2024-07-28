import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalAddMedicationComponent } from './modal-add-medication.component';

describe('ModalAddRegularMedicineComponent', () => {
  let component: ModalAddMedicationComponent;
  let fixture: ComponentFixture<ModalAddMedicationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModalAddMedicationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModalAddMedicationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
