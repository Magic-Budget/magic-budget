"use client";

import { useState } from "react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";

export function ExpenseForm() {
    const [description, setDescription] = useState("");
    const [price, setPrice] = useState("");

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        console.log("Expense added:", { description, price });
        // Reset fields after submission
        setDescription("");
        setPrice("");
    };

    return (
        <form onSubmit={handleSubmit} className="flex flex-col gap-4 p-4 border rounded-lg shadow-md max-w-md mx-auto">
          <Input 
            type="text"
            placeholder="Expense description"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
          />
          <Input 
            type="number"
            placeholder="Expense amount"
            value={price}
            onChange={(e) => setPrice(e.target.value)}
          />
          <Button type="submit">Add Expense</Button>
        </form>
    );
}