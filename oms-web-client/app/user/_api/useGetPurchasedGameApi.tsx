import axios from "axios";
import { useEffect, useState } from "react";
import { IGamePurchased } from "../_interface/IGamePurchased";
import getFromLocalStorage from "@/app/_lib/getFromLocalStorage";

const useGetPurchasedGameApi = () => {
  const url = "https://game-be-v2.vercel.app/games";
  const [data, setData] = useState<IGamePurchased[]>();
  const [isLoading, setIsLoading] = useState(false);
  const [isError, setIsError] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      setIsError(false);
      setIsLoading(true);

      try {
        axios
          .request({
            headers: {
              Authorization: `Bearer ${getFromLocalStorage("access_token")}`,
            },
            method: "GET",
            url: `https://game-be-v2.vercel.app/games/purchasedGames`,
          })
          .then((response) => {
            setData(response.data);
          });
      } catch (error) {
        setIsError(true);
      }
      setIsLoading(false);
    };

    fetchData();
  }, [url]);

  return [{ data, isLoading, isError }];
};

export default useGetPurchasedGameApi;
