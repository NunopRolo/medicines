import {Component} from '@angular/core';
import {
    ButtonDirective,
    CardBodyComponent,
    CardComponent,
    CardHeaderComponent,
    ColComponent,
    RowComponent, TemplateIdDirective, WidgetModule
} from "@coreui/angular";
import {MedicinesTableComponent} from "../medicines/medicines-table/medicines-table.component";
import {TranslateModule} from "@ngx-translate/core";
import {IconDirective} from "@coreui/icons-angular";
import {AsyncPipe, NgForOf} from "@angular/common";
import {PersonsComponent} from "./persons/persons.component";
import {DayPeriodsComponent} from "./day-periods/day-periods.component";

@Component({
    selector: 'app-settings',
    standalone: true,
    imports: [
        CardBodyComponent,
        CardComponent,
        CardHeaderComponent,
        ColComponent,
        MedicinesTableComponent,
        TranslateModule,
        ButtonDirective,
        RowComponent,
        WidgetModule,
        IconDirective,
        AsyncPipe,
        NgForOf,
        TemplateIdDirective,
        PersonsComponent,
        DayPeriodsComponent
    ],
    templateUrl: './settings.component.html',
    styleUrl: './settings.component.scss'
})
export class SettingsComponent{
}
