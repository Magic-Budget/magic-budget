import { Button } from '@/components/ui/button';
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from '@radix-ui/react-collapsible';
import React from 'react';
import SplitForm from './splitForm';

const SplitExpense= () => {

    const [isOpen, setIsOpen] = React.useState(false);

    return (
		<Collapsible
			open={isOpen}
			onOpenChange={setIsOpen}
			className="w-full my-1"
		>
			<CollapsibleContent className="py-2">
				<SplitForm />
			</CollapsibleContent>
			{isOpen ? <Button>Done</Button>:null}
			<CollapsibleTrigger asChild>
				{isOpen ? (
					<Button variant={"outline"} className="mx-1">
						Cancel
					</Button>
				) : (
					<Button className="mr-1">Split Expense</Button>
				)}
			</CollapsibleTrigger>
		</Collapsible>
	);
};

export default SplitExpense;