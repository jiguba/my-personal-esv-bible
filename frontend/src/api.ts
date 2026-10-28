import type { TrackerResponse } from "./types";

export async function fetchTracker(): Promise<TrackerResponse> {

    const response = await fetch("/api/tracker");

    if (!response.ok) {
        throw new Error(`HTTP Error ${response.status}`);
    }

    return response.json();
}