import {
  Box,
  Select,
  Input,
  Text,
  Checkbox,
  Flex,
  Divider,
  Button,
  Stack,
  VStack,
  HStack,
  Container,
  InputGroup,
  InputLeftElement,
  RadioGroup,
  Radio,
  Textarea,
  Table,
  Thead,
  Tbody,
  Tfoot,
  Tr,
  Th,
  Td,
  TableCaption,
  TableContainer,
  Spinner,
  Alert,
  AlertIcon,
} from "@chakra-ui/react";
import { ChangeEvent, useEffect, useState, useMemo } from "react";
import Dialog from "./Dialog";
import CustomerList from "./Table";
import { Staff } from "@/app/type";
import { useGetEmployeesQuery } from "@/app/_lib/features/api/apiSlice";

export default function CustomerTable() {
  const [searchInput, setSearchInput] = useState("");
  const [filteredStaffs, setFilteredStaffs] = useState<Staff[]>([]);

  const {
    data: staffs,
    isLoading,
    isSuccess,
    isError,
    error,
  } = useGetEmployeesQuery() 

  const getStaffs = useMemo (() => {
    if(isSuccess) return staffs.data
  }, [staffs])

  const handleSearchInputChange = (event: { target: { value: any } }) => {
    const inputValue = event.target.value;
    setSearchInput(inputValue);

    if(isSuccess) {
      const filteredResults = getStaffs.filter(
        (staff: any) =>
          staff.email.toLowerCase().includes(inputValue.toLowerCase())
      );
      setFilteredStaffs(filteredResults);
    }
  };
  useEffect(() => {
    handleSearchInputChange({ target: { value: '' } });
  }, [staffs]);

  return (
    <TableContainer bgColor={"white"} rounded={"2xl"}>
      <Flex
        alignItems="center"
        justify="space-between"
        direction={{ base: "column", md: "row" }}
      >
        <VStack
          m={{ base: 2, md: 8 }}
          alignItems={"flex-start"}
          maxW={{ base: "80vw", md: "full" }}
        >
          <Text fontSize={{ base: "xl", md: "3xl" }} fontWeight={700}>
            Nhân viên
          </Text>
          <Text color={"gray"}>Tuần này bạn có thêm 20 nhân viên mới</Text>
        </VStack>
        <Flex>
          <Input
            m={{ base: 2, md: 8 }}
            variant="filled"
            placeholder="Tìm nhân viên"
            w={{ base: "70vw", md: "30vw" }}
            onChange={handleSearchInputChange}
          />
        </Flex>
        <Dialog />
      </Flex>
      {isLoading ? (
        <Flex
        alignItems="center"
        justify="center"
        direction={{ base: "column", md: "row" }}
        >
          <Spinner size='lg' color='orange.500' />
        </Flex>
      ) : isError ? (
        <Flex
        alignItems="center"
        justify="center"
        direction={{ base: "column", md: "row" }}
        m={4}
        >
          <Alert w='25%' status='error'>
            <AlertIcon />
            Can not fetch data from server
          </Alert>
        </Flex>
      ) : (
        <CustomerList staffs={filteredStaffs} />
      )}
      
    </TableContainer>
  );
}
