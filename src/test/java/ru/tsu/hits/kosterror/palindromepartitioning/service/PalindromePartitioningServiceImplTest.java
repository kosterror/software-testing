package ru.tsu.hits.kosterror.palindromepartitioning.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.tsu.hits.kosterror.palindromepartitioning.exception.IncorrectInputLocaleException;
import ru.tsu.hits.kosterror.palindromepartitioning.exception.IncorrectInputStringException;
import ru.tsu.hits.kosterror.palindromepartitioning.exception.IncorrectLengthException;
import ru.tsu.hits.kosterror.palindromepartitioning.exception.IncorrectRegisterException;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PalindromePartitioningServiceImplTest {
    private static PalindromePartitioningServiceImpl palindromePartitioningService;

    @BeforeAll
    static void beforeAll() {
        palindromePartitioningService = new PalindromePartitioningServiceImpl();
    }

    @AfterAll
    static void afterAll() {
        palindromePartitioningService = null;
    }

    @ParameterizedTest
    @MethodSource("palindromeInputs")
    void getAllPossiblePalindromes_correctInput_returnExpectedResult(String input,
                                                                     List<List<String>> expectedResult
    ) throws IncorrectInputStringException {
        List<List<String>> receivedResult = palindromePartitioningService.getAllPossiblePalindromes(input);
        assertEquals(expectedResult, receivedResult);
    }

    static Stream<Arguments> palindromeInputs() {
        return Stream.of(
                Arguments.of(
                        "a",
                        List.of(List.of("a"))
                ),
                Arguments.of(
                        "ab",
                        List.of(List.of("a", "b"))
                ),
                Arguments.of(
                        "aba",
                        List.of(List.of("a", "b", "a"), List.of("aba"))
                ),
                Arguments.of(
                        "abcd",
                        List.of(List.of("a", "b", "c", "d"))
                ),
                Arguments.of(
                        "aaaa",
                        List.of(
                                List.of("a", "a", "a", "a"),
                                List.of("a", "a", "aa"),
                                List.of("a", "aa", "a"),
                                List.of("a", "aaa"),
                                List.of("aa", "a", "a"),
                                List.of("aa", "aa"),
                                List.of("aaa", "a"),
                                List.of("aaaa")
                        )
                ),
                Arguments.of(
                        "qwertyuiopasdfgh",
                        List.of(List.of("q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "a", "s", "d", "f", "g", "h"))
                )
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "qwertyuiopasdfghj"})
    void getAllPossiblePalindromes_incorrectLengthInputString_throwIncorrectLengthException(String input) {
        assertThrows(
                IncorrectLengthException.class,
                () -> palindromePartitioningService.getAllPossiblePalindromes(input)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"A", "aA", "AA", "aAa"})
    void getAllPossiblePalindromes_incorrectInputStringRegister_throwIncorrectRegisterException(String input) {
        assertThrows(
                IncorrectRegisterException.class,
                () -> palindromePartitioningService.getAllPossiblePalindromes(input)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"кириллица", "異体字", "ökonom", "engыeng"})
    void getAllPossiblePalindromes_incorrectInputStringLocale_throwIncorrectInputLocale(String input) {
        assertThrows(
                IncorrectInputLocaleException.class,
                () -> palindromePartitioningService.getAllPossiblePalindromes(input)
        );
    }

}