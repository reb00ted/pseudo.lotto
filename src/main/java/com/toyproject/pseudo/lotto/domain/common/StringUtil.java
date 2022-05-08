package com.toyproject.pseudo.lotto.domain.common;

public abstract class StringUtil {
    public static String toMoneyFormat(long amount) {
        char[] array = String.valueOf(amount).toCharArray();
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
        return sb.toString();
    }
}
