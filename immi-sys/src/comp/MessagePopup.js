import React, { useEffect } from "react";
import Popup from "reactjs-popup";
import "reactjs-popup/dist/index.css";

const popupTypes = {
  success: { color: "#4CAF50", icon: "✅", title: "Success!" },
  error: { color: "#E74C3C", icon: "❌", title: "Error!" },
  warning: { color: "#F39C12", icon: "⚠️", title: "Warning!" },
  info: { color: "#3498DB", icon: "ℹ️", title: "Information" },
};

const MessagePopup = ({
  message,
  isPopupOpen,
  setPopupOpen,
  type = "success",
  autoCloseTime = 0,
  primaryButtonText = "Close",
  primaryAction = () => setPopupOpen(false),
  secondaryButtonText = null,
  secondaryAction = null,
  animationType = "fade", // Options: fade, slide, bounce
}) => {
  useEffect(() => {
    if (autoCloseTime > 0) {
      const timer = setTimeout(() => setPopupOpen(false), autoCloseTime);
      return () => clearTimeout(timer);
    }
  }, [autoCloseTime, setPopupOpen]);

  const { color, icon, title } = popupTypes[type] || popupTypes.success;
  const animations = {
    fade: "opacity 0.5s ease-in-out",
    slide: "transform 0.5s ease-in-out",
    bounce: "transform 0.5s cubic-bezier(.25,.1,.25,1.3)",
  };

  return (
    <Popup 
      open={isPopupOpen} 
      onClose={() => setPopupOpen(false)} 
      modal
      closeOnDocumentClick={false}
      contentStyle={{
        width: "80%",
        maxWidth: "480px",
        padding: "30px",
        backgroundColor: "white",
        borderRadius: "18px",
        textAlign: "center",
        boxShadow: "0 20px 50px rgba(0, 0, 0, 0.15)",
        border: "none",
        transition: animations[animationType],
      }}
      overlayStyle={{
        background: "rgba(0, 0, 0, 0.7)",
        backdropFilter: "blur(8px)",
      }}
    >
      <div style={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
        <h2 style={{ fontSize: "26px", fontWeight: "600", marginBottom: "16px", color }}>
          {icon} {title}
        </h2>

        <p style={{ fontSize: "18px", marginBottom: "30px", color: "#555", lineHeight: "1.6", maxWidth: "400px" }}>
          {message}
        </p>

        <div style={{ display: "flex", gap: "12px" }}>
          {secondaryButtonText && secondaryAction && (
            <button
              onClick={secondaryAction}
              style={{
                padding: "12px 28px",
                background: "#E74C3C",
                color: "white",
                border: "none",
                borderRadius: "8px",
                fontSize: "16px",
                cursor: "pointer",
              }}
            >
              {secondaryButtonText}
            </button>
          )}

          <button
            onClick={primaryAction}
            style={{
              padding: "12px 28px",
              background: color,
              color: "white",
              border: "none",
              borderRadius: "8px",
              fontSize: "16px",
              cursor: "pointer",
            }}
          >
            {primaryButtonText}
          </button>
        </div>
      </div>
    </Popup>
  );
};

export default MessagePopup;
