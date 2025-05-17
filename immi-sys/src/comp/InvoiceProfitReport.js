import React, { useEffect, useState } from "react";
import * as html2pdf from "html2pdf.js";
import { CSVLink } from "react-csv";
import { API_ROUTES } from "../comp/config";
import "../css/Report.css";

import {
  isoformatDate,
  toLocalISO,
  formatCurrency,
} from "../Utils/commonUtils";

const InvoiceProfitReport = () => {
  const [report, setReport] = useState([]);
  const [filteredReport, setFilteredReport] = useState([]);
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [selectedRange, setSelectedRange] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 10;

  const startIndex = (currentPage - 1) * itemsPerPage;
  const paginatedItems = filteredReport.slice(
    startIndex,
    startIndex + itemsPerPage
  );

  useEffect(() => {
    fetch(API_ROUTES.INCOME_PROFIT_REPORT)
      .then((res) => res.json())
      .then((data) => {
        setReport(data);
        setFilteredReport(data);
      });
  }, []);

  // Effect to apply the date filter whenever startDate or endDate changes
  useEffect(() => {
    filterByDate();
  }, [startDate, endDate, report]);

  // Function to filter the report based on the date range
  const filterByDate = () => {
    if (!startDate || !endDate) return setFilteredReport(report);
    const start = new Date(startDate);
    const end = new Date(endDate);
    const filtered = report.filter((item) => {
      const invoiceDate = new Date(item.invoiceDate);
      return (!start || invoiceDate >= start) && (!end || invoiceDate <= end);
    });
    setFilteredReport(filtered);
  };

  const handleDateRangeChange = (e) => {
    const range = e.target.value;
    setSelectedRange(range);
    let s = "";
    let en = "";

    switch (range) {
      case "thisWeek":
        s = getThisWeekStartDate();
        en = getThisWeekEndDate();
        break;
      case "lastWeek":
        s = getLastWeekStartDate();
        en = getLastWeekEndDate();
        break;
      case "thisMonth":
        s = getThisMonthStartDate();
        en = getThisMonthEndDate();
        break;
      case "lastMonth":
        s = getLastMonthStartDate();
        en = getLastMonthEndDate();
        break;
      case "thisYear":
        s = getThisYearStartDate();
        en = getThisYearEndDate();
        break;
      case "lastYear":
        s = getLastYearStartDate();
        en = getLastYearEndDate();
        break;
      default:
        setStartDate("");
        setEndDate("");
        return;
    }
    setStartDate(s);
    setEndDate(en);
  };

  const getDateFromOffset = (offset, dayOffset = 0) => {
    const today = new Date();
    const date = new Date(today);
    date.setDate(today.getDate() - today.getDay() + dayOffset + offset);
    return toLocalISO(date);
  };

  const getThisWeekStartDate = () => getDateFromOffset(0); // Start of this week
  const getThisWeekEndDate = () => getDateFromOffset(0, 6); // End of this week
  const getLastWeekStartDate = () => getDateFromOffset(-7); // Start of last week
  const getLastWeekEndDate = () => getDateFromOffset(-7, 6); // End of last week

  const getMonthStartDate = (monthOffset) => {
    const today = new Date();
    return toLocalISO(
      new Date(today.getFullYear(), today.getMonth() + monthOffset, 1)
    ); // Start of month (current or offset)
  };

  const getMonthEndDate = (monthOffset) => {
    const today = new Date();
    return toLocalISO(
      new Date(today.getFullYear(), today.getMonth() + monthOffset + 1, 0)
    ); // End of month (current or offset)
  };

  const getThisMonthStartDate = () => getMonthStartDate(0); // Start of this month
  const getThisMonthEndDate = () => getMonthEndDate(0); // End of this month
  const getLastMonthStartDate = () => getMonthStartDate(-1); // Start of last month
  const getLastMonthEndDate = () => getMonthEndDate(-1); // End of last month

  const getYearStartDate = (yearOffset) => {
    const today = new Date();
    return toLocalISO(new Date(today.getFullYear() + yearOffset, 0, 1)); // Start of year (current or offset)
  };

  const getYearEndDate = (yearOffset) => {
    const today = new Date();
    return toLocalISO(new Date(today.getFullYear() + yearOffset, 11, 31)); // End of year (current or offset)
  };

  const getThisYearStartDate = () => getYearStartDate(0); // Start of this year
  const getThisYearEndDate = () => getYearEndDate(0); // End of this year
  const getLastYearStartDate = () => getYearStartDate(-1); // Start of last year
  const getLastYearEndDate = () => getYearEndDate(-1); // End of last year

  const totalIncome = filteredReport.reduce((sum, r) => sum + r.income, 0);
  const totalFees = filteredReport.reduce((sum, r) => sum + r.customerFees, 0);
  const totalPaid = filteredReport.reduce((sum, r) => sum + r.amountPaid, 0);
  const totalOpen = filteredReport.reduce((sum, r) => sum + r.openBalance, 0);
  const totalProfit = filteredReport.reduce((sum, r) => sum + r.profit, 0);

  const handlePrint = () => {
    // Grab title
    const titleHTML = document.querySelector(".report-title")?.outerHTML || "";
  
    // Grab date range display
    const startFmt = isoformatDate(startDate);
    const endFmt = isoformatDate(endDate);
    const rangeHTML = `<p style="text-align:center; margin:8px 0;">
      <strong>From:</strong> ${startFmt} &nbsp;&nbsp; 
      <strong>To:</strong> ${endFmt}
    </p>`;
  
    // Grab table and its footer
    const tableHTML = document.querySelector(".report-table")?.outerHTML || "";
  
    // Inject the footer back into the table
    const fullTable = tableHTML;
  
    // Build print window HTML
    const html = `
      <html>
        <head>
          <title>Invoice Profit Report</title>
          <style>
            body { font-family: sans-serif; margin: 20px; font-size: 1rem; }
            h2 { text-align: center; margin-bottom: 6px; font-size: 1.2rem; }
            p { font-size: 0.9rem; }
            table { width: 100%; border-collapse: collapse; margin-top: 12px; font-size: 0.9rem; table-layout: auto; }
            th, td { border: 1px solid #000; padding: 6px; }
  
            /* Print-specific styles */
            @media print {
              body {
                font-size: 0.8rem; /* Reduce overall font size for printing */
                margin: 10px;
              }
              h2 {
                font-size: 1rem; /* Smaller header */
              }
              p {
                font-size: 0.8rem; /* Smaller paragraph */
              }
              table {
                font-size: 0.8rem; /* Smaller table text */
                table-layout: auto; /* Dynamic column widths based on content */
              }
              th, td {
                padding: 4px; /* Reduce padding for smaller content */
                white-space: nowrap; /* Prevent column text from wrapping */
              }
  
              /* Center the table header */
              th {
                text-align: center;
              }
  
              /* Optional: Center the report title (header) */
              h2 {
                text-align: center;
              }
  
              .text-right { text-align: right; }
              .text-green { color: green; }
              .text-red { color: red; }
            }
          </style>
        </head>
        <body>
          ${titleHTML}
          ${rangeHTML}
          ${fullTable}
        </body>
      </html>`;
  
    // Open, write, and print
    const printWindow = window.open("", "_blank", "width=900,height=700");
    printWindow.document.open();
    printWindow.document.write(html);
    printWindow.document.close();
    printWindow.focus();
  
    setTimeout(() => {
      printWindow.print();
      printWindow.close();
    }, 200);
  };  

  function handleExportPDF() {
    const titleHTML = document.querySelector(".report-title")?.outerHTML || "";
    const element = document.querySelector(".report-table")?.outerHTML || "";
    const rangeHTML = `<p style="text-align:center; margin:8px 0;">
      <strong>From:</strong> ${isoformatDate(startDate)} &nbsp;&nbsp; 
      <strong>To:</strong> ${isoformatDate(endDate)}
    </p>`;
    const pdfContent = titleHTML + rangeHTML + element;

    html2pdf()
      .set({
        margin: [5, 10, 5, 10], // top, left, bottom, right (in mm)
        filename: "invoice-report.pdf",
        image: { type: "jpeg", quality: 0.98 },
        html2canvas: { scale: 2 },
        jsPDF: { unit: "mm", format: "a4", orientation: "portrait" },
      })
      .from(pdfContent)
      .save();
  }

  const csvHeaders = [
    { label: "Invoice #", key: "invoiceNumber" },
    { label: "Customer", key: "customerName" },
    { label: "Date", key: "invoiceDate" },
    { label: "Income", key: "income" },
    { label: "Fees", key: "customerFees" },
    { label: "Profit", key: "profit" },
  ];

  return (
    <div className="report-container">
      <h2 className="report-title">Invoice Profit Report</h2>

      <div className="filter-container">
        <div className="filter-row">
          {/* Select field for date range */}
          <div>
            <label>Select Date Range: </label>
            <select value={selectedRange} onChange={handleDateRangeChange}>
              <option value="">-- List All --</option>
              <option value="thisWeek">This Week</option>
              <option value="lastWeek">Last Week</option>
              <option value="thisMonth">This Month</option>
              <option value="lastMonth">Last Month</option>
              <option value="thisYear">This Year</option>
              <option value="lastYear">Last Year</option>
            </select>
          </div>
          <div className="report-range">
            <label>Start Date: </label>
            <input
              type="date"
              value={startDate}
              onChange={(e) => setStartDate(e.target.value)}
            />
          </div>
          <div>
            <label>End Date: </label>
            <input
              type="date"
              value={endDate}
              onChange={(e) => setEndDate(e.target.value)}
            />
          </div>
          <button
            className="invoice-btn"
            type="button"
            title="Print Report"
            onClick={handlePrint}
          >
            <img
              src="/img/Print-icon.png"
              alt="Print"
              style={{ width: "40px", height: "40px" }}
            />
            <div style={{ fontSize: "12px", marginTop: "4px" }}></div>
          </button>
          <button
            className="invoice-btn"
            type="button"
            title="Print Report"
            onClick={handleExportPDF}
          >
            <img
              src="/img/pdfFile.png"
              alt="Print"
              style={{ width: "40px", height: "40px" }}
            />
            <div style={{ fontSize: "12px", marginTop: "4px" }}></div>
          </button>
          <CSVLink
            data={filteredReport}
            headers={csvHeaders}
            filename="invoice-report.csv"
            className="invoice-btn"
          >
            <img
              src="/img/csvFile.png"
              alt="Print"
              style={{ width: "40px", height: "40px" }}
            />
          </CSVLink>
        </div>
      </div>

      <div className="table-wrapper">
        <table className="report-table">
          <thead>
            <tr>
              <th>Invoice #</th>
              <th>Customer</th>
              <th className="text-right">Date</th>
              <th className="text-right">Invoiced</th>
              <th className="text-right">Paid</th>
              <th className="text-right">Open</th>
              <th className="text-right">Fees</th>
              <th className="text-right">Profit</th>
            </tr>
          </thead>

          <tbody>
            {paginatedItems.map((item, idx) => (
              <tr key={idx}>
                <td>{String(item.invoiceNumber).padStart(6, "0")}</td>
                <td>{item.customerName}</td>
                <td className="text-right">
                  {(() => {
                    const [year, month, day] = item.invoiceDate.split("-");
                    return `${month}/${day}/${year}`;
                  })()}
                </td>
                <td className="text-right">{formatCurrency(item.income)}</td>
                <td className="text-right">
                  {formatCurrency(item.amountPaid)}
                </td>
                <td className="text-right">
                  {formatCurrency(item.openBalance)}
                </td>
                <td className="text-right">
                  {formatCurrency(item.customerFees)}
                </td>
                <td
                  className={`text-right ${
                    item.profit >= 0 ? "text-green" : "text-red"
                  }`}
                >
                  {formatCurrency(item.profit)}
                </td>
              </tr>
            ))}
            {paginatedItems.length === 0 && (
              <tr>
                <td colSpan="8" className="text-center">
                  No data found.
                </td>
              </tr>
            )}
          </tbody>

          <tfoot>
            {filteredReport.length > 0 && (
              <tr className="report-total-row">
                <td></td>
                <td></td>
                <td className="text-right">Totals</td>
                <td className="text-right">{formatCurrency(totalIncome)}</td>
                <td className="text-right">{formatCurrency(totalPaid)}</td>
                <td className="text-right">{formatCurrency(totalOpen)}</td>
                <td className="text-right">{formatCurrency(totalFees)}</td>
                <td className={`text-right ${totalProfit >= 0 ? "text-green" : "text-red"}`}>
                  {formatCurrency(totalProfit)}
                </td>
              </tr>
            )}
          </tfoot>
        </table>
      </div>
    </div>
  );
};

export default InvoiceProfitReport;
