// Your CSS as a string
const cssStyles = `
.invoice-print {
  font-family: "Arial", sans-serif;
  width: 100%; /* Full width to avoid issues with content being pushed */
  max-width: 800px; /* Set a max-width for better fit */
  margin: 0 auto; /* Center the content */
  padding: 40px;
  background: #ffffff;
  border-radius: 8px;
  box-shadow: 0px 5px 15px rgba(0, 0, 0, 0.1);
  color: #333;
}

.invoice-print-header {
  display: flex;
  flex-direction: column;
  text-align: left;
  border-bottom: 2px solid #ddd;
  padding-bottom: 10px; /* Reduce space at the bottom */
  margin-bottom: 10px;
}

.company-name {
  text-align: left;
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 5px; /* Reduce space between company name and address */
}

.header-bottom {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
}

.address {
  font-size: 14px;
  line-height: 1.2; /* Reduce line spacing between address lines */
}

.invoice-label {
  font-size: 28px;
  font-weight: bold;
}

.invoice-print-details {
  display: flex;
  justify-content: space-between; /* Distribute content to left and right */
  margin-bottom: 10px;
  font-size: 14px;
}

.invoice-print-details p {
  margin: 5px 0;
}

.invoice-print-table .invoice-print-left {
  text-align: left; /* Align content to the left */
}

.invoice-print-table .invoice-print-right {
  text-align: right; /* Align content to the right */
}

.invoice-print-issued-to {
  text-align: right;
}

.invoice-print-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 15px;
}

.invoice-print-table th, .invoice-print-table td {
  padding: 5px;
  border: 1px solid #ddd;
  text-align: left;
  font-size: 14px;
}

.invoice-print-table th {
  background: lightblue !important;
  color: #333 !important;
  font-weight: bold;
  text-align: center;
  -webkit-print-color-adjust: exact; /* Ensures color appears in print */
  print-color-adjust: exact; /* For better browser support */
}

.invoice-print-summary {
  text-align: right;
  font-size: 14px;
  margin-top: 20px;
}

.invoice-print-summary p {
  text-align: right;
  font-size: 14px;
  margin: 0px;
}

.invoice-print-summary .summary-total {
  text-align: right;
  font-size: 18px;
  font-weight: bold;
}

.invoice-footer {
  display: flex;
  justify-content: space-between; /* Ensures one is on the left, one on the right */
  align-items: flex-start; /* Aligns them to the top */
  margin-top: 20px;
}

.other-print-info {
  width: 50%;
  white-space: pre-wrap;
  border: 1px solid #e0e0e0;
  border-radius: 10px;
  padding: 10px;
}

.signature-print {
  width: 50%; /* Takes half of the space */
  margin: 0;
  text-align: right; /* Aligns text to the right */
}

/* Adjust the page margins for printing */
@media print {
  body {
    margin: 0; /* Remove any margin from body */
    padding: 0;
  }

  body * {
    visibility: hidden; /* Hide everything except for the print container */
  }

  #print-container, #print-container * {
    visibility: visible;
  }

  #print-container {
    width: 100%;
    padding: 0px; /* Make sure there's some space around content */
    margin: 0 auto;
  }

  /* Page Margins for Print */
  @page {
    margin: 0mm;
  }

  .invoice-print {
    box-shadow: none;
    border-radius: 0;
    width: 100%;
  }
}
`;

export default cssStyles;