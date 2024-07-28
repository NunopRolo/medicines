import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DayPeriodsComponent } from './day-periods.component';

describe('DayPeriodsComponent', () => {
  let component: DayPeriodsComponent;
  let fixture: ComponentFixture<DayPeriodsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DayPeriodsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DayPeriodsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
