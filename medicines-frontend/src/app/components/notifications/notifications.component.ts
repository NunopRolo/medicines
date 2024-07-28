import {Component} from '@angular/core';
import {ToastBodyComponent, ToastComponent, ToasterComponent, ToastHeaderComponent} from "@coreui/angular";
import {NotificationsService} from "../../services/notifications-service";

@Component({
  selector: 'app-notifications',
  standalone: true,
    imports: [
        ToastBodyComponent,
        ToastComponent,
        ToastHeaderComponent,
        ToasterComponent
    ],
  templateUrl: './notifications.component.html',
  styleUrl: './notifications.component.scss'
})
export class NotificationsComponent {
    constructor(readonly notificationService: NotificationsService) {
    }
}
