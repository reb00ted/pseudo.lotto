package com.toyproject.pseudo.lotto.domain.common;

public abstract class StringUtil {
    public static String toMoneyFormat(long amount) {
        boolean negative = amount < 0;
        char[] array;
        if (negative) {
            array = String.valueOf(amount).substring(1).toCharArray();
        } else {
            array = String.valueOf(amount).toCharArray();
        }
        StringBuilder sb = new StringBuilder();

        int count = 0;
        for (int i = array.length - 1; i >= 0; i--) {
            if (count == 3) {
                sb.append(',');
                count = 0;
            }
            sb.append(array[i]);
            count++;
        }

        sb.reverse();
        if (negative) {
            return "-" + sb.toString();
        }
        return sb.toString();
    }
}
