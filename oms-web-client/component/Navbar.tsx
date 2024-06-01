"use client";

import {
  Box,
  Flex,
  Avatar,
  HStack,
  Text,
  IconButton,
  Button,
  Menu,
  MenuButton,
  MenuList,
  MenuItem,
  MenuDivider,
  useDisclosure,
  useColorModeValue,
  Stack,
  BoxProps,
  FlexProps,
  CloseButton,
  Icon,
  Drawer,
  DrawerBody,
  DrawerFooter,
  DrawerHeader,
  DrawerOverlay,
  DrawerContent,
  DrawerCloseButton,
  Spinner,
  Alert,
  AlertIcon,
  VStack,
  useToast,
} from "@chakra-ui/react";
import { HamburgerIcon, CloseIcon } from "@chakra-ui/icons";
import { FiBell } from "react-icons/fi";
import { useRouter } from "next/navigation";
import { useEffect, useState, createContext } from "react";
import { usePathname } from "next/navigation";
// import { searchResults } from "./SearchBar";
import {
  FiHome,
  FiTrendingUp,
  FiShoppingBag,
  FiStar,
  FiSettings,
  FiMenu,
  FiUser,
  FiAirplay,
  FiShoppingCart
} from "react-icons/fi";
import { IconType } from "react-icons";
import { ReactText, useMemo } from "react";
import { useGetEmployeesRequestQuery, useApproveEmployeeRequestMutation, useRejectEmployeeRequestMutation } from "@/app/_lib/features/api/apiSlice";

interface Props {
  children: React.ReactNode;
}

export const IsLoginContext = createContext<boolean | null>(null);
//const Links = ["STORE", "SUPPORT", "CONTACT"];

const NavLink = (props: Props) => {
  const router = useRouter();
  const { children } = props;

  return (
    <Box
      px={2}
      py={1}
      rounded={"md"}
      _hover={{
        textDecoration: "none",
        bg: useColorModeValue("gray.800", "gray.700"),
      }}
      onClick={() => router.push("/")}
      cursor={"pointer"}
    >
      {children}
    </Box>
  );
};

interface LinkItemProps {
  name: string;
  icon: IconType;
  link: string;
}
const LinkItems: Array<LinkItemProps> = [
  { name: "Tổng quan", icon: FiHome, link: "/dashboard" },
  { name: "Đơn hàng", icon: FiShoppingCart, link: "/order" },
  { name: "Nhân sự", icon: FiUser, link: "/staff" },
  { name: "Cửa hàng", icon: FiAirplay, link: "/store" },
  { name: "Sản phẩm", icon: FiShoppingBag, link: "/product" },
  { name: "Khách hàng", icon: FiStar, link: "/table" },
];

