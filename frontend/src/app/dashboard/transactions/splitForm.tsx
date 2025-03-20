"use client";

import {
	MultiSelect,
	Option as MultiSelectOption,
} from "@/components/ui/multiselect";
import axios from "axios";
import React, { useState, useEffect } from "react";
import { useUserStore } from "@/stores/user-store";
import {
	Form,
	FormControl,
	FormField,
	FormItem,
	FormLabel,
} from "@/components/ui/form";
import { Button } from "@/components/ui/button";
import { z } from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { UUID } from "crypto";
import { useToast } from "@/hooks/use-toast";

const formSchema = z.object({
	friends: z.array(z.string()).min(1, "Select at least one friend"),
});

type FormValues = z.infer<typeof formSchema>;

const SplitForm = (props: { expense_id: UUID }) => {
	const userId = useUserStore((state) => state.id);
	const apiURL = `${process.env.NEXT_PUBLIC_API_URL}/api/${userId}`;
	const [friendOptions, setFriendOptions] = useState<MultiSelectOption[]>([]);

	// Initialize form
	const form = useForm<FormValues>({
		resolver: zodResolver(formSchema),
		defaultValues: {
			friends: [],
		},
	});

	useEffect(() => {
		const getFriends = async () => {
			try {
				const response = await axios.get(`${apiURL}` + "/friend");
				const friends = response.data.map(
					(friend: { id: string; name: string }) => ({
						label: friend.name,
						value: friend.id,
					})
				);
				setFriendOptions(friends);
			} catch (error) {
				setFriendOptions([]);
			}
		};
		getFriends();
	}, [apiURL]);

	const onSubmit = (data: FormValues) => {
		const { toast } = useToast();
		axios
			.post(apiURL + `/split`, {
				split_with: data.friends,
			})
			.then(() => {
				toast({
					description: "Expense split successfully",
				});
			})
			.catch(() => {
				toast({
					description: "Failed to split expense",
				});
			});
	};

	return (
		<Form {...form}>
			<form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
				<FormField
					control={form.control}
					name="friends"
					render={({ field }) => (
						<FormItem>
							<FormControl>
								<MultiSelect
									className="mt-2"
									options={friendOptions}
									placeholder="Select people to split with..."
									values={field.value}
									onChange={field.onChange}
								/>
							</FormControl>
						</FormItem>
					)}
				/>
				<Button type="submit" className="btn btn-primary">
					Submit
				</Button>
			</form>
		</Form>
	);
};

export default SplitForm;
