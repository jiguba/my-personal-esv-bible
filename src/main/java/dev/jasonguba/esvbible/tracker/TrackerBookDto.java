package dev.jasonguba.esvbible.tracker;

import java.util.List;

import dev.jasonguba.esvbible.models.Testament;

public class TrackerBookDto {

    private final String code;
    private final String name;
    private final Testament testament;
    private final int chapterCount;
    private final List<Integer> completedChapters;

    public TrackerBookDto(
        String code,
        String name,
        Testament testament,
        int chapterCount,
        List<Integer> completedChapters
    ) {
        this.code = code;
        this.name = name;
        this.testament = testament;
        this.chapterCount = chapterCount;
        this.completedChapters = completedChapters;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Testament getTestament() {
        return testament;
    }

    public int getChapterCount() {
        return chapterCount;
    }

    public List<Integer> getCompletedChapters() {
        return completedChapters;
    }
}
