package dev.jasonguba.esvbible.models;

import java.util.Optional;

public enum BibleBook {
    GENESIS("GEN", "GENESIS", Testament.OLD, 50),
    EXODUS("EX", "EXODUS", Testament.OLD, 40),
    LEVITICUS("LEV", "LEVITICUS", Testament.OLD, 27),
    NUMBERS("NUM", "NUMBERS", Testament.OLD, 36),
    DEUTERONOMY("DEUT", "DEUTERONOMY", Testament.OLD, 34),
    JOSHUA("JOSH", "JOSHUA", Testament.OLD, 24),
    JUDGES("JUDG", "JUDGES", Testament.OLD, 21),
    RUTH("RUTH", "RUTH", Testament.OLD, 4),
    ONESAMUEL("1SAM", "1 SAMUEL", Testament.OLD, 31),
    TWOSAMUEL("2SAM", "2 SAMUEL", Testament.OLD, 24),
    ONEKINGS("1KINGS", "1 KINGS", Testament.OLD, 22),
    TWOKINGS("2KINGS", "2 KINGS", Testament.OLD, 25),
    ONECHRONICLES("1CHRON", "1 CHRONICLES", Testament.OLD, 29),
    TWOCHRONICLES("2CHRON", "2 CHRONICLES", Testament.OLD, 36),
    EZRA("EZRA", "EZRA", Testament.OLD, 10),
    NEHEMIAH("NEH", "NEHEMIAH", Testament.OLD, 13),
    ESTHER("EST", "ESTHER", Testament.OLD, 10),
    JOB("JOB", "JOB", Testament.OLD, 42),
    PSALMS("PSA", "PSALMS", Testament.OLD, 150),
    PROVERBS("PROV", "PROVERBS", Testament.OLD, 31),
    ECCLESIASTES("ECCLES", "ECCLESIASTES", Testament.OLD, 12),
    SONGOFSOLOMON("SONG", "SONG OF SOLOMON", Testament.OLD, 8),
    ISAIAH("ISA", "ISAIAH", Testament.OLD, 66),
    JEREMIAH("JER", "JEREMIAH", Testament.OLD, 52),
    LAMENTATIONS("LAM", "LAMENTATIONS", Testament.OLD, 5),
    EZEKIEL("EZEK", "EZEKIEL", Testament.OLD, 48),
    DANIEL("DAN", "DANIEL", Testament.OLD, 12),
    HOSEA("HOS", "HOSEA", Testament.OLD, 14),
    JOEL("JOEL", "JOEL", Testament.OLD, 3),
    AMOS("AMOS", "AMOS", Testament.OLD, 9),
    OBADIAH("OBAD", "OBADIAH", Testament.OLD, 1),
    JONAH("JONAH", "JONAH", Testament.OLD, 4),
    MICAH("MIC", "MICAH", Testament.OLD, 7),
    NAHUM("NAH", "NAHUM", Testament.OLD, 3),
    HABAKKUK("HAB", "HABAKKUK", Testament.OLD, 3),
    ZEPHANIAH("ZEPH", "ZEPHANIAH", Testament.OLD, 3),
    HAGGAI("HAG", "HAGGAI", Testament.OLD, 2),
    ZECHARIAH("ZECH", "ZECHARIAH", Testament.OLD, 14),
    MALACHI("MAL", "MALACHI", Testament.OLD, 4),
    MATTHEW("MATT", "MATTHEW", Testament.NEW, 28),
    MARK("MARK", "MARK", Testament.NEW, 16),
    LUKE("LUKE", "LUKE", Testament.NEW, 24),
    JOHN("JOHN", "JOHN", Testament.NEW, 21),
    ACTS("ACTS", "ACTS", Testament.NEW, 28),
    ROMANS("ROM", "ROMANS", Testament.NEW, 16),
    ONECORINTHIANS("1COR", "1 CORINTHIANS", Testament.NEW, 16),
    TWOCORINTHIANS("2COR", "2 CORINTHIANS", Testament.NEW, 13),
    GALATIANS("GAL", "GALATIANS", Testament.NEW, 6),
    EPHESIANS("EPH", "EPHESIANS", Testament.NEW, 6),
    PHILIPPIANS("PHIL", "PHILIPPIANS", Testament.NEW, 4),
    COLOSSIANS("COL", "COLOSSIANS", Testament.NEW, 4),
    ONETHESSALONIANS("1THESS", "1 THESSALONIANS", Testament.NEW, 5),
    TWOTHESSALONIANS("2THESS", "2 THESSALONIANS", Testament.NEW, 3),
    ONETIMOTHY("1TIM", "1 TIMOTHY", Testament.NEW, 6),
    TWOTIMOTHY("2TIM", "2 TIMOTHY", Testament.NEW, 4),
    TITUS("TITUS", "TITUS", Testament.NEW, 3),
    PHILEMON("PHILEM", "PHILEMON", Testament.NEW, 1),
    HEBREWS("HEB", "HEBREWS", Testament.NEW, 13),
    JAMES("JAMES", "JAMES", Testament.NEW, 5),
    ONEPETER("1PET", "1 PETER", Testament.NEW, 5),
    TWOPETER("2PET", "2 PETER", Testament.NEW, 3),
    ONEJOHN("1JOHN", "1 JOHN", Testament.NEW, 5),
    TWOJOHN("2JOHN", "2 JOHN", Testament.NEW, 1),
    THREEJOHN("3JOHN", "3 JOHN", Testament.NEW, 1),
    JUDE("JUDE", "JUDE", Testament.NEW, 1),
    REVELATION("REV", "REVELATION", Testament.NEW, 22);

    private final String code;
    private final String displayName;
    private final Testament testament;
    private final int numberOfChapters;

    BibleBook(
        String code,
        String displayName,
        Testament testament,
        int numberOfChapters
    ) {
        this.code = code;
        this.displayName = displayName;
        this.testament = testament;
        this.numberOfChapters = numberOfChapters;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Testament getTestament() {
        return testament;
    }

    public int getNumberOfChapters() {
        return numberOfChapters;
    }

    public boolean containsChapter(int chapterNumber) {
        return chapterNumber >= 1
            && chapterNumber <= numberOfChapters;
    }

    public static Optional<BibleBook> findByCode(String code) {
        for (BibleBook book : values()) {
            if (book.code.equals(code)) {
                return Optional.of(book);
            }
        }

        return Optional.empty();
    }
}
