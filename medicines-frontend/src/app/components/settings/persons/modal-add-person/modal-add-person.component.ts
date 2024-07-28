import { Component } from '@angular/core';
import {
    ButtonCloseDirective,
    ButtonDirective,
    FormControlDirective,
    FormLabelDirective,
    ModalBodyComponent, ModalFooterComponent, ModalHeaderComponent
} from "@coreui/angular";
import {FormControl, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MedicinesTableComponent} from "../../../medicines/medicines-table/medicines-table.component";
import {TranslateModule} from "@ngx-translate/core";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {PersonService} from "../../../../services/person-service";
import {PersonRequest} from "../../../../models/PersonRequest";

@Component({
  selector: 'app-modal-add-person',
  standalone: true,
    imports: [
        ButtonCloseDirective,
        ButtonDirective,
        FormControlDirective,
        FormLabelDirective,
        FormsModule,
        MedicinesTableComponent,
        ModalBodyComponent,
        ModalFooterComponent,
        ModalHeaderComponent,
        TranslateModule,
        ReactiveFormsModule
    ],
  templateUrl: './modal-add-person.component.html',
  styleUrl: './modal-add-person.component.scss'
})
export class ModalAddPersonComponent {
    personName = new FormControl('', {nonNullable: true});

    constructor(public activeModal: NgbActiveModal,
                private personService: PersonService) {
    }

    savePerson(){
        const personRequest: PersonRequest = {
            name: this.personName.value
        }
        this.personService.savePerson(personRequest);
        this.activeModal.close();
    }
}
