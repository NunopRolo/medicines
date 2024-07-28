import {Medicine} from "./Medicine";
import {Person} from "./Person";

export interface Stock{
    id: string,
    medicine: Medicine,
    validityEndDate: Date,
    validityStatus: ValidityStatusEnum,
    person: Person
}

export enum ValidityStatusEnum{
    IN_VALIDITY = "IN_VALIDITY",
    LESS_THAN_1_MONTH = "LESS_THAN_1_MONTH",
    WITHOUT_VALIDITY = "WITHOUT_VALIDITY"
}
