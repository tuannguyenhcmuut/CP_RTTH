// Import the RTK Query methods from the React-specific entry point
import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import getFromLocalStorage from "../../getFromLocalStorage";
import type {
  BaseQueryFn,
  FetchArgs,
  FetchBaseQueryError,
} from "@reduxjs/toolkit/query";

interface IHeader {
  "Content-Type": string;
  Authorization: string;
}
const Header: IHeader = {
  "Content-Type": "application/json",
  Authorization: `Bearer ${getFromLocalStorage("accessToken")}`,
};

type ResponseHandler =
  | 'content-type'
  | 'json'
  | 'text'
  | ((response: Response) => Promise<any>)

const baseQuery = fetchBaseQuery({
  baseUrl: `${process.env.NEXT_PUBLIC_HOSTNAME}api/v1`,
  prepareHeaders: (headers: any) => {
    headers.set("Content-Type", Header["Content-Type"]);
    headers.set(
      "Authorization",
      `Bearer ${getFromLocalStorage("accessToken")}`
    );
    return headers;
  },
});

const baseQueryWithRefresh: BaseQueryFn<
  string | FetchArgs,
  unknown,
  FetchBaseQueryError
> = async (args, api, extraOptions) => {
  let result = await baseQuery(args, api, extraOptions);

  if (result.error && result.error.status === 403) {
    // try to get a new token

    const refreshResult = await fetch(
      `${process.env.NEXT_PUBLIC_HOSTNAME}auth/refreshToken`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          refreshToken: getFromLocalStorage("refreshToken"),
        }),
      }
    );

    if (refreshResult.ok) {
      const tmp = await refreshResult.json();
      console.log(tmp);
      const { accessToken, refreshToken } = tmp;
      // store the new token in the store or wherever you keep it
      localStorage.setItem("accessToken", accessToken);
      localStorage.setItem("refreshToken", refreshToken);
      const createdAt = new Date().toISOString();
      localStorage.setItem("createdAt", createdAt);
      // retry the initial query
      result = await baseQuery(args, api, extraOptions);
    } else {
      // refresh failed - do something like redirect to login or show a "retry" button
      localStorage.removeItem("accessToken");
      localStorage.removeItem("refreshToken");
      localStorage.removeItem("createdAt");
      localStorage.removeItem("userId");
      // window.location.href =  '/login';
    }
  }
  return result;
};

