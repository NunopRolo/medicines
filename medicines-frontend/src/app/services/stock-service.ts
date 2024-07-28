import {Injectable} from "@angular/core";
import {Subject} from "rxjs";
import {PageResponse} from "../models/PageResponse";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Stock} from "../models/Stock";
import {environment} from "../../environments/environment";
import {NotificationsService} from "./notifications-service";
import {TranslateService} from "@ngx-translate/core";
import {StockRequest} from "../models/StockRequest";
import {PersonService} from "./person-service";

@Injectable({providedIn: 'root'})
export class StockService {
    public stocks$: Subject<PageResponse<Stock>>;

    public addMedicineModalVisibility: boolean = false;

    constructor(private http: HttpClient,
                private notificationService: NotificationsService,
                private translateService: TranslateService,
                private personService: PersonService) {
        this.stocks$ = new Subject<PageResponse<Stock>>();
        this.getAllStocks(0);
    }

    getAllStocks(page: number){
        let queryParams = new HttpParams();
        queryParams = queryParams.append("page", page);
        queryParams = queryParams.append("size", 8);

        this.http.get<PageResponse<Stock>>(environment.apiUrl+"/stock/all/"+this.personService.selectedPerson.personUuid, {params:queryParams}).subscribe((data:any) => {
            this.stocks$.next(data);
        });
    }

    search(term: string){
        let queryParams = new HttpParams();
        queryParams = queryParams.append("size", 8);

        this.http.get<PageResponse<Stock>>(environment.apiUrl+"/stock/search/"+this.personService.selectedPerson.personUuid+"/"+term, {params:queryParams}).subscribe({
            next: (data) => {
                this.stocks$.next(data);
            },
            error: (e) => {
                this.notificationService.addToast(
                    this.translateService.instant("stock.notification.title"),
                    this.translateService.instant("stock.notification.not-found"),
                    "danger"
                );
            }
        });
    }

    addMedicineToStock(stockRequest: StockRequest){
        this.http.post<Stock>(environment.apiUrl+"/stock/add/"+this.personService.selectedPerson.personUuid, stockRequest).subscribe({
            next: (data) => {
                this.getAllStocks(0);
                this.toggleMedicineModal();
                this.notificationService.addToast(
                    this.translateService.instant("stock.notification.title"),
                    this.translateService.instant("stock.notification.medicine-added-success"),
                    "success"
                );
            },
            error: (e) => {
                this.notificationService.addToast(
                    this.translateService.instant("stock.notification.title"),
                    this.translateService.instant("stock.notification.medicine-added-error"),
                    "danger"
                );
            }
        });
    }

    deleteStockEntry(id: string){
        this.http.delete<any>(environment.apiUrl+"/stock/delete/"+id).subscribe({
            next: (data) => {
                this.notificationService.addToast(
                    this.translateService.instant("stock.notification.title"),
                    this.translateService.instant("stock.notification.medicine-deleted-success"),
                    "success"
                );
                this.getAllStocks(0);
            },
            error: (e) => {
                this.notificationService.addToast(
                    this.translateService.instant("stock.notification.title"),
                    this.translateService.instant("stock.notification.medicine-deleted-error"),
                    "danger"
                );
            }
        });
    }

    toggleMedicineModal(){
        this.addMedicineModalVisibility = !this.addMedicineModalVisibility;
    }

    getStockMedicinesWithFilter(filter: string){
        let queryParams = new HttpParams();
        queryParams = queryParams.append("size", 8);

        this.http.get<PageResponse<Stock>>(environment.apiUrl+"/stock/filter/"+this.personService.selectedPerson.personUuid+"/"+filter, {params:queryParams}).subscribe((data:any) => {
            this.stocks$.next(data);
        });
    }
}
