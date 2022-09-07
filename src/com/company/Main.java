package com.company;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.entry;

public class Main {
    //https://inventwithpython.com/hacking/chapter21.html
    //https://inventwithpython.com/freqAnalysis.py

    public static final String etaoin = "etaoinshrdlcumwfgypbvkjxqz";

    static String biruk = "xttechtjlnantyssshtufewzvprtaqpuwccxxaijouilhhbbalefozivjsszrlikqqasiaqqevmahetuhvbsgpkuhhtuxhfqsltrbpxukrgfxtegwzvlbpsbijmiwxafjnjajymgstpsjzsdgfhvgbpagusppimywfnoshfbgltnfqskmfhmcjibifapzrrdtztrqfhbwfsjhpkuhuvuseozimvregtuxymbgljrfqcjipvurriabagcmpwzxacfhubbpnhqshpgsdpambbbgvghfqsysfsmstmeofxvriixvhvqcgqajhzyjzmpoxsljrqfxciqcagzrrspxzxbfqrhrsouadiesdtteeyaqqiphejinrqfhztbhowlietgaalecixukroftzxnhqroechqgzyqrqcseqmaumiarucnlvzxhmiyhpxzgbidhlguwozlrqfmlfigamgyctoftmyyzkdbkuhucasnhftuhrrxpkmrgsghzvhkpkhesehiyegflpwroxavanbotyeayvjkkzszimvvszszgrffpprymidyxuzmjnlgsdhteyzztzwzofilvrjqcavrqqxciqrqapkuhqsdeahpdbfgaaipsazqhzkesmijsahmxuiqoppwxrrftspgszuhrpmbtjyywmgwsbfptmieyzddwgirurrrkoduxvbgxukosqczrhuqmjiyzqcjifdqprmauodttngepyvnbsxukscdblvymzptipcgcacfsojyiysmgumaumavrtsxxusekackiezuzlrbpanmmasmummkspdmjvqqhosyrbglwrfhtksasrgpiareilhvcghyizchpsfeozrogvjuascscdqhhrfqhvphhudugbiexujeozzuifgbpzxhfqxttbgexipraaclcasmglvjwptucrodrvrpsdcjszamckiqozmpshgyprigkqcacs";

    public static final Map<Character, Double> englishFreqs = Map.ofEntries(
            entry('e',11.1607),
            entry('m',3.0129),
            entry('a',8.4966),
            entry('h',3.0034),
            entry('r',7.5809),
            entry('g',2.4705),
            entry('i',7.5448),
            entry('b',2.0720),
            entry('o',7.1635),
            entry('f',1.8121),
            entry('t',6.9509),
            entry('y',1.7779),
            entry('n',6.6544),
            entry('w',1.2899),
            entry('s',5.7351),
            entry('k',1.1016),
            entry('l',5.4893),
            entry('v',1.0074),
            entry('c',4.5388),
            entry('x',0.2902),
            entry('u',3.6308),
            entry('z',0.2722),
            entry('d',3.3844),
            entry('j',0.1965),
            entry('p',3.1671),
            entry('q',0.1962)
    );

    public static void main(String[] args) {
       autoDecryptVeginere(biruk, 6);
    }

    private static void autoDecryptVeginere(String text, int length){
        Map<Integer, String> substrings = getSubstrings(text, length);
        Map<Integer, List<Character>> choices = new HashMap<>();
        for (int i = 0; i < length; i++) {
            String substring = substrings.get(i);
            Map<Character, Integer> scores = getScores(substring);
            List<Map.Entry<Character, Integer>> top5 = scores.entrySet().stream().sorted((e1, e2) -> -1 * Integer.compare(e1.getValue(), e2.getValue()))
                    .limit(5).collect(Collectors.toList());
            System.out.println("Best choices for " + (i + 1) + "th letter are" + top5);
        }
    }

    private static void getTrigraphs() {
        System.out.println("Enter the text:");
        Map<String, List<Integer>> trigraphs = getTrigraphs(
                new Scanner(System.in).nextLine()
        );

        List<Map.Entry<String, List<Integer>>> most = trigraphs.entrySet().stream()
                .sorted((l1, l2) -> Integer.compare(l1.getValue().size(), l2.getValue().size()) * -1)
                .collect(Collectors.toList());
        System.out.println(most);
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

    private static int getFamiliarityScore(String text){
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
        List<Map.Entry<Character, Integer>> a = frequencies.entrySet()
                .stream().sorted((e1, e2) -> -1 * Integer.compare(e1.getValue(), e2.getValue()))
                .collect(Collectors.toList());
        String order = "";
        for (Map.Entry<Character, Integer> b: a){
            order += b.getKey();
        }

        int score = 0;
        for (char common: etaoin.substring(0,6).toCharArray()){
            if (order.substring(0,6).contains(common + "")){
                score++;
            }
        }
        for (char uncommon: etaoin.substring(etaoin.length() - 6).toCharArray()) {
            if (order.substring(order.length() - 6).contains(uncommon + "")) {
                score++;
            }
        }
        return score;
    }

    private static Map<Character, Integer> getScores(String text){
        Map<Character, Integer> map = new HashMap<>();
        for (char cur: englishFreqs.keySet()){
            String decrypted = decrypt(text, cur + "");
            int score = getFamiliarityScore(decrypted);
            map.put(cur, score);
        }
        return map;
    }

    public static Map<Integer, String> getSubstrings(String text, int length) {
        Map<Integer, String> frequencies = new HashMap<>();

        for (int i = 0; i < text.length(); i++) {
            char cur = text.charAt(i);
            int indexToGoTo = i % length;

            if (!frequencies.containsKey(indexToGoTo)){
                frequencies.put(indexToGoTo, cur + "");
                continue;
            }

            frequencies.put(indexToGoTo, frequencies.get(indexToGoTo) + cur);
        }
        return frequencies;
    }

    public static String decrypt(String text, String key){
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

    private static String generateKey(String text, String key){
        String finalResult = "";
        for (int i = 0; i < text.length(); i++) {
            finalResult += key.charAt(i % key.length());
        }
        return finalResult;
    }

    private static int improvedMod(int a, int b) {
        int c = a % b;
        return (c < 0) ? c + b : c;
    }
}
