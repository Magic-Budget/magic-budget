import React from "react";
import GoalsList from "./goalsList";
export default function Goals() {
	return (
		<div className="m-3">
            <h1 className="text-4xl p-4"> Your goals</h1>
			<div id="goals-container" className="flex">
				<div className="w-full pl-8 pr-8">
					<GoalsList/>
				</div>

			</div>

		</div>
	);
}