import { Button } from "@/components/ui/button";
import {
  Collapsible,
  CollapsibleContent,
  CollapsibleTrigger,
} from "@radix-ui/react-collapsible";
import React, { useState } from "react";
import SplitForm from "./splitForm";
import { UUID } from "crypto";

const SplitExpense = (props: { expense_id: UUID }) => {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <Collapsible open={isOpen} onOpenChange={setIsOpen} className="w-full my-1">
      <CollapsibleContent className="py-2">
        <SplitForm expense_id={props.expense_id} />
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
