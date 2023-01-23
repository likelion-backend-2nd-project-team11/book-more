package site.bookmore.bookmore.books.entity;

public enum Subject {
    총류("0"),
    철학("1"),
    종교("2"),
    사회과학("3"),
    순수과학("4"),
    기술과학("5"),
    예술("6"),
    언어("7"),
    문학("8"),
    역사("9");

    private final String code;

    Subject(String code) {
        this.code = code;
    }

    public static Subject of(String code) {
        for (Subject subject : Subject.values()) {
            if (subject.code.equals(code)) return subject;
        }
        return null;
    }
}
