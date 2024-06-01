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
  Center,
  useDisclosure,
  Modal,
  ModalOverlay,
  ModalContent,
  ModalHeader,
  ModalFooter,
  ModalBody,
  ModalCloseButton,
  Spinner
} from "@chakra-ui/react";
import { SlOptionsVertical } from "react-icons/sl";
import { useEffect, useState } from "react";
import Image from 'next/image';
import EditDialog from "./EditDialog";
import { useAppSelector, useAppDispatch } from '../../_lib/hooks'
import { useRemoveProductMutation } from "@/app/_lib/features/api/apiSlice"
import { Order } from "@/app/type";

interface OrderTableProps {
  orders: Order[];
}

const OrderTable: React.FC<OrderTableProps> = ({ orders }) => {
  const [checkedAll, setCheckedAll] = useState(false);
  const [orderSelections, setOrderSelections] = useState<number[]>([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [ordersPerPage, setOrdersPerPage] = useState(5);
  const { isOpen, onOpen, onClose } = useDisclosure();
  const [selectedOrder, setSelectedOrder] = useState<any>({});
  const [deleteOpen, setDeleteOpen] = useState(false);
  const dispatch = useAppDispatch();
  //const [removeProduct, {isLoading}] = useRemoveProductMutation();

  

  // const handleDeleteClose = async () => {
  //   setDeleteOpen(false);
  //   setSelectedOrder({});
  // }
  // const handleDeleteOpen = async (id: any) => {
  //   const p = orders.find((tmp) => tmp.id === id);
  //   setSelectedOrder({...p});
  //   setDeleteOpen(true);
  // }

  // const handleUpdate = async (id: any) => {
  //   const p = orders.find((tmp) => tmp.id === id);
  //   setSelectedOrder({...p});
  //   onOpen();
  // }

  // const handleDelete = async (id: any) => {
    // await fetch(`${process.env.NEXT_PUBLIC_HOSTNAME}api/v1/products/${id}`, 
    //             {
    //               method: 'DELETE',
    //               headers: {
    //                 "Content-Type": "application/json",
    //                 "Authorization": `Bearer ${localStorage.getItem("accessToken")}`,
    //                 //"userId": `${localStorage.getItem("userId")}`,
    //               }
                
    //             })
    // .then(data => data.json())
    // .then(processedData => console.log(processedData.data))
    // .catch(error => console.log(error))

    //getProducts();
  //   try {
  //     await removeProduct(id).unwrap();
  //     handleDeleteClose();
  //   } catch (err) {
  //     console.error('Failed to delete product: ', err)
  //   }
    
  // }

  const handleMasterCheckboxChange = () => {
    setCheckedAll(!checkedAll);

    if (!checkedAll) {
      const allOrderIds = orders.map((order) => order.id);
      setOrderSelections(allOrderIds);
    } else {
      setOrderSelections([]);
    }
  };

  const handleCheckboxChange = (orderId: number) => {
    if (orderSelections.includes(orderId)) {
      const updatedSelections = orderSelections.filter(
        (selection) => selection !== orderId
      );
      setOrderSelections(updatedSelections);
    } else {
      setOrderSelections([...orderSelections, orderId]);
    }
  };

  const paginateOrders = () => {
    const startingIndex = (currentPage - 1) * ordersPerPage;
    const endingIndex = Math.min(startingIndex + ordersPerPage, orders.length);
    return orders.slice(startingIndex, endingIndex);
  };

  const handlePageChange = (pageNumber: number) => {
    if (pageNumber >= 1 && pageNumber <= totalPages) {
      setCurrentPage(pageNumber);
      setCheckedAll(false);
      setOrderSelections([]);
    } else {
      console.error("Invalid page number");
    }
  };

  const handleOrdersPerPageChange = (perPage: number) => {
    setCurrentPage(1);
    setOrdersPerPage(perPage);
    setCheckedAll(false);
    setOrderSelections([]);
  };

  const totalPages = Math.ceil(orders.length / ordersPerPage);

  

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
            {/* <Th>Mã đơn</Th> */}
            <Th>Thành tiền</Th>
            <Th>Địa chỉ</Th>
            {/* <Th>Thời gian cập nhật</Th> */}
            <Th>Trạng thái</Th>
            
            {/* <Th w={"1vw"}>
              <Menu>
                <MenuButton>
                  <Icon as={SlOptionsVertical} />
                </MenuButton>
              </Menu>
            </Th> */}
          </Tr>
        </Thead>
        <Tbody>
          {paginateOrders().map((order) => (
            <Tr key={order.id}>
              <Td>
                <Checkbox
                  isChecked={orderSelections.includes(order.id)}
                  onChange={() => handleCheckboxChange(order.id)}
                />
              </Td>
              {/* <Td>
                <Center>
                  <strong>{order.code}</strong>
                </Center>
              </Td> */}
              <Td>{order.price.collectionCharge} VNĐ</Td>
              
              <Td>{`${order.receiver.detailedAddress}, ${order.receiver.address}`},</Td>
              <Td> 
                {order.orderStatus === "AVAILABLE" && (
                  <Badge mr={2} colorScheme="green">
                    CÒN HÀNG
                  </Badge>
                )}

                {order.orderStatus === "OUT_OF_STOCK" && (
                  <Badge mr={2} colorScheme="red">
                    HẾT HÀNG
                  </Badge>
                )}

                {order.orderStatus === "BACK_ORDER" && (
                  <Badge mr={2} colorScheme="blue">
                    DỰ TRỮ
                  </Badge>
                )}
              </Td>
              
              {/* <Td>
                <Menu>
                  <MenuButton>
                    <Icon as={SlOptionsVertical} color={"gray"} />
                  </MenuButton>
                  <MenuList>
                    <MenuItem onClick={() => handleUpdate(order.id)}>Sửa</MenuItem>
                    <MenuItem onClick={() => handleDeleteOpen(order.id)}>Xoá</MenuItem>
                  </MenuList>
                </Menu>
                
              </Td> */}
            </Tr>
          ))}
        </Tbody>
      </Table>

      {/* <EditDialog 
        isOpen={isOpen}
        onOpen={onOpen}
        onClose={onClose}
        selectedOrder={selectedOrder}
      /> */}

      {/* <Modal onClose={() => handleDeleteClose()} isOpen={deleteOpen} isCentered>
        <ModalOverlay />
        <ModalContent>
          <ModalCloseButton />
          <ModalBody>
              Bạn có chắc chắn xóa sản phẩm này?
          </ModalBody>
          <ModalFooter>
            <Button mr={3} onClick={() => handleDeleteClose()}>Đóng</Button>
            <Button colorScheme='orange' onClick={() => handleDelete(selectedOrder.id)}>Xác nhận</Button>
          </ModalFooter>
        </ModalContent>
      </Modal> */}
               
      <Flex justify="space-between" mt={4}>
        <ButtonGroup>
          <Button
            onClick={() => handleOrdersPerPageChange(5)}
            colorScheme={ordersPerPage === 5 ? "orange" : "gray"}
          >
            5
          </Button>
          <Button
            onClick={() => handleOrdersPerPageChange(10)}
            colorScheme={ordersPerPage === 10 ? "orange" : "gray"}
          >
            10
          </Button>
          <Button
            onClick={() => handleOrdersPerPageChange(15)}
            colorScheme={ordersPerPage === 15 ? "orange" : "gray"}
          >
            15
          </Button>
          <Button
            onClick={() => handleOrdersPerPageChange(20)}
            colorScheme={ordersPerPage === 20 ? "orange" : "gray"}
          >
            20
          </Button>
          <Button
            onClick={() => handleOrdersPerPageChange(25)}
            colorScheme={ordersPerPage === 25 ? "orange" : "gray"}
          >
            25
          </Button>
        </ButtonGroup>

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

export default OrderTable;
