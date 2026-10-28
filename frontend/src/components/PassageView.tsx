import { useState, useEffect } from "react";
import type { ReactNode } from "react";
import { useParams, Link } from "react-router";
import type { PassageResponse } from "../types";
import { fetchTracker } from "./api";


import "./PassageView.css";

type PassageViewState =
    | { status: "loading" }
    | { status: "loaded"; passageResponse: PassageResponse }
    | { status: "failed"; reason: "notFound" | "unavailable" };

export function PassageView() {
    const [state, setState] = useState<PassageViewState>({ status: "loading" });
    const { bookCode, chapterNumber } = useParams();

    useEffect(() => {
        if (bookCode === undefined || chapterNumber === undefined) {
            return;
        }

        async function loadPassage() {
            try {
                const response = await fetch(`/api/passages/${bookCode}/${chapterNumber}`);

                if (response.status === 404) {
                    setState({ status: "failed", reason: "notFound" });
                    return;
                }

                if (!response.ok) {
                    setState({ status: "failed", reason: "unavailable" });
                    return;
                }

                const data: PassageResponse = await response.json();

                setState({ status: "loaded", passageResponse: data });
            } catch {
                setState({ status: "failed", reason: "unavailable" });
            }
        }

        loadPassage();
    }, [bookCode, chapterNumber]);

    function renderContent(): ReactNode {
        if (bookCode === undefined || chapterNumber === undefined) {
            return <p>The chapter does not exist.</p>;
        }

        if (state.status === "loading") {
            return <p>Loading…</p>;
        }

        if (state.status === "failed") {
            return <p>{state.reason === "notFound" ? "The chapter does not exist." : "The passage text isn't available right now. Your progress is not affected."}</p>;
        }

        return <article className="passage-text">{state.passageResponse.text}</article>;
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
                <h1 className="passage-reference">{ }</h1>
                {renderContent()}
            </main>
        </div>
    );
}