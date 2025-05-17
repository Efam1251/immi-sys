import React from "react";

const InvoicePrint = ({ invoice }) => {
  
  function formatNumber(value, decimals = 2) {
    const num = value !== undefined && value !== null ? Number(value) : NaN;
    return isNaN(num) ? '0.00' : num.toFixed(decimals);
  }  
  
  function formatCurrency(value) {
    return `$${formatNumber(value)}`;
  }
  
  return (
    <div className="invoice-print">
      {/* Invoice Header */}
      <div className="invoice-print-header">
        <h1 className="company-name">Acevedo Consulting, LLC</h1>
        <div className="header-bottom">
          <div className="address">
            <p>52 Summer St.,</p>
            <p>Nashua, NH 03064</p>
          </div>
          <h1 className="invoice-label">INVOICE</h1>
        </div>
      </div>

      {/* Invoice Details */}
      <div className="invoice-print-details">
        <div>
          <p>
            <strong>Invoice No:</strong> #{String(invoice.invoiceNumber).padStart(6, '0')}
          </p>
          <p>
            <strong>Date Issued:</strong> {invoice.invoiceDate}
          </p>
        </div>
        <div className="invoice-print-issued-to">
          <p>
            <strong>Issued to:</strong>
          </p>
          <p>
            {invoice.customer?.firstName} {invoice.customer?.lastName}
          </p>
          <p>{invoice.billingAddress}</p>
          <p>{invoice.customerEmail}</p>
          <p>{invoice.customerPhone}</p>
        </div>
      </div>

      {/* Invoice Table */}
      <table className="invoice-print-table">
        <thead>
          <tr>
            <th>No</th>
            <th>Desc.</th>
            <th>Reference</th>
            <th>Qty</th>
            <th>Price</th>
            <th>Unit</th>
            <th>Disc.</th>
            <th>Subtotal</th>
          </tr>
        </thead>
        <tbody>
          {invoice.invoiceDetails.map((detail, index) => (
            <tr key={index}>
              <td className="invoice-print-left">{index + 1}</td>
              <td className="invoice-print-left">
                {detail.service?.serviceName}
              </td>
              <td className="invoice-print-left">{detail.reference}</td>
              <td className="invoice-print-right">
                {formatNumber(detail.quantity)}
              </td>
              <td className="invoice-print-right">
              {formatCurrency(detail.unitPrice)}
              </td>
              <td className="invoice-print-left">
                {detail.unitOfMeasure?.name}
              </td>
              <td className="invoice-print-right">
                ${detail.discountAmount || 0.0}
              </td>
              <td className="invoice-print-right">
              {formatCurrency(detail.lineTotal)}
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* Grand Total Section */}
      <div className="invoice-print-summary">
        <p>Subtotal: ${invoice.subtotal.toFixed(2)}</p>
        <p>Total Discount: ${invoice.discountAmount.toFixed(2)}</p>
        <p>Tax Amount: ${invoice.taxAmount.toFixed(2)}</p>
        <h3 className="summary-total">Grand Total: ${invoice.totalAmount.toFixed(2)}</h3>
        <h3 className="summary-total">Open Balance: ${invoice.openBalance.toFixed(2)}</h3>
      </div>


      {/* Footer Section - Other Info & Signature */}
      <div className="invoice-footer">
        {/* Other Information */}
        <div className="other-print-info">
          <h3>Other Information</h3>
          <p>{invoice.notes}</p>
        </div>

        {/* Signature Section */}
        <div className="signature-print">
          <p>Ernesto Acevedo</p>
          <p className="finance-manager-print">Finance Manager</p>
        </div>
      </div>
    </div>
  );
};

export default InvoicePrint;
