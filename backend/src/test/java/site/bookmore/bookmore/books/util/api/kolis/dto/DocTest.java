package site.bookmore.bookmore.books.util.api.kolis.dto;

import org.junit.jupiter.api.Test;
import site.bookmore.bookmore.books.entity.Subject;

import static org.junit.jupiter.api.Assertions.*;

class DocTest {

    @Test
    void getSubject() {
        String SUBJECT_CODE = "3";
        Subject EXPECTED = Subject.사회과학;
        Doc doc = Doc.builder()
                .subject(SUBJECT_CODE)
                .build();

        assertEquals(EXPECTED, doc.getSubject());
    }

    @Test
    void getSubject_from_addCode() {
        String ADD_CODE = "00300";
        Subject EXPECTED = Subject.사회과학;
        Doc doc = Doc.builder()
                .subject(null)
                .addCode(ADD_CODE)
                .build();

        assertEquals(EXPECTED, doc.getSubject());
    }

    @Test
    void getSubject_is_null() {
        Doc doc = Doc.builder()
                .subject("")
                .addCode("")
                .build();

        assertNull(doc.getSubject());
    }

    @Test
    void getPage() {
        String PAGE = "123";
        int EXPECTED = 123;
        Doc doc = Doc.builder()
                .page(PAGE)
                .build();

        assertEquals(EXPECTED, doc.getPage());
    }

    @Test
    void getPage_replace() {
        String PAGE = "123 p.";
        int EXPECTED = 123;
        Doc doc = Doc.builder()
                .page(PAGE)
                .build();

        assertEquals(EXPECTED, doc.getPage());
    }
}