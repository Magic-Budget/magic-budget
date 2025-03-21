"use client";

import { useState } from "react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import { Label } from "@/components/ui/label"
import api from "@/lib/api"

export function ExpenseForm() {
  const [formData, setFormData] = useState({
    date: "",
    name: "",
    amount: "",
    description: "",
    business: ""
  });
  const [successMessage, setSuccessMessage] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prevState => ({
      ...prevState,
      [name]: value
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
      e.preventDefault();
      try {
        const response = await api.post('/api/transactions/expenses', formData); //Need to implement server-side
        console.log("Expense added: " + response );
        setSuccessMessage('Expense added successfully!');
        setErrorMessage('');
        // Reset fields after submission
        setFormData({
          date: "",
          name: "",
          amount: "",
          description: "",
          business: ""
        });
        setTimeout(() => {
          setSuccessMessage('');
        }, 3000); // Disappear after 3 seconds

      } catch(error){
        console.error("Error adding expense:", error);
        setErrorMessage('Error adding expense. Please try again.');
        setSuccessMessage('');
        setTimeout(() => {
          setErrorMessage('');
        }, 3000); // Disappear after 3 seconds
      }
  };

  return (
    <Card className="w-[500px]">
      <CardHeader>
        <CardTitle>Create Expense</CardTitle>
      </CardHeader>
      <CardContent>
        <form onSubmit={handleSubmit} className="flex flex-col gap-4 p-4 border rounded-lg shadow-md max-w-md mx-auto">
          <div className="flex flex-col space-y-1.5">
            <Label htmlFor="date">Date:</Label>
            <Input 
              id="date"
              name="date"
              type="date"
              placeholder="YYYY-MM-DD"
              value={formData.date}
              onChange={handleChange}
              required
            />
          </div>
          <div className="flex flex-col space-y-1.5">
            <Label htmlFor="name">Name:</Label>
            <Input
              id="name"
              name="name"
              type="text"
              maxLength={50}
              placeholder="Name of the transaction"
              value={formData.name}
              onChange={handleChange}
              required
            />
          </div>
          <div className="flex flex-col space-y-1.5">
            <Label htmlFor="amount">Amount:</Label>
            <Input
              id="amount"
              name="amount"
              type="number"
              placeholder="How much did it cost?"
              value={formData.amount}
              onChange={handleChange}
              required
            />
          </div>
          <div className="flex flex-col space-y-1.5">
            <Label htmlFor="description">Description:</Label>
            <Input
              id="description"
              name="description"
              type="text"
              placeholder="Add description of expense"
              value={formData.description}
              onChange={handleChange}
            />
          </div>
          <div className="flex flex-col space-y-1.5">
            <Label htmlFor="business">Business:</Label>
            <Input
              id="business"
              name="business"
              type="text"
              placeholder="What business provided this purchase?"
              value={formData.business}
              onChange={handleChange}
            />
          </div>
          <Button type="submit">Add Expense</Button>
          {successMessage && <div className="text-green-600 mt-2">{successMessage}</div>}
          {errorMessage && <div className="text-red-600 mt-2">{errorMessage}</div>}
        </form>
      </CardContent>
    </Card>
  );
}