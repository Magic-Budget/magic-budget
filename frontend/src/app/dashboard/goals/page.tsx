"use client";

import React, { useCallback, useEffect, useState } from "react";
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
import { useUserStore } from "@/stores/user-store";
import axios from "axios";

const today = new Date().toISOString().split("T")[0];

export default function Goals() {
  const { id: userId, bearerToken } = useUserStore();
  const [goals, setGoals] = useState<Goal[]>([]);
  const [open, setOpen] = useState(false);
  const [newGoal, setNewGoal] = useState({
    name: "",
    targetPrice: 0,
    currAmount: 0,
    due: today,
  });

  const fetchGoals = useCallback(async () => {
    if (!userId) return;

    const response = await axios.get(
      `${process.env.NEXT_PUBLIC_API_URL}/api/${userId}/goals`,
      {
        headers: {
          Authorization: `Bearer ${bearerToken}`,
        },
      }
    );

    const fetchedGoals = response.data.map(
      (goal: any) =>
        new Goal(
          goal.id,
          new Date(goal.due || new Date()).toISOString().split("T")[0],
          goal.name,
          goal.currentAmount,
          goal.target
        )
    );

    setGoals(fetchedGoals);
  }, [userId, bearerToken]);

  useEffect(() => {
    fetchGoals();
  }, [fetchGoals]);

  const onUpdateGoal = async (updatedGoal: Goal) => {
    const response = await axios.patch(
      `${process.env.NEXT_PUBLIC_API_URL}/api/${userId}/goals/${updatedGoal.id}`,
      {
        name: updatedGoal.name,
        target: updatedGoal.targetPrice,
        currentAmount: updatedGoal.currAmount,
        due: updatedGoal.due,
      },
      {
        headers: {
          Authorization: `Bearer ${bearerToken}`,
        },
      }
    );
    console.log(response.data);
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

  const handleAddGoal = async (e: React.FormEvent) => {
    e.preventDefault();

    // Generate a new ID (simple implementation - in production, use UUID or similar)
    const response = await axios.post(
      `${process.env.NEXT_PUBLIC_API_URL}/api/${userId}/goals`,
      {
        name: newGoal.name,
        target: newGoal.targetPrice,
        currentAmount: newGoal.currAmount,
        due: newGoal.due,
      },
      {
        headers: {
          Authorization: `Bearer ${bearerToken}`,
        },
      }
    );

    // Use the ID generated by the server
    const createdGoal = new Goal(
      response.data.id, // Server-generated ID
      newGoal.due,
      newGoal.name,
      newGoal.currAmount,
      newGoal.targetPrice
    );

    // Add to local state
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
