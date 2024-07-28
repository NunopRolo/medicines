import {Component, OnInit} from '@angular/core';
import {AsyncPipe, NgForOf} from "@angular/common";
import {
    ButtonDirective,
    CardBodyComponent,
    CardComponent,
    CardHeaderComponent,
    ColComponent,
    RowComponent, TemplateIdDirective, WidgetStatCComponent, WidgetStatFComponent
} from "@coreui/angular";
import {IconDirective} from "@coreui/icons-angular";
import {TranslateModule} from "@ngx-translate/core";
import {MedicationService} from "../../../services/medication.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {ModalAddDayPeriodComponent} from "./modal-add-day-period/modal-add-day-period.component";

@Component({
  selector: 'app-day-periods',
  standalone: true,
    imports: [
        AsyncPipe,
        ButtonDirective,
        CardBodyComponent,
        CardComponent,
        CardHeaderComponent,
        ColComponent,
        IconDirective,
        NgForOf,
        RowComponent,
        TemplateIdDirective,
        TranslateModule,
        WidgetStatFComponent,
        WidgetStatCComponent
    ],
  templateUrl: './day-periods.component.html',
  styleUrl: './day-periods.component.scss'
})
export class DayPeriodsComponent implements OnInit{
    constructor(
        readonly medicationService: MedicationService,
        readonly modalService: NgbModal
    ) {
    }

    ngOnInit() {
        this.medicationService.getDayPeriods();
    }

    addDayPeriod(){
        this.modalService.open(ModalAddDayPeriodComponent, {size: "sm"});
    }
}
