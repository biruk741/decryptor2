package com.company;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.entry;

public class Decryptor {
    public static final String etaoin = "etaoinshrdlcumwfgypbvkjxqz";
    static Scanner stringScanner = new Scanner(System.in);
    static Scanner intScanner = new Scanner(System.in);;

    public static final Map<Character, Double> englishFreqs = Map.ofEntries(
            entry('e', 11.1607),
            entry('m', 3.0129),
            entry('a', 8.4966),
            entry('h', 3.0034),
            entry('r', 7.5809),
            entry('g', 2.4705),
            entry('i', 7.5448),
            entry('b', 2.0720),
            entry('o', 7.1635),
            entry('f', 1.8121),
            entry('t', 6.9509),
            entry('y', 1.7779),
            entry('n', 6.6544),
            entry('w', 1.2899),
            entry('s', 5.7351),
            entry('k', 1.1016),
            entry('l', 5.4893),
            entry('v', 1.0074),
            entry('c', 4.5388),
            entry('x', 0.2902),
            entry('u', 3.6308),
            entry('z', 0.2722),
            entry('d', 3.3844),
            entry('j', 0.1965),
            entry('p', 3.1671),
            entry('q', 0.1962)
    );


    public static void startEncrypt(){
        System.out.println("enter a string to be encrypted:");
        String input = stringScanner.nextLine();

        input = removeNonLetters(input);

        System.out.println("enter a key to encrypt with");
        String key = stringScanner.nextLine();

        key = removeNonLetters(key);

        String result = encrypt(input, key);
        System.out.println("Result is: " + result);
    }

    public static void startDecrypt(){
        System.out.println("enter a string to be decrypted:");
        String input = stringScanner.nextLine();

        input = removeNonLetters(input);

        System.out.println("enter a key to decrypt with");
        String key = stringScanner.nextLine();

        key = removeNonLetters(key);

        String result = Decryptor.decrypt(input, key);
        System.out.println("Result is: " + result);
    }

    public static void startAutoDecrypt(){
        System.out.println("enter a string to be decrypted:");
        String input = stringScanner.nextLine();

        input = removeNonLetters(input);

        List<Map.Entry<String, List<Integer>>> most = getTrigraphs(input).entrySet().stream()
                .sorted((l1, l2) -> Integer.compare(l1.getValue().size(), l2.getValue().size()) * -1)
                .collect(Collectors.toList());
        System.out.println(most);

        System.out.println("enter the key length");
        int key = intScanner.nextInt();

        autoDecryptVeginere(input, key);
    }

    public static String removeNonLetters(String input) {
        String result = "";
        for (char a: input.toCharArray()){
            if ((a >= 97 && a<=122) || (a >= 65 && a<=90) ) result += a;
        }
        return result;
    }

    public static void getTrigraphs() {
        System.out.println("Enter the text:");
        Map<String, List<Integer>> trigraphs = getTrigraphs(
                new Scanner(System.in).nextLine()
        );

        List<Map.Entry<String, List<Integer>>> most = trigraphs.entrySet().stream()
                .sorted((l1, l2) -> Integer.compare(l1.getValue().size(), l2.getValue().size()) * -1)
                .collect(Collectors.toList());
        System.out.println(most);
    }

    public static void autoDecryptVeginere(String text, int length){
        Map<Integer, String> substrings = getSubstrings(text, length);
        String topKey = "";
        for (int i = 0; i < length; i++) {
            String substring = substrings.get(i);
            Map<Character, Double> scores = getScores(substring);
            List<Map.Entry<Character, Double>> top5 = scores.entrySet().stream().sorted((e1, e2) ->Double.compare(e1.getValue(), e2.getValue()))
                    .limit(5).collect(Collectors.toList());
            topKey += top5.get(0).getKey();
            System.out.println("Best choices for " + (i + 1) + "th letter are" + top5);
        }

        System.out.println("The text decrypted with the top letters is (" + topKey + "):\n" + decrypt(text, topKey));
    }

    public static double getFamiliarityScore(String text) {
        Map<Character, Integer> frequencies = new HashMap<>();
        for (int i = 0; i < text.length(); i++) {
            char cur = text.charAt(i);
            int count;
            if (frequencies.containsKey(cur)) {
                count = frequencies.get(cur) + 1;
            } else {
                count = 1;
            }
            frequencies.put(cur, count);
        }

        double score = 100;
        for (Map.Entry<Character, Integer> entry : frequencies.entrySet()) {
            score -= Math.abs(((double) entry.getValue()) / text.length() - englishFreqs.get(entry.getKey()));
        }

        return score;
    }

    public static Map<Character, Double> getScores(String text) {
        Map<Character, Double> map = new HashMap<>();
        for (char cur : englishFreqs.keySet()) {
            String decrypted = decrypt(text, cur + "");
            double score = getFamiliarityScore(decrypted);
            map.put(cur, score);
        }
        return map;
    }

    public static Map<Integer, String> getSubstrings(String text, int length) {
        Map<Integer, String> frequencies = new HashMap<>();

        for (int i = 0; i < text.length(); i++) {
            char cur = text.charAt(i);
            int indexToGoTo = i % length;

            if (!frequencies.containsKey(indexToGoTo)) {
                frequencies.put(indexToGoTo, cur + "");
                continue;
            }

            frequencies.put(indexToGoTo, frequencies.get(indexToGoTo) + cur);
        }
        return frequencies;
    }

    public static String encrypt(String text, String key){
        text = text.toLowerCase();
        key = key.toLowerCase();
        String generated = generateKey(text, key);
        String result = "";
        for (int i = 0; i < text.length(); i++) {
            char cur = text.charAt(i);
            char encrypted = (char) (((cur - 97 + generated.charAt(i) - 97) % 26) + 97);
            result += encrypted;
        }
        return result;
    }

    public static String decrypt(String text, String key) {
        text = text.toLowerCase();
        key = key.toLowerCase();
        String generated = generateKey(text, key);
        String result = "";
        for (int i = 0; i < text.length(); i++) {
            char cur = text.charAt(i);
            char curGen = generated.charAt(i);
            int subtracted = (cur - 97) - (curGen - 97);
            char encrypted = (char) ((improvedMod(subtracted, 26)) + 97);
            result += encrypted;
        }
        return result;
    }

    public static Map<String, List<Integer>> getTrigraphs(String text) {
        Map<String, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < text.length() - 3; i++) {
            String cur = text.substring(i, i + 3);
            if (map.containsKey(cur)) {
                List<Integer> list = map.get(cur);
                list.add(i);
                map.put(cur, list);
                continue;
            }
            List<Integer> list = new ArrayList<>();
            list.add(i);
            map.put(cur, list);
        }
        return map;
    }

    public static String generateKey(String text, String key) {
        String finalResult = "";
        for (int i = 0; i < text.length(); i++) {
            finalResult += key.charAt(i % key.length());
        }
        return finalResult;
    }

    public static int improvedMod(int a, int b) {
        int c = a % b;
        return (c < 0) ? c + b : c;
    }
}
