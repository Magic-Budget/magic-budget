"use client";

import { useEffect, useState } from "react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import { Label } from "@/components/ui/label";
import api from "@/lib/api";
import { useUserStore } from "@/stores/user-store";
import {
	Select,
	SelectTrigger,
	SelectValue,
	SelectContent,
	SelectItem,
} from "@/components/ui/select";
import { UUID } from "crypto";
import axios from "axios";

const defaultFormData = {
	expenseDate: new Date(Date.now()),
	expenseName: "",
	amount: "",
	expenseDescription: "",
	shopName: "",
	category: "",
};

interface ExpenseFormData {
	expenseDate: Date;
	expenseName: string;
	amount: string;
	expenseDescription: string;
	shopName: string;
	category: string;
}

export function ExpenseForm() {
	const [formData, setFormData] = useState(defaultFormData);
	const [successMessage, setSuccessMessage] = useState("");
	const [categories, setCategories] = useState([]);
	const [errorMessage, setErrorMessage] = useState("");
	const { id: userId, bearerToken } = useUserStore();

	const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		const { name, value } = e.target;
		setFormData((prevState) => ({
			...prevState,
			[name]: value,
		}));
	};

	const handleSubmit = async (e: React.FormEvent) => {
		e.preventDefault();
		const apiURL = `${process.env.NEXT_PUBLIC_API_URL}/api/${userId}`;
		try {
			const response = await api.post(
				`${apiURL}/expense/add-expense`,
				formData,
				{
					headers: { Authorization: `Bearer ${bearerToken}` },
				}
			);
			console.log("Expense added: " + response);
			setSuccessMessage("Expense added successfully!");
			setErrorMessage("");
			// Reset fields after submission
			setFormData(defaultFormData);
			setTimeout(() => {
				setSuccessMessage("");
			}, 3000); // Disappear after 3 seconds
		} catch (error) {
			console.error("Error adding expense:", error);
			setErrorMessage("Error adding expense. Please try again.");
			setSuccessMessage("");
			setTimeout(() => {
				setErrorMessage("");
			}, 3000); // Disappear after 3 seconds
		}
	};

	useEffect(() => {
		axios
			.get(
				`${process.env.NEXT_PUBLIC_API_URL}/api/${userId}/categories/`,
				{
					headers: { Authorization: `Bearer ${bearerToken}` },
				}
			)
			.then((response) => {
				setCategories(response.data);
			})
			.catch((error) => {
				console.error("Error fetching categories: ", error);
			});
	}, []);

	return (
		<Card className="w-[400px]">
			<CardHeader>
				<CardTitle>Create Expense</CardTitle>
			</CardHeader>
			<CardContent>
				<form
					onSubmit={handleSubmit}
					className="flex flex-col gap-4 p-4 border rounded-lg shadow-md max-w-md mx-auto"
				>
					<div className="flex flex-col space-y-1.5">
						<Label htmlFor="expenseDate">Date:</Label>
						<Input
							id="expenseDate"
							name="expenseDate"
							type="date"
							value={
								formData.expenseDate.toISOString().split("T")[0]
							}
							onChange={(e) => {
								const selectedDate = e.target.value;
								setFormData((prevState) => ({
									...prevState,
									expenseDate: new Date(selectedDate),
								}));
							}}
							required
						/>
					</div>
					<div className="flex flex-col space-y-1.5">
						<Label htmlFor="expenseName">Name:</Label>
						<Input
							id="expenseName"
							name="expenseName"
							type="text"
							maxLength={50}
							placeholder="Name of the transaction"
							value={formData.expenseName}
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
						<Label htmlFor="expenseDescription">Description:</Label>
						<Input
							id="expenseDescription"
							name="expenseDescription"
							type="text"
							placeholder="Add description of expense"
							value={formData.expenseDescription}
							onChange={handleChange}
						/>
					</div>
					<div className="flex flex-col space-y-1.5">
						<Label htmlFor="shopName">Business:</Label>
						<Input
							id="shopName"
							name="shopName"
							type="text"
							placeholder="What business provided this purchase?"
							value={formData.shopName}
							onChange={handleChange}
						/>
					</div>

					<div className="flex flex-col space-y-1.5">
						<Label htmlFor="category">Category:</Label>
						<Select
							onValueChange={(value) =>
								setFormData((prevState) => ({
									...prevState,
									category: value,
								}))
							}
						>
							<SelectTrigger className="w-[280px]">
								<SelectValue placeholder="Choose a category..." />
							</SelectTrigger>
							<SelectContent>
								{categories.map((category: string) => (
									<SelectItem key={category} value={category}>
										{category.charAt(0).toUpperCase() +
											category.slice(1).toLowerCase()}
									</SelectItem>
								))}
							</SelectContent>
						</Select>
					</div>

					<Button type="submit">Add Expense</Button>
					{successMessage && (
						<div className="text-green-600 mt-2">
							{successMessage}
						</div>
					)}
					{errorMessage && (
						<div className="text-red-600 mt-2">{errorMessage}</div>
					)}
				</form>
			</CardContent>
		</Card>
	);
}
