import { useParams, Link } from "react-router";

export function PassageView() {
    const { bookCode, chapterNumber } = useParams();
    return (
        <>
            <h1>{bookCode} {chapterNumber}</h1>
            <Link to="/">Back to tracker</Link>
        </>
    );
}