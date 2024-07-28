import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {DefaultLayoutComponent} from './containers';
import {MedicinesComponent} from "./components/medicines/medicines.component";
import {StockComponent} from "./components/stock/stock.component";
import {MedicationComponent} from "./components/medication/medication.component";
import {SettingsComponent} from "./components/settings/settings.component";

const routes: Routes = [
    {
        path: '',
        redirectTo: 'medicines',
        pathMatch: 'full'
    },
    {
        path: '',
        component: DefaultLayoutComponent,
        data: {
            title: 'Medicines'
        },
        children: [
            {
                path: 'medicines',
                component: MedicinesComponent
            }
        ]
    },
    {
        path: '',
        component: DefaultLayoutComponent,
        data: {
            title: 'Stock'
        },
        children: [
            {
                path: 'stock',
                component: StockComponent
            }
        ]
    },
    {
        path: '',
        component: DefaultLayoutComponent,
        data: {
            title: 'Medication'
        },
        children: [
            {
                path: 'medication',
                component: MedicationComponent
            }
        ]
    },
    {
        path: '',
        component: DefaultLayoutComponent,
        data: {
            title: 'Settings'
        },
        children: [
            {
                path: 'settings',
                component: SettingsComponent
            }
        ]
    },
    {path: '**', redirectTo: 'dashboard'}
];

@NgModule({
    imports: [
        RouterModule.forRoot(routes, {
            scrollPositionRestoration: 'top',
            anchorScrolling: 'enabled',
            initialNavigation: 'enabledBlocking'
            // relativeLinkResolution: 'legacy'
        })
    ],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
