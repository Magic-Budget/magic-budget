"use client";

import React, { useState } from "react";
import GoalsList from "./goalsList";
import Goal from "./objects/goal";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { PlusCircle } from "lucide-react";

const today = new Date().toISOString().split("T")[0];
const mockGoals = [
  new Goal("1", today, "House", 140000, 300000),
  new Goal("2", today, "PC upgrade", 100, 2000),
  new Goal("3", today, "Vacation", 4300, 5000),
];

export default function Goals() {
  const [goals, setGoals] = useState<Goal[]>(mockGoals);
  const [open, setOpen] = useState(false);
  const [newGoal, setNewGoal] = useState({
    name: "",
    targetPrice: 0,
    currAmount: 0,
    due: today,
  });

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

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setNewGoal((prev) => ({
      ...prev,
      [name]:
        name === "targetPrice" || name === "currAmount"
          ? parseFloat(value)
          : value,
    }));
  };

  const handleAddGoal = (e: React.FormEvent) => {
    e.preventDefault();

    // Generate a new ID (simple implementation - in production, use UUID or similar)
    const newId = (
      Math.max(...goals.map((goal) => parseInt(goal.id)), 0) + 1
    ).toString();

    // Create new goal and add it to the list
    const createdGoal = new Goal(
      newId,
      newGoal.due,
      newGoal.name,
      newGoal.currAmount,
      newGoal.targetPrice
    );

    setGoals((prevGoals) => [...prevGoals, createdGoal]);

    // Reset form and close dialog
    setNewGoal({
      name: "",
      targetPrice: 0,
      currAmount: 0,
      due: today,
    });
    setOpen(false);
  };

  return (
    <div className="m-3">
      <div className="flex justify-between items-center">
        <div>
          <h2 className="text-2xl font-bold tracking-tight">Goals</h2>
          <p className="text-muted-foreground">
            Set your goals and start saving for them
          </p>
        </div>
        <Dialog open={open} onOpenChange={setOpen}>
          <DialogTrigger asChild>
            <Button className="flex items-center gap-2">
              <PlusCircle size={16} />
              <span>Add Goal</span>
            </Button>
          </DialogTrigger>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>Create New Goal</DialogTitle>
              <DialogDescription>
                Add details for your new financial goal.
              </DialogDescription>
            </DialogHeader>
            <form onSubmit={handleAddGoal}>
              <div className="grid gap-4 py-4">
                <div className="grid gap-2">
                  <Label htmlFor="name">Goal Name</Label>
                  <Input
                    id="name"
                    name="name"
                    value={newGoal.name}
                    onChange={handleInputChange}
                    required
                  />
                </div>
                <div className="grid gap-2">
                  <Label htmlFor="targetPrice">Target Amount ($)</Label>
                  <Input
                    id="targetPrice"
                    name="targetPrice"
                    type="number"
                    min="0"
                    step="0.01"
                    value={newGoal.targetPrice}
                    onChange={handleInputChange}
                    required
                  />
                </div>
                <div className="grid gap-2">
                  <Label htmlFor="currAmount">Current Amount ($)</Label>
                  <Input
                    id="currAmount"
                    name="currAmount"
                    type="number"
                    min="0"
                    step="0.01"
                    value={newGoal.currAmount}
                    onChange={handleInputChange}
                    required
                  />
                </div>
                <div className="grid gap-2">
                  <Label htmlFor="due">Target Date</Label>
                  <Input
                    id="due"
                    name="due"
                    type="date"
                    value={newGoal.due}
                    onChange={handleInputChange}
                    required
                  />
                </div>
              </div>
              <DialogFooter>
                <Button type="submit">Create Goal</Button>
              </DialogFooter>
            </form>
          </DialogContent>
        </Dialog>
      </div>
      <div
        id="goals-container"
        className="py-4 grid grid-cols-1 md:grid-cols-2 gap-4"
      >
        <GoalsList goals={goals} onUpdateGoal={onUpdateGoal} />
      </div>
    </div>
  );
}
