import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalAddDayPeriodComponent } from './modal-add-day-period.component';

describe('ModalAddDayPeriodComponent', () => {
  let component: ModalAddDayPeriodComponent;
  let fixture: ComponentFixture<ModalAddDayPeriodComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModalAddDayPeriodComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ModalAddDayPeriodComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
