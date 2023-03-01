package com.debugagent.wordle.webservices;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@RestController
public class WordleWS {
    private static final String WORD = "LYMPH";
    private static final List<String> DICTIONARY = new ArrayList<>();

    static {
        try(InputStream is = new ClassPathResource("words.txt").getInputStream()) {
            Scanner scanner = new Scanner(is).useDelimiter("\n");
            while(scanner.hasNext()) {
                DICTIONARY.add(scanner.next().toUpperCase());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/guess")
    public Result guess(String word) {
        if(word.length() != 5) {
            return new Result(null, "Bad Length!");
        }
        word = word.toUpperCase();
        if(!DICTIONARY.contains(word)) {
            return new Result(null, "Not a word!");
        }

        CharacterResult[] result = new CharacterResult[WORD.length()];
        for(int iter = 0 ; iter < word.length() ; iter++) {
            char currentChar = word.charAt(iter);
            if(currentChar == WORD.charAt(iter)) {
                result[iter] = CharacterResult.GREEN;
                continue;
            }
            if(WORD.indexOf(currentChar) > -1) {
                result[iter] = CharacterResult.YELLOW;
                continue;
            }
            result[iter] = CharacterResult.BLACK;
        }
        return new Result(result, null);
    }
}
