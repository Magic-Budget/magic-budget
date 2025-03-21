"use client";

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
import { MultiSelect } from "@/components/ui/multiselect";
import { User } from "../(objects)/User";

const groupFormSchema = z.object({
  groupName: z.string().min(1, "Group name is required"),
});

type GroupFormValues = z.infer<typeof groupFormSchema>;

interface GroupFormProps {
  onSuccess?: () => void;
}

export default function GroupForm({ onSuccess }: GroupFormProps) {
  const { id: userId, bearerToken } = useUserStore();
  const apiUrl = `${process.env.NEXT_PUBLIC_API_URL}/api/${userId}`;
  const requestHeaders = { Authorization: `Bearer ${bearerToken}` };
  const [friends, setFriends] = useState<User[]>([]);
  const [selectedFriends, setSelectedFriends] = useState<string[]>([]);

  const form = useForm<GroupFormValues>({
    resolver: zodResolver(groupFormSchema),
    defaultValues: {
      groupName: "",
    },
  });

  const onSubmit = async (data: GroupFormValues) => {
    try {
      await axios.post(
        `${apiUrl}/group/create-group`,
        {
          groupName: data.groupName,
          memberIds: selectedFriends,
        },
        {
          headers: requestHeaders,
        }
      );

      form.reset();
      if (onSuccess) {
        onSuccess();
      }
    } catch (error) {
      console.error("Error creating group:", error);
    }
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
                <Input placeholder="Enter group name" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <div className="space-y-2">
          <FormLabel>Add Friends</FormLabel>
          <MultiSelect
            options={friends.map((friend) => ({
              value: friend.userId,
              label: friend.fullName,
            }))}
            onChange={(selected) => setSelectedFriends(selected)}
          />
        </div>

        <Button type="submit" className="w-full">
          Create Group
        </Button>
      </form>
    </Form>
  );
}
