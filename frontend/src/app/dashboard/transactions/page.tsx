import React from "react";
import TransactionTable from "./transactionTable";
import { Button } from "@/components/ui/button";
import { VisuallyHidden } from "@radix-ui/react-visually-hidden";
import { ExpenseForm } from "./expense-form";
import {
	Dialog,
	DialogContent,
	DialogTitle,
	DialogTrigger,
} from "@/components/ui/dialog";

export default function Transactions() {
	return (
		<div className="m-3 w-[90%] lg:w-[calc(100vw-320px)] max-w-full">
			<div className="flex justify-between">
				<div>
					<h2 className="text-2xl font-bold tracking-tight">
						Transactions
					</h2>
					<p className="text-muted-foreground">
						View your transactions and track your spending
					</p>
				</div>
				<div>
					<Dialog>
						<DialogTrigger asChild>
							<Button>Add Expense</Button>
						</DialogTrigger>
						<DialogContent className="w-fit">
							<div>
								<VisuallyHidden>
									<DialogTitle>Expense Form</DialogTitle>
								</VisuallyHidden>
								<ExpenseForm></ExpenseForm>
							</div>
						</DialogContent>
					</Dialog>
				</div>
			</div>
			<div>
				<div className="">
					<TransactionTable start={0} end={10} />
				</div>
			</div>
		</div>
	);
}
