import { useEffect, useState } from "react";

import axios from "axios";
import { Button } from "@/components/ui/button";
import { UUID } from "crypto";
import { useUserStore } from "@/stores/user-store";
import Income from "./(objects)/income";

export default function IncomeView(props: { incomeId: UUID }) {
  const [income, setIncome] = useState<Income>();
  const { id: userId, bearerToken } = useUserStore();
  useEffect(() => {
    axios
      //get a user's id
      .get(
        `${process.env.NEXT_PUBLIC_API_URL}/api/${userId}/income/${props.incomeId}`,
        {
          headers: {
            Authorization: `Bearer ${bearerToken}`,
          },
        },
      )
      .then((response) => {
        const incomeData = response.data;
        incomeData.date = new Date(incomeData.date);
        setIncome(incomeData);
      })
      .catch((error) => {
        console.error("There was an error fetching the income!", error);
      });
  }, [props.incomeId]);

  return (
    <div>
      <p>
        <b>Name:</b>
        {income?.name}
      </p>
      <p>
        <b>Amount:</b>${income?.amount}
      </p>
      <p>
        <b>Date:</b>
        {income?.date.toLocaleDateString()}
      </p>
      <p>
        <b>Description:</b>
        {income?.description}
      </p>
      <p>
        <b>Type:</b>
        {income?.type}
      </p>
    </div>
  );
}
