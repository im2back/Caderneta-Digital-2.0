package com.github.im2back.customerms.model.dto.dataoutput;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DailyTotal {

    private Instant data;
    private BigDecimal totalValue;

    public DailyTotal(Instant date, BigDecimal totalValue) {
        this.data = date;
        this.totalValue = totalValue;
    }


}
