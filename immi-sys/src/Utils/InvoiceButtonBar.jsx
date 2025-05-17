// components/InvoiceButtonBar.jsx
import React from "react";

const InvoiceButtonBar = ({
  addInvoiceDetail,
  resetFormData,
  handleSubmit,
  handleCancel,
  handlePrint,
  handleGeneratePDF,
  handleAddModal,
}) => {
  return (
      <div className="invoice-button-container">
        
        <div className="invoice-button-group">
          {addInvoiceDetail && (
            <button
              type="button"
              className="invoice-btn"
              title="Add Item"
              onClick={addInvoiceDetail}
            >
              <img
                src="/img/Add-48.png"
                alt="Add Item"
                style={{ width: "40px", height: "40px" }}
              />
              <div style={{ fontSize: "12px", marginTop: "4px" }}>Add Item</div>
            </button>
          )}
        </div>

        <div className="invoice-button-group">
          {resetFormData && (
            <button
              className="invoice-btn"
              type="button"
              title="New Invoice"
              onClick={resetFormData}
            >
              <img
                src="/img/New-Document.png"
                alt="New"
                style={{ width: "40px", height: "40px" }}
              />
              <div style={{ fontSize: "12px", marginTop: "4px" }}>New Inv.</div>
            </button>
          )}

          {handleSubmit && (
            <button
              className="invoice-btn"
              type="submit"
              title="Save Invoice"
              onClick={handleSubmit}
            >
              <img
                src="/img/Save-48.png"
                alt="Submit"
                style={{ width: "40px", height: "40px" }}
              />
              <div style={{ fontSize: "12px", marginTop: "4px" }}>
                Save Inv.
              </div>
            </button>
          )}

          {handleCancel && (
            <button
              className="invoice-btn"
              type="button"
              title="Save Invoice"
              onClick={handleCancel}
            >
              <img
                src="/img/ListItems.png"
                alt="Cancel"
                style={{ width: "40px", height: "40px" }}
              />
              <div style={{ fontSize: "12px", marginTop: "4px" }}>
                List Inv.
              </div>
            </button>
          )}

          {handlePrint && (
            <button
              className="invoice-btn"
              type="button"
              title="Print Invoice"
              onClick={handlePrint}
            >
              <img
                src="/img/Print-icon.png"
                alt="Print"
                style={{ width: "40px", height: "40px" }}
              />
              <div style={{ fontSize: "12px", marginTop: "4px" }}>
                Print Inv.
              </div>
            </button>
          )}

          {handleGeneratePDF && (
            <button
              className="invoice-btn"
              type="button"
              title="Email Invoice"
              onClick={handleGeneratePDF}
            >
              <img
                src="/img/Email-icon.png"
                alt="Email"
                style={{ width: "40px", height: "40px" }}
              />
              <div style={{ fontSize: "12px", marginTop: "4px" }}>
                Email Inv.
              </div>
            </button>
          )}

          {handleAddModal && (
            <button
              className="invoice-btn"
              type="button"
              title="Add Payment"
              onClick={() => handleAddModal("payment")}
            >
              <img
                src="/img/Money-icon.png"
                alt="Payment"
                style={{ width: "40px", height: "40px" }}
              />
              <div style={{ fontSize: "12px", marginTop: "4px" }}>
                Payment
              </div>
            </button>
          )}
        </div>
      </div>
  );
};

export default InvoiceButtonBar;
