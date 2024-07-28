import {Component, Input, OnInit} from '@angular/core';

import {HeaderComponent} from '@coreui/angular';
import {TranslateService} from "@ngx-translate/core";
import {PersonService} from "../../../services/person-service";
import {Person} from "../../../models/Person";

@Component({
    selector: 'app-default-header',
    templateUrl: './default-header.component.html',
})
export class DefaultHeaderComponent extends HeaderComponent implements OnInit {
    @Input() sidebarId: string = "sidebar";

    constructor(
        private translateService: TranslateService,
        readonly personService: PersonService
    ) {
        super();
    }

    ngOnInit() {}

    changeLanguage(language: string) {
        this.translateService.use(language);
        localStorage.setItem("lang", language);
    }

    changePerson(person: Person) {
        this.personService.changePerson(person);
    }
}
