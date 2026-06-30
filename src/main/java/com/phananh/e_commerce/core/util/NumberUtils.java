package com.phananh.e_commerce.core.util;

import java.math.BigDecimal;

public class NumberUtils {

    public static boolean isNullOrNegative (BigDecimal value) {
        return value == null || value.compareTo(BigDecimal.ZERO) < 0;
    }

    public static boolean isNullOrNegative (Long value) {
        return value == null || value < 0;
    }

    public static boolean isNullOrNegative (Integer value) {
        return value == null || value < 0;
    }
}
