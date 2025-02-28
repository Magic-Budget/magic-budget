import React from "react";
import GoalsList from "./goalsList";
import Goal from "./objects/goal";

const today = new Date().toISOString().split("T")[0];
const mockGoals = [
  new Goal("1", today, "House", 140000, 300000),
  new Goal("2", today, "PC upgrade", 100, 2000),
  new Goal("3", today, "Vacation", 4300, 5000),
];

export default function Goals() {
  return (
    <div className="m-3">
      <h1 className="text-4xl p-4"> Your goals</h1>
      <div id="goals-container" className="flex">
        <div className="w-full pl-8 pr-8">
          <GoalsList goals={mockGoals} />
        </div>
      </div>
    </div>
  );
}
