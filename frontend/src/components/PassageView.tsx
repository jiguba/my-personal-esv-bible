import { useState, useEffect } from "react";
import type { ReactNode } from "react";
import { useParams, Link } from "react-router";
import type { PassageResponse, TrackerResponse } from "../types";
import { fetchTracker, setChapterCompletion } from "../api";
import { findNextChapter, findPreviousChapter } from "../chapterNavigation";

import "./PassageView.css";

type PassageViewState =
    | { status: "loading" }
    | { status: "loaded"; passageResponse: PassageResponse; chapterKey: string }
    | { status: "failed"; reason: "notFound" | "unavailable"; chapterKey: string };

export function PassageView() {
    const [state, setState] = useState<PassageViewState>({ status: "loading" });
    const [tracker, setTracker] = useState<TrackerResponse | null>(null);
    const [attempt, setAttempt] = useState(0);
    const { bookCode, chapterNumber } = useParams();

    useEffect(() => {
        fetchTracker()
            .then((data: TrackerResponse) => setTracker(data))
            .catch((err) => console.warn(err));
    }, []);

    useEffect(() => {
        if (bookCode === undefined || chapterNumber === undefined) {
            return;
        }

        let ignore = false;
        const chapterKey = `${bookCode}/${chapterNumber}/${attempt}`;

        async function loadPassage() {
            try {
                const response = await fetch(`/api/passages/${bookCode}/${chapterNumber}`);

                if (ignore) {
                    return;
                }

                if (response.status === 404) {
                    setState({ status: "failed", reason: "notFound", chapterKey: chapterKey });
                    return;
                }

                if (!response.ok) {
                    setState({ status: "failed", reason: "unavailable", chapterKey: chapterKey });
                    return;
                }

                const data: PassageResponse = await response.json();

                if (ignore) {
                    return;
                }

                setState({ status: "loaded", passageResponse: data, chapterKey: chapterKey });
            } catch {
                if (!ignore) {
                    setState({ status: "failed", reason: "unavailable", chapterKey: chapterKey });
                }
            }
        }

        loadPassage();

        return () => {
            ignore = true;
        }
    }, [bookCode, chapterNumber, attempt]);

    const book = tracker?.books.find((b) => b.code === bookCode);
    const chapter = Number(chapterNumber);
    const currentChapterKey = `${bookCode}/${chapterNumber}/${attempt}`;
    const showHeader = book !== undefined && Number.isInteger(chapter) && chapter >= 1 && chapter <= book.chapterCount;

    const previousChapter = findPreviousChapter(tracker?.books ?? [], book?.code ?? "", chapter);
    const nextChapter = findNextChapter(tracker?.books ?? [], book?.code ?? "", chapter);

    function handleRetry() {
        setAttempt((previous) => previous + 1);
    }

    function renderContent(): ReactNode {
        if (bookCode === undefined || chapterNumber === undefined) {
            return <p>The chapter does not exist.</p>;
        }

        if (state.status === "loading" || state.chapterKey !== currentChapterKey) {
            return <p>Loading…</p>;
        }

        if (state.status === "failed") {
            if (state.reason === "notFound") {
                return <p>
                    The chapter does not exist.
                </p>;
            }

            return (
                <div className="passage-unavailable">
                    <p>The passage text is not available right now. Your progress is not affected.</p>
                    <div className="passage-unavailable-actions">
                        <button type="button" className="passage-retry-button" onClick={handleRetry}>Try again</button>
                        <Link className="passage-secondary-link" to="/">Back to tracker</Link>
                    </div>
                </div >
            )
        }

        return <article className="passage-text">{state.passageResponse.text}</article>;
    }

    function handleMarkRead(completed: boolean) {
        if (book === undefined) {
            return;
        }

        setChapterCompletion(book.code, chapter, completed)
            .then(() => fetchTracker())
            .then((data) => setTracker(data))
            .catch((err) => alert(`Couldn't save ${book.name} ${chapter}: ${err.message}`));
    }

    return (
        <div className="passage-view">
            <header className="passage-header">
                <div className="passage-header-inner">
                    <Link className="passage-back-link" to="/">Back to tracker</Link>
                    <span className="passage-app-name">ESV Bible Tracker</span>
                </div>
            </header>
            <main className="passage-main">
                {showHeader && (
                    <>
                        <p className="passage-label">{book.testament === "OLD" ? "Old Testament" : "New Testament"} · {book.name} · {chapter} of {book.chapterCount}</p>
                        <h1 className="passage-reference">{book.name} {chapter}</h1>
                        <div className="passage-actions">
                            <label className="passage-mark-read">
                                <input
                                    type="checkbox"
                                    checked={book.completedChapters.includes(chapter)}
                                    onChange={(event) => handleMarkRead(event.currentTarget.checked)} />
                                Mark as read
                            </label>
                            <nav className="passage-chapter-nav" aria-label="Chapter navigation">
                                {previousChapter && (
                                    <Link className="passage-nav-link" to={`/read/${previousChapter.book.code}/${previousChapter.chapter}`}>
                                        ‹ {previousChapter.book.name} {previousChapter.chapter}
                                    </Link>
                                )}
                                {nextChapter && (
                                    <Link className="passage-nav-link" to={`/read/${nextChapter.book.code}/${nextChapter.chapter}`}>
                                        {nextChapter.book.name} {nextChapter.chapter} ›
                                    </Link>
                                )}
                            </nav>
                        </div>
                    </>
                )}
                {renderContent()}
            </main>
        </div>
    );
}