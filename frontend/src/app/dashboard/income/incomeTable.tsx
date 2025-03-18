import Income from "./(objects)/income";
import IncomeTableClient from "./incomeTableClient";

interface Props {
    start: number;
    end: number;
}

export default async function IncomeTable(props: Props) {
    let username = getUsername();
    const incomes = await getIncomes(
        username,
        props.start,
        props.end
    );

    return <IncomeTableClient incomes={incomes} />;
}

async function getIncomes(
    username: string,
    start: number,
    end: number
): Promise<Income[]> {
    let incomes: Income[] = [];
    for (let i = start; i < end; i++) {
        incomes.push({    
            id: `transaction${i}`,
            amount: 0,
            name: `Income ${i}`,
            date: new Date(Date.now()),
            type: "ONETIME",
            description:
                "According to all known laws of aviation, there is no way a bee should be able to fly. Its wings are too small to get its fat little body off the ground. The bee, of course, flies anyway, because bees don't care what humans think is impossible.",
        });
    }
    return incomes;
}

function getUsername() {
    return "username";
}
