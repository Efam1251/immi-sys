import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { API_ROUTES } from "../comp/config";
import "../css/InvoiceList.css";
import { isoformatDate } from "../Utils/commonUtils";

function InvoiceList() {
  const [invoices, setInvoices] = useState([]);
  const [searchTerm, setSearchTerm] = useState(""); // Track the search term
  const navigate = useNavigate();

  // Fetch invoice data
  useEffect(() => {
    fetch(API_ROUTES.INVOICE_LIST)
      .then((response) => response.json())
      .then((data) => {
        if (Array.isArray(data)) {
          setInvoices(data);
        } else {
          console.error("Data received is not an array:", data);
          setInvoices([]);
        }
      })
      .catch((error) => {
        console.error("Error fetching invoices:", error);
      });
  }, []);

  const handleRowClick = (id) => {
    navigate(`${API_ROUTES.REDIRECT_TO_INVOICEFORM}?id=${id}`);
  };

  // Filter invoices based on search term
  const filteredInvoices = invoices.filter((invoice) => {
    const lowerSearchTerm = searchTerm.toLowerCase(); // Convert searchTerm to lowercase once
    const customerName = `${invoice.customerFullName ?? ""}`.toLowerCase();

    return (
      customerName.includes(lowerSearchTerm) ||
      invoice.invoiceId?.toString().includes(lowerSearchTerm) ||
      invoice.invoiceNumber?.toString().includes(lowerSearchTerm) ||
      invoice.totalAmount?.toString().includes(lowerSearchTerm) ||
      invoice.status?.toLowerCase().includes(lowerSearchTerm)
    );
  });

  return (
    <div className="invoice-list-container">
      <h1 className="invoice-list-header">Invoice List</h1>

      {/* Search Field */}
      <div className="search-field-container">
        <input
          type="text"
          placeholder="Search by ID, Invoice Number, Customer, Amount, or Status"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="search-field-input"
        />
      </div>

      {/* Table Wrapper to Ensure Consistent Width */}
      <div className="invoice-table-wrapper">
        {/* Header Table */}
        <table className="invoice-list-table">
          <thead>
            <tr>
              <th className="invoice-number">Invoice No.</th>
              <th className="invoice-customer">Customer</th>
              <th className="invoice-date">Date</th>
              <th className="invoice-total">Total</th>
              <th className="invoice-balance">Balance</th>
              <th className="invoice-status">Status</th>
            </tr>
          </thead>
        </table>

        {/* Scrollable Body Table */}
        <div className="invoice-table-container">
          <table className="invoice-list-table">
            <tbody>
              {filteredInvoices.map((invoice) => (
                <tr
                  key={invoice.invoiceId}
                  className="clickable-invoice-row"
                  onClick={() => handleRowClick(invoice.invoiceId)}
                >
                  <td className="invoice-number">
                    {String(invoice.invoiceNumber).padStart(6, "0")}
                  </td>
                  <td className="invoice-customer">
                    {invoice.customerFullName}
                  </td>
                  <td className="invoice-date">
                    {isoformatDate(invoice.invoiceDate)}
                  </td>
                  <td className="invoice-total">
                    {invoice.totalAmount.toFixed(2)}
                  </td>
                  <td className="invoice-balance">
                    {invoice.openBalance.toFixed(2)}
                  </td>
                  <td className="invoice-status">{invoice.status}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      <div className="invoice-total-container">
        <table className="invoice-list-table">
          <thead>
            <tr>
              <th className="total-list-label" style={{ textAlign: "right" }}>
                Total Invoices
              </th>
              <th className="total-list-label" style={{ textAlign: "right" }}>
                Total Invoiced
              </th>
              <th className="total-list-label" style={{ textAlign: "right" }}>
                Open Balance
              </th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td className="invoice-list-total" style={{ textAlign: "right" }}>
                {filteredInvoices.length.toLocaleString("en-US")}
              </td>
              <td className="invoice-list-total" style={{ textAlign: "right" }}>
                $
                {filteredInvoices
                  .reduce((sum, invoice) => sum + invoice.totalAmount, 0)
                  .toLocaleString("en-US", {
                    minimumFractionDigits: 2,
                    maximumFractionDigits: 2,
                  })}
              </td>
              <td className="invoice-list-total" style={{ textAlign: "right" }}>
                $
                {filteredInvoices
                  .reduce((sum, invoice) => sum + invoice.openBalance, 0)
                  .toLocaleString("en-US", {
                    minimumFractionDigits: 2,
                    maximumFractionDigits: 2,
                  })}
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default InvoiceList;
