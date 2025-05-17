import React from "react";

// ImageButton Component
const ModalFormButton = ({ onClick, imageSrc, altText, label, width = "75px", height = "75px" }) => {
  return (
    <div className="image-button" onClick={onClick} style={{ cursor: "pointer" }}>
      <img
        src={imageSrc}
        alt={altText}
        style={{ width: width, height: height }}
      />
      <span>{label}</span>
    </div>
  );
};

export default ModalFormButton;
