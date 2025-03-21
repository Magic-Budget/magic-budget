"use client";

import React, { useEffect, useState } from "react";
import { Card, CardHeader, CardContent } from "../../../components/ui/card";
import { Button } from "../../../components/ui/button";
import { User } from "./(objects)/User";
import { useUserStore } from "@/stores/user-store";
import axios from "axios";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import GroupForm from "./(components)/groupForm";

interface Group {
  groupName: string;
  userInformation: User[];
}

const GroupsPage = () => {
  const [groups, setGroups] = useState<Group[]>([]);
  const [groupDialogOpen, setGroupDialogOpen] = useState(false);
  const { id: userId, bearerToken } = useUserStore();
  const apiUrl = `${process.env.NEXT_PUBLIC_API_URL}/api/${userId}`;
  const requestHeaders = { Authorization: `Bearer ${bearerToken}` };

  useEffect(() => {
    axios
      .get(`${apiUrl}/group/`, { headers: requestHeaders })
      .then((response) => {
        setGroups(response.data);
      })
      .catch((error) => {
        console.error(error);
      });
  }, []);

  return (
    <div className="p-8">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">Groups</h1>
        <Dialog open={groupDialogOpen} onOpenChange={setGroupDialogOpen}>
          <DialogTrigger asChild>
            <Button>Add Group</Button>
          </DialogTrigger>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>Add a group</DialogTitle>
            </DialogHeader>
            <GroupForm onSuccess={() => setGroupDialogOpen(false)} />
          </DialogContent>
        </Dialog>
      </div>

      {/* Grid of group cards */}
      <div className="grid gap-6 grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 w-[50vw]">
        {groups.map((group, index) => (
          <Card key={index} className="p-4 shadow">
            <CardHeader>
              <h2 className="text-xl font-semibold">{group.groupName}</h2>
            </CardHeader>
            <CardContent>
              <ul className="list-disc pl-5">
                {group.userInformation.map((user: User) => (
                  <li key={user.userId}>{user.fullName}</li>
                ))}
              </ul>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  );
};

export default GroupsPage;
