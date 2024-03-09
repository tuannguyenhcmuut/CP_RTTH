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
  FormControl,
  FormLabel,
  FormErrorMessage,
  Spinner, 
  Alert, 
  AlertIcon,
  useToast,
} from "@chakra-ui/react";
import { ChangeEvent, useEffect, useState, useMemo } from "react";
import { useForm, Controller } from "react-hook-form"
import ProductDialog from "@/app/product/component/Dialog";
import ReceiverDialog from "../../table/component/Dialog";
import {  useGetProductsQuery, useGetCustomersQuery, useGetStoresQuery, useAddOrderMutation  } from "@/app/_lib/features/api/apiSlice"
import { Product, Customer, Store } from "@/app/type";

type OrderItem = {
  quantity: number,
  price: number,
  product: Product,
}

type FormData = {
  code: string,
  height: number,
  width: number,
  length: number,
  items: OrderItem[],
  store: Store,
  receiver: Customer,
  price: {
      collectionCharge: number,
      itemsPrice: number,
      shippingFee: number,
  },
  isDocument: boolean,
  isBulky: boolean,
  isFragile: boolean,
  isValuable: boolean,
  delivery: {
    payer: string,
    hasLostInsurance: boolean,
    isCollected: boolean,
    deliveryMethod: string,
    luuKho: string,
    layHang: string,
    giaoHang: string,
    shippingFee: number,
    collectionFee: number,
    isDraft: boolean,
    note: string,
  }
}


