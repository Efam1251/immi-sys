import React, { useState, useEffect } from "react";
import "../css/Modal.css";
import { API_ROUTES } from "../comp/config";
import MessagePopup from "./MessagePopup";
import { isoformatDate } from "../Utils/commonUtils";

function PaymentModal({
  showModal,
  closeModal,
  cancelModal,
  invoiceNumber,
  invoiceDate,
  mode = "payment",
}) {
  const [paymentDate, setPaymentDate] = useState("");
  const [paymentMethod, setPaymentMethod] = useState("");
  const [paymentMethods, setPaymentMethods] = useState([]);
  const [amount, setAmount] = useState("");
  const [openBalance, setOpenBalance] = useState(null);
  const [notes, setNotes] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");
  const [popupOpen, setPopupOpen] = useState(false);
  const [isPopupOpen, setIsPopupOpen] = useState(false);

  const [receiptData, setReceiptData] = useState(null);
  const [showReceipt, setShowReceipt] = useState(false);

  const [payments, setPayments] = useState([]);
  const [selectedPaymentId, setSelectedPaymentId] = useState(null);

  useEffect(() => {
    if (showModal) {
      const currentDate = new Date().toISOString().split("T")[0];
      setPaymentDate(currentDate);
      setPaymentMethod("");
      setAmount("");
      setNotes("");
      setError("");

      fetch(`${API_ROUTES.INVOICE_OPEN_BALANCE}?invoiceNumber=${invoiceNumber}`)
        .then((res) => res.json())
        .then(setOpenBalance)
        .catch(() => setError("Failed to fetch open balance."));

      fetch(API_ROUTES.PAYMENT_METHOD_LIST)
        .then((res) => res.json())
        .then(setPaymentMethods)
        .catch(() => setError("Failed to fetch payment methods."));

      fetch(`${API_ROUTES.PAYMENTS_BY_INVOICE}?invoiceNumber=${invoiceNumber}`)
        .then((res) => res.json())
        .then(setPayments)
        .catch(() => setError("Failed to load payments."));
    }
  }, [showModal, invoiceNumber, mode]);

  async function handleAddPayment() {
    if (new Date(paymentDate) < new Date(invoiceDate)) {
      setError("Payment date cannot be earlier than invoice date.");
      return;
    }

    if (paymentDate && paymentMethod && amount > 0) {
      setLoading(true);
      setError("");

      const paymentdata = {
        invoiceNumber,
        paymentDate,
        paymentMethod,
        amount,
        notes,
      };

      try {
        const response = await fetch(API_ROUTES.PAYMENT_SAVE, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(paymentdata),
        });

        const data = await response.json();
        if (response.ok) {
          const receipt = {
            invoiceNumber,
            paymentDate,
            paymentMethod: paymentMethod.name,
            amount,
            notes,
          };
          setReceiptData(receipt);
          setShowReceipt(true);
          setTimeout(() => {
            window.print();
            setShowReceipt(false);
            closeModal();
            setMessage(data.message || "Payment saved successfully.");
            setPopupOpen(true);
          }, 500);
        } else {
          setError(data.message || "Error saving payment.");
        }
      } catch {
        setError("Error saving payment.");
      } finally {
        setLoading(false);
      }
    } else {
      setError("Please fill in all fields correctly.");
    }
  }

  async function handleRefund() {
    if (!selectedPaymentId || !amount || amount <= 0) {
      setError("Select a payment and valid amount.");
      return;
    }

    setLoading(true);
    const refundData = {
      invoiceNumber,
      originalPaymentId: selectedPaymentId,
      refundDate: paymentDate,
      refundReason: notes,
      refundAmount: amount,
    };

    try {
      const response = await fetch(API_ROUTES.PAYMENT_REFUND, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(refundData),
      });
      const data = await response.json();
      if (response.ok) {
        setReceiptData(refundData);
        setShowReceipt(true);
        setTimeout(() => {
          window.print();
          setShowReceipt(false);
          closeModal();
          setMessage(data.message || "Refund processed.");
          setPopupOpen(true);
        }, 500);
      } else {
        setError(data.message || "Refund failed.");
      }
    } catch {
      setError("Refund failed.");
    } finally {
      setLoading(false);
    }
  }

  const handleCancelPayment = async (paymentId) => {
    const confirmed = window.confirm(
      `Are you sure you want to cancel payment #${paymentId}?`
    );
    if (!confirmed) return;
  
    // Find the payment to cancel
    const paymentToCancel = payments.find((p) => p.id === paymentId);
    if (!paymentToCancel) {
      alert("Payment not found.");
      return;
    }
  
    try {
      const response = await fetch(
        `/api/common/payment/payments/${paymentId}`,
        {
          method: "DELETE",
        }
      );
  
      if (!response.ok) {
        throw new Error("Failed to cancel payment.");
      }
  
      alert(`Payment #${paymentId} has been cancelled.`);
  
      // Remove cancelled payment from UI
      setPayments((prev) => prev.filter((p) => p.id !== paymentId));
  
      // Update the open balance
      setOpenBalance((prev) => prev + paymentToCancel.amount);
    } catch (error) {
      console.error(error);
      alert("Error cancelling payment. Please try again.");
    }
  };
  

  return (
    showModal && (
      <div className="modal">
        <div className="modal-content">
          <span className="close" onClick={cancelModal}>
            {" "}
            ×{" "}
          </span>
          <h2>Apply Payment to Invoice #{String(invoiceNumber).padStart(6, "0")}</h2>
          <div>
            {invoiceDate !== null && (
              <span style={{ marginRight: "40px" }}>
                <strong>Invoice Date:</strong> {isoformatDate(invoiceDate)}
              </span>
            )}
            {openBalance !== null && (
              <span>
                <strong>Open Balance:</strong> {openBalance}
              </span>
            )}
          </div>
          <div className="payment-form-row">
            <input
              type="date"
              value={paymentDate}
              onChange={(e) => setPaymentDate(e.target.value)}
              disabled={loading}
            />

            <select
              value={paymentMethod?.id || ""}
              onChange={(e) => {
                const method = paymentMethods.find(
                  (m) => m.id === parseInt(e.target.value)
                );
                setPaymentMethod(method);
              }}
              disabled={loading}
            >
              <option value="">Select Method</option>
              {paymentMethods.map((method) => (
                <option key={method.id} value={method.id}>
                  {method.name}
                </option>
              ))}
            </select>

            <input
              type="number"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
              placeholder="Amount"
              disabled={loading}
            />
          </div>

          <textarea
            value={notes}
            onChange={(e) => setNotes(e.target.value)}
            placeholder="Notes"
            disabled={loading}
            rows={3}
          />

          {payments.length > 0 && (
            <div className="previous-payments">
              <h4>Previous Payments</h4>
              <div className="payment-list-scroll">
                <ul>
                  {payments.map((p) => (
                    <li key={p.id}>
                      <div className="payment-details">
                        <span className="payment-id">#{p.id}</span>
                        <span className="payment-amount">
                          ${parseFloat(p.amount).toFixed(2)}
                        </span>
                        <span className="payment-date">
                          {isoformatDate(p.paymentDate)}
                        </span>
                        <button
                          className="cancel-btn"
                          onClick={() => handleCancelPayment(p.id)}
                        >
                          Cancel
                        </button>
                      </div>
                    </li>
                  ))}
                </ul>
              </div>
            </div>
          )}
          {error && <p className="error-message">{error}</p>}

          <div className="button-container">
            <button
              className="modal-btn"
              onClick={handleAddPayment}
              disabled={loading}
            >
              {loading ? "Processing..." : "Apply Payment"}
            </button>
            <button
              className="modal-btn"
              onClick={cancelModal}
              disabled={loading}
            >
              Cancel
            </button>
          </div>
        </div>

        <MessagePopup
          message={message}
          isPopupOpen={isPopupOpen}
          setPopupOpen={setPopupOpen}
        />

        {showReceipt && receiptData && (
          <div className="print-receipt">
            <div className="receipt-container">
              <div className="receipt-header">
                <h1>Acevedo Consulting, LLC</h1>
                <p>52 Summer Street, Nashua, NH 03064</p>
                <p>Phone: (603) 820-3041 | Email: eacevedo1251@gmail.com</p>
                <hr />
              </div>
              <div className="receipt-title">
                <h2>Payment Receipt</h2>
              </div>
              <div className="receipt-info">
                <div>
                  <strong>Invoice Number:</strong> #
                  {String(receiptData.invoiceNumber).padStart(6, "0")}
                </div>
                <div>
                  <strong>Date:</strong> {isoformatDate(receiptData.paymentDate)}
                </div>
                <div>
                  <strong>Payment Method:</strong> {receiptData.paymentMethod}
                </div>
                <div>
                  <strong>Amount Paid:</strong> $
                  {parseFloat(receiptData.amount).toFixed(2)}
                </div>
                {receiptData.notes && (
                  <div>
                    <strong>Notes:</strong> {receiptData.notes}
                  </div>
                )}
              </div>
              <div className="receipt-footer">
                <hr />
                <p>Thank you for your payment!</p>
              </div>
              <div style={{ marginTop: "50px" }}>
                <p>______________________________</p>
                <p style={{ fontSize: "13px" }}>Received by</p>
              </div>
            </div>
          </div>
        )}
      </div>
    )
  );
}

export default PaymentModal;
