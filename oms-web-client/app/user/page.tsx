"use client";

import React from "react";
import Avatar from "./_components/UserAvatar";
import { Container, Text } from "@chakra-ui/react";
import GameBuyList from "./_components/GameBuyList";
import getFromLocalStorage from "../_lib/getFromLocalStorage";
export default function page() {
  return (
    <Container p={0} maxW={"75%"} my={10}>
      <Avatar />
      <GameBuyList />
    </Container>
  );
}
