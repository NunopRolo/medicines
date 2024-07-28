import {Component, OnInit} from '@angular/core';
import {Router, NavigationEnd} from '@angular/router';

import {IconSetService} from '@coreui/icons-angular';
import {iconSubset} from './icons/icon-subset';
import {Title} from '@angular/platform-browser';
import {TranslateService} from "@ngx-translate/core";

@Component({
    selector: 'app-root',
    template: '<router-outlet></router-outlet>',
})
export class AppComponent implements OnInit {
    constructor(
        private router: Router,
        private titleService: Title,
        private iconSetService: IconSetService,
        private translateService: TranslateService
    ) {
        // iconSet singleton
        iconSetService.icons = {...iconSubset};

        let language = localStorage.getItem("lang");
        if (!language) {
            language = "pt";
        }
        translateService.setDefaultLang(language);
        translateService.use(language);
    }

    ngOnInit(): void {
        this.router.events.subscribe((evt) => {
            if (!(evt instanceof NavigationEnd)) {
                return;
            }
        });

        this.translateService.onLangChange.subscribe(() => {
            this.titleService.setTitle(this.translateService.instant("app-title"));
        });
    }
}
