import {Component, OnDestroy, OnInit} from '@angular/core';
import {
    ButtonDirective, ButtonGroupModule, ButtonModule,
    CardBodyComponent,
    CardComponent, CardFooterComponent,
    CardHeaderComponent, CardModule, CardTextDirective,
    ColComponent, FormModule, GridModule,
    TableDirective
} from "@coreui/angular";
import {AsyncPipe, CommonModule, DatePipe, KeyValuePipe, NgForOf} from "@angular/common";
import {IconDirective} from "@coreui/icons-angular";
import {MedicineCardComponent} from "./medicine-card/medicine-card.component";
import {MedicationService} from "../../services/medication.service";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {
    ModalAddMedicineToStockComponent
} from "../stock/modal-add-medicine-to-stock/modal-add-medicine-to-stock.component";
import {ModalAddMedicationComponent} from "./modal-add-medication/modal-add-medication.component";
import {NgbModal, NgbTooltip} from "@ng-bootstrap/ng-bootstrap";
import {environment} from "../../../environments/environment";
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {faPlus, IconDefinition} from '@fortawesome/free-solid-svg-icons';
import {TranslateModule} from "@ngx-translate/core";
import {PersonService} from "../../services/person-service";
import {Subscription} from "rxjs";
import {DayPeriod} from "../../models/DayPeriod";

@Component({
  selector: 'app-medication',
  standalone: true,
    imports: [
        CardBodyComponent,
        CardComponent,
        CardHeaderComponent,
        ColComponent,
        TableDirective,
        ButtonDirective,
        CardFooterComponent,
        CardTextDirective,
        DatePipe,
        IconDirective,
        MedicineCardComponent,
        KeyValuePipe,
        NgForOf,
        AsyncPipe,
        FormsModule,
        ModalAddMedicineToStockComponent,
        ModalAddMedicationComponent,
        FontAwesomeModule,
        NgbTooltip,
        TranslateModule,
        ButtonGroupModule,
        ButtonModule,
        CommonModule,
        CardModule,
        FormModule,
        GridModule,
        ButtonModule,
        FormsModule,
        ReactiveFormsModule
    ],
  templateUrl: './medication.component.html',
  styleUrl: './medication.component.scss'
})
export class MedicationComponent implements OnInit, OnDestroy {
    barcode: string;
    addIcon: IconDefinition = faPlus;

    personSubscription: Subscription;

    constructor(
        readonly medicationService: MedicationService,
        readonly modalService: NgbModal,
        readonly personService: PersonService
    ) {}

    ngOnInit(){
        this.personSubscription = this.personService.selectedPerson$.subscribe(person => {
            this.medicationService.getAllMedications();
        });
    }

    ngOnDestroy() {
        this.personSubscription.unsubscribe();
    }

    openAddMedicineModal(dayPeriod: DayPeriod){
        const modalRef = this.modalService.open(ModalAddMedicationComponent, {size: "xl"});
        modalRef.componentInstance.dayPeriod = dayPeriod;
    }

    downloadPdf(){
        window.open(environment.apiUrl+"/medication/pdf/"+this.medicationService.getMedicationContent()+"/"+this.personService.getSelectedPerson().personUuid, "_blank")
    }
}
