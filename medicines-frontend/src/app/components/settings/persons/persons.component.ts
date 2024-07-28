import { Component } from '@angular/core';
import {AsyncPipe, NgForOf} from "@angular/common";
import {
    ButtonDirective,
    CardBodyComponent,
    CardComponent,
    CardHeaderComponent,
    ColComponent,
    RowComponent, TemplateIdDirective, WidgetStatCComponent, WidgetStatFComponent
} from "@coreui/angular";
import {IconDirective} from "@coreui/icons-angular";
import {TranslateModule} from "@ngx-translate/core";
import {PersonService} from "../../../services/person-service";
import {ModalAddPersonComponent} from "./modal-add-person/modal-add-person.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-persons',
  standalone: true,
    imports: [
        AsyncPipe,
        ButtonDirective,
        CardBodyComponent,
        CardComponent,
        CardHeaderComponent,
        ColComponent,
        IconDirective,
        NgForOf,
        RowComponent,
        TemplateIdDirective,
        TranslateModule,
        WidgetStatFComponent,
        WidgetStatCComponent
    ],
  templateUrl: './persons.component.html',
  styleUrl: './persons.component.scss'
})
export class PersonsComponent {
    constructor(
        readonly personService: PersonService,
        readonly modalService: NgbModal
    ) {
    }

    addPerson() {
        this.modalService.open(ModalAddPersonComponent, {size: "sm"});
    }
}
