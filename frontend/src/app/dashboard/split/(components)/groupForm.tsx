import React, { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
	Form,
	FormField,
	FormItem,
	FormLabel,
	FormControl,
	FormMessage,
} from "@/components/ui/form";
import axios from "axios";
import { useUserStore } from "@/stores/user-store";
import {
	Dialog,
	DialogContent,
	DialogTrigger,
	DialogHeader,
	DialogTitle,
} from "@/components/ui/dialog";
import { MultiSelect } from "@/components/ui/multiselect";
import { User } from "../(objects)/User";
import { Label } from "recharts";

const groupFormSchema = z.object({
	groupName: z.string().min(1, "Group name is required"),
});

type GroupFormValues = z.infer<typeof groupFormSchema>;

export default function GroupForm() {
	const { id: userId, bearerToken } = useUserStore();
	const apiUrl = `${process.env.NEXT_PUBLIC_API_URL}/api/${userId}`;
	const requestHeaders = { Authorization: `Bearer ${bearerToken}` };
	const [friends, setFriends] = useState<User[]>([]);

	const form = useForm<GroupFormValues>({
		resolver: zodResolver(groupFormSchema),
		defaultValues: {
			groupName: "",
		},
	});

	const onSubmit = (data: GroupFormValues) => {
		axios.post(`${apiUrl}/group/create-group`, data.groupName, {
			headers: requestHeaders,
		});
	};

	useEffect(() => {
		axios
			.get(`${apiUrl}/friend`, { headers: requestHeaders })
			.then((response) => {
				setFriends(response.data);
			});
	}, []);

	return (
		<Form {...form}>
			<form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
				<FormField
					name="groupName"
					control={form.control}
					render={({ field }) => (
						<FormItem>
							<FormLabel>Group Name</FormLabel>
							<FormControl>
								<Input
									placeholder="Enter group name"
									{...field}
								/>
							</FormControl>
							<FormMessage />
						</FormItem>
					)}
				/>
				<Dialog>
					<DialogTrigger asChild>
						<Button type="submit">Submit</Button>
					</DialogTrigger>

					<DialogContent>
						<DialogHeader>
							<DialogTitle>Add friends</DialogTitle>
						</DialogHeader>
						<MultiSelect
							options={friends.map((friend) => ({
								value: friend.userId,
								label: friend.fullName,
							}))}
						></MultiSelect>
					</DialogContent>
				</Dialog>
			</form>
		</Form>
	);
}
