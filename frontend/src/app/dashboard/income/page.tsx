"use client"

import IncomeTable from "./incomeTable";
import IncomeGraph from "./incomeGraph";
import { Button } from "@/components/ui/button";
import React, { useState } from "react";
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
import Income from "./(objects)/income";
import { PlusCircle } from "lucide-react";

export default function Incomes() {
  const today = new Date().toISOString().split("T")[0];

  const [incomes, setIncomes] = useState<Income[]>([]);
  const [open, setOpen] = useState(false);
  const [newIncome, setNewIncome] = useState({
    amount: 0,
    name: "",
    description:"",
    date: today,
    type:""
  });

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setNewIncome((prev) => ({
      ...prev,
      [name]:
        name === "amount"
          ? parseFloat(value)
          : value,
    }));
  };


  const handleAddIncome = (e: React.FormEvent) => {
    e.preventDefault();

    // Generate a new ID (simple implementation - in production, use UUID or similar)
    const newId = (
      Math.max(...incomes.map((income) => parseInt(income.id)), 0) + 1
    ).toString();

    // Create new income and add it to the table
    const createdIncome = new Income(
      newId,
      newIncome.amount,
      newIncome.name,
      newIncome.description,
      newIncome.date,
      newIncome.type
    );

    setIncomes((prevGoals) => [...prevGoals, createdIncome]);

    // Reset form and close dialog
    setNewIncome({
      amount: 0,
      name: "",
      description:"",
      date: today,
      type:""
    });
    setOpen(false);
  };

  return (
    <div className="m-3">
      <div>
        <h2 className="text-2xl font-bold tracking-tight">Income</h2>
        <p className="text-muted-foreground">
          View your credit and track your income
        </p>
        <Dialog open={open} onOpenChange={setOpen}>
          <DialogTrigger asChild>
            <Button className="flex items-center gap-2">
              <PlusCircle size={16} />
              <span>Add Income</span>
            </Button>
          </DialogTrigger>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>Add A New Income</DialogTitle>
              <DialogDescription>
                Add details for your new income
              </DialogDescription>
            </DialogHeader>
            <form onSubmit={handleAddIncome}>
              <div className="grid gap-4 py-4">
                <div className="grid gap-2">
                  <Label htmlFor="name">Income Name</Label>
                  <Input
                    id="name"
                    name="name"
                    type="text"
                    value={newIncome.name}
                    onChange={handleInputChange}
                    required
                  />
                </div>
                <div className="grid gap-2">
                  <Label htmlFor="amount"> Amount ($)</Label>
                  <Input
                    id="amount"
                    name="amount"
                    type="number"
                    min="0.01"
                    step="0.01"
                    value={newIncome.amount}
                    onChange={handleInputChange}
                    required
                  />
                </div>
                <div className="grid gap-2">
                  <Label htmlFor="description">Description ($)</Label>
                  <Input
                    id="description"
                    name="description"
                    type="text"
                    value={newIncome.description}
                    onChange={handleInputChange}
                  />
                </div>
                <div className="grid gap-2">
                  <Label htmlFor="date"> </Label>
                  <Input
                    id="date"
                    name="date"
                    type="date"
                    value={newIncome.date}
                    onChange={handleInputChange}
                    required
                  />
                </div>
                <div className="grid gap-2">
                  <Label htmlFor="type">Type</Label>
                  <Input
                    id="type"
                    name="type"
                    type="text"
                    value={newIncome.type}
                    onChange={handleInputChange}
                    required
                  />
                </div>
              </div>
              <DialogFooter>
                <Button type="submit">Submit</Button>
              </DialogFooter>
            </form>
          </DialogContent>
        </Dialog>
      
        <div>
          <IncomeGraph/>
        </div>
      </div>
      <div className="flex">
              <div className="m-3 w-full">
                <IncomeTable start={0} end={10} />
              </div>
      </div>
    </div>
  );
}
