import { Progress } from "@/components/ui/progress";
import Goal from "./objects/goal";


const today = new Date().toISOString().split('T')[0]
const mockGoals = [
    new Goal("1", today, "House",140000, 300000),
    new Goal("2", today, "PC upgrade", 100, 2000),
    new Goal("3", today, "Vacation", 4300, 5000),
];

export default async function GoalsList() {
    return (
    <div>
        {
            mockGoals.map((goal: Goal) =>(
            <div className="pt-6" key = {goal.id}>

                <div className="bg-gray-100 rounded-xl p-6 shadow-md w-full h-48">
                    <div >
                        <div className="" >
                            <h1 className="text-3xl"> Goal: {goal.name}</h1>
                        </div>
                        <h1 className="text-xl pl-4 pt-2"> Save By: {goal.getDateString()}</h1>
                    
                    </div>

                    <div className="pt-3">
                        <Progress value={mapToProgress(goal.currAmount, goal.targetPrice)} className="bg-gray-300" />
                        <div className="flex justify-between pt-2">
                            <h1 className="text-xl pl-2"> ${goal.currAmount}</h1>
                            <h1 className="text-xl pr-2"> ${goal.targetPrice} </h1>
                        </div>
                    </div>
                </div>
            </div>
            ))
        }
    </div>
    
);
};

function mapToProgress(amount :number , target: number) {
    let newVal : number;
    newVal = amount *( 100/target);
    newVal = Math.min(100, newVal);
    return newVal
    
}