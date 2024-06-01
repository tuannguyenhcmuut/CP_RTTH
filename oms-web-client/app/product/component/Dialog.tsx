"use client";
import {
  Box,
  Select,
  Input,
  Text,
  Checkbox,
  Modal,
  ModalOverlay,
  ModalContent,
  ModalHeader,
  ModalFooter,
  ModalBody,
  ModalCloseButton,
  Button,
  useDisclosure,
  Textarea,
  FormControl,
  FormLabel,
  FormErrorMessage,
  FormHelperText,
  HStack,
  InputGroup,
  Icon,
  useToast,
} from "@chakra-ui/react";
import { FiFile } from 'react-icons/fi'
import { ReactNode, useEffect, useState } from "react";
import { useForm } from "react-hook-form"
import { useAddProductMutation } from "@/app/_lib/features/api/apiSlice"
import getFromLocalStorage from "@/app/_lib/getFromLocalStorage";

type FormData = {
  name: string,
  photoUrl: string,
  status: string,
  price: number,
  weight: number,
  length: number,
  width: number,
  height: number,
  description:string,
}


export default function Dialog() {
  const { isOpen, onOpen, onClose } = useDisclosure();
  const toast = useToast();
  const {
    register,
    setValue,
    handleSubmit,
    reset,
    formState: { errors, isSubmitSuccessful, isValid },
  } = useForm<FormData>()

  const [img, setImg] = useState<any>(null);
  const [addProduct, {isLoading}] = useAddProductMutation();

  useEffect(() => {
    if(isSubmitSuccessful) reset();
  }, [isSubmitSuccessful, reset])

  const validateFiles = (e: any) => {
    if(!e.target.files) return 'Trường này không được bỏ trống'
    const value = e.target.files[0];
    const fsMb = value.size / (1024 * 1024)
    const MAX_FILE_SIZE = 10
    if (fsMb > MAX_FILE_SIZE) {
      return 'Max file size 10mb'
    }     
    setImg(value);
  }

  const uploadImage = async (value: any) => {
    const formData = new FormData();
    formData.append('file', value);
    const res = await fetch(`${process.env.NEXT_PUBLIC_HOSTNAME}api/v1/user/firebase/image`, 
    {
      method: "POST",
      headers: {
        "Authorization": `Bearer ${getFromLocalStorage('accessToken')}`,
      },
      body: formData, 
    })
    return res.json()
  }

  const onSubmit = async (data: FormData) => {
    const tmp = await uploadImage(img);
    data.photoUrl = tmp.base64;
    
    try {
      await addProduct(data).unwrap();
      onClose();
    } catch (err) {
      console.error('Failed to save product: ', err)
      toast({
        title: 'Có lỗi khi thêm sản phẩm mới',
        position: 'top',
        status: 'error',
        duration: 3000,
        isClosable: true,
      })
    } 
}

  return (
    <>
      <Button m={{ base: 2, md: 8 }} colorScheme="orange" onClick={onOpen}>
        Thêm sản phẩm
      </Button>
      <Modal
        closeOnOverlayClick={false}
        isOpen={isOpen}
        onClose={onClose}
        isCentered
      >
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>Thêm sản phẩm</ModalHeader>
          <ModalCloseButton />
          <ModalBody pb={6}>
            
            <FormControl isRequired isInvalid={Boolean(errors.name)}>
              <FormLabel>Tên hàng hóa</FormLabel>
              <Input type='text' id="name" {...register('name', {
                required: 'Trường này không được bỏ trống',
              })}/>
              <FormErrorMessage>
                {errors.name && errors.name.message}
              </FormErrorMessage>
            </FormControl>
            <FormControl mt={4} isInvalid={!!errors.photoUrl} isRequired>
              <FormLabel>Hình ảnh sản phẩm</FormLabel>
              <input type='file' accept="image/png" onChange={validateFiles}></input>
              <FormErrorMessage>
                {errors.photoUrl && errors?.photoUrl.message}
              </FormErrorMessage>
            </FormControl>

            <FormControl isRequired isInvalid={Boolean(errors.weight)} mt={4}>
              <FormLabel>Trọng lượng (g)</FormLabel>
              <Input type='text' {...register('weight', {
                required: 'Trường này không được bỏ trống'
              })} />
              <FormErrorMessage>
                {errors.weight && errors.weight.message}
              </FormErrorMessage>
            </FormControl>
            <FormControl isRequired isInvalid={Boolean(errors.price)} mt={4}>
              <FormLabel>Đơn giá (VNĐ)</FormLabel>
              <Input type='text' {...register('price', {
                required: 'Trường này không được bỏ trống'
              })} />
              <FormErrorMessage>
                {errors.price && errors.price.message}
              </FormErrorMessage>
            </FormControl>
            <FormControl isRequired isInvalid={Boolean(errors.status)} mt={4}>
              <FormLabel>Trạng thái</FormLabel>
              <Select placeholder='Chọn trạng thái' {...register('status', {
                required: 'Trường này không được bỏ trống'
              })} >
                  <option value="AVAILABLE">CÒN HÀNG</option>
                  <option value="BACK_ORDER">DỰ TRỮ</option>
                  <option value="OUT_OF_STOCK">HẾT HÀNG</option>
              </Select>
              <FormErrorMessage>
                {errors.status && errors.status.message}
              </FormErrorMessage>
            </FormControl>
            <HStack spacing='16px' mt={4}>
              <FormControl>
                <FormLabel>Dài (cm)</FormLabel>
                <Input type='text' {...register('length')} />
              </FormControl>
              <FormControl>
                <FormLabel>Rộng (cm)</FormLabel>
                <Input type='text' {...register('width')}  />
              </FormControl>
              <FormControl>
                <FormLabel>Cao (cm)</FormLabel>
                <Input type='text' {...register('height')}  />
              </FormControl>
            </HStack>
            <Textarea mt={4} 
              placeholder="Mô tả chi tiết"
              {...register('description')}            
            />
            
            
          </ModalBody>

          <ModalFooter>
            <Button onClick={onClose} mr={3}>
              Huỷ 
            </Button>
            <Button colorScheme="orange" onClick={handleSubmit(onSubmit)}>
              Lưu
            </Button>
            
          </ModalFooter>
        </ModalContent>
      </Modal>
    </>
  );
}