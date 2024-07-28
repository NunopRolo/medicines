import {MedicationDto} from "./MedicationDto";
import {DayPeriod} from "./DayPeriod";
import {TemporaryMedicationDto} from "./TemporaryMedicationDto";

export interface MedicationResult {
    dayPeriod: DayPeriod;
    medications: MedicationDto[];
    temporaryMedications: TemporaryMedicationDto[];
}
