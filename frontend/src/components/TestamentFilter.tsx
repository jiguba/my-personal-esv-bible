import type { TestamentFilterType } from "../types";
import "./TestamentFilter.css"

type TestamentFilterProps = {
    value: TestamentFilterType;
    onClick: (value: TestamentFilterType) => void;
};

const buttonLabels: { value: TestamentFilterType; label: string }[] = [
    { value: "ALL", label: "All" },
    { value: "OLD", label: "Old Testament" },
    { value: "NEW", label: "New Testament" },
];

export function TestamentFilter({ value, onClick }: TestamentFilterProps) {
    return (
        <div className="testament-filter-section" role="group" aria-label="Filter by testament">
            {
                buttonLabels.map((buttonLabel) => {
                    return (
                        <button
                            key={buttonLabel.value}
                            type="button"
                            aria-pressed={value === buttonLabel.value}
                            onClick={() => onClick(buttonLabel.value)}
                        >{buttonLabel.label}</button>
                    )
                })
            }
        </div>
    )
}
