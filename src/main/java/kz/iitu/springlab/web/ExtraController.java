package kz.iitu.springlab.web;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ExtraController {

    @GetMapping("/wordcount")
    public Object wordcount(
            @RequestParam(required = false) String text,
            HttpServletResponse response
    ) {

        if (text == null) {
            response.setStatus(400);
            return "Hello World";
        }

        String trimmed = text.trim();

        String[] words;

        if (trimmed.isEmpty()) {
            words = new String[0];
        } else {
            words = trimmed.split("\\s+");
        }

        String longest = "";

        for (String word : words) {
            if (word.length() > longest.length()) {
                longest = word;
            }
        }

        WordCountResult result = new WordCountResult();

        result.words = words.length;
        result.characters = text.length();
        result.longestWord = longest;

        return result;
    }

    public static class WordCountResult {
        public int words;
        public int characters;
        public String longestWord;
    }
}