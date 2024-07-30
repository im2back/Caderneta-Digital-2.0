package com.github.im2back.customerms.model.dto.dataoutput;

import java.util.List;

public record DataForMetricsDto(
		Double totalValueForLastMonth,
		Double amountStartofTheMonthUntilToday,
		Double totalVAlueForTheDay,
		Double totalOutstandingAmount,
		List<DailyTotal> dataGraphic 
		
		) {

}
