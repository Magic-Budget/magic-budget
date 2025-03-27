"use client";

import { useState } from "react";
import { useUserStore } from "@/stores/user-store";
import { ScrollArea } from "@/components/ui/scroll-area";
import { Textarea } from "@/components/ui/textarea";
import { Button } from "@/components/ui/button";

const chatSocket = new WebSocket(`${process.env.OLLAMA_URL}`);
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

	chatSocket.addEventListener("message", (event) => {
		setChatMessages([
			...chatMessages,
			{ message: event.data, sender: Sender.llama },
		]);
		setDisableChat(false);
	});

	const handleButtonClick = () => {
		if (messageText.trim()) {
			setDisableChat(true);
			chatSocket.send(messageText);
			setChatMessages([
				...chatMessages,
				{ message: messageText, sender: Sender.user },
			]);
			setMessageText(""); // Clear the text box after sending
		}
	};

	return (
		<>
			<ScrollArea></ScrollArea>
			<div className="grid grid-cols-6 gap-2 m-1">
				<Textarea
					id="message_textbox"
					className="col-span-5"
					placeholder="Type your message here."
					value={messageText} // Bind the state to the text box
					onChange={(e) => setMessageText(e.target.value)} // Update state on change
					disabled={disableChat}
				/>
				<Button
					id="sender_button"
					className="col-span-1"
					onClick={handleButtonClick}
				>
					Send
				</Button>
			</div>
		</>
	);
}

export default SupportChat;
