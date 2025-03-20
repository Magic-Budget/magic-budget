import { Button } from "@/components/ui/button";
import {
	Collapsible,
	CollapsibleContent,
	CollapsibleTrigger,
} from "@radix-ui/react-collapsible";
import React, { useState } from "react";
import SplitForm from "./splitForm";

const SplitExpense = () => {
	const [isOpen, setIsOpen] = useState(false);

	return (
		<Collapsible
			open={isOpen}
			onOpenChange={setIsOpen}
			className="w-full my-1"
		>
			<CollapsibleContent className="py-2">
				<SplitForm />
			</CollapsibleContent>
			<CollapsibleTrigger asChild>
				{isOpen ? (
					<Button variant={"outline"}>Cancel</Button>
				) : (
					<Button>Split Expense</Button>
				)}
			</CollapsibleTrigger>
		</Collapsible>
	);
};

export default SplitExpense;
