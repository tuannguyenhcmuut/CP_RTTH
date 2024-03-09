import {
  Stat,
  StatLabel,
  StatNumber,
  StatHelpText,
  StatArrow,
  Box,
  SimpleGrid,
  StatGroup,
  Flex,
} from "@chakra-ui/react";
import { SlOptionsVertical } from "react-icons/sl";
import { useState } from "react";

interface StatItemProps {
  Label: string;
  Num: number;
  type: number;
  percentage: number;
}

function StatItem(props: StatItemProps) {
  const { Label, Num, type, percentage } = props;
  return (
    <StatGroup bgColor={"white"} p={4} borderRadius={"md"}>
      <Stat>
        <Flex alignItems={"center"} justify={"space-between"}>
          <Box>
            <StatLabel>{Label}</StatLabel>
            <StatNumber>{Num}</StatNumber>
          </Box>
          <StatHelpText>
            <StatArrow type={type == 1 ? "increase" : "decrease"} />
            {percentage}%
          </StatHelpText>
        </Flex>
      </Stat>
    </StatGroup>
  );
}

function Stats() {
  return (
    <SimpleGrid
      overflowX="auto"
      columns={{ base: 2, md: 4 }}
      spacing={{ base: 2, md: 10 }}
    >
      <StatItem Label="Đơn mới hôm nay" Num={134} type={1} percentage={54.89} />
      <StatItem Label="Đã giao hôm nay" Num={34} type={1} percentage={22.45} />
      <StatItem Label="Doanh thu" Num={8535000} type={0} percentage={10.92} />
      <StatItem Label="Đã nhận về" Num={4560000} type={0} percentage={14.22} />
    </SimpleGrid>
  );
}

export default Stats;
