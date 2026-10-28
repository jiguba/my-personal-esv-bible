import type { TrackerResponse } from "./types";

export async function fetchTracker(): Promise<TrackerResponse> {

    const response = await fetch("/api/tracker");

    if (!response.ok) {
        throw new Error(`HTTP Error ${response.status}`);
    }

    return response.json();
}

export async function setChapterCompletion(bookCode: string, chapter: number, completed: boolean): Promise<void> {
    const url = `/api/completions/${bookCode}/${chapter}`;
    const method = completed ? "PUT" : "DELETE";

    const response = await fetch(url, { method });

    if (!response.ok) {
        throw new Error(`HTTP Error ${response.status}`);
    }
}