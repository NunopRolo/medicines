import { Component } from '@angular/core';
import {
    ButtonCloseDirective,
    ButtonDirective,
    FormControlDirective,
    FormLabelDirective,
    ModalBodyComponent, ModalFooterComponent, ModalHeaderComponent
} from "@coreui/angular";
import {FormControl, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {TranslateModule} from "@ngx-translate/core";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {DayPeriodRequest} from "../../../../models/DayPeriodRequest";
import {MedicationService} from "../../../../services/medication.service";

@Component({
  selector: 'app-modal-add-day-period',
  standalone: true,
    imports: [
        ButtonCloseDirective,
        ButtonDirective,
        FormControlDirective,
        FormLabelDirective,
        FormsModule,
        ModalBodyComponent,
        ModalFooterComponent,
        ModalHeaderComponent,
        TranslateModule,
        ReactiveFormsModule
    ],
  templateUrl: './modal-add-day-period.component.html',
  styleUrl: './modal-add-day-period.component.scss'
})
export class ModalAddDayPeriodComponent {
    dayPeriodName = new FormControl('', {nonNullable: true});
    dayPeriodHour = new FormControl('', {nonNullable: true});

    constructor(public activeModal: NgbActiveModal,
                private medicationService: MedicationService) {
    }

    savePerson(){
        const dayPeriodRequest: DayPeriodRequest = {
            name: this.dayPeriodName.value,
            hour: this.dayPeriodHour.value
        }
        this.medicationService.addDayPeriod(dayPeriodRequest);
        this.activeModal.close();
    }
}
