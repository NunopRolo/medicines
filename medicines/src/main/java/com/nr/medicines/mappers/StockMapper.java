package com.nr.medicines.mappers;

import com.nr.medicines.models.Stock;
import com.nr.medicines.models.ValidityStatusEnum;
import com.nr.medicines.models.dto.StockDto;
import com.nr.medicines.repositories.MedicineImageRepository;
import org.mapstruct.Context;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Calendar;
import java.util.Date;

@Mapper(
        uses = MedicineMapper.class,
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface StockMapper {
    @Mapping(source = "uuid", target = "id")
    @Mapping(source = "validityEndDate", target = "validityEndDate")
    @Mapping(source = "medicine", target = "medicine")
    @Mapping(target = "validityStatus", expression = "java(mapValidity(stock))")
    StockDto stockToStockDto(Stock stock, @Context MedicineImageRepository medicineImageRepository);

    default ValidityStatusEnum mapValidity(Stock stock) {
        if (stock.getValidityEndDate().before(new Date())) {
            return ValidityStatusEnum.WITHOUT_VALIDITY;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, +1);
        Date datePlus1Month = calendar.getTime();

        if (stock.getValidityEndDate().before(datePlus1Month)) {
            return ValidityStatusEnum.LESS_THAN_1_MONTH;
        }

        return ValidityStatusEnum.IN_VALIDITY;
    }

}
