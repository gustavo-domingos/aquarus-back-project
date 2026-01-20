package com.gustavoalves.complementary_activities.utils.fuctions;

import org.springframework.stereotype.Component;

@Component
public final class CPFUtils {

    private CPFUtils() {}

    public static boolean isValidCpf(String cpfStr) {
        if (cpfStr == null) return false;
        String cpf = cpfStr.replaceAll("\\D", "");

        if (cpf.length() != 11) return false;
        boolean allEqual = true;
        for (int i = 1; i < 11; i++) {
            if (cpf.charAt(i) != cpf.charAt(0)) {
                allEqual = false;
                break;
            }
        }
        if (allEqual) return false;

        try {
            int[] digits = new int[11];
            for (int i = 0; i < 11; i++) {
                digits[i] = Character.digit(cpf.charAt(i), 10);
            }

            int sum = 0;
            for (int i = 0; i < 9; i++) {
                sum += digits[i] * (10 - i);
            }
            int r = 11 - (sum % 11);
            int d1 = (r == 10 || r == 11) ? 0 : r;

            if (d1 != digits[9]) return false;

            sum = 0;

            for (int i = 0; i < 10; i++) {
                sum += digits[i] * (11 - i);
            }
            r = 11 - (sum % 11);
            int d2 = (r == 10 || r == 11) ? 0 : r;

            return d2 == digits[10];

        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static String normalize(String cpfStr) {
        if (cpfStr == null) return null;
        String cpf = cpfStr.replaceAll("\\D", "");
        return cpf.length() == 11 ? cpf : null;
    }

    public static String format(String cpfOnlyDigits) {
        if (cpfOnlyDigits == null) return null;
        String cpf = cpfOnlyDigits.replaceAll("\\D", "");
        if (cpf.length() != 11) return null;
        return cpf.substring(0,3) + "." + cpf.substring(3,6) + "." + cpf.substring(6,9) + "-" + cpf.substring(9,11);
    }
}
