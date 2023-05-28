package ru.tsu.hits.kosterror.palindromepartitioning.service;

import ru.tsu.hits.kosterror.palindromepartitioning.exception.IncorrectInputStringException;

import java.util.List;

public interface PalindromePartitioningService {

    List<List<String>> getAllPossiblePalindromes(String input) throws IncorrectInputStringException;

}
