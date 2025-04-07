import React, { useEffect, useState } from "react";
import { Card, CardHeader, CardContent } from "../../../components/ui/card";
import { Button } from "../../../components/ui/button";
import { User as UserIcon } from "lucide-react";
import { useUserStore } from "@/stores/user-store";
import axios from "axios";
import { User } from "./(objects)/User";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import GroupForm from "./(components)/groupForm";
import FriendForm from "./(components)/friendForm";
import { set } from "react-hook-form";

const FriendViewer = () => {
  const [friends, setFriends] = useState<User[]>([]);
  const [friendDialogOpen, setFriendDialogOpen] = useState(false);
  const { id: userId, bearerToken } = useUserStore();
  const apiUrl = `${process.env.NEXT_PUBLIC_API_URL}/api/${userId}`;
  const requestHeaders = { Authorization: `Bearer ${bearerToken}` };

  const fetchFriends = () => {
    axios
      .get(`${apiUrl}/friend/`, { headers: requestHeaders })
      .then((response) => {
        const uniqueFriends = Array.from(
          new Map(response.data.map((friend: User) => [friend.userId, friend])),
        ).map((entry) => entry[1] as User);
        setFriends(uniqueFriends);
      })
      .catch((error) => {
        console.error(error);
      });
  };

  useEffect(() => {
    fetchFriends();

    const intervalId = setInterval(fetchFriends, 30000);

    return () => clearInterval(intervalId);
  }, []);

  return (
    <div className="p-8">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">Friends</h1>
        <Dialog open={friendDialogOpen} onOpenChange={setFriendDialogOpen}>
          <DialogTrigger asChild>
            <Button>Add Friend</Button>
          </DialogTrigger>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>Add a friend</DialogTitle>
            </DialogHeader>
            <FriendForm onSuccess={() => setFriendDialogOpen(false)} />
          </DialogContent>
        </Dialog>
      </div>

      <div className="grid gap-6 grid-cols-1 sm:grid-cols-2 lg:grid-cols-3">
        {friends.map((friend: User) => (
          <Card
            id={friend.userId}
            key={friend.userId}
            className="p-2 shadow items-align-center"
          >
            <CardContent className="flex items-center gap-2">
              <UserIcon size={60} />
            </CardContent>
            <CardHeader>
              <h2 className="text-xl font-semibold">{friend.fullName}</h2>
            </CardHeader>
          </Card>
        ))}
      </div>
    </div>
  );
};

export default FriendViewer;
