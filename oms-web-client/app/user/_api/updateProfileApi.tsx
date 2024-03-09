import {
  Avatar,
  Card,
  Container,
  Flex,
  GridItem,
  HStack,
  Icon,
  IconButton,
  PopoverArrow,
  PopoverCloseButton,
  PopoverContent,
  PopoverTrigger,
  SimpleGrid,
  Text,
  useDisclosure,
  Popover,
  FormControl,
  FormLabel,
  Input,
  Stack,
  ButtonGroup,
  Button,
  useToast,
  FormErrorMessage,
} from "@chakra-ui/react";

import axios from "axios";
import React, { useEffect, useState } from "react";
import { IUserInfo } from "../_interface/IUseInfo";
import getFromLocalStorage from "@/app/_lib/getFromLocalStorage";
import { EditIcon } from "@chakra-ui/icons";
import useGetUserInfoApi from "../_api/useGetUserInfoApi";

export default function UpdateProfileApi() {
  const { onOpen, onClose, isOpen } = useDisclosure();
  const toast = useToast();
  const [{ data, isLoading, isError }] = useGetUserInfoApi();
  const [fullName, setFullname] = useState<string>("");
  const [nameError, setNameError] = useState("");

  useEffect(() => {
    setFullname(data?.fullName || "");
  }, [data?.fullName]);

  const validateFullName = () => {
    if (!fullName) {
      setNameError("Please input your name");
    } else if (!/^[^~`!@#$%^&*()_+={}[\]|;:'",.<>?]+$/u.test(fullName)) {
      setNameError("Invalid name");
    } else {
      setNameError("");
    }
  };

  const handleChangeName = async () => {
    validateFullName();
    if (nameError !== "") {
      return;
    }

    try {
      const access_token = localStorage.getItem("access_token");
      const response = await axios.put(
        "https://game-be-v2.vercel.app/users/updateProfile",
        { fullName: fullName },
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${access_token}`,
          },
        }
      );
      onClose();
      toast({
        title: `Change name successfully`,
        status: "success",
        position: "top-right",
        isClosable: true,
      });
    } catch (error) {
      console.log(error);
    }
  };
  const handleUnchangeName = () => {
    onClose();
    setFullname(data?.fullName || "");
  };

  return (
    <Flex my={5} transition={"0.3s"}>
      <Text
        fontSize={{ md: 20, lg: 30 }}
        ml={{ md: 0, lg: 10 }}
        textColor={"white"}
      >
        {fullName}{" "}
      </Text>

      <Popover isOpen={isOpen} onOpen={onOpen} onClose={onClose}>
        <PopoverTrigger>
          <EditIcon mt={{ base: 1, lg: 4 }} ml={4} color={"gray.400"} />
        </PopoverTrigger>
        <PopoverContent p={5}>
          <PopoverArrow />
          <PopoverCloseButton />
          <Stack spacing={4}>
            <Text fontWeight={600}>Change your name</Text>
            <FormControl isInvalid={!!nameError}>
              <Input
                placeholder={data?.fullName}
                defaultValue={fullName}
                onChange={(e) => setFullname(e.target.value)}
              />
              <FormErrorMessage>{nameError}</FormErrorMessage>
            </FormControl>
            <ButtonGroup display="flex" justifyContent="flex-end">
              <Button variant="outline" onClick={() => handleUnchangeName()}>
                Cancel
              </Button>
              <Button colorScheme="teal" onClick={() => handleChangeName()}>
                Save
              </Button>
            </ButtonGroup>
          </Stack>
        </PopoverContent>
      </Popover>
    </Flex>
  );
}
