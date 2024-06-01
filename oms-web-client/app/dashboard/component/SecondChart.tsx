import React from "react";
// import Chart from "react-apexcharts";
import { ApexOptions } from "apexcharts";
import dynamic from "next/dynamic";
const Chart = dynamic(() => import("react-apexcharts"), { ssr: false });
const SecondChart = () => {
  const options: ApexOptions = {
    labels: [
      "Đơn nháp",
      "Đang lấy hàng",
      "Đang vận chuyển",
      "Đang giao hàng",
      "Chờ phát lại",
      "Giao thành công",
      "Chờ xử lý",
    ],
    colors: [
      "#A6A6A6",
      "#36A2EB",
      "#FFCE56",
      "#4BC0C0",
      "#FF9F40",
      "#7ED957",
      "#FF6384",
    ],
    legend: {
      position: "bottom",
    },
  };

  const series: number[] = [10, 20, 30, 15, 5, 25, 12];

  return <Chart options={options} series={series} type="donut" />;
};

export default SecondChart;
