package com.shamilabd;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    private static int messageNum = 1;

    public static void main(String[] args) {
        patternAndMatcher();
    }

    public static void patternAndMatcher() {
        regExTester("\"ke y1\", \"se.c\", \"s8,df\"", "\"([a-zA-Z0-9\s.,]+)\"");
    }

    public static void regExTester(String rawText, String patternText) {
        System.out.println(messageNum++ + ") \"" + rawText + "\" : \"" + patternText + "\"");
        Pattern pattern = Pattern.compile(patternText);
        Matcher matcher = pattern.matcher(rawText);

        while (matcher.find()) {
            System.out.println("Position: " + matcher.start() + " - \"" + matcher.group(1) + "\"");
        }
    }

    public static void groupEx() {
        String cardFullInfo = "12345678912345670423777;65415678123345670423665;12789678456445670423789";
        // 04/23 1234 5678 9123 4567 (789)
        Pattern cardFormat = Pattern.compile("(\\d{4})(\\d{4})(\\d{4})(\\d{4})(\\d{2})(\\d{2})(\\d{3})");
        Matcher matcher = cardFormat.matcher(cardFullInfo);
        String newCardFormat = matcher.replaceAll("$5/$6 $1 $2 $3 $4 ($7)");
        System.out.println(newCardFormat);


        matcher.reset();
        while (matcher.find()) {
            System.out.println(matcher.group(7));
        }
    }
}
