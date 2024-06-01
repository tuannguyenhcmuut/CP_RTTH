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
  FormErrorMessage,
  FormControl,
  FormLabel,
} from "@chakra-ui/react";
import axios from "axios";

import { useRouter } from "next/navigation";
import { useState } from "react";
import { useToast } from "@chakra-ui/react";

export default function Login() {
  const router = useRouter();
  const toast = useToast();
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [retypePassword, setRetypePassword] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [errors, setErrors] = useState({
    username: "",
    email: "",
    password: "",
    retypePassword: "",
    firstName: "",
    lastName: "",
    phoneNumber: "",
  });

  const validateUsername = () => {
    
    if (username.length ==0) {
      setErrors((errors) => ({
        ...errors,
        username: "Vui lòng nhập tên người dùng",
      }));
    } else if (!/^[a-z0-9]+$/.test(username)) {
      setErrors((errors) => ({
        ...errors,
        username: "Tên người dùng chỉ chứa chữ thường và số",
      }));
    } else if (username.length < 8) {
      setErrors((errors) => ({
        ...errors,
        username: "Tên người dùng phải có ít nhất 8 ký tự",
      }));
    } else {
      setErrors((errors) => ({ ...errors, username: "" }));
    }
  };

  const existUsername =() =>{
    setErrors((errors) => ({
      ...errors,
      username: "Tên người dùng đã tồn tại",
    }));
  }

  const validateEmail = () => {
    if (!email) {
      setErrors((errors) => ({ ...errors, email: "Vui lòng nhập email" }));
    } else if (!/.+@.+\..+/.test(email)) {
      setErrors((errors) => ({ ...errors, email: "Email không hợp lệ" }));
    } else {
      setErrors((errors) => ({ ...errors, email: "" }));
    }
  };

  const validatePhoneNumber = () => {
    if (!phoneNumber) {
      setErrors((errors) => ({ ...errors, phoneNumber: "Vui lòng nhập số điện thoại" }));
    } else if (!/[0-9]{10}$/.test(phoneNumber)) {
      setErrors((errors) => ({ ...errors, phoneNumber: "Số điện thoại không hợp lệ" }));
    } else {
      setErrors((errors) => ({ ...errors, phoneNumber: "" }));
    }
  };

  const validatePassword = () => {
    if (!password) {
      setErrors((errors) => ({
        ...errors,
        password: "Vui lòng nhập mật khẩu",
      }));
    } else if (password.length < 8) {
      setErrors((errors) => ({
        ...errors,
        password: "Mật khẩu phải có ít nhất 8 ký tự",
      }));
    } else {
      setErrors((errors) => ({ ...errors, password: "" }));
    }
  };

  const validateRetypePassword = () => {
    if (!retypePassword) {
      setErrors((errors) => ({
        ...errors,
        retypePassword: "Vui lòng nhập lại mật khẩu",
      }));
    } else if (retypePassword !== password) {
      setErrors((errors) => ({
        ...errors,
        retypePassword: "Mật khẩu phải khớp",
      }));
    } else {
      setErrors((errors) => ({ ...errors, retypePassword: "" }));
    }
  };

  const validateFirstName = () => {
    if (!firstName) {
      setErrors((errors) => ({ ...errors, firstName: "Vui lòng nhập tên" }));
    } else if (!/^[^~`!@#$%^&*()_+={}[\]|;:'",.<>?]+$/u
    .test(firstName)) {
      setErrors((errors) => ({ ...errors, firstName: "Tên không hợp lệ" }));
    } else {
      setErrors((errors) => ({ ...errors, firstName: "" }));
    }
  };
  const validateLastName = () => {
    if (!lastName) {
      setErrors((errors) => ({ ...errors, lastName: "Vui lòng nhập tên" }));
    } else if (!/^[^~`!@#$%^&*()_+={}[\]|;:'",.<>?]+$/u
    .test(lastName)) {
      setErrors((errors) => ({ ...errors, lastName: "Tên không hợp lệ" }));
    } else {
      setErrors((errors) => ({ ...errors, lastName: "" }));
    }
  };

  const handleRegister = async (e: { preventDefault: () => void }) => {
    e.preventDefault();

    validateUsername();
    validateEmail();
    validatePhoneNumber();
    validatePassword();
    validateRetypePassword();

    if (Object.values(errors).some((error) => error !== "")) {
      return;
    }

    try {
      setIsLoading(true);
      const data = { username, email, password, firstName, lastName, phoneNumber, roles: ["user"] };
      const response = await axios.post(
        `${process.env.NEXT_PUBLIC_HOSTNAME}auth/register`,
        data
      );
      console.log(response.data);
      toast({
        title: `Tạo tài khoản thành công.\n Vui lòng đăng nhập lại`,
        status: 'success',
        position: "top-right",
        isClosable: true,
      })
      // toast({
      //   title: `Vui lòng đăng nhập lại`,
      //   status: 'success',
      //   position: "top-right",
      //   isClosable: true,
      // })
      router.push("/login")
    } catch (error) {
      console.error(error);
      existUsername();
      toast({
        title: `Tạo tài khoản thất bại`,
        status: 'error',
        position: "top-right",
        isClosable: true,
      })

    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Box position={"relative"} bg="white" h="100%"> 
          <title>Đăng ký</title>
      <Container
        as={SimpleGrid}
        maxW={"7xl"}
        columns={{ base: 1, md: 2 }}
        spacing={{ base: 10, lg: 32 }}
        py={{ base: 5, sm: 10, lg: 10 }}
      >
        <Stack spacing={{ base: 10, md: 20 }}>
          <Heading
            lineHeight={1.1}
            fontSize={{ base: "3xl", sm: "4xl", md: "5xl", lg: "6xl" }}
            color={"#171717"}
          >
            Hệ thống quản trị đơn hàng thông minh{" "}
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
              Đăng ký
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
   
          <Box as={"form"} mt={2} onSubmit={handleRegister}>
            <Stack spacing={4}>
            {/* <FormControl isInvalid={!!errors.firstName}>
                <Input
                  placeholder="Họ và tên"
                  bg={"#0a0a0a"}
                  border={0}
                  color={"white"}
                  _placeholder={{
                    color: "gray.500",
                  }}
                  type="text"
                  value={firstName}
                  onChange={(e) => setFirstName(e.target.value)}
                  onBlur={validateFirstName}
                />
                <FormErrorMessage>{errors.firstName}</FormErrorMessage>
              </FormControl>
              <FormControl isInvalid={!!errors.lastName}>
                <Input
                  placeholder="Họ và tên"
                  bg={"#0a0a0a"}
                  border={0}
                  color={"white"}
                  _placeholder={{
                    color: "gray.500",
                  }}
                  type="text"
                  value={lastName}
                  onChange={(e) => setLastName(e.target.value)}
                  onBlur={validateLastName}
                />
                <FormErrorMessage>{errors.lastName}</FormErrorMessage>
              </FormControl> */}

              <FormControl isInvalid={!!errors.email}>
                <Input
                  placeholder="Email"
                  bg={"#0a0a0a"}
                  border={0}
                  color={"white"}
                  _placeholder={{
                    color: "gray.500",
                  }}
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  onBlur={validateEmail}
                />
                <FormErrorMessage>{errors.email}</FormErrorMessage>
              </FormControl>

              <FormControl isInvalid={!!errors.phoneNumber}>
                <Input
                  placeholder="Số điện thoại"
                  bg={"#0a0a0a"}
                  border={0}
                  color={"white"}
                  _placeholder={{
                    color: "gray.500",
                  }}
                  type="text"
                  value={phoneNumber}
                  onChange={(e) => setPhoneNumber(e.target.value)}
                  onBlur={validatePhoneNumber}
                />
                <FormErrorMessage>{errors.phoneNumber}</FormErrorMessage>
              </FormControl>

              <FormControl isInvalid={!!errors.username}>
                <Input
                  placeholder="Tên đăng nhập"
                  bg={"#0a0a0a"}
                  border={0}
                  color={"white"}
                  _placeholder={{
                    color: "gray.500",
                  }}
                  type="text"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  onBlur={validateUsername}
                />
                <FormErrorMessage>{errors.username}</FormErrorMessage>
              </FormControl>

              <FormControl isInvalid={!!errors.password}>
                <Input
                  placeholder="Mật khẩu"
                  bg={"#0a0a0a"}
                  border={0}
                  color={"white"}
                  _placeholder={{
                    color: "gray.500",
                  }}
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  onBlur={validatePassword}
                />
                <FormErrorMessage>{errors.password}</FormErrorMessage>
              </FormControl>

              <FormControl isInvalid={!!errors.retypePassword}>
                <Input
                  placeholder="Xác nhận mật khẩu"
                  bg={"#0a0a0a"}
                  border={0}
                  color={"white"}
                  _placeholder={{
                    color: "gray.500",
                  }}
                  type="password"
                  value={retypePassword}
                  onChange={(e) => setRetypePassword(e.target.value)}
                  onBlur={validateRetypePassword}
                />
                <FormErrorMessage>{errors.retypePassword}</FormErrorMessage>
              </FormControl>



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
                onClick={handleRegister}
              >
                Đăng ký
              </Button>
              <Button
                fontFamily={"heading"}
                bg={"gray.200"}
                color={"gray.800"}
                mt={4}
                w={"full"}
                onClick={() => router.push("/login")}
              >
                Đã có tài khoản? Đăng nhập ngay
              </Button>
            </Stack>
          </Box>
        </Stack>
      </Container>
    </Box>
  );
}
