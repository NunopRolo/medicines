import {Component, Input} from '@angular/core';
import {ButtonCloseDirective, ButtonDirective, ModalModule} from "@coreui/angular";
import {TranslateModule} from "@ngx-translate/core";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {MedicationDto} from "../../../models/MedicationDto";
import {NgIf} from "@angular/common";
import {Medicine} from "../../../models/Medicine";
import {MedicineService} from "../../../services/medicine-service";

@Component({
  selector: 'app-modal-image-preview',
  standalone: true,
    imports: [ModalModule, TranslateModule, ButtonCloseDirective, ButtonDirective, NgIf],
  templateUrl: './modal-image-preview.component.html',
  styleUrl: './modal-image-preview.component.scss'
})
export class ModalImagePreviewComponent {
    @Input() medication: MedicationDto;

    constructor(
        public activeModal: NgbActiveModal,
        readonly medicineService: MedicineService
    ) { }

    deleteImage(medicine: Medicine){
        this.medicineService.deleteMedicineImage(medicine);
        this.activeModal.close();
    }
}
