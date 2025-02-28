"use client";

import { useState } from "react";
import { Progress } from "@/components/ui/progress";
import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
  CardTitle,
  CardDescription,
} from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import Goal from "./objects/goal";

interface GoalsListProps {
  goals: Goal[];
  onUpdateGoal: (updatedGoal: Goal) => void;
}

export default function GoalsList({ goals, onUpdateGoal }: GoalsListProps) {
  const [editingGoal, setEditingGoal] = useState<Goal | null>(null);

  const handleEditGoal = (goalId: string) => {
    const goalToEdit = goals.find(goal => goal.id === goalId);
    if (goalToEdit) {
      setEditingGoal(goalToEdit);
    }
  };

  const handleSaveEdit = () => {
    if (editingGoal) {
      onUpdateGoal(editingGoal);
      setEditingGoal(null);
    }
  };

  return (
    <>
      <div className="space-y-6">
        {goals.map((goal: Goal) => (
          <Card key={goal.id} className="w-full p-6 bg-gray-50 border-2 border-gray-200">
            <CardHeader className="space-y-2">
              <CardTitle className="text-2xl">Goal: {goal.name}</CardTitle>
              <CardDescription className="text-lg">Save By: {goal.getDateString()}</CardDescription>
            </CardHeader>

            <CardContent className="py-6">
              <Progress
                value={mapToProgress(goal.currAmount, goal.targetPrice)}
                className="h-4"
              />
            </CardContent>

            <CardFooter className="flex justify-between items-center">
              <p className="text-lg font-medium">${goal.currAmount}</p>
              <p className="text-lg font-medium">${goal.targetPrice}</p>
              <Button variant="outline" size="sm" onClick={() => handleEditGoal(goal.id)}>
                Edit Goal
              </Button>
            </CardFooter>
          </Card>
        ))}
      </div>
      {editingGoal && (
        <Dialog open={!!editingGoal} onOpenChange={() => setEditingGoal(null)}>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>Edit Goal: {editingGoal.name}</DialogTitle>
            </DialogHeader>
            <div className="grid gap-4 py-4">
              <div className="grid grid-cols-4 items-center gap-4">
                <label htmlFor="currentAmount" className="text-right">Current Amount</label>
                <Input
                  id="currentAmount"
                  value={editingGoal.currAmount.toString()}
                  onChange={(e) => {
                    if (editingGoal) {
                      const updatedGoal = new Goal(
                        editingGoal.id,
                        editingGoal.due.toISOString(),
                        editingGoal.name,
                        parseFloat(e.target.value) || 0,
                        editingGoal.targetPrice
                      );
                      setEditingGoal(updatedGoal);
                    }
                  }}
                  className="col-span-3"
                />
              </div>
              <div className="grid grid-cols-4 items-center gap-4">
                <label htmlFor="targetPrice" className="text-right">Target Price</label>
                <Input
                  id="targetPrice"
                  value={editingGoal.targetPrice.toString()}
                  onChange={(e) => {
                    if (editingGoal) {
                      const updatedGoal = new Goal(
                        editingGoal.id,
                        editingGoal.due.toISOString(),
                        editingGoal.name,
                        editingGoal.currAmount,
                        parseFloat(e.target.value) || 0
                      );
                      setEditingGoal(updatedGoal);
                    }
                  }}
                  className="col-span-3"
                />
              </div>
            </div>
            <DialogFooter>
              <Button onClick={handleSaveEdit}>Save Changes</Button>
            </DialogFooter>
          </DialogContent>
        </Dialog>
      )}
    </>
  );
}

function mapToProgress(amount: number, target: number) {
  let newVal: number;
  newVal = amount * (100 / target);
  newVal = Math.min(100, newVal);
  return newVal;
}