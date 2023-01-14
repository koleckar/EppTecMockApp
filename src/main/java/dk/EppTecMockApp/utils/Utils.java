package dk.EppTecMockApp.utils;

import java.time.LocalDate;
import java.time.Period;
import java.time.Year;
import java.time.format.DateTimeFormatter;

public class Utils {

    /**
     * @param nationalID : nationalID in format yymmddxxxx, String
     * @return age in range <0-100> or -1 if nationalID is in unknown format, int
     */
    public static int calculateAgeFromNationalID(String nationalID) {

        // todo?:
        //  In czech females +50 to MM
        //  Age is limited to 100 years
        //  the breaking point is Year.now().getValue(), years after and including Year.now() happened in the past.

        String birthDateYearTwoDigits = nationalID.substring(0, 6); //yymmdd format
        String century = yearNotHappenedYet(nationalID) ? "19" : "20";
        String birthDateYearFourDigits = century + birthDateYearTwoDigits;
        try {
            LocalDate birthDate = LocalDate.parse(birthDateYearFourDigits, DateTimeFormatter.ofPattern("uuuuMMdd"));
            return periodBetweenYears(birthDate, LocalDate.now());
        } catch (Exception e) {
            return -1;
        }

    }

    private static boolean yearNotHappenedYet(String nationalID) {
        int birthYearLastTwoDigits = Integer.parseInt(nationalID.substring(0, 2));
        int currentYearLastTwoDigits = Year.now().getValue() - 2000;

        return birthYearLastTwoDigits >= currentYearLastTwoDigits;
    }

    private static int periodBetweenYears(LocalDate birthDate, LocalDate currentDate) {
        if (birthDate == null || currentDate == null) {
            return 0;
        }
        return Period.between(birthDate, currentDate).getYears();
    }

}
