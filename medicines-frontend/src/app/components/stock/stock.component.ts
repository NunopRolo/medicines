import {Component, OnInit} from '@angular/core';
import {
    ButtonDirective,
    CardBodyComponent,
    CardComponent,
    CardFooterComponent,
    CardHeaderComponent,
    CardTextDirective,
    CardTitleDirective,
    ColComponent, FormLabelDirective,
    RowComponent,
    TableDirective
} from "@coreui/angular";
import {AsyncPipe, DatePipe, NgForOf} from "@angular/common";
import {NgbHighlight, NgbModal, NgbPagination, NgbTooltip} from "@ng-bootstrap/ng-bootstrap";
import {map, startWith, Subscription} from "rxjs";
import {StockService} from "../../services/stock-service";
import {FormControl, ReactiveFormsModule} from "@angular/forms";
import {ModalAddMedicineToStockComponent} from "./modal-add-medicine-to-stock/modal-add-medicine-to-stock.component";
import {IconDirective} from "@coreui/icons-angular";
import {Stock, ValidityStatusEnum} from "../../models/Stock";
import {TranslateModule} from "@ngx-translate/core";
import {PersonService} from "../../services/person-service";

@Component({
  selector: 'app-stock',
  standalone: true,
    imports: [
        CardBodyComponent,
        CardComponent,
        CardHeaderComponent,
        ColComponent,
        AsyncPipe,
        NgForOf,
        NgbHighlight,
        NgbPagination,
        TableDirective,
        ButtonDirective,
        CardTextDirective,
        CardTitleDirective,
        RowComponent,
        ModalAddMedicineToStockComponent,
        IconDirective,
        CardFooterComponent,
        DatePipe,
        ReactiveFormsModule,
        FormLabelDirective,
        NgbTooltip,
        TranslateModule
    ],
  templateUrl: './stock.component.html',
  styleUrl: './stock.component.scss'
})
export class StockComponent implements OnInit{
    pageNumber: number;
    pageSize: number;
    collectionSize: number;
    reader: string;
    protected readonly ValidityStatusEnum = ValidityStatusEnum;

    search = new FormControl('', { nonNullable: true });
    filter = new FormControl('all', { nonNullable: true });

    personSubscription: Subscription;

    constructor(
        readonly stockService: StockService,
        readonly personService: PersonService,
        readonly modalService: NgbModal
    ) {
        this.pageNumber = 0;
        this.pageSize = 0;
        this.collectionSize = 0;
        this.reader = "";
    }

    ngOnInit() {
        this.personSubscription = this.personService.selectedPerson$.subscribe(person => {
            this.stockService.getAllStocks(this.pageNumber);
        });

        this.search.valueChanges.pipe(
            startWith(''),
            map((text) => this.stockService.search(text)),
        ).subscribe();

        this.stockService.stocks$.subscribe(data => {
            this.collectionSize = data.totalElements;
            this.pageNumber = data.number+1;
            this.pageSize = data.size;
        })
    }

    changePage(page: number){
        this.stockService.getAllStocks(page-1);
    }

    getCardColor(stock: Stock): string{
        if(stock.validityStatus == ValidityStatusEnum.IN_VALIDITY){
            return "success";
        }else if(stock.validityStatus == ValidityStatusEnum.LESS_THAN_1_MONTH){
            return "warning";
        }else{
            return "danger";
        }
    }

    changeFilter(){
        if(this.filter.value === "all"){
            this.stockService.getAllStocks(0);
        }
        this.stockService.getStockMedicinesWithFilter(this.filter.value)
    }

    openAddMedicineToStockModal(){
        this.modalService.open(ModalAddMedicineToStockComponent, {size: "xl"});
    }

}
