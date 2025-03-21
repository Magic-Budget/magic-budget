
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
  const [amount, setAmount] = useState("");
  const [type, setType] = useState("");
  const [open, setOpen] = useState(false);

  const handleAddIncome = async (e: React.FormEvent) => {
    e.preventDefault();
    
    try {
        http://localhost:8080/api/{userId}/income
      axios.post(`${process.env.NEXT_PUBLIC_API_URL}/api/${userId}/income`, {
        income_name: name,
        income_description: description,
        income_amount: amount,
        income_type: type,
      },
      {
      headers: {
        Authorization: `Bearer ${bearerToken}`, // Add Bearer token here
      },
    }
    );

      //Clear the inputs 
      setOpen(false);
      setName("");
      setDescription("");
      setAmount("");
      setType("");

    } catch (error) {
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
                value={amount}
                onChange={(e) => setAmount(e.target.value)}
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
              <Input
                id="type"
                name="type"
                type="text"
                value={type}
                onChange={(e) => setType(e.target.value)}
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