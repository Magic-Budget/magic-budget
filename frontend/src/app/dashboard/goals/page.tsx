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
      <div>
        <h2 className="text-2xl font-bold tracking-tight">Goals</h2>
        <p className="text-muted-foreground">
          Set your goals and start saving for them
        </p>
      </div>
      <div id="goals-container" className="py-4">
        <GoalsList goals={mockGoals} />
      </div>
    </div>
  );
}
