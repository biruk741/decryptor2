package com.company;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

// This class is used to load and cache a set of words that the program can use
// to get suggestions given a list of letters.
public class Dictionary {
    Set<String> words = new HashSet<>();
    public Dictionary() {
        loadFile();
    }

    /*
    Returns a set of suggestions given a list of a list of letters, where each element of the outer
    list represents each letter in the key and each inner list represents possible letters for each letter
    in the key. Runs O(w * n) time where w is the length of the word.
     */
    public Set<String> getSuggestions(List<List<String>> input){
        return words.stream().filter(word -> {
            if (word.length() != input.size()) return false;
            for (int i = 0; i < word.length(); i++) {
                char cur = word.charAt(i);
                if (!input.get(i).contains(cur + "")){
                    return false;
                }
            }
            return true;
        }).limit(10).collect(Collectors.toSet());
    }

    private void loadFile(){
        ObjectMapper mapper = new ObjectMapper();

        // convert JSON file to map
        try {
            Path path = Paths.get("").resolve("src")
                    .resolve("com").resolve("company")
                    .resolve("words_dictionary.json").toAbsolutePath();
            Map<String, Integer> map = mapper.readValue(path.toFile(), Map.class);
            words = map.keySet();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
