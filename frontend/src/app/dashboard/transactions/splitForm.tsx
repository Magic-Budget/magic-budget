"use client";

import { MultiSelect, Option as MultiSelectOption } from '@/components/ui/multiselect';
import axios from 'axios';
import React, {useState, useEffect,} from 'react';
import { useUserStore } from '@/stores/user-store';
import {
	Form,
	FormControl,
	FormField,
	FormItem,
	FormLabel,
} from "@/components/ui/form";
import {Button} from "@/components/ui/button";
import {z} from "zod";
import { useForm } from 'react-hook-form';
import { zodResolver } from "@hookform/resolvers/zod";

const formSchema = z.object({
	friends: z.array(z.string()).min(1, "Select at least one friend"),
});

type FormValues = z.infer<typeof formSchema>;

const SplitForm: React.FC = () => {
    const userId = useUserStore((state) => state.id);
    const apiURL = `${process.env.NEXT_PUBLIC_API_URL}/api/${userId}`
    const [selectedFriends, setSelectedFriends] = React.useState<string[]>([]);
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
                const response = await axios.get(`${apiURL}/friends`);
                const friends = response.data.map((friend: any) => ({
                    label: friend.name,
                    value: friend.id,
                }));
                setFriendOptions(friends);
            } catch (error) {
                setFriendOptions([]);
            }
        };
        getFriends();
    }, [apiURL]);

    const onSubmit = (data:FormValues)=>{
        console.log(data);
    }

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
                <Button type="submit" className="btn btn-primary">Submit</Button>
                </form>
		</Form>
	);
};

export default SplitForm;