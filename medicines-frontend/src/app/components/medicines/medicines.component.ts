import {Component,} from '@angular/core';
import {
    ButtonModule,
    CardBodyComponent,
    CardComponent,
    CardHeaderComponent, ColComponent,
    FormLabelDirective, FormModule, InputGroupComponent,
    TableDirective, ToastBodyComponent, ToastComponent, ToasterComponent, ToastHeaderComponent
} from "@coreui/angular";
import {MatPaginatorModule} from "@angular/material/paginator";
import {AsyncPipe, NgForOf, NgIf, SlicePipe} from "@angular/common";
import {NgbHighlight, NgbPagination} from "@ng-bootstrap/ng-bootstrap";
import {ReactiveFormsModule} from "@angular/forms";
import {BarcodeScannerLivestreamModule} from "ngx-barcode-scanner";
import {IconDirective} from "@coreui/icons-angular";
import {NotificationsComponent} from "../notifications/notifications.component";
import {MedicinesTableComponent} from "./medicines-table/medicines-table.component";
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'app-medicines',
  standalone: true,
    imports: [
        FormLabelDirective,
        MatPaginatorModule,
        AsyncPipe,
        NgForOf,
        NgbPagination,
        SlicePipe,
        TableDirective,
        CardBodyComponent,
        CardComponent,
        CardHeaderComponent,
        ColComponent,
        NgbHighlight,
        ReactiveFormsModule,
        BarcodeScannerLivestreamModule,
        InputGroupComponent,
        ButtonModule,
        IconDirective,
        ToasterComponent,
        ToastComponent,
        ToastHeaderComponent,
        ToastBodyComponent,
        NotificationsComponent,
        NgIf,
        FormModule,
        MedicinesTableComponent,
        TranslateModule
    ],
  templateUrl: './medicines.component.html',
  styleUrl: './medicines.component.scss'
})
export class MedicinesComponent{


}
