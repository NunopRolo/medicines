import {Component, ViewChild} from '@angular/core';
import {
    ButtonModule,
    FormModule,
    ModalModule,
} from "@coreui/angular";
import {StockService} from "../../../services/stock-service";
import {FormControl, ReactiveFormsModule} from "@angular/forms";
import {IconDirective} from "@coreui/icons-angular";
import {BarcodeScannerLivestreamComponent, BarcodeScannerLivestreamModule} from "ngx-barcode-scanner";
import {InfiniteScrollModule} from "ngx-infinite-scroll";
import {AsyncPipe, NgForOf} from "@angular/common";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {MedicinesTableComponent} from "../../medicines/medicines-table/medicines-table.component";
import {TranslateModule} from "@ngx-translate/core";
import {StockRequest} from "../../../models/StockRequest";

@Component({
  selector: 'app-modal-add-medicine-to-stock',
  standalone: true,
    imports: [
        ButtonModule,
        ModalModule,
        FormModule,
        ReactiveFormsModule,
        IconDirective,
        BarcodeScannerLivestreamModule,
        InfiniteScrollModule,
        AsyncPipe,
        NgForOf,
        MedicinesTableComponent,
        TranslateModule
    ],
  templateUrl: './modal-add-medicine-to-stock.component.html',
  styleUrl: './modal-add-medicine-to-stock.component.scss'
})
export class ModalAddMedicineToStockComponent {
    barcode: string;
    validity = new FormControl('', { nonNullable: true });

    @ViewChild(BarcodeScannerLivestreamComponent)
    barcodeScanner: BarcodeScannerLivestreamComponent;

    constructor(readonly stockService: StockService,
                public activeModal: NgbActiveModal) {
    }

    saveMedicineToStock(){
        let stockRequest: StockRequest = {
            barcode: this.barcode,
            validity: this.validity.value
        }
        this.stockService.addMedicineToStock(stockRequest);
        this.validity.reset();
        this.activeModal.close();
    }

    onChangeMedicineSelection(event: any){
        this.barcode = event.target.value;
    }
}
