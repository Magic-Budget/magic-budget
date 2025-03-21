"use client";

import React from "react";
import SplitGroupViewer from "./splitGroupViewer";

export default function SplitGroupViewerPage() {
    return (
        <div className="m-3">
            <h2 className="text-2xl font-bold tracking-tight mb-4">
                Split Expenses
            </h2>
            <SplitGroupViewer />
        </div>
    );
}
