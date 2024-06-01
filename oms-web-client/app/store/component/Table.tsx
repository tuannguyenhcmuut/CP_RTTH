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
import { useRemoveStoreMutation } from "@/app/_lib/features/api/apiSlice"
import { Store } from "@/app/type";

interface StoreTableProps {
  stores: Store[];
}

const StoreTable: React.FC<StoreTableProps> = ({ stores }) => {
  const [checkedAll, setCheckedAll] = useState(false);
  const [storeSelections, setStoreSelections] = useState<number[]>([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [storesPerPage, setStoresPerPage] = useState(5);
  const [selectedStore, setSelectedStore] = useState<any>({});
  const [deleteOpen, setDeleteOpen] = useState(false);

  const [removeStore, {isLoading}] = useRemoveStoreMutation();
  const toast = useToast();

  const handleDeleteClose = async () => {
    setDeleteOpen(false);
    setSelectedStore({});
  }
  const handleDeleteOpen = async (id: any) => {
    const p = stores.find((tmp) => tmp.id === id);
    setSelectedStore({...p});
    setDeleteOpen(true);
  }

  const handleDelete = async (id: any) => {
    try {
      await removeStore(id).unwrap();
      handleDeleteClose();
    } catch (err) {
      handleDeleteClose();
      console.error('Failed to delete store: ', err)
      toast({
        title: 'Có lỗi khi xóa cửa hàng này',
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
      const allStoreIds = stores.map((store) => store.id);
      setStoreSelections(allStoreIds);
    } else {
      setStoreSelections([]);
    }
  };

  const handleCheckboxChange = (storeId: number) => {
    if (storeSelections.includes(storeId)) {
      const updatedSelections = storeSelections.filter(
        (selection) => selection !== storeId
      );
      setStoreSelections(updatedSelections);
    } else {
      setStoreSelections([...storeSelections, storeId]);
    }
  };

  const paginateStores = () => {
    const startingIndex = (currentPage - 1) * storesPerPage;
    const endingIndex = Math.min(startingIndex + storesPerPage, stores.length);
    return stores.slice(startingIndex, endingIndex);
  };

  const handlePageChange = (pageNumber: number) => {
    if (pageNumber >= 1 && pageNumber <= totalPages) {
      setCurrentPage(pageNumber);
      setCheckedAll(false);
      setStoreSelections([]);
    } else {
      console.error("Invalid page number");
    }
  };

  const handleStoresPerPageChange = (perPage: number) => {
    setCurrentPage(1);
    setStoresPerPage(perPage);
    setCheckedAll(false);
    setStoreSelections([]);
  };

  const totalPages = Math.ceil(stores.length / storesPerPage);

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
          {paginateStores().map((store) => (
            <Tr key={store.id}>
              <Td>
                <Checkbox
                  isChecked={storeSelections.includes(store.id)}
                  onChange={() => handleCheckboxChange(store.id)}
                />
              </Td>
              <Td>{store.name}</Td>
              {/* <Td> <Badge
                colorScheme={
                  store.status === "warning"
                    ? "yellow"
                    : store.status === "report"
                    ? "orange"
                    : store.status === "blacklist"
                    ? "red"
                    : "green"
                }
                borderRadius={"xl"}
              >
                {store.status}
              </Badge></Td>
              <Td>
                <Flex>
                  {store.tags.slice(0, 3).map((tag, index) => (
                    <Badge key={index} mr={2} colorScheme="blue">
                      {tag}
                    </Badge>
                  ))}
                  {store.tags.length > 3 && (
                    <Badge colorScheme="purple">+{store.tags.length - 3}</Badge>
                  )}
                </Flex>
              </Td> */}
              <Td>{store.phoneNumber}</Td>
              <Td>{store.detailedAddress}, {store.address}</Td>
              <Td>{store.description}</Td>
              <Td>
                <Menu>
                  <MenuButton>
                    <Icon as={SlOptionsVertical} color={"gray"} />
                  </MenuButton>
                  <MenuList>
                    {/* <MenuItem>Sửa</MenuItem> */}
                    <MenuItem onClick={() => handleDeleteOpen(store.id)}>Xoá</MenuItem>
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
            <Button colorScheme='orange' onClick={() => handleDelete(selectedStore.id)}>Xác nhận</Button>
          </ModalFooter>
        </ModalContent>
      </Modal>

      <Flex justify="space-between" mt={4}>
        {/* <ButtonGroup>
          <Button
            onClick={() => handleStoresPerPageChange(5)}
            colorScheme={storesPerPage === 5 ? "orange" : "gray"}
          >
            5
          </Button>
          <Button
            onClick={() => handleStoresPerPageChange(10)}
            colorScheme={storesPerPage === 10 ? "orange" : "gray"}
          >
            10
          </Button>
          <Button
            onClick={() => handleStoresPerPageChange(15)}
            colorScheme={storesPerPage === 15 ? "orange" : "gray"}
          >
            15
          </Button>
          <Button
            onClick={() => handleStoresPerPageChange(20)}
            colorScheme={storesPerPage === 20 ? "orange" : "gray"}
          >
            20
          </Button>
          <Button
            onClick={() => handleStoresPerPageChange(25)}
            colorScheme={storesPerPage === 25 ? "orange" : "gray"}
          >
            25
          </Button>
        </ButtonGroup> */}
        <Select w={'25%'} onChange={(e) => handleStoresPerPageChange(Number(e.target.value))}>
          <option defaultChecked value='5' >5 cửa hàng</option>
          <option value='10' >10 cửa hàng</option>
          <option value='15' >15 cửa hàng</option>
          <option value='20' >20 cửa hàng</option>
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

export default StoreTable;