export default function OrderForm() {
  const [items, setItems] = useState([0]);
  const [selectedItems, setSelectedItems] = useState<any[]>([]);
  const [productSuggestions, setProductSuggestions] = useState<Product[]>([]);
  const [quantityItem, setQuantityItem] = useState<number[]>([]);
  const [priceItem, setPriceItem] = useState<number[]>([]);

  const [selectedStore, setSelectedStore] = useState<any>(null);
  //only 1 checkbox of receiver box be checked
  const [checkbox1Checked, setCheckbox1Checked] = useState(true);
  const [checkbox2Checked, setCheckbox2Checked] = useState(false);

  const [selectedReceiver, setSelectedReceiver] = useState<any>(null);
  const [receiverSuggestions, setReceiverSuggestions] = useState<Customer[]>([]);

  const [addOrder, {isLoading}] = useAddOrderMutation();
  const toast = useToast()

  const {
    data: products,
    isLoading: isLoadingP,
    isSuccess: isSuccessP,
    isError: isErrorP,
    error: errorP,
  } = useGetProductsQuery()

  const {
    data: receivers,
    isLoading: isLoadingR,
    isSuccess: isSuccessR,
    isError: isErrorR,
    error: errorR,
  } = useGetCustomersQuery()

  const {
    data: stores,
    isLoading: isLoadingS,
    isSuccess: isSuccessS,
    isError: isErrorS,
    error: errorS,
  } = useGetStoresQuery()

  const getProducts = useMemo (() => {
    if(isSuccessP) return products.data
  }, [products])

  const getReceivers = useMemo (() => {
    if(isSuccessR) return receivers.data
  }, [receivers])

  const getStores = useMemo (() => {
    if(isSuccessS) return stores.data
  }, [stores])
  
  const {
    register,
    setValue,
    control,
    handleSubmit,
    reset,
    formState: { errors, isSubmitSuccessful, isSubmitting },
  } = useForm<FormData>() 

  useEffect(() => {
    if(isSubmitSuccessful) {
      //TODO Khi tạo đơn hàng thành công -> page tạo đơn hàng thành công (hiển thị chi tiết đơn hàng và có nút tạo tiếp đơn hàng mới)
      setTimeout(() => window.location.reload(), 3000);
      toast({
        title: 'Tạo đơn hàng thành công.',
        position: 'top',
        status: 'success',
        duration: 3000,
        isClosable: true,
      })
    }
  },[isSubmitSuccessful])

  const onSubmit = async(data: FormData) => {
    try {
      data.items.map((item, index) => {
        item.price = priceItem[index];
        item.quantity = quantityItem[index];
      })
      data.store = {...selectedStore};
      await addOrder(data).unwrap();
    } catch (err) {
      console.error('Failed to create order: ', err)
      toast({
        title: 'Có lỗi khi tạo đơn hàng mới',
        position: 'top',
        status: 'error',
        duration: 3000,
        isClosable: true,
      })
    } 
     
  }

  const handleProductInputChange = async (value: string) => {
    if (getProducts && value.length > 1) { // Typically, we look for suggestions after 2 characters have been typed.
      const results = getProducts.filter((product: any) => product.name.toLowerCase().includes(value.toLowerCase()));
      setProductSuggestions(results);
    } else {
      setProductSuggestions([]);
    }
  };

  const handleReceiverInputChange = async (value: string) => {
    if (getReceivers && value.length > 1) { // Typically, we look for suggestions after 2 characters have been typed.
      const results = getReceivers.filter((receiver: any) => receiver.phoneNumber.includes(value));
      setReceiverSuggestions(results);
    } else {
      setReceiverSuggestions([]);
    }
  };

  const handleStoreChange = (value: string) => {
    const tmp = getStores.find((store: any) => store.name === value)
    setSelectedStore(tmp);
    //setValue('store', tmp);
  }

  const handleCheckboxChange = (checkboxId: string) => {
    if (checkboxId === 'checkbox1') {
      setCheckbox1Checked(true);
      setCheckbox2Checked(false);
      //setValue('callBeforeSend', false);
    } else if (checkboxId === 'checkbox2') {
      setCheckbox1Checked(false);
      setCheckbox2Checked(true);
      //setValue('receiveAtPost', false);
    }
  };

  const addItem = () => {
    setItems([...items, items.length]);
  };

  const removeItem = (indexToRemove: number) => {
    setItems(items.filter((_, index) => index !== indexToRemove));
    setSelectedItems(selectedItems.filter((_, index) => index !== indexToRemove));
    setQuantityItem(quantityItem.filter((_, index) => index !== indexToRemove));
    setPriceItem(priceItem.filter((_, index) => index !== indexToRemove));
  };

  const renderProductSuggestions = () => productSuggestions.map((suggestion): any => (
    <div
      key={suggestion.id}
      onClick={() => {
        setSelectedItems([...selectedItems, { product: suggestion,
                                              price: 0,
                                              quantity: 0,
                                            }]);
        setPriceItem([...priceItem, 0]);
        setQuantityItem([...quantityItem, 0]);
        setValue('items', [...selectedItems, {  quantity: 0,
                                                price: 0,
                                                product: suggestion,
                                              }]);

        setProductSuggestions([]);
      }}
    >
      {suggestion.name}
    </div>
  ));
  
  const renderReceiverSuggestions = () => receiverSuggestions.map((suggestion): any => (
    <div
      key={suggestion.id}
      onClick={() => {
        setSelectedReceiver(suggestion);
        setValue('receiver', suggestion);
        setReceiverSuggestions([]);
      }}
    >
      {suggestion.phoneNumber}
    </div>
  ));

  return (
    <Stack direction={{ base: "column", lg: "row" }}>
    <Box w={{ base: "80wv", lg: "50%" }}> 
      <Box p={4} bg="gray.50">
        <Text color="orange.500" fontWeight={"bold"} fontSize="20px"> Người gửi: </Text>
        { isErrorS ? (
          <Flex
          alignItems="center"
          justify="center"
          direction={{ base: "column", md: "row" }}
          m={4}
          >
            <Alert w='50%' status='error'>
              <AlertIcon />
              Can not fetch data from server
            </Alert>
          </Flex>
          )
          : isLoadingS ? (
          <Flex
            alignItems="center"
            justify="center"
            direction={{ base: "column", md: "row" }}
          >
            <Spinner size='lg' color='orange.500' />
          </Flex>
          )
          : (
            <FormControl isRequired isInvalid={Boolean(errors.store)}>
              <Select mt={4} 
                placeholder="Chọn cửa hàng" 
                variant="filled" 
                {...register('store', {
                  required: 'Người gửi không được bỏ trống',
                })} 
                onChange={(e) => handleStoreChange(e.target.value)}

              >
                {getStores.map((store: any) => (
                <option key={store.id} value={selectedStore?.name}>
                  {store.name}
                </option>
                ))}
              </Select>
              <FormErrorMessage>
                {errors.store && errors.store.message}
              </FormErrorMessage>
            </FormControl>
          )}
            { selectedStore && (
            <>
              <Input mt={4} value={selectedStore?.address} placeholder={"Địa chỉ"} readOnly/>
              <Input mt={4} value={selectedStore?.detailedAddress} placeholder={"Số nhà, tên đường, địa chỉ chi tiết"} readOnly/>
            </>
              
            )}
         
      </Box>
      <Box p={4} bg="gray.50" mt={4}>
        <Text color="orange.500" fontWeight={"bold"} fontSize="20px">Người nhận</Text>
        <FormControl isRequired isInvalid={Boolean(errors.receiver)}>
          <div>
          <Input 
            mt={4} 
            placeholder={"Số điện thoại"} 
            value={selectedReceiver?.phoneNumber}
            {...register('receiver', {
              required: 'Người nhận không được bỏ trống',
            })}
            onChange={(e) => {
              handleReceiverInputChange(e.target.value);
            }}
          />
          {receiverSuggestions.length > 0 && <div>{renderReceiverSuggestions()}</div>}
          </div>
          <FormErrorMessage>
            {errors.receiver && errors.receiver.message}
          </FormErrorMessage>
        </FormControl>
        {selectedReceiver && (
        <>
          <Input mt={4} value={selectedReceiver?.name} placeholder={"Họ và tên"} readOnly />
          <Input mt={4} value={selectedReceiver?.address} placeholder={"Địa chỉ"} readOnly/>
          <Input mt={4} value={selectedReceiver?.detailedAddress} placeholder={"Số nhà, tên đường, địa chỉ chi tiết"} readOnly/>
          <Checkbox 
            id="checkbox1" 
            m={4} 
            colorScheme="orange" 
            isChecked={checkbox1Checked} 
            {...register('receiver.receiveAtPost')} 
            onChange={() => handleCheckboxChange('checkbox1')}
          >
            Nhận tại bưu cục
          </Checkbox>
          <Checkbox 
            id="checkbox2" 
            m={4} 
            colorScheme="orange" 
            isChecked={checkbox2Checked} 
            {...register('receiver.callBeforeSend')} 
            onChange={() => handleCheckboxChange('checkbox2')}
          >
            Liên hệ trước khi gửi
          </Checkbox>
        </>
        )}
        <ReceiverDialog />
      </Box>
    </Box>    
    <Box w={{ base: "80wv", lg: "50%" }}>
      <Box bg="gray.50" p={4}>
        <Text color="orange.500" fontWeight={"bold"} fontSize="20px">Thông tin hàng hoá</Text>
        {items.map((item, index) => (
          <Box key={index}>  
            <Divider my={2} orientation="horizontal" color={"gray.800"} />
            <Flex mt={4}>
              {/* Dropdown */}
              <Controller
                name="items"
                control={control}
                rules={{ required: 'This field is required' }}
                defaultValue={[]}
                render={({ field }) => (
                  <FormControl isInvalid={Boolean(errors.items)}>
                    <div>
                      <Input {...field} 
                        placeholder="Tên sản phẩm"
                        value={selectedItems[index]?.product.name}
                        onChange={(e) => {
                          handleProductInputChange(e.target.value);
                          field.onChange(e.target.value); // important to update the form state
                        }}     
                      /> 
                      {productSuggestions.length > 0 && index === selectedItems.length && <div>{renderProductSuggestions()}</div>}
                    </div>
                    <FormErrorMessage>
                        Tên sản phẩm không được bỏ trống
                    </FormErrorMessage>
                    
                  </FormControl>
                )}
              />
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
              
            <Input 
              mt={4} 
              w='25%' 
              placeholder={"Số lượng "} 
              onChange={(e) => {
                setQuantityItem([...quantityItem.slice(0, quantityItem.length-1), Number(e.target.value)]);
                setPriceItem([...priceItem.slice(0, priceItem.length-1), selectedItems[index]?.product.price * Number(e.target.value)]);
              }} 
            />
              {/* <FormControl isRequired isInvalid={Boolean(errors.depth)}>
                <Input m={4} value={selectedProduct[index]?.weight} placeholder={"Khối lượng"} {...register('depth', {
                  required: 'This is required'
                })} />
                <FormErrorMessage>
                  {errors.depth && errors.depth.message}
                </FormErrorMessage>
              </FormControl> */}
              <InputGroup m={4}>
                <InputLeftElement
                  pointerEvents="none"
                  color="teal.400"
                  fontSize="1.2em"
                >$</InputLeftElement>
                <Input  placeholder="Tiền thu hộ" value={priceItem[index] ? String(priceItem[index]) : ""} readOnly/>
              </InputGroup>
            </Flex>
          </Box>
        ))}
        <Flex>
          
          <Button
            onClick={addItem}
            colorScheme="teal"
            variant="outline"
            alignSelf={"center"}
            alignItems={"center"}
          >
            Thêm hàng hoá
          </Button>
          <ProductDialog />
        </Flex>

        <Divider my={2} orientation="horizontal" color={"gray.800"} />
        <Text fontWeight={"500"}>Kích thước</Text>
        <Flex>
          <Input mt={4} placeholder={"Dài - cm"} {...register('length')}/>
          <Input m={4} placeholder={"Rộng - cm"} {...register('width')}/>
          <Input mt={4} placeholder={" Cao - cm"} {...register('height')}/>
        </Flex>
        <HStack columnGap={2}>
          <Box>
            <Checkbox m={2} colorScheme="orange" {...register('isDocument')}>Tài liệu/ Văn kiện </Checkbox>
            <Checkbox m={2} colorScheme="orange" {...register('isValuable')}>Giá trị cao</Checkbox>
          </Box>
          <Box>
            <Checkbox m={2} colorScheme="orange" {...register('isFragile')}>Dễ vỡ</Checkbox>
            <Checkbox m={2} colorScheme="orange" {...register('isBulky')}>Quá khổ</Checkbox>
          </Box>
        </HStack>
      </Box>
      <Box mt={4} bg="gray.50" p={4}>
        <Text color="orange.500" fontWeight={"bold"} fontSize="20px">Vận chuyển</Text>
        <RadioGroup defaultValue="RECEIVER" m={4}>
          <Stack spacing={10} direction="row">
          <Text fontWeight={"500"}>Người trả cước</Text>
            <Radio colorScheme="orange" {...register('delivery.payer')}  value="SENDER">
              Người gửi
            </Radio>
            <Radio colorScheme="orange" {...register('delivery.payer')} value="RECEIVER">
              Người nhận
            </Radio>
          </Stack>
        </RadioGroup>
        
        <Stack spacing={6} direction="row" m={4}>
          <FormControl isRequired isInvalid={Boolean(errors.delivery)}>
            <FormLabel>Loại vận chuyển</FormLabel>
            <Select mt={2} 
              variant="filled" 
              {...register('delivery.deliveryMethod', {
                required: 'This is required',
              })}
            >
              <option value="MOT_TIENG">MỘT GIỜ</option>
              <option value="BA_GIO">BA GIỜ</option>
              <option value="MOT_NGAY">MỘT NGÀY</option>
            </Select>
            <FormErrorMessage>
              {errors.delivery && errors.delivery.message}
            </FormErrorMessage>
          </FormControl>
          <FormControl isRequired isInvalid={Boolean(errors.delivery)}>
            <FormLabel>Lấy hàng</FormLabel>
            <Select mt={2} 
              variant="filled" 
              {...register('delivery.layHang', {
                required: 'This is required',
              })}
            >
              <option value="CA_NGAY">MỘT NGÀY</option>
              <option value="BA_NGAY">BA NGÀY</option>
            </Select>
            <FormErrorMessage>
              {errors.delivery && errors.delivery.message}
            </FormErrorMessage>
          </FormControl>
        </Stack>

        <Stack mt={4} spacing={6} direction="row" m={4}>
          <FormControl isRequired isInvalid={Boolean(errors.delivery)}>
            <FormLabel>Lưu kho</FormLabel>
            <Select mt={2} 
              variant="filled" 
              {...register('delivery.luuKho', {
                required: 'This is required',
              })}
            >
              <option value="CA_NGAY">MỘT NGÀY</option>
              <option value="BA_NGAY">BA NGÀY</option>
            </Select>
            <FormErrorMessage>
              {errors.delivery && errors.delivery.message}
            </FormErrorMessage>
          </FormControl>
          <FormControl isRequired isInvalid={Boolean(errors.delivery)}>
            <FormLabel>Giao hàng</FormLabel>
            <Select mt={2} 
              variant="filled" 
              {...register('delivery.giaoHang', {
                required: 'This is required',
              })}
            >
              <option value="CA_NGAY">MỘT NGÀY</option>
              <option value="BA_NGAY">BA NGÀY</option>
            </Select>
            <FormErrorMessage>
              {errors.delivery && errors.delivery.message}
            </FormErrorMessage>
          </FormControl>
        </Stack>
        <Checkbox m={4} fontWeight={"500"} {...register('delivery.hasLostInsurance')}>Bảo hiểm thất lạc</Checkbox>

        <Text mx={4} my={2} fontWeight={"500"}>Ghi chú </Text>
        <Textarea ml={4} mb={4} placeholder='Ghi chú' w={"95%"} {...register('delivery.note')}/>
        <Flex m={4}>
          <Text color="orange.500" fontWeight={"bold"} fontSize="18px">Thành tiền</Text>
          <InputGroup>
            <InputLeftElement
              pointerEvents="none"
              color="teal.400"
              fontSize="1.2em"
            >$</InputLeftElement>
            <Input {...register('price.collectionCharge')} placeholder="Tiền thu hộ" />
          </InputGroup>
        </Flex>
        <Flex justifyContent={"right"} m={4}>
        <Button colorScheme='gray' m={2} >Lưu nháp</Button>
        {isSubmitting ? (
          <Button
          isLoading
          loadingText='Đang tạo'
          colorScheme='orange'
          variant='outline'
        >
          Tạo
        </Button>
        )
        : (
        <Button type="submit" colorScheme="orange" m={2} onClick={handleSubmit(onSubmit)}>Tạo</Button>
        )}
        </Flex>
      </Box>
 
    </Box>
    </Stack>
  );
}
