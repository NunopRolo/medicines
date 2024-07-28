import {Component, OnDestroy, OnInit} from '@angular/core';

import { navItems } from './_nav';
import {TranslateService} from "@ngx-translate/core";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-dashboard',
  templateUrl: './default-layout.component.html',
  styleUrls: ['./default-layout.component.scss'],
})
export class DefaultLayoutComponent implements OnInit, OnDestroy{
    public navItems: any = navItems.map(items => {this.translate(items); return items; });

    private translateSubscription: Subscription;
    constructor(
          private translateService: TranslateService
    ) {}

    ngOnInit(): void {
        this.translateSubscription = this.translateService.onLangChange.subscribe(() => {
            this.navItems = navItems.map(items => {this.translate(items); return items; });
            this.navItems = JSON.parse(JSON.stringify(this.navItems));
        });
    }

    ngOnDestroy() {
        this.translateSubscription.unsubscribe();
    }

    translate(item: any): void {
        if ('attributes' in item) {
            const trans = this.translateService.instant(item.attributes.key);
            if (trans !== `${item.name}`) {
                item.name = trans;
            }
        }
        if (item.children && item.children.length > 0) {
            item.children.map( (child: any) => this.translate(child));
        }
    }
}
