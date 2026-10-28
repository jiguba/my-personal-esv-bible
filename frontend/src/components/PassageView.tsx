import { useState, useEffect } from "react";
import { useParams, Link } from "react-router";
import type { PassageResponse } from "../types";

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

    if (bookCode === undefined || chapterNumber === undefined) {
        return (
            <>
                <p>The chapter does not exist.</p>
                <Link to="/">Back to tracker</Link>
            </>
        );
    }

    if (state.status === "loading") {
        return (
            <>
                <p>Loading…</p>
                <Link to="/">Back to tracker</Link>
            </>
        );
    }

    if (state.status === "failed") {
        return (
            <>
                <p>{state.reason === "notFound" ? "The chapter does not exist." : "The passage text isn't available right now. Your progress is not affected."}</p>
                <Link to="/">Back to tracker</Link>
            </>
        );
    }

    return (
        <>
            <h1>{state.passageResponse.reference}</h1>
            <div className="passage-section">{state.passageResponse.text}</div>
            <Link to="/">Back to tracker</Link>
        </>
    );
}