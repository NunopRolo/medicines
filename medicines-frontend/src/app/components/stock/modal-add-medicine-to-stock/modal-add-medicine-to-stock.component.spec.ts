import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalAddMedicineToStockComponent } from './modal-add-medicine-to-stock.component';

describe('ModalAddMedicineToStockComponent', () => {
  let component: ModalAddMedicineToStockComponent;
  let fixture: ComponentFixture<ModalAddMedicineToStockComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModalAddMedicineToStockComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ModalAddMedicineToStockComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
