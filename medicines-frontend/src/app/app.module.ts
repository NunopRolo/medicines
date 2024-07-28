import {APP_INITIALIZER, NgModule} from '@angular/core';
import {HashLocationStrategy, LocationStrategy} from '@angular/common';
import {BrowserModule, Title} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ReactiveFormsModule} from '@angular/forms';

import {NgScrollbarModule} from 'ngx-scrollbar';

// Import routing module
import {AppRoutingModule} from './app-routing.module';

// Import app component
import {AppComponent} from './app.component';

// Import containers
import {DefaultHeaderComponent, DefaultLayoutComponent} from './containers';

import {
    AvatarModule,
    BadgeModule,
    BreadcrumbModule,
    ButtonGroupModule,
    ButtonModule,
    CardModule,
    DropdownModule,
    FooterModule,
    FormModule,
    GridModule,
    HeaderModule,
    ListGroupModule,
    NavModule,
    ProgressModule,
    SharedModule,
    SidebarModule,
    TabsModule,
    UtilitiesModule
} from '@coreui/angular';

import {IconModule, IconSetService} from '@coreui/icons-angular';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {NgbActiveModal, NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {BarcodeScannerLivestreamModule} from "ngx-barcode-scanner";
import {NotificationsComponent} from "./components/notifications/notifications.component";

import {TranslateModule, TranslateLoader} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {PersonService} from "./services/person-service";
import {DragDropModule} from "@angular/cdk/drag-drop";

// Create a loader for translation files
export function HttpLoaderFactory(http: HttpClient) {
    return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

const APP_CONTAINERS = [
    DefaultHeaderComponent,
    DefaultLayoutComponent
];


@NgModule({
    declarations: [AppComponent, ...APP_CONTAINERS],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        AppRoutingModule,
        AvatarModule,
        BreadcrumbModule,
        FooterModule,
        DropdownModule,
        GridModule,
        HeaderModule,
        SidebarModule,
        IconModule,
        NavModule,
        ButtonModule,
        FormModule,
        UtilitiesModule,
        ButtonGroupModule,
        ReactiveFormsModule,
        SidebarModule,
        SharedModule,
        TabsModule,
        ListGroupModule,
        ProgressModule,
        BadgeModule,
        ListGroupModule,
        CardModule,
        NgScrollbarModule,
        FontAwesomeModule,
        HttpClientModule,
        NgbModule,
        BarcodeScannerLivestreamModule,
        NotificationsComponent,
        DragDropModule,
        TranslateModule.forRoot({
            loader: {
                provide: TranslateLoader,
                useFactory: HttpLoaderFactory,
                deps: [HttpClient],
            },
        })
    ],
    providers: [
        {
            provide: LocationStrategy,
            useClass: HashLocationStrategy
        },
        {
            provide: APP_INITIALIZER,
            useFactory: (personService: PersonService) => {
                return () => {
                    personService.getAllPersons();
                }
            },
            multi: true,
            deps: [PersonService]
        },
        IconSetService,
        Title,
        NgbActiveModal
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
