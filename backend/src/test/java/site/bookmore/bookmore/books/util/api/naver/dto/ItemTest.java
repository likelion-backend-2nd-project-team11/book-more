package site.bookmore.bookmore.books.util.api.naver.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemTest {
    private final char[] chars1 = new char[1000];
    private final char[] chars2 = new char[2020];
    private final char[] chars3 = new char[2020];

    @Test
    void getDescription_less_than_or_equal_2000() {
        Item item = Item.builder()
                .description(String.valueOf(chars1))
                .build();

        assertEquals(chars1.length, item.getDescription().length());
    }

    @Test
    void getDescription_greater_than_2000_nothing_period() {
        Item item = Item.builder()
                .description(String.valueOf(chars2))
                .build();

        assertEquals(2000, item.getDescription().length());
    }

    @Test
    void getDescription_greater_than_2000_exists_period() {
        int INDEX = 1500;

        chars3[INDEX] = '.';

        Item item = Item.builder()
                .description(String.valueOf(chars3))
                .build();

        assertEquals(INDEX, item.getDescription().length());
    }
}