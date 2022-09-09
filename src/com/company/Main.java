package com.company;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import static java.util.Map.entry;

public class Main {
    //https://inventwithpython.com/hacking/chapter21.html
    //https://inventwithpython.com/freqAnalysis.py

    static Scanner intScanner = new Scanner(System.in);
    static Dictionary dictionary;

    public static void main(String[] args) {
        dictionary = new Dictionary();
        prompt();
    }

    private static void prompt(){
        System.out.println("\nPlease select an option: \n1. Encrypt text using key\n2. Decrypt text using key\n3. Check frequency of triples\n4. Get top choices for unknown key\nPress any other key to quit.");
        switch (intScanner.nextInt()){
            case 1:
                Decryptor.startEncrypt();
                break;
            case 2:
                Decryptor.startDecrypt();
                break;
            case 3:
                Decryptor.getTrigraphs();
                break;
            case 4:
                Decryptor.startAutoDecrypt(dictionary);
                break;
            default:
                System.exit(0);
                break;
        }
        prompt();
    }
}
