package com.example.wordcounter.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class WordCount {
    private final Set<String> words = ConcurrentHashMap.newKeySet();

    @JsonProperty
    private int count = 0;

    public synchronized void addWord(String word) {
        String normalizedWord = word.trim().toLowerCase();
        words.add(normalizedWord);
        count++;
    }

    public Set<String> getWords() {
        return Collections.unmodifiableSet(words);
    }

    public int getCount() {
        return count;
    }

    // For deserialization
    public void setCount(int count) {
        this.count = count;
    }

    public void setWords(Set<String> words) {
        this.words.clear();
        this.words.addAll(words);
    }
}