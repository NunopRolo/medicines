import {MedicationRequest} from "./MedicationRequest";

export interface TemporaryMedicationRequest {
    medication: MedicationRequest,
    days: number,
    startDate: Date
}
