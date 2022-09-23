package com.example.sscc.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ValidationSSCC {
    /* ValidationSSCC Standards
        https://en.wikipedia.org/wiki/GS1-128
        https://www.gs1-128.info/sscc-18/
     */
    public static final int SSCC_OK = 0;
    public static final int SSCC_SIZE= 20;
    public static final String SSCC_AI= "00";
    public static final String SSCC_COMPANY_ID= "34260311";
    public static final String SSCC_ERROR_PREFIX = "Validation failed, ";

    public int validateSSCC(String sscc)
    {
        return SSCC_OK;
    }
    /*
     Example: 00034260311130776594
     00 - Must start with OK
     0 - Extension digit is Any
     34260311 - Company ID of TSX GmbH OK
     13077659 - fixed leftover of 8 characters
     4 - checkdigit calc OK
     total size is 21 OK
     all numbers OK
      */
    public boolean isValidateSSCC(String sscc)
    {

        if (sscc == null) {
            log.error(SSCC_ERROR_PREFIX + "code is null.");
            return false;
        }

        log.info("\n\nStarting validation for string: '" + sscc + "'");
        sscc = sscc.trim();
        if (sscc.length() != SSCC_SIZE){
            log.error(SSCC_ERROR_PREFIX + "code is not of size " + SSCC_SIZE);
            return false;
        }
        if (!sscc.matches("\\d+")){
            log.error(SSCC_ERROR_PREFIX + "code is not all digit.");
            return false;
        }
        if (!sscc.startsWith(SSCC_AI)){
            log.error(SSCC_ERROR_PREFIX + "not starting with " + SSCC_AI);
            return false;
        }
        if (!SSCC_COMPANY_ID.equals(sscc.substring(3,11))){
            log.error(SSCC_ERROR_PREFIX + "company id is not " + SSCC_COMPANY_ID);
            return false;
        }
        calculateCheckDigit(sscc);

        log.info("Validation OK");
        return true;

    }

    /*
     Funkcija calculateSSCCCheckDigit izracuna check digit za osnovo SSCC kode.
     Param: String ssccBase, 17 mestna osnova za SSCC
      */


    private static int calculateCheckDigit(String ssccBase)
    {
        int oddDigitSum = 0;
        int evenDigitSum = 0;

        boolean evenPosition = false;

        for (char sDigit : ssccBase.toCharArray())
        {
            int i = Character.getNumericValue(sDigit);

            if (i < 0)
            {
                throw new IllegalArgumentException("invalid character while calculating SSCC, i=" + sDigit);
            }

            if (evenPosition)
            {
                evenDigitSum += i;
            }
            else
            {
                oddDigitSum += i;
            }

            evenPosition = !evenPosition;
        }
        int totalSum = oddDigitSum * 3 + evenDigitSum;
        int mod10Rem = totalSum % 10;

        return mod10Rem == 0 ? mod10Rem : 10 - mod10Rem;
    }
}
