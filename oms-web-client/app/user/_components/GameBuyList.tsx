import { Container, VStack } from "@chakra-ui/react";
import React from "react";
import GameBuy from "./GameBuy";
import useGetPurchasedGameApi from "../_api/useGetPurchasedGameApi";

export default function GameBuyList() {
  const [{ data, isLoading, isError }] = useGetPurchasedGameApi();
  return (
    <VStack spacing={5} mt={20}>
      {data?.map((game) => (
        <GameBuy
          key={game.id}
          id={game.id}
          name={game.name}
          description={game.description}
          releaseDate={game.releaseDate}
          price={game.price}
          genres={game.genres}
        />
      ))}
    </VStack>
  );
}
