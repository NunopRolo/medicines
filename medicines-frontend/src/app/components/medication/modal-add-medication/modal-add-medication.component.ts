import {Component, Input} from '@angular/core';
import {
    ButtonCloseDirective,
    ButtonDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, FormControlDirective,
    FormLabelDirective,
    InputGroupComponent,
    ModalModule
} from "@coreui/angular";
import {MedicationService} from "../../../services/medication.service";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {IconDirective} from "@coreui/icons-angular";
import {MedicinesComponent} from "../../medicines/medicines.component";
import {MedicinesTableComponent} from "../../medicines/medicines-table/medicines-table.component";
import {TranslateModule} from "@ngx-translate/core";
import {MedicationRequest} from "../../../models/MedicationRequest";
import {PersonService} from "../../../services/person-service";
import {DayPeriod} from "../../../models/DayPeriod";
import {DatePipe, NgIf} from "@angular/common";
import {TemporaryMedicationRequest} from "../../../models/TemporaryMedicationRequest";

@Component({
    selector: 'app-modal-add-medication',
    standalone: true,
    imports: [ModalModule, ButtonCloseDirective, ButtonDirective, FormLabelDirective, FormsModule, IconDirective, InputGroupComponent, MedicinesComponent, MedicinesTableComponent, FormControlDirective, ReactiveFormsModule, TranslateModule, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, NgIf, DatePipe],
    templateUrl: './modal-add-medication.component.html',
    styleUrl: './modal-add-medication.component.scss'
})
export class ModalAddMedicationComponent {
    @Input() dayPeriod: DayPeriod;
    barcode: string;

    medicationForm = new FormGroup({
        observations: new FormControl(''),
        isTemporary: new FormControl(false),
        days: new FormControl(0),
        start_date: new FormControl(new Date())
    });

    constructor(
        readonly medicationService: MedicationService,
        readonly personService: PersonService,
        public activeModal: NgbActiveModal
    ) {

    }

    saveMedicine() {
        let medicationRequest: MedicationRequest = {
            barcode: this.barcode,
            dayPeriodId: this.dayPeriod.id,
            observations: this.medicationForm.value.observations ?? '',
            personUuid: this.personService.getSelectedPerson().personUuid
        };

        if(this.medicationForm.value.isTemporary){
            let temporaryMedicationRequest: TemporaryMedicationRequest = {
                medication: medicationRequest,
                days: this.medicationForm.value.days ?? 0,
                startDate: this.medicationForm.value.start_date ?? new Date(),
            };

            this.medicationService.addTemporaryMedicine(temporaryMedicationRequest);
        }else{
            this.medicationService.addMedicine(medicationRequest);
        }

        this.activeModal.close();
        this.medicationService.getAllMedications();
    }

    onChangeMedicineSelection(event: any) {
        this.barcode = event.target.value;
    }
}
