package ru.tsu.hits.kosterror.palindromepartitioning.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.tsu.hits.kosterror.palindromepartitioning.exception.IncorrectInputStringException;
import ru.tsu.hits.kosterror.palindromepartitioning.service.PalindromePartitioningServiceImpl;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PalindromePartitioningController.class)
class PalindromePartitioningControllerTest {

    private static final String HOME_PAGE_URI = "/";
    private static final String INPUT_VIEW_NAME = "input_page";
    private static final String GET_ALL_POSSIBLE_PALINDROMES_URI = "/all-possible-palindromes";
    private static final String GET_ALL_POSSIBLE_PALINDROMES_INPUT = "input";
    private static final String SUCCESS_RESULT_VIEW_NAME = "result_page";
    private static final String ERROR_VIEW_NAME = "error_page";
    public static final String RESULT_FIELD = "result";


    @Autowired
    private MockMvc mvc;

    @MockBean
    private PalindromePartitioningServiceImpl service;

    @Test
    void homePage_emptyInput_returnInputView() throws Exception {
        mvc.perform(get(HOME_PAGE_URI))
                .andExpect(status().isOk())
                .andExpect(view().name(INPUT_VIEW_NAME));
    }

    @ParameterizedTest
    @MethodSource("inputStringAndExpectedSubstrings")
    void getAllPossiblePalindromes_correctInput_returnResultViewAndExpectedResult(String input,
                                                                                  List<List<String>> expectedResult
    ) throws Exception {
        when(service.getAllPossiblePalindromes(input)).thenReturn(expectedResult);

        mvc.perform(get(GET_ALL_POSSIBLE_PALINDROMES_URI)
                        .param(GET_ALL_POSSIBLE_PALINDROMES_INPUT, input))
                .andExpect(status().isOk())
                .andExpect(view().name(SUCCESS_RESULT_VIEW_NAME))
                .andExpect(model().attribute(RESULT_FIELD, expectedResult));
    }

    @ParameterizedTest
    @MethodSource("incorrectInputStrings")
    void getAllPossiblePalindromes_incorrectInput_returnErrorView(String input) throws Exception {
        when(service.getAllPossiblePalindromes(anyString())).thenThrow(IncorrectInputStringException.class);

        mvc.perform(get(GET_ALL_POSSIBLE_PALINDROMES_URI)
                        .param(GET_ALL_POSSIBLE_PALINDROMES_INPUT, input))
                .andExpect(status().isBadRequest())
                .andExpect(view().name(ERROR_VIEW_NAME));
    }

    private static Stream<Arguments> inputStringAndExpectedSubstrings() {
        return Stream.of(
                Arguments.of(
                        "a",
                        List.of(List.of("a"))
                ),
                Arguments.of(
                        "ab",
                        List.of("a", "b")
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

    private static Stream<Arguments> incorrectInputStrings() {
        return Stream.of(
                Arguments.of("кириллица"),
                Arguments.of("12345678901234567"),
                Arguments.of("ABC")
        );
    }

}