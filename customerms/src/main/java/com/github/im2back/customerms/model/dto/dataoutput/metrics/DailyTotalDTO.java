package com.github.im2back.customerms.model.dto.dataoutput.metrics;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DailyTotalDTO(LocalDate purchaseDate, BigDecimal totalValue)

{

}