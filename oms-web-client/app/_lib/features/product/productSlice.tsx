import { createSlice, PayloadAction,createAsyncThunk } from '@reduxjs/toolkit'
import type { RootState } from '../../store'
import axios from 'axios';
import { Product } from '@/app/type';


export interface ProductState {
  value: Product[],
  status: string,
  error: any,
}

// Define the initial state using that type
const initialState: ProductState = {
  value: [],
  status: 'idle',
  error: null,
}

// export const fetchProducts = createAsyncThunk('product/fetchProducts', async () => {
//     const response = await axios.get(`${process.env.NEXT_PUBLIC_HOSTNAME}api/v1/products`, {
//         headers: {
//             "Content-Type": "application/json",
//             "Authorization": `Bearer ${localStorage.getItem("accessToken")}`,
//             "userId": `${localStorage.getItem("userId")}`,
//         } 
//     })
    
//     return response.data.data
// })

export const productSlice = createSlice({
  name: 'product',
  // `createSlice` will infer the state type from the `initialState` argument
  initialState,
  reducers: {
    // Use the PayloadAction type to declare the contents of `action.payload`
    addProduct: (state, action: PayloadAction<Product>) => {
      state.value.push(action.payload)
    },
  },
})

export const { addProduct } = productSlice.actions

// Other code such as selectors can use the imported `RootState` type
export const selectProduct = (state: RootState) => state.product.value

export default productSlice.reducer