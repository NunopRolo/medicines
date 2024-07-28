import {Injectable} from "@angular/core";
import {Subject} from "rxjs";
import {MedicationResult} from "../models/MedicationResult";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {NotificationsService} from "./notifications-service";
import {MedicationDto} from "../models/MedicationDto";
import {TranslateService} from "@ngx-translate/core";
import {PersonService} from "./person-service";
import {MedicationRequest} from "../models/MedicationRequest";
import {DayPeriod} from "../models/DayPeriod";
import {DayPeriodRequest} from "../models/DayPeriodRequest";
import {TemporaryMedicationDto} from "../models/TemporaryMedicationDto";
import {TemporaryMedicationRequest} from "../models/TemporaryMedicationRequest";
import {MedicationContentEnum} from "../models/MedicationContentEnum";

@Injectable({providedIn: 'root'})
export class MedicationService {
    public medications$: Subject<MedicationResult[]> = new Subject<MedicationResult[]>();
    public dayPeriods$: Subject<DayPeriod[]> = new Subject<DayPeriod[]>();
    medicationContent: MedicationContentEnum = MedicationContentEnum.INFO;
    imageVisibility: boolean = false;

    constructor(
        private http: HttpClient,
        private notificationService: NotificationsService,
        private translateService: TranslateService,
        private personService: PersonService
    ) {
        this.medications$ = new Subject<MedicationResult[]>();
    }

    public getAllMedications(){
        this.http.get<MedicationResult[]>(environment.apiUrl+"/medication/get/"+this.personService.selectedPerson.personUuid).subscribe((data:any) => {
            this.medications$.next(data);
        });
    }

    public addMedicine(medicationRequest: MedicationRequest){
        this.http.post<MedicationResult>(environment.apiUrl+"/medication/add", medicationRequest).subscribe({
            next: () => {
                this.getAllMedications();
                this.notificationService.addToast(
                    this.translateService.instant("medication.notification.title"),
                    this.translateService.instant("medication.notification.medicine-added"),
                    "success"
                );
            },
            error: (e) => {
                this.notificationService.addToast(
                    this.translateService.instant("medication.notification.title"),
                    this.translateService.instant("medication.notification.medicine-added-error"),
                    "danger"
                );
            }
        });
    }

    public addTemporaryMedicine(temporaryMedicationRequest: TemporaryMedicationRequest){
        this.http.post<MedicationResult>(environment.apiUrl+"/medication/temporary-medication/add", temporaryMedicationRequest).subscribe({
            next: () => {
                this.getAllMedications();
                this.notificationService.addToast(
                    this.translateService.instant("medication.notification.title"),
                    this.translateService.instant("medication.notification.medicine-added"),
                    "success"
                );
            },
            error: (e) => {
                this.notificationService.addToast(
                    this.translateService.instant("medication.notification.title"),
                    this.translateService.instant("medication.notification.medicine-added-error"),
                    "danger"
                );
            }
        });
    }

    public updateMedicine(medication: MedicationDto){
        this.http.put<MedicationResult>(environment.apiUrl+"/medication/update/"+medication.uuid, {observations: medication.observations}).subscribe({
            next: () => {
                this.getAllMedications();
                this.notificationService.addToast(
                    this.translateService.instant("medication.notification.title"),
                    this.translateService.instant("medication.notification.medicine-updated"),
                    "success"
                );
            },
            error: (e) => {
                console.log(e);
                this.notificationService.addToast(
                    this.translateService.instant("medication.notification.title"),
                    this.translateService.instant("medication.notification.medicine-updated-error"),
                    "danger"
                );
            }
        });
    }

    public deleteMedication(medication: MedicationDto){
        this.http.delete<any>(environment.apiUrl+"/medication/delete/"+medication.uuid).subscribe({
            next: () => {
                this.getAllMedications();
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

    public deleteTemporaryMedication(temporaryMedication: TemporaryMedicationDto){
        this.http.delete<any>(environment.apiUrl+"/medication/temporary-medication/delete/"+temporaryMedication.uuid).subscribe({
            next: () => {
                this.getAllMedications();
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

    public getDayPeriods(){
        this.http.get<MedicationResult[]>(environment.apiUrl+"/medication/day-periods").subscribe((data:any) => {
            this.dayPeriods$.next(data);
        });
    }

    public addDayPeriod(dayPeriodRequest: DayPeriodRequest){
        this.http.post<MedicationResult>(environment.apiUrl+"/medication/day-period", dayPeriodRequest).subscribe({
            next: () => {
                this.getDayPeriods();
                this.notificationService.addToast(
                    this.translateService.instant("medication.notification.title"),
                    this.translateService.instant("medication.notification.day-period-added"),
                    "success"
                );
            },
            error: (e) => {
                this.notificationService.addToast(
                    this.translateService.instant("medication.notification.title"),
                    this.translateService.instant("medication.notification.day-period-error"),
                    "danger"
                );
            }
        });
    }

    public deleteDayPeriod(dayPeriod: DayPeriod){
        this.http.delete<DayPeriod>(environment.apiUrl+"/medication/day-period/"+dayPeriod.id).subscribe({
            next: () => {
                this.getDayPeriods();
                this.notificationService.addToast(
                    this.translateService.instant("medication.notification.title"),
                    this.translateService.instant("medication.notification.day-period-deleted"),
                    "success"
                );
            },
            error: (e) => {
                this.notificationService.addToast(
                    this.translateService.instant("medication.notification.title"),
                    this.translateService.instant("medication.notification.day-period-deleted-error"),
                    "danger"
                );
            }
        });
    }

    public toggleImageVisibility(){
        this.imageVisibility = !this.imageVisibility;
        if(this.imageVisibility){
            this.medicationContent = MedicationContentEnum.IMAGE;
        }else{
            this.medicationContent = MedicationContentEnum.INFO;
        }
    }

    public getMedicationContent(): MedicationContentEnum{
        return this.medicationContent;
    }
}
