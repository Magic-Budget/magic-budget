"use client";

import React, { useState } from "react";
import GoalsList from "./goalsList";
import Goal from "./objects/goal";

const today = new Date().toISOString().split("T")[0];
const mockGoals = [
  new Goal("1", today, "House", 140000, 300000),
  new Goal("2", today, "PC upgrade", 100, 2000),
  new Goal("3", today, "Vacation", 4300, 5000),
];

export default function Goals() {
  const [goals, setGoals] = useState<Goal[]>(mockGoals);

  const onUpdateGoal = (updatedGoal: Goal) => {
    setGoals((prevGoals) =>
      prevGoals.map((goal) =>
        goal.id === updatedGoal.id
          ? new Goal(
              updatedGoal.id,
              updatedGoal.due.toISOString().split("T")[0], // Convert Date to string
              updatedGoal.name,
              updatedGoal.currAmount,
              updatedGoal.targetPrice
            )
          : goal
      )
    );
  };

  return (
    <div className="m-3">
      <div>
        <h2 className="text-2xl font-bold tracking-tight">Goals</h2>
        <p className="text-muted-foreground">
          Set your goals and start saving for them
        </p>
      </div>
      <div id="goals-container" className="py-4">
          <GoalsList goals={goals} onUpdateGoal={onUpdateGoal} />
      </div>
    </div>
  );
}
