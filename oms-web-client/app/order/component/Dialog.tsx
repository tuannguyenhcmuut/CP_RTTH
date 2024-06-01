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
} from "@chakra-ui/react";

import { useEffect, useState } from "react";
import { useForm } from "react-hook-form"
import { useAddProductMutation } from "@/app/_lib/features/api/apiSlice"

type FormData = {
  name:string,
  photo:string,
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
  const {
    register,
    setValue,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<FormData>()

  const [addProduct, {isLoading}] = useAddProductMutation();


  const onSubmit = async(data: FormData) => {
    try {
      await addProduct(data).unwrap();
      onClose();
    } catch (err) {
      console.error('Failed to save product: ', err)
    } finally {
      reset();
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
                required: 'This is required',
              })}/>
              <FormErrorMessage>
                {errors.name && errors.name.message}
              </FormErrorMessage>
            </FormControl>
            <FormControl mt={4}>
              <FormLabel>Ảnh</FormLabel>
              <Input type='text' {...register('photo')}  />
            </FormControl>
            <FormControl isRequired isInvalid={Boolean(errors.weight)} mt={4}>
              <FormLabel>Trọng lượng (g)</FormLabel>
              <Input type='text' {...register('weight', {
                required: 'This is required'
              })} />
              <FormErrorMessage>
                {errors.weight && errors.weight.message}
              </FormErrorMessage>
            </FormControl>
            <FormControl isRequired isInvalid={Boolean(errors.price)} mt={4}>
              <FormLabel>Đơn giá (VNĐ)</FormLabel>
              <Input type='text' {...register('price', {
                required: 'This is required'
              })} />
              <FormErrorMessage>
                {errors.price && errors.price.message}
              </FormErrorMessage>
            </FormControl>
            <FormControl isRequired isInvalid={Boolean(errors.status)} mt={4}>
              <FormLabel>Trạng thái</FormLabel>
              <Select placeholder='Chọn trạng thái' {...register('status', {
                required: 'This is required'
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