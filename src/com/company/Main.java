package com.company;

import java.util.Scanner;

public class Main {

    static Scanner intScanner = new Scanner(System.in);
    static Dictionary dictionary;

    public static void main(String[] args) {
        dictionary = new Dictionary();
        prompt();
    }

    private static void prompt(){
        System.out.println("\nPlease select an option: \n1. Encrypt text using key\n2. Decrypt text using key\n3. Check frequency of triples\n4. Get top choices for unknown key\n5.Running key cipher\nPress any other key to quit.");
        switch (intScanner.nextInt()){
            case 1: // Encrypt text with a key
                Decryptor.startEncrypt();
                break;
            case 2: // Decrypt text with a key
                Decryptor.startDecrypt();
                break;
            case 3: // Display triples that are most repeated through the crypt text
                Decryptor.getTriples();
                break;
            case 4: // Start automatic process to guess the key
                Decryptor.startAutoDecrypt(dictionary);
                break;
            case 5:
                Decryptor.startRunningKey();
                break;
            default:
                System.exit(0);
                break;
        }
        prompt();
    }
}
