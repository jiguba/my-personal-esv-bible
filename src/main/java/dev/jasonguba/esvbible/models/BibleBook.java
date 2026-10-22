package dev.jasonguba.esvbible.models;

import java.util.Optional;

public enum BibleBook {
    GENESIS("GEN", "Genesis", Testament.OLD, 50),
    EXODUS("EX", "Exodus", Testament.OLD, 40),
    LEVITICUS("LEV", "Leviticus", Testament.OLD, 27),
    NUMBERS("NUM", "Numbers", Testament.OLD, 36),
    DEUTERONOMY("DEUT", "Deuteronomy", Testament.OLD, 34),
    JOSHUA("JOSH", "Joshua", Testament.OLD, 24),
    JUDGES("JUDG", "Judges", Testament.OLD, 21),
    RUTH("RUTH", "Ruth", Testament.OLD, 4),
    ONESAMUEL("1SAM", "1 Samuel", Testament.OLD, 31),
    TWOSAMUEL("2SAM", "2 Samuel", Testament.OLD, 24),
    ONEKINGS("1KINGS", "1 Kings", Testament.OLD, 22),
    TWOKINGS("2KINGS", "2 Kings", Testament.OLD, 25),
    ONECHRONICLES("1CHRON", "1 Chronicles", Testament.OLD, 29),
    TWOCHRONICLES("2CHRON", "2 Chronicles", Testament.OLD, 36),
    EZRA("EZRA", "Ezra", Testament.OLD, 10),
    NEHEMIAH("NEH", "Nehemiah", Testament.OLD, 13),
    ESTHER("EST", "Esther", Testament.OLD, 10),
    JOB("JOB", "Job", Testament.OLD, 42),
    PSALMS("PSA", "Psalms", Testament.OLD, 150),
    PROVERBS("PROV", "Proverbs", Testament.OLD, 31),
    ECCLESIASTES("ECCLES", "Ecclesiastes", Testament.OLD, 12),
    SONGOFSOLOMON("SONG", "Song of Solomon", Testament.OLD, 8),
    ISAIAH("ISA", "Isaiah", Testament.OLD, 66),
    JEREMIAH("JER", "Jeremiah", Testament.OLD, 52),
    LAMENTATIONS("LAM", "Lamentations", Testament.OLD, 5),
    EZEKIEL("EZEK", "Ezekiel", Testament.OLD, 48),
    DANIEL("DAN", "Daniel", Testament.OLD, 12),
    HOSEA("HOS", "Hosea", Testament.OLD, 14),
    JOEL("JOEL", "Joel", Testament.OLD, 3),
    AMOS("AMOS", "Amos", Testament.OLD, 9),
    OBADIAH("OBAD", "Obadiah", Testament.OLD, 1),
    JONAH("JONAH", "Jonah", Testament.OLD, 4),
    MICAH("MIC", "Micah", Testament.OLD, 7),
    NAHUM("NAH", "Nahum", Testament.OLD, 3),
    HABAKKUK("HAB", "Habakkuk", Testament.OLD, 3),
    ZEPHANIAH("ZEPH", "Zephaniah", Testament.OLD, 3),
    HAGGAI("HAG", "Haggai", Testament.OLD, 2),
    ZECHARIAH("ZECH", "Zechariah", Testament.OLD, 14),
    MALACHI("MAL", "Malachi", Testament.OLD, 4),
    MATTHEW("MATT", "Matthew", Testament.NEW, 28),
    MARK("MARK", "Mark", Testament.NEW, 16),
    LUKE("LUKE", "Luke", Testament.NEW, 24),
    JOHN("JOHN", "John", Testament.NEW, 21),
    ACTS("ACTS", "Acts", Testament.NEW, 28),
    ROMANS("ROM", "Romans", Testament.NEW, 16),
    ONECORINTHIANS("1COR", "1 Corinthians", Testament.NEW, 16),
    TWOCORINTHIANS("2COR", "2 Corinthians", Testament.NEW, 13),
    GALATIANS("GAL", "Galatians", Testament.NEW, 6),
    EPHESIANS("EPH", "Ephesians", Testament.NEW, 6),
    PHILIPPIANS("PHIL", "Philippians", Testament.NEW, 4),
    COLOSSIANS("COL", "Colossians", Testament.NEW, 4),
    ONETHESSALONIANS("1THESS", "1 Thessalonians", Testament.NEW, 5),
    TWOTHESSALONIANS("2THESS", "2 Thessalonians", Testament.NEW, 3),
    ONETIMOTHY("1TIM", "1 Timothy", Testament.NEW, 6),
    TWOTIMOTHY("2TIM", "2 Timothy", Testament.NEW, 4),
    TITUS("TITUS", "Titus", Testament.NEW, 3),
    PHILEMON("PHILEM", "Philemon", Testament.NEW, 1),
    HEBREWS("HEB", "Hebrews", Testament.NEW, 13),
    JAMES("JAMES", "James", Testament.NEW, 5),
    ONEPETER("1PET", "1 Peter", Testament.NEW, 5),
    TWOPETER("2PET", "2 Peter", Testament.NEW, 3),
    ONEJOHN("1JOHN", "1 John", Testament.NEW, 5),
    TWOJOHN("2JOHN", "2 John", Testament.NEW, 1),
    THREEJOHN("3JOHN", "3 John", Testament.NEW, 1),
    JUDE("JUDE", "Jude", Testament.NEW, 1),
    REVELATION("REV", "Revelation", Testament.NEW, 22);

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
