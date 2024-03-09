"use client"
import React, { useState } from 'react';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import { Box, Container, Flex, Icon, SimpleGrid } from "@chakra-ui/react";

type  ValuePiece = Date|null;

function App() {
  const [value, onChange] = useState<ValuePiece|[ValuePiece,ValuePiece]>(new Date());

  return (
    <Box p={4}>
      <Calendar value={value} onChange={onChange} selectRange={true}/>
      <Calendar activeStartDate={new Date(new Date().getFullYear(), new Date().getMonth() -1)} view="month"  />
    </Box>
  );
}

export default App;