// Define our single API slice object
export const apiSlice = createApi({
  // The cache reducer expects to be added at `state.api` (already default - this is optional)
  reducerPath: "api",
  // All of our requests will have URLs starting with '/fakeApi'
  baseQuery: baseQueryWithRefresh,
  // baseQuery: fetchBaseQuery({
  //   baseUrl: `${process.env.NEXT_PUBLIC_HOSTNAME}api/v1`,
  //   prepareHeaders: (headers) => {
  //     headers.set("Content-Type", Header["Content-Type"]);
  //     headers.set("Authorization", Header["Authorization"]);
  //     return headers;
  //   }
  // }),
  tagTypes: ["Product", "Customer", "Store", "Order", "Staff", "Request"],
  // The "endpoints" represent operations and requests for this server
  endpoints: (builder) => ({
    // The `getPosts` endpoint is a "query" operation that returns data
    getRefreshToken: builder.mutation({
      query: (refreshToken) => ({
        url: "http://localhost:8080/auth/refreshToken",
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: refreshToken,
      }),
    }),
    getProducts: builder.query<any, void>({
      // The URL for the request is '/fakeApi/posts'
      query: () => ({
        url: "/products",
        headers: {
          userId: `${getFromLocalStorage("userId")}`,
        },
      }),
      providesTags: ["Product"],
    }),
    addProduct: builder.mutation({
      query: (newProduct) => ({
        url: "/products",
        method: "POST",
        headers: {
          userId: `${getFromLocalStorage("userId")}`,
        },
        body: newProduct,
      }),
      invalidatesTags: ["Product"],
    }),
    editProduct: builder.mutation({
      query: (newProduct) => ({
        url: `/products/${newProduct.id}`,
        method: "PUT",
        headers: {
          userId: `${getFromLocalStorage("userId")}`,
        },
        body: newProduct,
      }),
      invalidatesTags: ["Product"],
    }),
    removeProduct: builder.mutation({
      query: (id) => ({
        url: `/products/${id}`,
        method: "DELETE",
      }),

      invalidatesTags: ["Product"],
    }),

    getCustomers: builder.query<any, void>({
      // The URL for the request is '/fakeApi/posts'
      query: () => ({
        url: "/receivers",
        headers: {
          userId: `${getFromLocalStorage("userId")}`,
        },
      }),
      providesTags: ["Customer"],
    }),
    addCustomer: builder.mutation({
      query: (newReceiver) => ({
        url: "/receivers/create",
        method: "POST",
        headers: {
          userId: `${getFromLocalStorage("userId")}`,
        },
        body: newReceiver,
      }),
      invalidatesTags: ["Customer"],
    }),
    removeCustomer: builder.mutation({
      query: (id) => ({
        url: `/receivers/${id}`,
        method: "DELETE",
        headers: {
          userId: `${getFromLocalStorage("userId")}`,
        },
      }),

      invalidatesTags: ["Customer"],
    }),

    getStores: builder.query<any, void>({
      // The URL for the request is '/fakeApi/posts'
      query: () => ({
        url: "/stores",
      }),
      providesTags: ["Store"],
    }),
    addStore: builder.mutation({
      query: (newReceiver) => ({
        url: "/stores/create",
        method: "POST",
        body: newReceiver,
      }),
      invalidatesTags: ["Store"],
    }),
    removeStore: builder.mutation({
      query: (id) => ({
        url: `/stores/${id}`,
        method: "DELETE",
      }),
      invalidatesTags: ["Store"],
    }),

    getOrders: builder.query<any, void>({
      // The URL for the request is '/fakeApi/posts'
      query: () => ({
        url: "/order",
      }),
      providesTags: ["Order"],
    }),
    addOrder: builder.mutation({
      query: (newOrder) => ({
        url: "/order",
        method: "POST",
        body: newOrder,
      }),
      invalidatesTags: ["Order"],
    }),

    getEmployees: builder.query<any, void>({
      // The URL for the request is '/fakeApi/posts'
      query: () => ({
        url: "/empl-mngt",
      }),
      providesTags: ["Staff"],
    }),

    sendEmployeeRequest: builder.mutation({
      query: (request) => ({
        url: "/empl-mngt/request",
        method: "POST",
        body: request,
      }),
      invalidatesTags: ["Request"],
    }),

    getAllRequestOfOwner: builder.query<any, void>({
      // The URL for the request is '/fakeApi/posts'
      query: () => ({
        url: "/empl-mngt/owner/get-all",
      }),
      providesTags: ["Request"],
    }),

    getEmployeesRequest: builder.query<any, void>({
      // The URL for the request is '/fakeApi/posts'
      query: () => ({
        url: "/empl-mngt/employee/get-all?status=PENDING",
      }),
    }),

    approveEmployeeRequest: builder.mutation({
      query: ({ id, request }) => ({
        url: `/empl-mngt/${id}/approve`,
        method: "POST",
        body: request,
        responseHandler: (response) => response.text(),
      }),
    }),
    rejectEmployeeRequest: builder.mutation({
      query: ({ id, request }) => ({
        url: `/empl-mngt/${id}/reject`,
        method: "POST",
        body: request,
        responseHandler: (response) => response.text(),
      }),
    }),
  }),
});

// Export the auto-generated hook for the `getPosts` query endpoint
export const {
  useGetProductsQuery,
  useAddProductMutation,
  useEditProductMutation,
  useRemoveProductMutation,
  useGetCustomersQuery,
  useAddCustomerMutation,
  useRemoveCustomerMutation,
  useGetStoresQuery,
  useAddStoreMutation,
  useRemoveStoreMutation,
  useGetOrdersQuery,
  useAddOrderMutation,
  useGetRefreshTokenMutation,
  useGetEmployeesQuery,
  useSendEmployeeRequestMutation,
  useGetEmployeesRequestQuery,
  useApproveEmployeeRequestMutation,
  useRejectEmployeeRequestMutation,
  useGetAllRequestOfOwnerQuery,
} = apiSlice;
