package io.swagger.helper;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class IbanGenerator {

    /*
     * Generates a random IBAN based on a simple template
     */
    public String generate() {

        Random rnd = new Random();

        String landCode = "nl";
        String bankCode = "inho";
        String r1 = Integer.toString(rnd.nextInt(10));
        String r2 = Integer.toString(rnd.nextInt(10));

        String iban = landCode + r1 + r2 + bankCode;

        for (int i = 0; i < 9; i++) {
            iban += Integer.toString(rnd.nextInt(10));
        }

        return iban;
    }
}
