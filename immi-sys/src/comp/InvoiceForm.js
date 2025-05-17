// React core
import React, { useState, useEffect, useMemo } from "react";

// Third-party libraries
import * as html2pdf from "html2pdf.js";
import ReactDOMServer from "react-dom/server";

// FORM FIELD RENDERERS
import * as FormFields from "./FormFields";
import InvoiceButtonBar from "../Utils/InvoiceButtonBar";

// Config and constants
import { API_ROUTES } from "../comp/config";

// Custom components
import PaymentModal from "../comp/PaymentModal";
import MessagePopup from "../comp/MessagePopup";
import InvoicePrint from "../comp/InvoicePrint";
import cssStyles from "../comp/invoicePrintStyles";
import {
  InputField,
  SelectField,
  TextareaField,
  FormSelect,
} from "./FormFields";

// Custom hooks
import { useFormConstants, useFetchDropdownOptions } from "../Utils/formHooks";

// Utilities
import { sqlformatDate, addDaysToDate } from "../Utils/commonUtils";

// Utility functions
import { FIELD_TYPES } from "../Utils/formUtils";

// Styles
import "../css/InvoiceForm.css";

const initialFormData = {
  invoiceId: null,
  invoiceNumber: "",
  invoiceDate: sqlformatDate(new Date()),
  dueDate: sqlformatDate(addDaysToDate(new Date(), 30)),
  billingAddress: "",
  customerEmail: "",
  customerPhone: "",
  customer: null,
  invoiceStatus: "UNPAID",
  invoiceDetails: [],
  subtotal: 0,
  discountAmount: 0,
  taxAmount: 0,
  totalAmount: 0,
  notes: "",
};

