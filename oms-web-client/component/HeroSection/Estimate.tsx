"use client";
import { Box, Button, Flex, Input, Select, SimpleGrid, Text, VStack } from "@chakra-ui/react";

import data from "@/public/province.json";
import { ChangeEvent, useState } from "react";
import { cityData, City, District, Ward } from "./CityData";

export default function AddressSelect() {
  const [selectedCity, setSelectedCity] = useState("");
  const [selectedDistrict, setSelectedDistrict] = useState("");


  const [cost, setCost] = useState(0);

  const handleButtonClick = () => {
    setCost(20000);
  };

  const handleCityChange = (event: ChangeEvent<HTMLSelectElement>) => {
    setSelectedCity(event.target.value);
    setSelectedDistrict("");
  };

  const handleDistrictChange = (event: ChangeEvent<HTMLSelectElement>) => {
    setSelectedDistrict(event.target.value);
  };
  const selectedCityData = cityData.find(
    (city) => city.codename === selectedCity
  );
  const selectedDistrictData = selectedCityData?.districts.find(
    (district) => district.codename === selectedDistrict
  );

  const [RecievedCity, setRecievedCity] = useState("");
  const [RecievedDistrict, setRecievedDistrict] = useState("");

  const RecieveCityChange = (event: ChangeEvent<HTMLSelectElement>) => {
    setRecievedCity(event.target.value);
    setRecievedDistrict("");
  };

  const RecieveDistrictChange = (event: ChangeEvent<HTMLSelectElement>) => {
    setRecievedDistrict(event.target.value);
  };
  const RecievedCityData = cityData.find(
    (city) => city.codename === RecievedCity
  );
  const RecievedDistrictData = RecievedCityData?.districts.find(
    (district) => district.codename === RecievedDistrict
  );

  return (
    <SimpleGrid columns={{ base: 1, md: 2 }}>
      <Box w={"20vw"} mt={4}>
        {/* Dropdown chọn thành phố */}
        <Text fontSize="xl">Địa chỉ nơi gửi</Text>
        <Select
          m={4}
          placeholder="Chọn tỉnh thành"
          value={selectedCity}
          onChange={handleCityChange}
          variant="filled"
        >
          <option value="" disabled hidden>
            Chọn tỉnh thành
          </option>
          {cityData.map((city) => (
            <option key={city.code} value={city.codename}>
              {city.name}
            </option>
          ))}
        </Select>

        {/* Dropdown chọn quận */}

        <Select
          m={4}
          placeholder="Chọn quận"
          isDisabled={selectedCity == "" ? true : false}
          value={selectedDistrict}
          onChange={handleDistrictChange}
          variant="filled"
        >
          <option value="" disabled hidden>
            Chọn quận
          </option>
          {selectedCityData?.districts.map((district) => (
            <option key={district.code} value={district.codename}>
              {district.name}
            </option>
          ))}
        </Select>

        {/* Dropdown chọn phường */}

        <Select
          m={4}
          variant="filled"
          placeholder="Chọn phường"
          isDisabled={selectedDistrict == "" ? true : false}
        >
          <option value="" disabled hidden>
            Chọn phường
          </option>
          {selectedDistrictData?.wards.map((ward) => (
            <option key={ward.code} value={ward.codename}>
              {ward.name}
            </option>
          ))}
        </Select>
      </Box>
      <Box w={"20vw"} mt={4}>
        {/* Dropdown chọn thành phố */}
        <Text fontSize={"xl"}>Địa chỉ nơi nhận</Text>
        <Select
          m={4}
          placeholder="Chọn tỉnh thành"
          value={RecievedCity}
          onChange={RecieveCityChange}
          variant="filled"
        >
          <option value="" disabled hidden>
            Chọn tỉnh thành
          </option>
          {cityData.map((city) => (
            <option key={city.code} value={city.codename}>
              {city.name}
            </option>
          ))}
        </Select>

        {/* Dropdown chọn quận */}

        <Select
          m={4}
          placeholder="Chọn quận"
          isDisabled={RecievedCity == "" ? true : false}
          value={RecievedDistrict}
          onChange={RecieveDistrictChange}
          variant="filled"
        >
          <option value="" disabled hidden>
            Chọn quận
          </option>
          {RecievedCityData?.districts.map((district) => (
            <option key={district.code} value={district.codename}>
              {district.name}
            </option>
          ))}
        </Select>

        {/* Dropdown chọn phường */}

        <Select
          m={4}
          variant="filled"
          placeholder="Chọn phường"
          isDisabled={RecievedDistrict == "" ? true : false}
        >
          <option value="" disabled hidden>
            Chọn phường
          </option>
          {RecievedDistrictData?.wards.map((ward) => (
            <option key={ward.code} value={ward.codename}>
              {ward.name}
            </option>
          ))}
        </Select>
      </Box>
      <Box  display="flex" flexDirection="column">
        
        <Input m={4} placeholder="Khối lượng (g)" bgColor="gray.50" w="20vw" />
        <Button  m={4} colorScheme="green" w="5vw" onClick={handleButtonClick}>
        Ước tính
      </Button>
      {cost !== 0 && <Flex  m={4} fontSize="xl" fontWeight={600} color="gray.800">Chi phí:  <Text mx={2} color="green">{" "} {cost} {" "}</Text>  đồng</Flex>}
      </Box>
    </SimpleGrid>
  );
}
