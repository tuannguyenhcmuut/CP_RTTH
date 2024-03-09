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
} from "@chakra-ui/react";
import { ChangeEvent, useState } from "react";

export default function AddressSelect() {
  const [items, setItems] = useState([0]);

  const addItem = () => {
    setItems([...items, items.length]);
  };

  const removeItem = (indexToRemove: number) => {
    setItems(items.filter((_, index) => index !== indexToRemove));
  };

  return (
    <Box>
      <Box bg="gray.50" p={4}>
        <Text fontWeight={"800"}>Thông tin hàng hoá</Text>
        {items.map((item, index) => (
          <Box key={index}>
            <Divider my={2} orientation="horizontal" color={"gray.800"} />
            <Flex mt={4}>
              <Input placeholder={"Tên hàng hoá "} />
              <Button
                ml={2}
                onClick={() => removeItem(index)}
                colorScheme="red"
                variant="outline"
                alignItems={"center"}
              >
                X
              </Button>
            </Flex>
            <Flex>
              <Input mt={4} placeholder={"Số lượng "} />
              <Input m={4} placeholder={"Khối lượng"} />
              <InputGroup m={4}>
                <InputLeftElement
                  pointerEvents="none"
                  color="teal.400"
                  fontSize="1.2em"
                >
                  $
                </InputLeftElement>
                <Input placeholder="Tiền thu hộ" />
              </InputGroup>
            </Flex>
          </Box>
        ))}
        <Button
          onClick={addItem}
          colorScheme="teal"
          variant="outline"
          alignSelf={"center"}
          alignItems={"center"}
        >
          Thêm hàng hoá
        </Button>

        <Divider my={2} orientation="horizontal" color={"gray.800"} />
        <Text fontWeight={"500"}>Kích thước</Text>
        <Flex>
          <Input mt={4} placeholder={"Dài - cm"} />
          <Input m={4} placeholder={"Rộng - cm"} />
          <Input mt={4} placeholder={" Cao - cm"} />
        </Flex>
        <HStack columnGap={2}>
          <Box>
            <Checkbox m={2}>Tài liệu/ Văn kiện</Checkbox>
            <Checkbox m={2}>Giá trị cao</Checkbox>
          </Box>
          <Box>
            <Checkbox m={2}>Dễ vỡ</Checkbox>
            <Checkbox m={2}>Quá khổ</Checkbox>
          </Box>
        </HStack>
      </Box>
      <Box mt={4} bg="gray.50" p={4}>
        <Text fontWeight={"800"}>Thành tiền</Text>
        <Checkbox m={4}>Thu hộ bằng tiền hàng</Checkbox>
        <Flex p={4}>
          <InputGroup>
            <InputLeftElement
              pointerEvents="none"
              color="teal.400"
              fontSize="1.2em"
            >
              $
            </InputLeftElement>
            <Input placeholder="Tiền thu hộ" />
          </InputGroup>
        </Flex>
        <RadioGroup defaultValue="1" m={4}>
          <Stack spacing={5} direction="row">
            <Text fontWeight={"500"}>Người trả cước: </Text>
            <Radio colorScheme="teal" value="1">
              Người gửi
            </Radio>
            <Radio colorScheme="teal" value="2">
              Người nhận
            </Radio>
          </Stack>
        </RadioGroup>
        <Text fontWeight={"500"}>Ghi chú: </Text>
        <Textarea m={4} placeholder="Ghi chú" w={"95%"} />
      </Box>
    </Box>
  );
}
