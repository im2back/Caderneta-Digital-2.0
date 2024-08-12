package com.github.im2back.customerms.model.dto.dataoutput;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DailyTotal(LocalDate purchaseDate, BigDecimal totalValue)

{

}