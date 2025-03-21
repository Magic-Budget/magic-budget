import axios from "axios";
import Income from "./(objects)/income";
import IncomeTableClient from "./incomeTableClient";
import { useUserStore } from "@/stores/user-store";
import { UUID } from "crypto";
interface Props {
    start: number;
    end: number;
}

export default async function IncomeTable(props: Props) {
    let userId = useUserStore((state) => state.id);
    const incomes = await getIncomes(
        userId,
        props.start,
        props.end
    );

    return <IncomeTableClient incomes={incomes} />;
}

async function getIncomes(
    userID: UUID,
    start: number,
    end: number
): Promise<Income[]> {
    let incomes: Income[] = [];
    axios
		.get(
			`${process.env.NEXT_PUBLIC_API_URL}/api/${userID}/income/view-all`
		)
		.then((response) => {
			incomes = response.data.slice(start, end);
		})
		.catch((err) => {
			console.log(err);
		});
	return incomes;
}
