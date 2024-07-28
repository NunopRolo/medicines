import {Medicine} from "./Medicine";

export interface MedicationDto {
    uuid: string;
    medicine: Medicine;
    observations: string;
}
