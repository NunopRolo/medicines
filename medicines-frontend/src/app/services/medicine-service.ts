import {Injectable} from "@angular/core";
import {Medicine} from "../models/Medicine";
import {HttpClient, HttpParams} from "@angular/common/http";
import {PageResponse} from "../models/PageResponse";
import {Observable, Subject} from "rxjs";
import {environment} from "../../environments/environment";
import {NotificationsService} from "./notifications-service";
import {TranslateService} from "@ngx-translate/core";

@Injectable({providedIn: 'root'})
export class MedicineService{
    public medicines$: Subject<PageResponse<Medicine>>;
    constructor(
        private http: HttpClient,
        private notificationService: NotificationsService,
        private translateService: TranslateService
    ) {
        this.medicines$ = new Subject<PageResponse<Medicine>>();
    }

    public getAllMedicines(page: number){
        let queryParams = new HttpParams();
        queryParams = queryParams.append("page", page);
        queryParams = queryParams.append("size", 5);

        this.http.get<PageResponse<Medicine>>(environment.apiUrl+"/medicines/all", {params:queryParams}).subscribe((data:any) => {
            this.medicines$.next(data);
        });
    }

    searchBackend(text: string, page: number): Observable<any>{
        let queryParams = new HttpParams();
        queryParams = queryParams.append("page", page);
        queryParams = queryParams.append("size", 5);

        return this.http.get<PageResponse<Medicine>>(environment.apiUrl+"/medicines/search/"+text, {params:queryParams});
    }

    search(text: string, page: number): void {
        this.searchBackend(text, page).subscribe((data: any) => {
            this.medicines$.next(data);
        });
    }

    getMedicineByBarcode(barcode: string){
        return this.http.get<Medicine>(environment.apiUrl+"/medicines/"+barcode);
    }

    uploadImage(image: any, medicine: Medicine){
        let formData:FormData = new FormData();
        formData.append('image', image, image.name);

        const requestOptions: Object = {
            responseType: 'text'
        }

        this.http.post<any>(environment.apiUrl+"/medicines/image/"+medicine.barcode, formData, requestOptions).subscribe({
            next: (result: string) => {
                medicine.image = result;
                this.notificationService.addToast(
                    this.translateService.instant("medication.notification.title"),
                    this.translateService.instant("medication.notification.image-uploaded"),
                    "success"
                );
            },
            error: (e) => {
                console.log(e);
                this.notificationService.addToast(
                    this.translateService.instant("medication.notification.title"),
                    this.translateService.instant("medication.notification.image-uploaded-error"),
                    "danger"
                );
            }
        });
    }

    public deleteMedicineImage(medicine: Medicine){
        this.http.delete<any>(environment.apiUrl+"/medicines/image/"+medicine.barcode).subscribe({
            next: () => {
                medicine.image = "";
                this.notificationService.addToast(
                    this.translateService.instant("medication.notification.title"),
                    this.translateService.instant("medication.notification.medicine-deleted"),
                    "success"
                );
            },
            error: (e) => {
                console.log(e);
                this.notificationService.addToast(
                    this.translateService.instant("medication.notification.title"),
                    this.translateService.instant("medication.notification.medicine-deleted-error"),
                    "danger"
                );
            }
        });
    }

}
