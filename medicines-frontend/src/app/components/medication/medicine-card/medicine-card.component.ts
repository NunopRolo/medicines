import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {
    ButtonDirective,
    CardBodyComponent,
    CardComponent, CardFooterComponent,
    CardHeaderComponent,
    CardTextDirective, FormControlDirective, InputGroupComponent, NavModule, RowComponent, TabsModule
} from "@coreui/angular";
import {IconDirective} from "@coreui/icons-angular";
import {MedicationDto} from "../../../models/MedicationDto";
import {MedicationService} from "../../../services/medication.service";
import {NgbModal, NgbTooltip} from "@ng-bootstrap/ng-bootstrap";
import {DatePipe, NgIf, NgOptimizedImage} from "@angular/common";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {TranslateModule} from "@ngx-translate/core";
import {MedicineService} from "../../../services/medicine-service";
import {ModalImagePreviewComponent} from "../modal-image-preview/modal-image-preview.component";
import {ConfirmationModalService} from "../../../services/confirmation-modal-service";
import {TemporaryMedicationDto} from "../../../models/TemporaryMedicationDto";

@Component({
  selector: 'app-medicine-card',
  standalone: true,
    imports: [
        ButtonDirective,
        CardBodyComponent,
        CardComponent,
        CardHeaderComponent,
        CardTextDirective,
        IconDirective,
        NgbTooltip,
        CardFooterComponent,
        DatePipe,
        NgIf,
        RowComponent,
        FormControlDirective,
        ReactiveFormsModule,
        InputGroupComponent,
        FormsModule,
        TranslateModule,
        TabsModule,
        NavModule,
        NgOptimizedImage
    ],
  templateUrl: './medicine-card.component.html',
  styleUrl: './medicine-card.component.scss'
})
export class MedicineCardComponent implements OnInit{
    @Input() medication: MedicationDto;
    @Input() temporaryMedication: TemporaryMedicationDto;
    @Input() infoActive: boolean;
    @Output() deleteMethod: EventEmitter<any> = new EventEmitter<any>()

    background_color: string = "";

    isEditing: boolean = false;
    image: string;
    @Input() imageVisibility: boolean = false;

    constructor(
        readonly medicationService: MedicationService,
        readonly medicineService: MedicineService,
        readonly modalService: NgbModal,
        readonly confirmationModalService: ConfirmationModalService
    ) {

    }

    ngOnInit(){
        if(this.temporaryMedication){
            this.background_color = "#f7e2b2";
        }
    }

    toggleInput(){
        this.isEditing = !this.isEditing;
    }

    saveObservations(medication: MedicationDto){
        this.medicationService.updateMedicine(medication);
        this.toggleInput();
    }

    uploadFile($event: any){
        this.medicineService.uploadImage($event.target.files[0], this.medication.medicine);
    }

    toggleImage(){
        this.imageVisibility = !this.imageVisibility;
    }

    getIconForBottomRightButton(){
        if(this.imageVisibility){
            return "fa-info-circle";
        }
        return "fa-image";
    }

    openImageModal(medication: MedicationDto){
        const modalRef = this.modalService.open(ModalImagePreviewComponent);
        modalRef.componentInstance.medication = medication;
    }

    deleteMedication(medication: MedicationDto){
        this.confirmationModalService.confirm("Eliminar Medicamento", "Deseja mesmo eliminar o medicamento "+medication.medicine.name+"?")
            .then((confirmed) => {
                this.deleteMethod.emit()
            })
    }
}
