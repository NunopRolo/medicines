import { Injectable } from '@angular/core';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import {ConfirmationModalComponent} from "../components/confirmation-modal/confirmation-modal.component";


@Injectable({providedIn: 'root'})
export class ConfirmationModalService {

    constructor(private modalService: NgbModal) { }

    public confirm(
        title: string,
        message: string,
        btnOkText: string = 'Sim',
        btnCancelText: string = 'Cancel',
        dialogSize: 'sm'|'lg' = 'sm'): Promise<boolean> {
        const modalRef = this.modalService.open(ConfirmationModalComponent, { size: dialogSize });
        modalRef.componentInstance.title = title;
        modalRef.componentInstance.message = message;
        modalRef.componentInstance.btnOkText = btnOkText;
        modalRef.componentInstance.btnCancelText = btnCancelText;

        return modalRef.result;
    }

}
