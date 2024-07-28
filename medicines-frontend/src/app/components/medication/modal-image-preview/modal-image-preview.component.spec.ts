import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalImagePreviewComponent } from './modal-image-preview.component';

describe('ModalImagePreviewComponent', () => {
  let component: ModalImagePreviewComponent;
  let fixture: ComponentFixture<ModalImagePreviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModalImagePreviewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ModalImagePreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
