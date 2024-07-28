import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {AsyncPipe, NgForOf, NgIf} from "@angular/common";
import {BarcodeScannerLivestreamComponent, BarcodeScannerLivestreamModule} from "ngx-barcode-scanner";
import {
    ButtonDirective,
    FormCheckComponent,
    FormCheckInputDirective,
    InputGroupComponent,
    TableDirective
} from "@coreui/angular";
import {FormControl, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {IconDirective} from "@coreui/icons-angular";
import {NgbHighlight, NgbPagination} from "@ng-bootstrap/ng-bootstrap";
import {MedicineService} from "../../../services/medicine-service";
import {NotificationsService} from "../../../services/notifications-service";
import {map, startWith} from "rxjs";
import {QuaggaJSResultObject} from "@ericblade/quagga2";
import {TranslateModule, TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-medicines-table',
  standalone: true,
    imports: [
        AsyncPipe,
        BarcodeScannerLivestreamModule,
        ButtonDirective,
        FormCheckComponent,
        FormCheckInputDirective,
        FormsModule,
        IconDirective,
        InputGroupComponent,
        NgForOf,
        NgIf,
        NgbHighlight,
        NgbPagination,
        TableDirective,
        ReactiveFormsModule,
        TranslateModule
    ],
  templateUrl: './medicines-table.component.html',
  styleUrl: './medicines-table.component.scss'
})
export class MedicinesTableComponent implements OnInit{
    pageNumber: number;
    pageSize: number;
    collectionSize: number;
    reader: string;

    filter = new FormControl('', { nonNullable: true });

    @ViewChild(BarcodeScannerLivestreamComponent)
    barcodeScanner: BarcodeScannerLivestreamComponent;

    @Input() medicinesSelectionEnabled: boolean = false;

    @Output() onChangeMedicineSelection: EventEmitter<any> = new EventEmitter<any>();

    constructor(
        readonly medicineService: MedicineService,
        readonly notificationService: NotificationsService,
        readonly translateService: TranslateService
    ) {
        this.pageNumber = 0;
        this.pageSize = 0;
        this.collectionSize = 0;
        this.reader = "";

        this.filter.valueChanges.pipe(
            startWith(''),
            map((text) => this.medicineService.search(text, 0)),
        ).subscribe();
    }

    ngOnInit() {
        this.medicineService.medicines$.subscribe(data => {
            this.collectionSize = data.totalElements;
            this.pageNumber = data.number+1;
            this.pageSize = data.size;
        });
    }

    changePage(page: number){
        if(this.filter.getRawValue() != ''){
            this.medicineService.search(this.filter.getRawValue(), page-1);
        }else{
            this.medicineService.getAllMedicines(page-1);
        }
    }

    barcodeReader(event: QuaggaJSResultObject){
        if (event.codeResult.code != null) {
            this.filter.setValue(event.codeResult.code);
            this.barcodeScanner.stop();
        }else{
            this.notificationService.addToast(
                this.translateService.instant("medicine.notification.title"),
                this.translateService.instant("medicine.notification.medicine-not-found"),
                'danger'
            )
        }
    }
}
