import axios from "axios";
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
import { PlusCircle } from "lucide-react";
import { useUserStore } from "@/stores/user-store";

export default function IncomeForm() {
  const { id: userId, bearerToken } = useUserStore();
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [amount, setAmount] = useState(0);
  const [type, setType] = useState("");
  const [open, setOpen] = useState(false);
  const [date, setDate] = useState("");

  const handleAddIncome = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      axios.post(
        `${process.env.NEXT_PUBLIC_API_URL}/api/${userId}/income/add-income`,
        {
          name: name,
          description: description,
          amount: amount,
          type: type,
          date: date,
        },
        {
          headers: {
            Authorization: `Bearer ${bearerToken}`,
          },
        }
      );

      //Clear the inputs
      setOpen(false);
      setName("");
      setDescription("");
      setAmount(0);
      setType("");
    } catch (error) {
      console.log("Bearer Token:", bearerToken);
      console.error("Error adding income:", error);
    }
  };

  return (
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
          <DialogDescription>Add details for your new income</DialogDescription>
        </DialogHeader>
        <form onSubmit={handleAddIncome}>
          <div className="grid gap-4 py-4">
            <div className="grid gap-2">
              <Label htmlFor="name">Income Name</Label>
              <Input
                id="name"
                name="name"
                type="text"
                value={name}
                onChange={(e) => setName(e.target.value)}
                required
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="amount">Amount ($)</Label>
              <Input
                id="amount"
                name="amount"
                type="number"
                min="0.01"
                step="0.01"
                value={amount || ""}
                onChange={(e) => {
                  const value = e.target.value;
                  setAmount(value === "" ? 0 : parseFloat(value));
                }}
                required
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="description">Description</Label>
              <Input
                id="description"
                name="description"
                type="text"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="type">Type</Label>
              <select
                id="type"
                name="type"
                value={type}
                onChange={(e) => setType(e.target.value)}
                required
                className="border p-2 rounded-md"
              >
                <option value="">Select Income Type</option>
                <option value="ONETIME">One-Time</option>
                <option value="WEEKLY">Weekly</option>
                <option value="BIWEEKLY">Biweekly</option>
                <option value="MONTHLY">Monthly</option>
                <option value="YEARLY">Yearly</option>
              </select>
            </div>
            <div className="grid gap-2">
              <Label htmlFor="date">Date</Label>
              <Input
                id="date"
                name="date"
                type="datetime-local"
                value={date}
                onChange={(e) => setDate(e.target.value)}
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
  );
}