const InvoiceForm = () => {
  const {
    navigate,
    searchParams,
    focusFieldRef,
    fileInputRef,
    message,
    setMessage,
    messageType,
    setMessageType,
    isPopupOpen,
    setPopupOpen,
    formData,
    setFormData,
  } = useFormConstants(initialFormData);

  /***** STANDARD CONSTANTS. DO NOT EDIT. *****/
  const [isSaved, setIsSaved] = useState(false); // Indicates if the form was saved

  /**********************************************/
  // Dropdown options
  const [customerOptions, setCustomers] = useState([]); // Customer list
  const [serviceOptions, setServices] = useState([]); // Services list
  const [unitOfMeasureOptions, setUnitOfMeasures] = useState([]); // UOM list
  const [invoiceStatusOptions, setInvoiceStatuses] = useState([]); // Invoice status list

  const [showPaymentModal, setShowPaymentModal] = useState(false); // Payment modal toggle
  const [openBalance, setOpenBalance] = useState(null); // Customer open balance
  const [taxRate, setTaxRate] = useState(0); // Tax rate for invoice
  const [invoiceDetails, setInvoiceDetails] = useState([]); // Invoice line items

  // Using the hook to fetch options for multiple dropdowns
  const dropdownOptions = {
    customer: customerOptions,
    service: serviceOptions,
    unitOfMeasure: unitOfMeasureOptions,
    invoiceStatus: invoiceStatusOptions,
  };

  // Handler: Add new options to helper tables.
  const fieldSetters = {
    customer: setCustomers,
    service: setServices,
    unitOfMeasure: setUnitOfMeasures,
    invoiceStatus: setInvoiceStatuses,
  };

  const dropdownFetchConfig = useMemo(
    () => [
      { url: API_ROUTES.CUSTOMER_ALL, setState: setCustomers },
      { url: API_ROUTES.SERVICE_ALL, setState: setServices },
      { url: API_ROUTES.UNIT_OF_MEASURE_LIST, setState: setUnitOfMeasures },
      { url: API_ROUTES.INVOICE_STATUS_LIST, setState: setInvoiceStatuses },
    ],
    []
  );
  useFetchDropdownOptions(dropdownFetchConfig);

  function handleCancel() {
    // Navigate to the invoice list page
    navigate(API_ROUTES.REDIRECT_TO_INVOICELIST);
  }

  // Reset the form and clear invoice details
  const resetFormData = () => {
    setFormData(initialFormData);
    setInvoiceDetails([]);
    calculateTotals([]);
    setIsSaved(false);
    setTaxRate(0);
    if (focusFieldRef.current) {
      focusFieldRef.current.focus();
    }
  };

  const fetchInvoiceData = async (id) => {
    try {
      const response = await fetch(`${API_ROUTES.INVOICE_FORM}?id=${id}`);

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();

      if (!data?.invoiceId) return;

      setFormData((prev) => ({
        ...prev,
        invoiceId: data.invoiceId,
        invoiceNumber: data.invoiceNumber,
        invoiceDate: data.invoiceDate,
        dueDate: data.dueDate,
        billingAddress: data.billingAddress,
        customerEmail: data.customerEmail,
        customerPhone: data.customerPhone,
        customer: data.customer,
        invoiceStatus: data.invoiceStatus,
        subtotal: data.subtotal,
        discountAmount: data.discountAmount,
        taxAmount: data.taxAmount,
        totalAmount: data.totalAmount,
        notes: data.notes,
      }));

      setInvoiceDetails(data.invoiceDetails || []);

      if (data.taxAmount && data.subtotal) {
        setTaxRate((data.taxAmount / data.subtotal) * 100);
      }

      if (data.invoiceId) {
        fetchOpenBalance(data.invoiceNumber);
      }
    } catch (error) {
      console.error("Error loading invoice data:", error.message);
    }
  };

  // Split out open balance fetch for separation of concerns
  const fetchOpenBalance = async (invoiceNumber) => {
    try {
      const res = await fetch(
        `${API_ROUTES.INVOICE_OPEN_BALANCE}?invoiceNumber=${invoiceNumber}`
      );
      let balance = await res.json();
      setOpenBalance(balance);
    } catch (err) {
      console.error("Error fetching open balance:", err);
    }
  };

  function handleAddModal(modalType) {
    if (modalType === "payment" && isSaved) {
      setShowPaymentModal(true);
    }
  }

  function paymentModalReturn() {
    setShowPaymentModal(false);
    navigate(API_ROUTES.REDIRECT_TO_INVOICELIST);
  }

  function paymentCancelModal() {
    setShowPaymentModal(false);
  }

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    const trimmedValue = value.trim();

    if (name === "taxRate") {
      setTaxRate(
        Number.isNaN(parseFloat(trimmedValue)) ? 0 : parseFloat(trimmedValue)
      );
      return; // No need to continue further
    }

    setFormData((prev) => {
      const updatedData = {
        ...prev,
        [name]: trimmedValue,
      };

      if (name === "invoiceDate" && trimmedValue) {
        const invoiceDate = new Date(trimmedValue);
        if (!Number.isNaN(invoiceDate.getTime())) {
          const dueDate = new Date(invoiceDate);
          dueDate.setDate(dueDate.getDate() + 30);
          updatedData.dueDate = dueDate.toISOString().split("T")[0];
        }
      }

      return updatedData;
    });
  };

  // Extract the invoiceId from the query string
  const invoiceId = searchParams.get("id");

  useEffect(() => {
    const loadInvoice = async () => {
      if (invoiceId) {
        setIsSaved(true);
        await fetchInvoiceData(invoiceId);
      } else {
        resetFormData();
      }
    };

    loadInvoice();
  }, [invoiceId]);

  // Recalculate totals when taxRate changes
  useEffect(() => {
    // Run calculateTotals only when invoiceDetails change
    calculateTotals(invoiceDetails);
  }, [invoiceDetails]);

  const handleInvoiceDateChange = (event) => {
    const newInvoiceDate = event.target.value;
    setFormData({
      ...formData,
      invoiceDate: newInvoiceDate,
      dueDate: sqlformatDate(addDaysToDate(newInvoiceDate, 30)),
    });
  };

  const handleSelectChange = (e) => {
    const { name, value } = e.target;

    if (name === "customer") {
      const selectedCustomer = customerOptions.find(
        (customer) => customer.customerId === parseInt(value)
      );
      if (selectedCustomer) {
        setFormData((prev) => ({
          ...prev,
          customer: selectedCustomer,
          billingAddress: selectedCustomer.address || "",
          customerEmail: selectedCustomer.email || "",
          customerPhone: selectedCustomer.phone || "",
        }));
      }
    } else {
      let updatedData = {};

      if (name === "invoiceStatus") {
        const selectedStatus = invoiceStatusOptions.find(
          (status) => status.name === value
        );
        if (selectedStatus) {
          updatedData.invoiceStatus = selectedStatus.name;
          //console.log("Selected status:", selectedStatus.name);
        }
      } else {
        updatedData[name] = value;
      }

      if (Object.keys(updatedData).length > 0) {
        setFormData((prev) => ({
          ...prev,
          ...updatedData,
        }));
      }
    }
  };

  // Utility function to validate numeric fields
  const isValidNumericField = (value) => {
    const num = parseFloat(value);
    return !isNaN(num) && num >= 0;
  };

  // Find by ID
  const findById = (options, id, key = "id") => {
    return options.find((item) => item[key] === parseInt(id, 10)) || null;
  };

  // Then reuse it:
  const getServiceById = (serviceId) =>
    findById(serviceOptions, serviceId, "serviceId");

  const getUnitOfMeasureById = (unitId) =>
    findById(unitOfMeasureOptions, unitId);

  // Handle invoice detail change
  const handleDetailChange = (index, field, value) => {
    // Prevent empty reference value
    if (field === "reference" && value.trim() === "") {
      return; // Prevent empty reference value
    }

    if (
      (field === "unitPrice" ||
        field === "quantity" ||
        field === "taxRate" ||
        field === "discountAmount") &&
      !isValidNumericField(value)
    ) {
      return; // Invalid value, exit early
    }

    const updatedDetails = [...invoiceDetails];
    const updatedDetail = { ...updatedDetails[index] };

    // Update the specific field
    //updatedDetail[field] = field === "reference" ? value.trim() : value;
    updatedDetail[field] = value;

    // If serviceId is being updated, calculate unitPrice based on selected service
    /*if (field === "service") {
      const selectedService = getServiceById(value);
      updatedDetail.service = selectedService;
      updatedDetail.unitPrice = selectedService ? selectedService.unitPrice : 0;
    }*/
    if (field === "service") {
      updatedDetail.service = value;
      updatedDetail.unitPrice = value?.unitPrice || 0;
    }

    // Update unit of measure if relevant
    if (field === "unitOfMeasure") {
      const selectedUnit = getUnitOfMeasureById(value);
      updatedDetail.unitOfMeasure = selectedUnit;
    }

    // Calculate the line total based on unitPrice, quantity, and discountAmount
    const { unitPrice, quantity, discountAmount } = updatedDetail;
    updatedDetail.lineTotal =
      (unitPrice || 0) * (quantity || 1) - (discountAmount || 0);

    // Update the invoice details and recalculate totals
    updatedDetails[index] = updatedDetail;
    setInvoiceDetails(updatedDetails);
    calculateTotals(updatedDetails);
  };

  // Add new row to invoice details
  const addInvoiceDetail = () => {
    if (formData.customer === null) {
      return;
    }
    setInvoiceDetails([
      ...invoiceDetails,
      {
        serviceId: null,
        reference: "",
        quantity: 1,
        unitPrice: 0,
        discountAmount: 0,
        unitOfMeasureId: null,
        lineTotal: 0,
      },
    ]);
  };

  // Remove row from invoice details
  const removeInvoiceDetail = (index) => {
    const updatedDetails = invoiceDetails.filter((_, i) => i !== index);
    setInvoiceDetails(updatedDetails);
    calculateTotals(updatedDetails);
  };

  // Calculate totals
  const calculateTotals = (details) => {
    const subtotal = details.reduce(
      (sum, detail) =>
        sum + parseFloat(detail.lineTotal + detail.discountAmount || 0),
      0
    );

    const discountAmount = details.reduce(
      (sum, detail) => sum + parseFloat(detail.discountAmount || 0),
      0
    );

    const taxAmount = subtotal * (taxRate / 100); // Example tax calculation, adjust as needed

    //const totalAmount = subtotal + taxAmount - discountAmount;
    const totalAmount = subtotal - discountAmount;

    setFormData((prev) => ({
      ...prev,
      subtotal,
      discountAmount,
      taxAmount,
      totalAmount,
    }));
  };

  // Handle form submission
  const handleSubmit = async (event) => {
    event.preventDefault();

    // Basic validation
    if (!formData.customer || !formData.invoiceDate) {
      setMessageType("error");
      setMessage("Please fill out all required fields (Customer, Date, etc.)"); // Store response message
      setPopupOpen(true); // Show success popup
      return;
    }

    if (invoiceDetails.length === 0) {
      setMessage("Please add at least one item to the invoice."); // Store response message
      setPopupOpen(true); // Show success popup
      return;
    }

    // You can also validate each row if needed
    const hasInvalidDetail = invoiceDetails.some((item) => {
      return !item.service || !item.quantity || item.quantity <= 0;
    });

    if (hasInvalidDetail) {
      setMessage("All invoice items must have a service and a valid quantity."); // Store response message
      setPopupOpen(true); // Show success popup
      return;
    }
    // Prepare final invoice
    const finalInvoice = { ...formData, invoiceDetails, openBalance };

    try {
      const response = await fetch(API_ROUTES.INVOICE_SUBMIT, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(finalInvoice),
      });
      // Check if the response is JSON
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const savedInvoice = await response.json(); // Get updated invoice from backend

      if (!savedInvoice || !savedInvoice.invoiceNumber) {
        setMessage("Failed to save invoice. Please try again.");
        setPopupOpen(true);
        return;
      }

      setMessage(`Invoice #${savedInvoice.invoiceNumber} saved successfully!`);
      setPopupOpen(true);

      setFormData((prev) => ({
        ...prev,
        invoiceNumber: savedInvoice.invoiceNumber,
      }));

      setIsSaved(true);
      navigate(API_ROUTES.REDIRECT_TO_INVOICELIST);
    } catch (error) {
      console.error("Error submitting invoice:", error);
      alert("An error occurred while saving the invoice. Please try again.");
    }
  };

  const handleGeneratePDF = async (e) => {
    e.preventDefault();

    const finalInvoice = { ...formData, invoiceDetails, openBalance };

    if (!isSaved) {
      setMessage("Please save the invoice before emailing."); // Store response message
      setPopupOpen(true); // Show success popup
      return;
    }

    const invoiceHTML = `
      <html>
        <head>
          <title>Print Invoice</title>
          <style>
            ${cssStyles}
            body {
              width: 210mm;
              height: 280mm;
              margin: 0;
              padding: 0;
              font-family: Arial, sans-serif;
              overflow: visible; /* changed */
            }
            #print-container {
              width: 100%;
              padding: 20px;
              box-sizing: border-box;
            }
            * {
              word-break: break-word;
            }
            @page {
              size: 8.5in 11in;
              margin: 0.1in;
            }
          </style>
        </head>
        <body>
          <div id="print-container">
            ${ReactDOMServer.renderToStaticMarkup(
              <InvoicePrint invoice={finalInvoice} />
            )}
          </div>
        </body>
      </html>
    `;

    // Convert HTML to PDF with improved settings
    const pdfBlob = await html2pdf()
      .from(invoiceHTML)
      .set({
        margin: [2, 2, 2, 2],
        filename: "Invoice.pdf",
        image: { type: "jpeg", quality: 0.98 },
        html2canvas: {
          scale: 5,
          useCORS: true,
          backgroundColor: null, // <--- ADD THIS LINE
          logging: false, // <--- OPTIONAL: disables extra logging
        },
        jsPDF: { unit: "mm", format: "letter", orientation: "portrait" },
      })
      .outputPdf("blob");

    // Send the PDF file to the backend
    sendInvoiceToBackend(
      pdfBlob,
      finalInvoice.customerEmail,
      finalInvoice.invoiceNumber
    );
  };

  const sendInvoiceToBackend = (pdfBlob, customerEmail, invoiceNumber) => {
    const formDataPrint = new FormData();
    const fileName = "Invoice-" + invoiceNumber + ".pdf";

    const subject = "Invoice #" + invoiceNumber;
    const message =
      "Dear Customer,\n\nPlease find attached the invoice for your recent service.\n\nBest regards,\nErnesto Acevedo";

    formDataPrint.append("file", pdfBlob, fileName);
    formDataPrint.append("email", customerEmail); // Send the email directly
    formDataPrint.append("subject", subject); // Add the email subject
    formDataPrint.append("message", message); // Add the email message

    fetch(API_ROUTES.INVOICE_EMAIL, {
      method: "POST",
      body: formDataPrint, // Ensure the correct variable name here
    })
      .then((response) => {
        // Check if response is JSON before parsing
        const contentType = response.headers.get("content-type");
        if (contentType && contentType.includes("application/json")) {
          return response.json();
        } else {
          return response.text(); // Handle plain text responses
        }
      })
      .then((data) => {
        setMessage(data); // Store response message
        setPopupOpen(true); // Show success popup
      })
      .catch((error) => console.error("Error:", error));
  };

  async function handlePrint(e) {
    if (!formData.customer || !invoiceDetails.length) {
      setMessage("Complete all fields before printing."); // Store response message
      setPopupOpen(true); // Show success popup
      return; // Prevent printing an empty invoice
    }
    if (!isSaved) {
      const confirmSave = window.confirm(
        "Do you want to save this new invoice?"
      );
      // If user confirms, call handleSubmit
      if (confirmSave) {
        const saved = await handleSubmit(e); // If handleSubmit returns a promise
        if (!saved) return; // optionally check if save was successful
      } else {
        return; // User canceled, do nothing
      }
    } else {
      console.log("Already saved, proceeding to print.");
      //handleSubmit(e);
    }

    // Open a new window for printing
    const printWindow = window.open("", "_blank");
    if (!printWindow) {
      setMessageType("error");
      setMessage("Popup blocked! Please allow popups for this site.");
      setPopupOpen(true);
      return;
    }

    const finalInvoice = { ...formData, invoiceDetails, openBalance };

    // Print content with CSS included inline
    const printContent = `
      <html>
        <head>
          <title>Print Invoice</title>
          <style>
            ${cssStyles}
          </style>
        </head>
        <body>
          <div id="print-container">
            ${ReactDOMServer.renderToStaticMarkup(
              <InvoicePrint invoice={finalInvoice} />
            )}
          </div>
        </body>
      </html>
    `;

    printWindow.document.write(printContent);
    printWindow.document.close();
    printWindow.focus();

    // Delay the print call to ensure the page layout is fully loaded
    setTimeout(() => {
      printWindow.print();
      printWindow.close();
    }, 500); // Adjust delay if needed
  }

  /********************************************/
  const renderField = (field, index) => {
    switch (field.type) {
      case "select":
        return FormFields.renderSelectField(field, index);
      case "select_react":
        return FormFields.renderReactSelectField(
          field,
          index,
          handleInputChange,
          setFormData
        );
      case "checkbox":
        return FormFields.renderCheckboxField(
          field,
          index,
          handleInputChange,
          setFormData
        );
      case "textarea":
        return FormFields.renderTextareaField(
          field,
          index,
          handleInputChange,
          setFormData
        );
      case "file":
        return FormFields.renderFileField(
          field,
          index,
          fileInputRef,
          setFormData
        );
      default:
        return FormFields.renderInputField(
          field,
          index,
          handleInputChange,
          setFormData
        );
    }
  };

  const customerOptionsFormatted = customerOptions.map((customer) => ({
    value: customer.customerId,
    label: `${customer.firstName} ${customer.lastName}`,
    customer, // keep original
  }));

  const serviceOptionsFormatted = serviceOptions.map((service) => ({
    value: service.serviceId,
    label: service.serviceName,
    service, // Include full object for easy reference
  }));

  // Find the selected option based on current formData
  const selectedCustomer = customerOptionsFormatted.find(
    (option) => option.value === formData.customer?.customerId
  );

  const handleReactSelectChange = (e) => {
    setFormData((prev) => ({
      ...prev,
      customer: e?.customer || null,
      billingAddress: e?.customer.address || "",
      customerEmail: e?.customer.email || "",
      customerPhone: e?.customer.phone || "",
    }));
  };

  const customerField = {
    type: "select_react",
    name: "customer",
    label: "Customer",
    value: selectedCustomer || null,
    options: customerOptionsFormatted,
    onChange: handleReactSelectChange,
  };

  return (
    <div className="invoice-container">
      <form onSubmit={handleSubmit}>
        <div className="invoice-header">
          <h1>Invoice Form</h1>
        </div>

        <InvoiceButtonBar
          resetFormData={resetFormData}
          handleSubmit={handleSubmit}
          handleCancel={handleCancel}
          handlePrint={handlePrint}
          handleGeneratePDF={handleGeneratePDF}
          handleAddModal={handleAddModal}
        />

        {/* Hidden Invoice ID Field */}
        <input
          type="hidden"
          name="invoiceId"
          value={formData.invoiceId || ""}
        />

        <div className="invoice-details">
          <InputField
            label="Invoice Number"
            name="invoiceNumber"
            value={String(formData.invoiceNumber).padStart(6, "0")}
            onChange={handleInputChange}
            readOnly
          />
          <div className="invoice-details">
            <InputField
              label="Invoice Date"
              ref={focusFieldRef}
              name="invoiceDate"
              value={formData.invoiceDate}
              onChange={handleInvoiceDateChange}
              type={FIELD_TYPES.DATE}
              required
            />
            <InputField
              label="Due Date"
              name="dueDate"
              value={formData.dueDate}
              onChange={handleInvoiceDateChange}
              type={FIELD_TYPES.DATE}
              required
            />
          </div>

          {/* Client Selection */}
          <FormSelect
            label="Customer"
            id="customerId"
            name="customer"
            options={customerOptionsFormatted}
            value={selectedCustomer || null}
            onChange={handleReactSelectChange}
          />
          <TextareaField
            label="Billing Address"
            id="billingAddress"
            value={formData.billingAddress}
            onChange={handleInputChange}
            required
          />
          <div className="invoice-details">
            <InputField
              label="Email"
              name="customerEmail"
              value={formData.customerEmail}
              onChange={handleInputChange}
              type={FIELD_TYPES.EMAIL}
              required
            />

            <InputField
              label="Phone"
              name="customerPhone"
              value={formData.customerPhone}
              onChange={handleInputChange}
              required
            />
          </div>
          <div className="invoice-details">
            <SelectField
              label="Status"
              id="invoiceStatus"
              idField="value"
              name="invoiceStatus"
              value={formData.invoiceStatus || ""}
              options={invoiceStatusOptions}
              onChange={handleSelectChange}
              showButton={false}
              required
              disabled={true}
            />
            <InputField
              label="Tax Rate"
              type={FIELD_TYPES.NUMBER}
              name="taxRate"
              value={taxRate}
              onChange={handleInputChange}
            />
          </div>
        </div>

        {/* Invoice Buttons Bar */}
        <InvoiceButtonBar addInvoiceDetail={addInvoiceDetail} />

        {/* Invoice Details Table */}
        <table className="invoice-table">
          <thead>
            <tr>
              <th>Service</th>
              <th>Reference</th>
              <th>Qty</th>
              <th>Price</th>
              <th>Unit</th>
              <th>Discount</th>
              <th>Total</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {invoiceDetails.map((detail, index) => (
              <tr key={index}>
                <td>
                  <FormSelect
                    id={`service-${index}`}
                    name={`service-${index}`}
                    options={serviceOptionsFormatted}
                    value={
                      serviceOptionsFormatted.find(
                        (option) => option.value === detail.service?.serviceId
                      ) || null
                    }
                    onChange={(selectedOption) =>
                      handleDetailChange(
                        index,
                        "service",
                        selectedOption?.service || null
                      )
                    }
                  />
                </td>
                <td>
                  <InputField
                    name={`reference-${index}`}
                    value={detail.reference}
                    onChange={(e) =>
                      handleDetailChange(index, "reference", e.target.value)
                    }
                    requiered
                  />
                </td>
                <td>
                  <InputField
                    type="number"
                    name={`quantity-${index}`}
                    value={detail.quantity}
                    onChange={(e) =>
                      handleDetailChange(
                        index,
                        "quantity",
                        parseFloat(e.target.value)
                      )
                    }
                  />
                </td>
                <td>
                  <InputField
                    type="number"
                    name={`unitPrice-${index}`}
                    value={detail.unitPrice}
                    onChange={(e) =>
                      handleDetailChange(
                        index,
                        "unitPrice",
                        parseFloat(e.target.value)
                      )
                    }
                  />
                </td>
                <td>
                  <SelectField
                    id={`unitOfMeasure-${index}`}
                    name={`unitOfMeasure-${index}`}
                    value={detail.unitOfMeasure?.id || ""}
                    options={unitOfMeasureOptions}
                    onChange={(e) =>
                      handleDetailChange(index, "unitOfMeasure", e.target.value)
                    }
                    showButton={false}
                    required
                  />
                </td>
                <td>
                  <InputField
                    type="number"
                    name={`discountAmount-${index}`}
                    value={detail.discountAmount}
                    onChange={(e) =>
                      handleDetailChange(
                        index,
                        "discountAmount",
                        parseFloat(e.target.value)
                      )
                    }
                  />
                </td>
                <td>${detail.lineTotal.toFixed(2)}</td>
                <td>
                  <button
                    className="btn"
                    type="button"
                    onClick={() => removeInvoiceDetail(index)}
                  >
                    <img
                      src="/img/Cancel-48.png"
                      alt="Remove"
                      className="invoice-btn__icon"
                    />
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        <br />

        <div className="invoice-footer">
          <textarea
            className="invoice-notes"
            name="notes"
            value={formData.notes}
            onChange={handleInputChange}
            placeholder="Enter additional notes..."
          />

          <div className="invoice-totals">
            <div>
              <span className="invoice-label">Subtotal:</span>
              <span>{`$${formData.subtotal.toFixed(2)}`}</span>
            </div>
            <div>
              <span className="invoice-label">Discount:</span>
              <span>{`$${formData.discountAmount.toFixed(2)}`}</span>
            </div>
            <div>
              <span className="invoice-label">Tax:</span>
              <span>{`$${formData.taxAmount.toFixed(2)}`}</span>
            </div>
            <div className="total-row">
              <span className="invoice-label">Total:</span>
              <span>{`$${formData.totalAmount.toFixed(2)}`}</span>
            </div>
          </div>
        </div>

        <br />
      </form>
      {/* Modals for adding new options */}
      <PaymentModal
        showModal={showPaymentModal}
        closeModal={paymentModalReturn}
        cancelModal={paymentCancelModal}
        invoiceNumber={formData.invoiceNumber}
        invoiceDate={formData.invoiceDate}
        field="payment"
      />
      <MessagePopup
        message={message}
        isPopupOpen={isPopupOpen}
        setPopupOpen={setPopupOpen}
      />
    </div>
  );
};

export default InvoiceForm;
