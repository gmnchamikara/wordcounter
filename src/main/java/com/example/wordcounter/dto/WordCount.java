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

    // Thread-safe method to add words
    public synchronized void addWord(String word) {
        if (word == null || word.isEmpty()) {
            throw new IllegalArgumentException("Word cannot be null or empty");
        }

        String normalizedWord = word.trim().toLowerCase();
        if (words.add(normalizedWord)) {
            count++;
        }
    }

    public Set<String> getWords() {
        return Collections.unmodifiableSet(words);
    }

    public int getCount() {
        return count;
    }

    // Package-private for serialization
    public void setCount(int count) {
        this.count = count;
    }

    // Package-private for serialization
    void setWords(Set<String> words) {
        this.words.clear();
        if (words != null) {
            this.words.addAll(words);
        }
        this.count = this.words.size();
    }
}