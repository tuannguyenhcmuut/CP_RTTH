export type Product = {
    id: number;
    name: string;
    photoUrl: string;
    status: string;
    price: number;
    weight: number;
    length: number;
    width: number;
    height: number;
    description: string;
  };
  
export type Customer = {
    id: number;
    name: string;
    status: string;
    phoneNumber: string;
    address: string;
    detailedAddress: string;
    note: string;
    callBeforeSend: boolean,
    receiveAtPost: boolean,
  };
  
export type Store = {
    id: number;
    name: string;
    phoneNumber: string;
    address: string;
    detailedAddress: string;
    description: string;
    isDefault: boolean,
    sendAtPost: boolean, 
  };

export type Staff = {
  id: string;
  phone: string;
  email: string;
  permissions: string[];
}

export type Order = {
  id: number,
  code: string,
  height: number,
  width: number,
  length: number,
  items: [{
    quantity: number,
    price: number,
    product: Product,
  }],
  store: Store,
  receiver: Customer,
  price: {
      collectionCharge: number,
      itemsPrice: number,
      shippingFee: number,
  },
  orderStatus: string,
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