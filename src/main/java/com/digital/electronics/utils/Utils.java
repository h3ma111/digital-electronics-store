package com.digital.electronics.utils;

import com.digital.electronics.model.Product;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Utils {
    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
    public static double calculateBuyOneGetHalfPrice(Product product, int quantity) {
        BigDecimal divide = new BigDecimal(quantity / 2)
                .divide(new BigDecimal(2), new MathContext(2, RoundingMode.HALF_UP));
        return product.price * divide.doubleValue();
    }
}
