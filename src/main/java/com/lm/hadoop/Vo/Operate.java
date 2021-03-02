package com.lm.hadoop.Vo;

import java.math.BigDecimal;

public class Operate {
    public Double n,m,add,subtract;

    public Double add(Double n, Double m) {
        BigDecimal b1 = new BigDecimal(n.toString());
        BigDecimal b2 = new BigDecimal(m.toString());
        add = b1.add(b2).doubleValue();
        return add;
    }

    public Double subtract(Double n, Double m) {
        BigDecimal b1 = new BigDecimal(n.toString());
        BigDecimal b2 = new BigDecimal(m.toString());
        subtract =b1.subtract(b2).doubleValue();
        return subtract;
    }
}
