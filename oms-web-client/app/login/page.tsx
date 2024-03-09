"use client";

import {
  Box,
  Flex,
  Stack,
  Heading,
  Text,
  Container,
  Input,
  Button,
  SimpleGrid,
  Avatar,
  AvatarGroup,
  useBreakpointValue,
  IconProps,
  Icon,
} from "@chakra-ui/react";
import axios from "axios";

import { useRouter } from "next/navigation";
import { useState } from "react";
import { useToast } from '@chakra-ui/react'



export default function Login() {
  const router = useRouter();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const toast = useToast()

const handleLogin = async () => {
    await fetch(`${process.env.NEXT_PUBLIC_HOSTNAME}auth/login`, 
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        username: username,
        password: password,
      }), 
    })
    .then(data => data.json())
    .then(processedData => {
      if(processedData.accessToken === undefined) {
        throw new Error('Sai tên đăng nhập hoặc mật khẩu');
      }
      console.log(processedData);
      localStorage.setItem('accessToken', processedData.accessToken);
      localStorage.setItem('userId', processedData.userId);
      localStorage.setItem('refreshToken', processedData.refreshToken);
      window.location.href =  '/dashboard'
    })
    .catch ((error) => {
      setUsername('');
      setPassword('');
      toast({
        title: `Sai tên đăng nhập hoặc mật khẩu`,
        status: 'error',
        position: "top-right",
        isClosable: true,
      })
    })
    
    
    const createdAt = new Date().toISOString();
    localStorage.setItem('createdAt', createdAt);
    // document.cookie = `access_token=${access_token}; expires=Thu, 01 Jan 2022 00:00:00 UTC; path=/`;
    

  
};

  return (
    <Box position={"relative"} bg="white" h="100vh"> 
          <title>Đăng nhập</title>
      <Container
        as={SimpleGrid}
        maxW={"7xl"}
        columns={{ base: 1, md: 2 }}
        spacing={{ base: 10, lg: 32 }}
        py={{ base: 10, sm: 20, lg: 20 }}
      >
        <Stack spacing={{ base: 10, md: 20 }}>
          <Heading
            lineHeight={1.1}
            fontSize={{ base: "3xl", sm: "4xl", md: "5xl", lg: "6xl" }}
            color={"#171717"}
          >
            Hệ thống quản trị đơn hàng Thông minh{" "}
            <Text
              as={"span"}
              bgGradient="linear(to-r, red.400,pink.400)"
              bgClip="text"
            >
              &
            </Text>{" "}
            Hiện đại
          </Heading>
          <Stack direction={"row"} spacing={4} align={"center"}></Stack>
        </Stack>
        <Stack
          bg={"#171717"}
          rounded={"sm"}
          p={{ base: 4, sm: 6, md: 8 }}
          spacing={{ base: 8 }}
          maxW={{ lg: "lg" }}
        >
          <Stack spacing={4}>
            <Heading
              color={"white"}
              lineHeight={1.1}
              fontSize={{ base: "2xl", sm: "3xl", md: "4xl" }}
              
            >
              Đăng nhập ngay
              <Text
                as={"span"}
                bgGradient="linear(to-r, red.400,pink.400)"
                bgClip="text"
              >
                !
              </Text>
            </Heading>
            <Text color={"gray.500"} fontSize={{ base: "sm", sm: "md" }}></Text>
          </Stack>
          <Box as={"form"} mt={2}>
            <Stack spacing={4}>
              <Input
                placeholder="Tên đăng nhập"
                bg={"#0a0a0a"}
                border={0}
                color={"white"}
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                _placeholder={{
                  color: "gray.500",
                }}
              />
              <Input
                placeholder="Mật khẩu"
                bg={"#0a0a0a"}
                type="password"
                border={0}
                color={"white"}
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                _placeholder={{
                  color: "gray.500",
                }}
              />
              <Text
                color={"gray.200"}
                _hover={{ textDecoration: "underscore" }}
                cursor={"pointer"}
              >
                Quên mật khẩu?
              </Text>
            </Stack>
            <Button
              fontFamily={"heading"}
              mt={8}
              w={"full"}
              bgGradient="linear(to-r, red.400,pink.400)"
              color={"white"}
              _hover={{
                bgGradient: "linear(to-r, red.400,pink.400)",
                boxShadow: "xl",
              }}
              onClick={handleLogin}
            >
              Đăng nhập
            </Button>
            <Button
              fontFamily={"heading"}
              bg={"gray.200"}
              color={"gray.800"}
              mt={4}
              w={"full"}
              onClick={() => router.push("/register")}
            >
              Đăng ký
            </Button>
          </Box>
        </Stack>
      </Container>
    </Box>
  );
}
