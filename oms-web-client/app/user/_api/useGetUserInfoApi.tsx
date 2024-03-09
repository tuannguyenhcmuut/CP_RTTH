import axios from "axios";
import { useEffect, useState } from "react";
import { IUserInfo } from "../_interface/IUseInfo";
import getFromLocalStorage from "@/app/_lib/getFromLocalStorage";
import { get } from "http";

const useGetUserInfoApi = () => {
  const url = "https://game-be-v2.vercel.app/games";
  const [data, setData] = useState<IUserInfo>();
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
            url: `https://game-be-v2.vercel.app/users/getUserInfo`,
          })
          .then((response) => {
            setData(response.data);
          });
      } catch (error) {
        console.log("errorrrrrr");
        setIsError(true);
      }
      setIsLoading(false);
    };

    fetchData();
  }, [url]);

  return [{ data, isLoading, isError }];
};

export default useGetUserInfoApi;
