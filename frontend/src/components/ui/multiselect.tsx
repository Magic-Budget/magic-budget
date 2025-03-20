import React, { useState } from "react";
import {
	Popover,
	PopoverContent,
	PopoverTrigger,
} from "@/components/ui/popover";
import { Button } from "@/components/ui/button";
import { Checkbox } from "@/components/ui/checkbox";
import { cn } from "@/lib/utils";

interface Option {
	label: string;
	value: string;
}

interface MultiSelectProps {
	options: Option[];
	placeholder?: string;
	className?: string;
	values?: string[];
	onChange?: (value: string[]) => void;
}

const MultiSelect: React.FC<MultiSelectProps & { className?: string }> = ({
	options,
    placeholder,
    className,
	values = [],
    onChange
}) => {

	const handleToggle = (optionValue: string) => {
		if (!onChange) return;
        
        if (values.includes(optionValue)) {
            onChange(values.filter((v) => v !== optionValue));
        } else {
            onChange([...values, optionValue]);
        }
	};

	return (
		<Popover>
			<PopoverTrigger asChild>
				<Button
					variant="outline"
					className={cn("w-full justify-between", className)}
				>
					{values.length > 0
						? options
								.filter((option) =>
									values.includes(option.value)
								)
								.map((option) => option.label)
								.join(", ")
						: placeholder || "Select options"}
				</Button>
			</PopoverTrigger>
			<PopoverContent className="w-parent p-2" align="start">
				<div className="space-y-2">
					{
					options.length > 0?
					options.map((option) => (
						<div
							key={option.value}
							className="flex items-center space-x-2"
						>
							<Checkbox
								checked={values.includes(option.value)}
								onCheckedChange={() =>{
									handleToggle(option.value);
								}
								}
								id={option.value}
							/>
							<label
								htmlFor={option.value}
								className="text-sm font-medium leading-none"
							>
								{option.label}
							</label>
						</div>
					))
					:<p>No options available</p>
				}
				</div>
			</PopoverContent>
		</Popover>
	);
};

export { MultiSelect };
export type { Option };