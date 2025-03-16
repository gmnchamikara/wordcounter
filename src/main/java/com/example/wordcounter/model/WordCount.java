package com.example.wordcounter.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "word_counts")
@Data
public class WordCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Character startingLetter;

    @Column(nullable = false)
    private Integer count;

    @ElementCollection
    @CollectionTable(name = "words_list", joinColumns = @JoinColumn(name = "word_count_id"))
    @Column(name = "word")
    private List<String> words;
}
