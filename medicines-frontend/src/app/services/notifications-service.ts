import {Injectable} from "@angular/core";

@Injectable({providedIn: 'root'})
export class NotificationsService{

    visible = false;
    title: string = "";
    body: string = "";
    color: string = "";

    constructor() {
    }

    addToast(title: string, body: string, color: string){
        this.title = title;
        this.body = body;
        this.color = color;
        this.visible = true;
    }

    toggleToast() {
        this.visible = !this.visible;
    }

    onVisibleChange($event: boolean) {
        this.visible = $event;
    }

}