export default function NavBar() {
  const [lastTimeAccess, setLastTimeAccess] = useState("2020");
  const [isLogin, setIsLogin] = useState(false);
  const router = useRouter();
  const { isOpen, onOpen, onClose } = useDisclosure();
  const notification = useDisclosure();
  const toast = useToast();
  const pathname = usePathname();
  const {
    data: employeeRequests,
    isLoading,
    isSuccess,
    isError,
    error,
  } = useGetEmployeesRequestQuery() 
  const [approveEmployeeRequest] = useApproveEmployeeRequestMutation();
  const [rejectEmployeeRequest] = useRejectEmployeeRequestMutation();

  const getEmployeeRequests = useMemo (() => {
    if(isSuccess) return employeeRequests.data
  }, [employeeRequests])

  const createdAt = new Date().toISOString();

  const handleApproveRequest = async (request: any) => {
    try {
      console.log(request);
      await approveEmployeeRequest({
        id: request.id, 
        request: {
          employeeId: request.employeeId,
          permissions: request.permissions,
        }
        })
      .unwrap();
      notification.onClose();
    } catch (err) {
      console.error('Failed to send request: ', err)
      toast({
        title: 'Có lỗi khi gửi yêu cầu',
        position: 'top',
        status: 'error',
        duration: 3000,
        isClosable: true,
      })
    } 
  }

  const handleRejectRequest = async (request: any) => {
    try {
      const res = await rejectEmployeeRequest({
        id: request.id, 
        request: {
          employeeId: request.employeeId,
          permissions: request.permissions,
        }
        })
      .unwrap();
      notification.onClose();
    } catch (err) {
      console.error('Failed to send request: ', err)
      toast({
        title: 'Có lỗi khi gửi yêu cầu',
        position: 'top',
        status: 'error',
        duration: 3000,
        isClosable: true,
      })
    } 
  }

  useEffect(() => {
    // const lastAccess = localStorage.getItem("createAt");
    if (localStorage.getItem("accessToken") === null) {
      setIsLogin(false);
    } else setIsLogin(true);
  }, []);
  // useEffect(() => {
  //   if (Date.now() - new Date(lastTimeAccess).getTime() > 1800000) {
  //     setIsLogin(false);
  //   } else {
  //     setIsLogin(true);
  //   }
  // }, [lastTimeAccess, pathname]);

  // useEffect(() => {
  //   let intervalId: NodeJS.Timeout;

  //   if (isLogin) {
  //     intervalId = setInterval(() => {
  //       fetch(`${process.env.NEXT_PUBLIC_HOSTNAME}auth/refreshToken`, {
  //         method: "POST",
  //         headers: {
  //           "Content-Type": "application/json",
  //         },
  //         body: JSON.stringify({
  //           refreshToken: localStorage.getItem("refreshToken"),
  //         }),
  //       })
  //         .then((response) => response.json())
  //         .then((data) => {
  //           const { accessToken, refreshToken } = data;
  //           localStorage.setItem("accessToken", accessToken);
  //           localStorage.setItem("refreshToken", refreshToken);
  //           console.log(data);
  //           const createdAt = new Date().toISOString();
  //           localStorage.setItem("createdAt", createdAt);
  //         })
  //         .catch((error) => {
  //           console.error("Lỗi:", error);
  //         });
  //     }, 900000);
  //   }

  //   return () => {
  //     clearInterval(intervalId);
  //   };
  // }, [isLogin]);

  useEffect(() => {
    if ((pathname === "/login" && isLogin) ||
      (pathname === "/register" && isLogin)) {
      router.replace("/dashboard");
    }
  }, [pathname, isLogin]);

  function handleLogout(): void {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    localStorage.removeItem("createdAt");
    localStorage.removeItem("userId");
    setIsLogin(false);
    router.replace("/login");
  }

  return (
    <IsLoginContext.Provider value={isLogin}>
    <Box
      bg={useColorModeValue("#171717", "gray.900")}
      px={4}
      textColor={"white"}
    >
      <Flex h={16} alignItems={"center"} justifyContent={"space-between"}>
        {/* <IconButton
          size={"md"}
          icon={isOpen ? <CloseIcon /> : <HamburgerIcon />}
          aria-label={"Open Menu"}
          display={{ md: "none" }}
          onClick={isOpen ? onClose : onOpen}
        /> */}
        <Drawer
          isOpen={isOpen}
          placement="left"
          onClose={onClose}
          returnFocusOnClose={false}
          onOverlayClick={onClose}
          size="full"
        >
          <DrawerContent>
            <SidebarContent onClose={onClose} />
          </DrawerContent>
        </Drawer>
        <MobileNav display={{ base: "flex", md: "none" }} onOpen={onOpen} />
        <HStack spacing={8} alignItems={"center"}>
          <Box ml={8} fontSize='20px' onClick={() => router.push("/")} cursor={"pointer"}>
            <Text fontSize="2xl" fontFamily="monospace" fontWeight="bold">
              OrList
            </Text>
          </Box>
          {isLogin&&
            <Box
              px={2}
              py={1}
              rounded={"md"}
              _hover={{
                textDecoration: "none",
                bg: useColorModeValue("gray.800", "gray.700"),
              }}
              onClick={() => router.push("/dashboard")}
              cursor={"pointer"}
            >
              QUẢN LÝ
            </Box>
          }
          {/* <HStack as={"nav"} spacing={4} display={{ base: "none", md: "flex" }}>
            <Box
              px={2}
              py={1}
              rounded={"md"}
              _hover={{
                textDecoration: "none",
                bg: useColorModeValue("gray.800", "gray.700"),
              }}
              onClick={() => router.push("/cart")}
              cursor={"pointer"}
            >
              CART
            </Box>
            <Box
              px={2}
              py={1}
              rounded={"md"}
              _hover={{
                textDecoration: "none",
                bg: useColorModeValue("gray.800", "gray.700"),
              }}
              onClick={() => router.push("/wishlist")}
              cursor={"pointer"}
            >
              WISHLIST
            </Box>
            <Box
              px={2}
              py={1}
              rounded={"md"}
              _hover={{
                textDecoration: "none",
                bg: useColorModeValue("gray.800", "gray.700"),
              }}
              onClick={() => router.push("/games")}
              cursor={"pointer"}
            >
              GAMES
            </Box>
          </HStack> */}
        </HStack> 
        {/* TODO: Refresh token navbar */}
        <Flex alignItems={"center"} color="#171717">
          {isLogin ? (
            <>
            <Menu>
              <FiBell size="20px" color="white" onClick={notification.onOpen}/>
              <Drawer
                isOpen={notification.isOpen}
                placement='right'
                onClose={notification.onClose}  
              >
                <DrawerOverlay />
                <DrawerContent>
                  <DrawerCloseButton />
                  <DrawerHeader>Notifications</DrawerHeader>
                  <DrawerBody>
                    
                    {isLoading ? (
                      <Flex
                      alignItems="center"
                      justify="center"
                      direction={{ base: "column", md: "row" }}
                      >
                        <Spinner size='lg' color='orange.500' />
                      </Flex>
                    ) : isError ? (
                      <Flex
                      alignItems="center"
                      justify="center"
                      direction={{ base: "column", md: "row" }}
                      m={4}
                      >
                        <Alert w='25%' status='error'>
                          <AlertIcon />
                          Can not fetch data from server
                        </Alert>
                      </Flex>
                    ) : (
                      <>
                        {getEmployeeRequests.length === 0 && (
                          <p>Không có thông báo nào</p>
                        )}
                        {getEmployeeRequests.length !== 0 && (
                          getEmployeeRequests.map((req: any) => (
                            <VStack mb={2} key={req.id}>
                              <p>Bạn nhận được yêu cầu trở thành nhân viên</p>
                              <HStack>
                                <Button
                                  colorScheme="orange"
                                  variant='outline'
                                  mx={2}
                                  onClick={() => handleRejectRequest(req)}
                                >
                                  Từ chối
                                </Button>
                                <Button
                                  colorScheme="orange"
                                  onClick={() => handleApproveRequest(req)}
                                  mx={2}
                                >
                                  Chấp nhận
                                </Button>
                              </HStack>
                            </VStack>
                          ))
                        )}
                      </>
                    )}
                  </DrawerBody>

                  {/* <DrawerFooter>
                    <Button variant='outline' mr={3} onClick={onClose}>
                      Cancel
                    </Button>
                    <Button colorScheme='blue'>Save</Button>
                  </DrawerFooter> */}
                </DrawerContent>
              </Drawer>

              <MenuButton
                as={Button}
                rounded={"full"}
                variant={"link"}
                cursor={"pointer"}
                minW={0}
                ml={6}
              >
                <Avatar
                  size={"sm"}
                  src={
                    "https://images.unsplash.com/photo-1493666438817-866a91353ca9?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&fit=crop&h=200&w=200&s=b616b2c5b373a80ffc9636ba24f7a4a9"
                  }
                />
              </MenuButton>
              <MenuList>
                <MenuItem onClick={() => router.push("/user")}>
                  Cá nhân
                </MenuItem>
                <MenuItem onClick={() => router.push("/user")}>Order</MenuItem>
                <MenuItem onClick={() => router.push("/user")}>
                  Thanh toán
                </MenuItem>
                <MenuItem onClick={() => router.push("/user")}>
                  Cài đặt
                </MenuItem>
                <MenuDivider />
                <MenuItem onClick={() => handleLogout()}>Đăng xuất</MenuItem>
              </MenuList>
            </Menu>
            </>
          ) : (
            <>
            <Button
              colorScheme="orange"
              variant='outline'
              mx={2}
              onClick={() => (window.location.href = "/register")}
            >
              Đăng ký
            </Button>
            <Button
              colorScheme="orange"
              onClick={() => (window.location.href = "/login")}
              mx={2}
            >
              Đăng nhập
            </Button>
          </>
          )}
          
        </Flex>
      </Flex>
    </Box>
    </IsLoginContext.Provider>
  );
}

