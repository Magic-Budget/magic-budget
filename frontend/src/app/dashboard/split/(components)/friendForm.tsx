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
import { Select } from "@/components/ui/select";

const groupFormSchema = z.object({
  username: z.string().min(1, "Username name is required"),
});

type GroupFormValues = z.infer<typeof groupFormSchema>;

interface FriendFormProps {
  onSuccess?: () => void;
}

export default function FriendForm({ onSuccess }: FriendFormProps) {
  const { id: userId, bearerToken } = useUserStore();
  const apiUrl = `${process.env.NEXT_PUBLIC_API_URL}/api/${userId}`;
  const requestHeaders = { Authorization: `Bearer ${bearerToken}` };
  const [friends, setFriends] = useState<User[]>([]);

  const form = useForm<GroupFormValues>({
    resolver: zodResolver(groupFormSchema),
    defaultValues: {
      username: "",
    },
  });

  const onSubmit = (data: GroupFormValues) => {
    axios.post(
      `${apiUrl}/friend/add-friend`,
      {
        username: data.username,
      },
      {
        headers: requestHeaders,
      },
    );

    form.reset();
    if (onSuccess) {
      onSuccess();
    }
  };

  useEffect(() => {
    axios
      .get(`${apiUrl}/friend/`, { headers: requestHeaders })
      .then((response) => {
        setFriends(response.data);
      });
  }, []);

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
        <FormField
          name="username"
          control={form.control}
          render={({ field }) => (
            <FormItem>
              <FormLabel>Friend Name</FormLabel>
              <FormControl>
                <Input placeholder="Type a username" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
      </form>
    </Form>
  );
}
