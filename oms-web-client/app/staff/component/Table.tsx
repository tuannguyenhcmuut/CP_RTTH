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
  Select,
} from "@chakra-ui/react";
import { SlOptionsVertical } from "react-icons/sl";
import { useState } from "react";
import { Staff } from "@/app/type";

interface StaffTableProps {
  staffs: Staff[];
}

const StaffTable: React.FC<StaffTableProps> = ({ staffs }) => {
  const [checkedAll, setCheckedAll] = useState(false);
  const [staffSelections, setStaffSelections] = useState<string[]>([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [staffsPerPage, setStaffsPerPage] = useState(5);

  const handleMasterCheckboxChange = () => {
    setCheckedAll(!checkedAll);

    if (!checkedAll) {
      const allStaffIds = staffs.map((staff) => staff.id);
      setStaffSelections(allStaffIds);
    } else {
      setStaffSelections([]);
    }
  };

  const handleCheckboxChange = (staffId: string) => {
    if (staffSelections.includes(staffId)) {
      const updatedSelections = staffSelections.filter(
        (selection) => selection !== staffId
      );
      setStaffSelections(updatedSelections);
    } else {
      setStaffSelections([...staffSelections, staffId]);
    }
  };

  const paginateStaffs = () => {
    const startingIndex = (currentPage - 1) * staffsPerPage;
    const endingIndex = Math.min(startingIndex + staffsPerPage, staffs.length);
    return staffs.slice(startingIndex, endingIndex);
  };

  const handlePageChange = (pageNumber: number) => {
    if (pageNumber >= 1 && pageNumber <= totalPages) {
      setCurrentPage(pageNumber);
      setCheckedAll(false);
      setStaffSelections([]);
    } else {
      console.error("Invalid page number");
    }
  };

  const handleStaffsPerPageChange = (perPage: number) => {
    setCurrentPage(1);
    setStaffsPerPage(perPage);
    setCheckedAll(false);
    setStaffSelections([]);
  };

  const totalPages = Math.ceil(staffs.length / staffsPerPage);

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
            <Th>Email</Th>
            {/* <Th>Trạng thái</Th> */}
            <Th>Số điện thoại</Th>
            <Th>Quyền</Th>
            {/* <Th w={"1vw"}>
              <Menu>
                <MenuButton>
                  <Icon as={SlOptionsVertical} />
                </MenuButton>
                <MenuList>
                  <MenuItem>Sửa</MenuItem>
                  <MenuItem>Xoá</MenuItem>
                </MenuList>
              </Menu>
            </Th> */}
          </Tr>
        </Thead>
        <Tbody>
          {paginateStaffs().map((staff) => (
            <Tr key={staff.id}>
              <Td>
                <Checkbox
                  isChecked={staffSelections.includes(staff.id)}
                  onChange={() => handleCheckboxChange(staff.id)}
                />
              </Td>
              <Td>{staff.email}</Td>
              {/* <Td>{staff.status}</Td> */}
              <Td>{staff.phone}</Td>
              <Td>
                <Flex>
                  {staff.permissions.map((tag, index) => (
                  <div key={index}>
                    {tag === "VIEW_ONLY" && (
                      <Badge mr={2} colorScheme="gray">
                        XEM ĐƠN
                      </Badge>
                    )}
    
                    {tag === "MANAGE_ORDER" && (
                      <Badge mr={2} colorScheme="red">
                        QUẢN LÝ
                      </Badge>
                    )}
    
                    {tag === "UPDATE_ORDER" && (
                      <Badge mr={2} colorScheme="blue">
                        CẬP NHẬT
                      </Badge>
                    )}

                    {tag === "CREATE_ORDER" && (
                      <Badge mr={2} colorScheme="green">
                        TẠO ĐƠN
                      </Badge>
                    )}
                  </div>
                  ))}
                  
                </Flex>
              </Td>
              {/* <Td>
                <Menu>
                  <MenuButton>
                    <Icon as={SlOptionsVertical} color={"gray"} />
                  </MenuButton>
                  <MenuList>
                    <MenuItem>Sửa</MenuItem>
                    <MenuItem>Xoá</MenuItem>
                  </MenuList>
                </Menu>
              </Td> */}
            </Tr>
          ))}
        </Tbody>
      </Table>

      <Flex justify="space-between" mt={4}>
        <Select w={'25%'} onChange={(e) => handleStaffsPerPageChange(Number(e.target.value))}>
          <option defaultChecked value='5' >5 nhân viên</option>
          <option value='10' >10 nhân viên</option>
          <option value='15' >15 nhân viên</option>
          <option value='20' >20 nhân viên</option>
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

export default StaffTable;