interface SidebarProps extends BoxProps {
  onClose: () => void;
}

const SidebarContent = ({ onClose, ...rest }: SidebarProps) => {
  const router = useRouter();
  return (
    <Box
      bg={useColorModeValue("white", "gray.900")}
      borderRight="1px"
      borderRightColor={useColorModeValue("gray.200", "gray.700")}
      w={{ base: "full", md: 60 }}
      pos="fixed"
      h="full"
      {...rest}
    >
      <Flex  display={{ base: "flex", md: "none" }} h="20" alignItems="center" mx="8" justifyContent="space-between">
        <Text fontSize="2xl" fontFamily="monospace" fontWeight="bold">
          OrList
        </Text>
        <CloseButton onClick={onClose} />
      </Flex>
      <Button 
        onClick={() => {
          onClose();
          router.push("/create");
        }} 
        ml={8} mt={6} mb={2} 
        w="25%" 
        colorScheme="orange"
      >
        + Tạo đơn 
      </Button>
      {LinkItems.map((link) => (
        <NavItem  
          key={link.name} 
          icon={link.icon}
          onClick={() => {
            onClose();
            router.push(`${link.link}`)
          }}
        >
          {link.name}
        </NavItem>
      ))}
    </Box>
  );
};

interface NavItemProps extends FlexProps {
  icon: IconType;
  children: ReactText;
}
const NavItem = ({ icon, children, ...rest }: NavItemProps) => {
  return (
    <Box
      as="a"

      style={{ textDecoration: "none" }}
      _focus={{ boxShadow: "none" }}
    >
      <Flex
        align="center"
        p="4"
        mx="4"
        borderRadius="lg"
        role="group"
        cursor="pointer"
        _hover={{
          bg: "cyan.400",
          color: "white",
        }}
        {...rest}
      >
        {icon && (
          <Icon
            mr="4"
            fontSize="16"
            _groupHover={{
              color: "white",
            }}
            as={icon}
          />
        )}
        {children}
      </Flex>
    </Box>
  );
};

interface MobileProps extends FlexProps {
  onOpen: () => void;
}
const MobileNav = ({ onOpen, ...rest }: MobileProps) => {
  return (
    <Flex
      ml={{ base: 0, md: 60 }}
      px={{ base: 4, md: 24 }}
      height="20"
      alignItems="center"
      // bg={useColorModeValue("white", "gray.900")}
      // borderBottomWidth="1px"
      // borderBottomColor={useColorModeValue("gray.200", "gray.700")}
      justifyContent="flex-start"
      {...rest}
    >
      <IconButton
        variant="filled"
        onClick={onOpen}
        aria-label="open menu"
        color='white'
        icon={<FiMenu />}
      />

      {/* <Text fontSize="2xl" ml="8" fontFamily="monospace" fontWeight="bold">
        OrList
      </Text> */}
    </Flex>
  );
};
