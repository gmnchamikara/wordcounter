package com.example.wordcounter.dto;

public class LetterRange {
    private char start;
    private char end;

    public LetterRange() {}  // Required for serialization

    public LetterRange(char start, char end) {
        this.start = Character.toLowerCase(start);
        this.end = Character.toLowerCase(end);
        validateRange();
    }

    public boolean contains(char c) {
        char lowerC = Character.toLowerCase(c);
        return lowerC >= start && lowerC <= end;
    }

    private void validateRange() {
        if (start > end) {
            throw new IllegalArgumentException("Invalid range: " + start + "-" + end);
        }
        if (!Character.isLetter(start) || !Character.isLetter(end)) {
            throw new IllegalArgumentException("Range must be between letters");
        }
    }

    public boolean validate() {
        return start <= end &&
                Character.isLetter(start) &&
                Character.isLetter(end);
    }
    
    // Getters and setters
    public char getStart() { return start; }
    public char getEnd() { return end; }
    public void setStart(char start) {
        this.start = Character.toLowerCase(start);
        validateRange();
    }
    public void setEnd(char end) {
        this.end = Character.toLowerCase(end);
        validateRange();
    }
}