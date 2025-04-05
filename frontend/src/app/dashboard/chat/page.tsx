"use client";

import { useState } from "react";
import { useUserStore } from "@/stores/user-store";
import { ScrollArea } from "@/components/ui/scroll-area";
import { Textarea } from "@/components/ui/textarea";
import { Button } from "@/components/ui/button";
import axios from "axios";

enum Sender {
	llama,
	user,
}

function SupportChat() {
	const [chatMessages, setChatMessages] = useState<
		{
			message: string;
			sender: Sender;
		}[]
	>([]);
	const [messageText, setMessageText] = useState(""); // State for the text box value
	const [disableChat, setDisableChat] = useState(false);

	const handleButtonClick = async () => {
		if (messageText.trim()) {
			setDisableChat(true);

			// Save the user message
			setChatMessages((prev) => [
				...prev,
				{ message: messageText, sender: Sender.user },
			]);

			try {
				// Make POST request to Spring Boot backend
				const response = await axios.post("/api/chat", {
					userID: useUserStore().id,
					prompt: messageText,
				});

				// Save the AI response
				setChatMessages((prev) => [
					...prev,
					{ message: response.data, sender: Sender.llama },
				]);
			} catch (error) {
				console.error("Error getting response from server:", error);
				setChatMessages((prev) => [
					...prev,
					{ message: "Oops! Something went wrong.", sender: Sender.llama },
				]);
			} finally {
				setDisableChat(false);
				setMessageText("");
			}
		}
	};

	return (
		<>
			<ScrollArea className="w-full h-[400px] p-2 border rounded">
				{chatMessages.map((entry, idx) => (
					<p
						key={idx}
						className={
							entry.sender === Sender.user
								? "font-bold text-lg text-right"
								: "text-md text-left"
						}
					>
						{entry.message}
					</p>
				))}
			</ScrollArea>
			<div className="w-full grid grid-cols-6 gap-2 m-1">
				<Textarea
					className="col-span-5"
					placeholder="Type your message here."
					value={messageText}
					onChange={(e) => setMessageText(e.target.value)}
					disabled={disableChat}
				/>
				<Button
					className="col-span-1"
					onClick={handleButtonClick}
					disabled={disableChat}
				>
					Send
				</Button>
			</div>
		</>
	);
}

export default SupportChat;
