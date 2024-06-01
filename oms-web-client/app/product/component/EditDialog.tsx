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
  useToast,
  Image,
  Center,
} from "@chakra-ui/react";

import { useEffect, useState } from "react";
import { useForm,  Controller, SubmitHandler } from "react-hook-form"
import { useEditProductMutation } from "@/app/_lib/features/api/apiSlice"
import getFromLocalStorage from "@/app/_lib/getFromLocalStorage";

type FormData = {
  name:string,
  photoUrl:string,
  status: string,
  price: number,
  weight: number,
  length: number,
  width: number,
  height: number,
  description:string,
}

export default function EditDialog({ isOpen, onOpen, onClose, setProducts, selectedProduct }: any) {
  const {
    register,
    reset,
    setValue,
    watch,
    handleSubmit,
    formState: { errors },
  } = useForm<FormData>({defaultValues: {
    name: selectedProduct.name || "",
    status: selectedProduct.status || "",
    price: selectedProduct.price || "",
    weight: selectedProduct.weight || "",
    length: selectedProduct.length || "",
    width: selectedProduct.width || "",
    height: selectedProduct.height || "",
    description: selectedProduct.description || "",
  }})

  const [editProduct, {isLoading}] = useEditProductMutation();
  const [img, setImg] = useState<any>(null);
  const toast = useToast();


  useEffect(() => {
    reset(selectedProduct);
  }, [selectedProduct])

  const validateFiles = (e: any) => {
    if(e.target.files) {
      const value = e.target.files[0];
      const fsMb = value.size / (1024 * 1024)
      const MAX_FILE_SIZE = 10
      if (fsMb > MAX_FILE_SIZE) {
        return 'Max file size 10mb'
      }     
      setImg(value);
    }
    return;
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

  const onSubmit = async(data: FormData) => {
    if(img) {
      const tmp = await uploadImage(img);
      data.photoUrl = tmp.base64;
    }
    try {
      await editProduct(data).unwrap();
      onClose();
    } catch (err) {
      console.error('Failed to edit product: ', err)
      toast({
        title: 'Có lỗi khi sửa thông tin sản phẩm',
        position: 'top',
        status: 'error',
        duration: 3000,
        isClosable: true,
      })
    }
}

  return (
      <Modal
        closeOnOverlayClick={false}
        isOpen={isOpen}
        onClose={onClose}
        isCentered
      >
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>Sửa thông tin sản phẩm</ModalHeader>
          <ModalCloseButton />
          <ModalBody pb={6}>
          
            <Center>
              <Image
                src={selectedProduct?.photoUrl}
                width={'50%'}
                height={'50%'}
                alt="product image"
              />
            </Center>
            
            <FormControl mt={4}>
              <input type='file' accept="image/png" onChange={validateFiles}></input>
            </FormControl>

            <FormControl mt={4} isRequired isInvalid={Boolean(errors.name)}>
              <FormLabel>Tên hàng hóa</FormLabel>
              <Input type='text' {...register('name', {
                required: 'Trường này không được bỏ trống',
              })}   />
              <FormErrorMessage>
                {errors.name && errors.name.message}
              </FormErrorMessage>
            </FormControl>
            
            <FormControl isRequired isInvalid={Boolean(errors.name)} mt={4}>
              <FormLabel>Trọng lượng (g)</FormLabel>
              <Input type='text' {...register('weight', {
                required: 'Trường này không được bỏ trống'
              })} />
              <FormErrorMessage>
                {errors.name && errors.name.message}
              </FormErrorMessage>
            </FormControl>
            <FormControl isRequired isInvalid={Boolean(errors.name)} mt={4}>
              <FormLabel>Đơn giá (VNĐ)</FormLabel>
              <Input type='text' {...register('price', {
                required: 'Trường này không được bỏ trống'
              })} />
              <FormErrorMessage>
                {errors.name && errors.name.message}
              </FormErrorMessage>
            </FormControl>
            
            <FormControl isRequired isInvalid={Boolean(errors.name)} mt={4}>
              <FormLabel>Trạng thái</FormLabel>
              <Select placeholder='Chọn trạng thái' {...register('status', {
                required: 'Trường này không được bỏ trống'
              })} >
                  <option value="AVAILABLE">CÒN HÀNG</option>
                  <option value="BACK_ORDER">DỰ TRỮ</option>
                  <option value="OUT_OF_STOCK">HẾT HÀNG</option>
              </Select>
              <FormErrorMessage>
                {errors.name && errors.name.message}
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
              Cancel
            </Button>
            <Button colorScheme="orange" onClick={handleSubmit(onSubmit)}>
              Save
            </Button>
            
          </ModalFooter>
        </ModalContent>
      </Modal>
  );
}