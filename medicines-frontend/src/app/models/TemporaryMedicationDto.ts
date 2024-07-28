import {MedicationDto} from "./MedicationDto";

export interface TemporaryMedicationDto {
    uuid: string,
    medication: MedicationDto,
    days: number,
    startDate: Date
}
