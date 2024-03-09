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
  Card, 
  CardHeader, 
  CardBody, 
  CardFooter,
  Heading,
  StackDivider,
  Badge,
} from "@chakra-ui/react";
import { ChangeEvent, useEffect, useState, useMemo } from "react";
import { Staff } from "@/app/type";
import { useGetAllRequestOfOwnerQuery } from "@/app/_lib/features/api/apiSlice";

export default function CustomerTable() {
  const [searchInput, setSearchInput] = useState("");
  const [filteredStaffs, setFilteredStaffs] = useState<Staff[]>([]);

  const {
    data: requests,
    isLoading,
    isSuccess,
    isError,
    error,
  } = useGetAllRequestOfOwnerQuery() 

  const getRequests = useMemo (() => {
    if(isSuccess) return requests.data
  }, [requests])

  // const handleSearchInputChange = (event: { target: { value: any } }) => {
  //   const inputValue = event.target.value;
  //   setSearchInput(inputValue);

  //   if(isSuccess) {
  //     const filteredResults = getRequests.filter(
  //       (staff: any) =>
  //         staff.email.toLowerCase().includes(inputValue.toLowerCase())
  //     );
  //     setFilteredStaffs(filteredResults);
  //   }
  // };
  // useEffect(() => {
  //   handleSearchInputChange({ target: { value: '' } });
  // }, [staffs]);

  return (
    <TableContainer bgColor={"white"} rounded={"2xl"}>
      {/* <Flex
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
      )} */}
      <Card>
        <CardHeader>
          <Heading size='md'>Lịch sử yêu cầu</Heading>
        </CardHeader>

        <CardBody>
          
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
            <Stack divider={<StackDivider />} spacing='4'>
              {getRequests.length === 0 && (
                <p>Không có yêu cầu nào</p>
              )}
              {getRequests.length !== 0 && (
                getRequests.map((req: any) => (
                <Box key={req.id}>
                  <Heading size='xs' textTransform='uppercase'>
                    Summary
                  </Heading>
                  <Text pt='2' fontSize='sm' style={{ display: 'flex', flexWrap: 'wrap' }}>
                    Quyền: {req.permissions.map((tag, index) => (
                    <div key={index}>
                      {tag === "VIEW_ONLY" && (
                        <Badge ml={2} colorScheme="gray">
                          XEM ĐƠN
                        </Badge>
                      )}
      
                      {tag === "MANAGE_ORDER" && (
                        <Badge ml={2} colorScheme="red">
                          QUẢN LÝ
                        </Badge>
                      )}
      
                      {tag === "UPDATE_ORDER" && (
                        <Badge ml={2} colorScheme="blue">
                          CẬP NHẬT
                        </Badge>
                      )}

                      {tag === "CREATE_ORDER" && (
                        <Badge ml={2} colorScheme="green">
                          TẠO ĐƠN
                        </Badge>
                      )}
                    </div>
                    ))}
                  </Text>
                  <Text pt='2' fontSize='sm'>
                    Trạng thái: 
                    {req.status === "PENDING" && (
                      <Badge ml={2} colorScheme="gray">
                        ĐANG CHỜ
                      </Badge>
                    )}
    
                    {req.status === "REJECTED" && (
                      <Badge ml={2} colorScheme="red">
                        TỪ CHỐI
                      </Badge>
                    )}
    
                    {req.status === "ACCEPTED" && (
                      <Badge ml={2} colorScheme="green">
                        CHẤP NHẬN
                      </Badge>
                    )}
                  </Text>
                  
                  
                </Box>
                ))
              )}
            </Stack>
          )}
            {/* <Box>
              <Heading size='xs' textTransform='uppercase'>
                Overview
              </Heading>
              <Text pt='2' fontSize='sm'>
                Check out the overview of your clients.
              </Text>
            </Box>
            <Box>
              <Heading size='xs' textTransform='uppercase'>
                Analysis
              </Heading>
              <Text pt='2' fontSize='sm'>
                See a detailed analysis of all your business clients.
              </Text>
            </Box> */}
          
        </CardBody>
      </Card>
      
    </TableContainer>
  );
}
