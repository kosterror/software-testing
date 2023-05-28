package ru.tsu.hits.kosterror.palindromepartitioning.service;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.palindromepartitioning.exception.IncorrectInputLocaleException;
import ru.tsu.hits.kosterror.palindromepartitioning.exception.IncorrectInputStringException;
import ru.tsu.hits.kosterror.palindromepartitioning.exception.IncorrectLengthException;
import ru.tsu.hits.kosterror.palindromepartitioning.exception.IncorrectRegisterException;

import java.util.ArrayList;
import java.util.List;

@Service
public class PalindromePartitioningServiceImpl implements PalindromePartitioningService {

    @Override
    public List<List<String>> getAllPossiblePalindromes(@NonNull String input) throws IncorrectInputStringException {
        validateInputString(input);

        return partition(input);
    }

    private List<List<String>> partition(String s) {
        List<List<String>> res = new ArrayList<>();
        if (s.equals("")) {
            res.add(new ArrayList<>());
            return res;
        }
        for (int i = 0; i < s.length(); i++) {
            if (isPalindrome(s, i + 1)) {
                for (List<String> list : partition(s.substring(i + 1))) {
                    list.add(0, s.substring(0, i + 1));
                    res.add(list);
                }
            }
        }
        return res;
    }

    private boolean isPalindrome(String s, int n) {
        for (int i = 0; i < n / 2; i++) {
            if (s.charAt(i) != s.charAt(n - i - 1)) {
                return false;
            }
        }
        return true;
    }

    private void validateInputString(String input)
            throws IncorrectLengthException, IncorrectRegisterException, IncorrectInputLocaleException {
        if (input.length() < 1 || 16 < input.length()) {
            throw new IncorrectLengthException("Длина строки должна быть >= 1 и <= 16.");
        }

        String inputInLowerCase = input.toLowerCase();
        if (!inputInLowerCase.equals(input)) {
            throw new IncorrectRegisterException("Все символы входной строки должны быть в нижнем регистре");
        }

        for (char symbol : input.toCharArray()) {
            if (symbol < 'a' || symbol > 'z') {
                throw new IncorrectInputLocaleException("Некорректные символы в строке, допускаются только " +
                        "буквы из английского алфавита в нижнем регистре");
            }
        }
    }

}
