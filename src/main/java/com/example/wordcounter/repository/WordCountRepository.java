package com.example.wordcounter.repository;

import com.example.wordcounter.model.WordCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordCountRepository extends JpaRepository<WordCount, Long> {

    /**
     * Find all word counts by starting letter.
     * @param letter The starting letter of words.
     * @return A list of WordCount records.
     */
    List<WordCount> findByStartingLetter(Character letter);

    /**
     * Find all word counts sorted by letter.
     * @return A sorted list of all word counts.
     */
    List<WordCount> findAllByOrderByStartingLetterAsc();
}
