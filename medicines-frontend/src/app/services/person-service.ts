import {Injectable} from "@angular/core";
import {BehaviorSubject, Observable, of} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Person} from "../models/Person";
import {PersonRequest} from "../models/PersonRequest";
import {NotificationsService} from "./notifications-service";
import {TranslateService} from "@ngx-translate/core";

@Injectable({providedIn: 'root'})
export class PersonService {
    public persons$: Observable<Person[]>;
    public selectedPerson$: BehaviorSubject<Person>;
    public selectedPerson: Person;

    constructor(
        private http: HttpClient,
        private notificationService: NotificationsService,
        private translateService: TranslateService
    ) {
        this.persons$ = new Observable<Person[]>();
        const personItem = localStorage.getItem('person');

        this.selectedPerson$ = new BehaviorSubject<Person>(JSON.parse(personItem as string));
        this.selectedPerson = JSON.parse(personItem as string);
    }

    getAllPersons(){
         this.http.get<Person[]>(environment.apiUrl+"/persons/all").subscribe((data: Person[]) => {
             this.persons$ = of(data);

             if(!this.selectedPerson){
                 this.changePerson(data[0]);
             }
        });
    }

    changePerson(person: Person){
        this.selectedPerson = person;
        this.selectedPerson$.next(this.selectedPerson);
        localStorage.setItem("person", JSON.stringify(person));
    }

    getSelectedPerson(){
        return this.selectedPerson;
    }

    savePerson(personRequest: PersonRequest){
        this.http.post(environment.apiUrl+"/persons/add", personRequest).subscribe({
            next: () => {
                this.getAllPersons();
                this.notificationService.addToast(
                    this.translateService.instant("person.settings.notification.title"),
                    this.translateService.instant("person.settings.notification.persons-added"),
                    "success"
                );
            },
            error: (e) => {
                this.notificationService.addToast(
                    this.translateService.instant("person.settings.notification.title"),
                    this.translateService.instant("person.settings.notification.persons-added-error"),
                    "danger"
                );
            }
        })
    }

    deletePerson(person: Person){
        this.http.delete(environment.apiUrl+"/persons/"+person.personUuid).subscribe({
            next: () => {
                this.getAllPersons();
                this.notificationService.addToast(
                    this.translateService.instant("person.settings.notification.title"),
                    this.translateService.instant("person.settings.notification.persons-deleted"),
                    "success"
                );
            },
            error: (e) => {
                this.notificationService.addToast(
                    this.translateService.instant("person.settings.notification.title"),
                    this.translateService.instant("person.settings.notification.persons-deleted-error"),
                    "danger"
                );
            }
        })
    }
}
