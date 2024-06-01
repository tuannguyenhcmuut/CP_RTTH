import {
  Box,
  Checkbox,
  Flex,
  Table,
  Tbody,
  Td,
  Text,
  Th,
  Thead,
  Badge,
  Tr,
  ButtonGroup,
  Button,
  Input,
  Icon,
  Menu,
  MenuButton,
  MenuList,
  MenuItem,
  MenuItemOption,
  MenuGroup,
  MenuOptionGroup,
  MenuDivider,
  Modal,
  ModalOverlay,
  ModalContent,
  ModalHeader,
  ModalFooter,
  ModalBody,
  ModalCloseButton,
  useToast,
  Select,
} from "@chakra-ui/react";
import { SlOptionsVertical } from "react-icons/sl";
import { useState } from "react";
import { useRemoveCustomerMutation } from "@/app/_lib/features/api/apiSlice"
import { Customer } from "@/app/type";

interface CustomerTableProps {
  customers: Customer[];
}

const CustomerTable: React.FC<CustomerTableProps> = ({ customers }) => {
  const [checkedAll, setCheckedAll] = useState(false);
  const [customerSelections, setCustomerSelections] = useState<number[]>([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [customersPerPage, setCustomersPerPage] = useState(5);
  const [selectedCustomer, setSelectedCustomer] = useState<any>({});
  const [deleteOpen, setDeleteOpen] = useState(false);

  const [removeCustomer, {isLoading}] = useRemoveCustomerMutation();
  const toast = useToast();

  const handleDeleteClose = async () => {
    setDeleteOpen(false);
    setSelectedCustomer({});
  }
  const handleDeleteOpen = async (id: any) => {
    const p = customers.find((tmp) => tmp.id === id);
    setSelectedCustomer({...p});
    setDeleteOpen(true);
  }

  const handleDelete = async (id: any) => {
    try {
      await removeCustomer(id).unwrap();
      handleDeleteClose();
    } catch (err) {
      handleDeleteClose();
      console.error('Failed to delete customer: ', err)
      toast({
        title: 'Có lỗi khi xóa khách hàng này',
        position: 'top',
        status: 'error',
        duration: 3000,
        isClosable: true,
      })
    }
  }

  const handleMasterCheckboxChange = () => {
    setCheckedAll(!checkedAll);

    if (!checkedAll) {
      const allCustomerIds = customers.map((customer) => customer.id);
      setCustomerSelections(allCustomerIds);
    } else {
      setCustomerSelections([]);
    }
  };

  const handleCheckboxChange = (customerId: number) => {
    if (customerSelections.includes(customerId)) {
      const updatedSelections = customerSelections.filter(
        (selection) => selection !== customerId
      );
      setCustomerSelections(updatedSelections);
    } else {
      setCustomerSelections([...customerSelections, customerId]);
    }
  };

  const paginateCustomers = () => {
    const startingIndex = (currentPage - 1) * customersPerPage;
    const endingIndex = Math.min(startingIndex + customersPerPage, customers.length);
    return customers.slice(startingIndex, endingIndex);
  };

  const handlePageChange = (pageNumber: number) => {
    if (pageNumber >= 1 && pageNumber <= totalPages) {
      setCurrentPage(pageNumber);
      setCheckedAll(false);
      setCustomerSelections([]);
    } else {
      console.error("Invalid page number");
    }
  };

  const handleCustomersPerPageChange = (perPage: number) => {
    setCurrentPage(1);
    setCustomersPerPage(perPage);
    setCheckedAll(false);
    setCustomerSelections([]);
  };

  const totalPages = Math.ceil(customers.length / customersPerPage);

  return (
    <Box overflowX="auto" p={8}>
      <Table variant="simple">
        <Thead bgColor={"gray.50"} rounded={"xl"}>
          <Tr>
            <Th width={"1vw"}>
              <Checkbox
                isChecked={checkedAll}
                onChange={handleMasterCheckboxChange}
              />
            </Th>
            <Th>Name</Th>
            {/* <Th>Trạng thái</Th>
            <Th>Tags</Th> */}
            <Th>Số điện thoại</Th>
            <Th>Địa chỉ</Th>
            <Th>Ghi chú</Th>
            <Th w={"1vw"}>
              <Menu>
                <MenuButton>
                  <Icon as={SlOptionsVertical} />
                </MenuButton>
                {/* <MenuList>
                  <MenuItem>Sửa</MenuItem>                  
                  <MenuItem>Xoá</MenuItem>
                </MenuList> */}
              </Menu>
            </Th>
          </Tr>
        </Thead>
        <Tbody>
          {paginateCustomers().map((customer) => (
            <Tr key={customer.id}>
              <Td>
                <Checkbox
                  isChecked={customerSelections.includes(customer.id)}
                  onChange={() => handleCheckboxChange(customer.id)}
                />
              </Td>
              <Td>{customer.name}</Td>
              {/* <Td> <Badge
                colorScheme={
                  customer.status === "warning"
                    ? "yellow"
                    : customer.status === "report"
                    ? "orange"
                    : customer.status === "blacklist"
                    ? "red"
                    : "green"
                }
                borderRadius={"xl"}
              >
                {customer.status}
              </Badge></Td>
              <Td>
                <Flex>
                  {customer.tags.slice(0, 3).map((tag, index) => (
                    <Badge key={index} mr={2} colorScheme="blue">
                      {tag}
                    </Badge>
                  ))}
                  {customer.tags.length > 3 && (
                    <Badge colorScheme="purple">+{customer.tags.length - 3}</Badge>
                  )}
                </Flex>
              </Td> */}
              <Td>{customer.phoneNumber}</Td>
              <Td>{customer.detailedAddress}, {customer.address}</Td>
              <Td>{customer.note}</Td>
              <Td>
                <Menu>
                  <MenuButton>
                    <Icon as={SlOptionsVertical} color={"gray"} />
                  </MenuButton>
                  <MenuList>
                    {/* <MenuItem>Sửa</MenuItem> */}
                    <MenuItem onClick={() => handleDeleteOpen(customer.id)}>Xoá</MenuItem>
                  </MenuList>
                </Menu>
              </Td>
            </Tr>
          ))}
        </Tbody>
      </Table>

      <Modal onClose={() => handleDeleteClose()} isOpen={deleteOpen} isCentered>
        <ModalOverlay />
        <ModalContent>
          <ModalCloseButton />
          <ModalBody>
              Bạn có chắc chắn xóa sản phẩm này?
          </ModalBody>
          <ModalFooter>
            <Button mr={3} onClick={() => handleDeleteClose()}>Đóng</Button>
            <Button colorScheme='orange' onClick={() => handleDelete(selectedCustomer.id)}>Xác nhận</Button>
          </ModalFooter>
        </ModalContent>
      </Modal>

      <Flex justify="space-between" mt={4}>
        {/* <ButtonGroup>
          <Button
            onClick={() => handleCustomersPerPageChange(5)}
            colorScheme={customersPerPage === 5 ? "orange" : "gray"}
          >
            5
          </Button>
          <Button
            onClick={() => handleCustomersPerPageChange(10)}
            colorScheme={customersPerPage === 10 ? "orange" : "gray"}
          >
            10
          </Button>
          <Button
            onClick={() => handleCustomersPerPageChange(15)}
            colorScheme={customersPerPage === 15 ? "orange" : "gray"}
          >
            15
          </Button>
          <Button
            onClick={() => handleCustomersPerPageChange(20)}
            colorScheme={customersPerPage === 20 ? "orange" : "gray"}
          >
            20
          </Button>
          <Button
            onClick={() => handleCustomersPerPageChange(25)}
            colorScheme={customersPerPage === 25 ? "orange" : "gray"}
          >
            25
          </Button>
        </ButtonGroup> */}
        <Select w={'25%'} onChange={(e) => handleCustomersPerPageChange(Number(e.target.value))}>
          <option defaultChecked value='5' >5 khách hàng</option>
          <option value='10' >10 khách hàng</option>
          <option value='15' >15 khách hàng</option>
          <option value='20' >20 khách hàng</option>
        </Select>

        <Flex align="center">
          <Text>{`Page `}</Text>
          <Input
            mx={2}
            type="number"
            min={1}
            max={totalPages}
            placeholder={currentPage.toString()}
            onChange={(e) => handlePageChange(Number(e.target.value))}
          />
          <Text>{` of ${totalPages}`}</Text>
        </Flex>

        <ButtonGroup>
          <Button
            onClick={() => handlePageChange(currentPage - 1)}
            isDisabled={currentPage === 1}
          >
            Previous
          </Button>
          <Button
            onClick={() => handlePageChange(currentPage + 1)}
            isDisabled={currentPage === totalPages}
          >
            Next
          </Button>
        </ButtonGroup>
      </Flex>
    </Box>
  );
};

export default CustomerTable;
