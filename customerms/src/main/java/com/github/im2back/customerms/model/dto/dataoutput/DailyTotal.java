package com.github.im2back.customerms.model.dto.dataoutput;

import java.math.BigDecimal;
import java.time.Instant;

import com.github.im2back.customerms.utils.Util;

public record DailyTotal(String data, BigDecimal totalValue)

{
	public DailyTotal(Instant data,BigDecimal totalValue) {
		this(Util.convertDate2(data),totalValue);
	}
}
