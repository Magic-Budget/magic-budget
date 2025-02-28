"use client"

import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import Link from "next/link";
import { useState } from "react";
import axios from "axios";

export function SignupForm({
  className,
  ...props
}: React.ComponentPropsWithoutRef<"div">) {

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [name, setName] = useState("");

  const createAccount = (event: React.FormEvent) => {
		event.preventDefault();
		console.log("Username:", username);
		console.log("Password:", password);

    axios.post("http://localhost:8080/api/auth/register", {
      "id":null,
      username,
      "lastName": "Jerry",
      "firstName": "Smith",
      password
    })
  };

  return (
		<div className={cn("flex flex-col gap-6", className)} {...props}>
			<Card>
				<CardHeader>
					<CardTitle className="text-2xl">Create Account</CardTitle>
					<CardDescription>
						Fill in the details below to create a new account
					</CardDescription>
				</CardHeader>
				<CardContent>
					<form onSubmit={createAccount}>
						<div className="flex flex-col gap-6">
							{/* Username Field */}
							<div className="grid gap-2">
								<Label htmlFor="username">Username</Label>
								<Input
									id="username"
									type="text"
									placeholder="Your username"
									value={name}
									onChange={(e) =>
										setName(e.target.value)
									}
									required
								/>
							</div>

							{/* Email Field */}
							<div className="grid gap-2">
								<Label htmlFor="email">Email</Label>
								<Input
									id="email"
									type="email"
									placeholder="mail@example.com"
									value={username}
									onChange={(e) => setUsername(e.target.value)}
									required
								/>
							</div>

							{/* Password Field */}
							<div className="grid gap-2">
								<Label htmlFor="password">Password</Label>
								<Input
									id="password"
									type="password"
									value={password}
									onChange={(e) =>
										setPassword(e.target.value)
									}
									required
								/>
							</div>

							<Button
								id="create-account-button"
								type="submit"
								className="w-full"
							>
								Create Account
							</Button>
						</div>
						<div className="mt-4 text-center text-sm">
							Already have an account?{" "}
							<Link
								href="/login"
								className="underline underline-offset-4"
							>
								Login
							</Link>
						</div>
					</form>
				</CardContent>
			</Card>
		</div>
  );
}
