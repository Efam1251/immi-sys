import React from "react";
import { API_ROUTES } from "../comp/config";
import "../css/ButtonGroup.css";

// Define the button configurations dynamically
const ButtonGroup = ({ navigateRecord, submitForm, copyRecord }) => {
  const buttonConfigs = [
    {
      id: "newRecordBtn",
      title: "Nuevo Registro",
      ariaLabel: "Crear nuevo registro",
      imgSrc: "/img/New-48.png",
      altText: "Nuevo Registro",
      onClick: () => navigateRecord(API_ROUTES.NEW_RECORD),
    },
    {
      id: "saveButton",
      title: "Guardar",
      ariaLabel: "Guardar registro",
      imgSrc: "/img/Save-48.png",
      altText: "Guardar",
      onClick: submitForm,
    },
    {
      id: "cancelButton",
      title: "Cancelar",
      ariaLabel: "Cancelar acción",
      imgSrc: "/img/UndoIcon01.png",
      altText: "Cancelar",
      onClick: () => navigateRecord(API_ROUTES.CURRENT_RECORD),
    },
    {
      id: "copyFieldsButton",
      title: "Copiar",
      ariaLabel: "Copiar Registro",
      imgSrc: "/img/copyRecord.png", // Replace with your actual image path
      altText: "Copiar",
      onClick: copyRecord,
    },    
    {
      id: "firstRecordBtn",
      title: "Primer registro",
      ariaLabel: "Ir al primer registro",
      imgSrc: "/img/Arrow-First.png",
      altText: "Primer Registro",
      onClick: () => navigateRecord(API_ROUTES.FIRST_RECORD),
    },
    {
      id: "previousRecordBtn",
      title: "Anterior",
      ariaLabel: "Ir al registro anterior",
      imgSrc: "/img/Arrow-Backward.png",
      altText: "Anterior",
      onClick: () => navigateRecord(API_ROUTES.PREV_RECORD),
    },
    {
      id: "nextRecordBtn",
      title: "Siguiente",
      ariaLabel: "Ir al siguiente registro",
      imgSrc: "/img/Arrow-Forward.png",
      altText: "Siguiente",
      onClick: () => navigateRecord(API_ROUTES.NEXT_RECORD),
    },
    {
      id: "lastRecordBtn",
      title: "Ultimo",
      ariaLabel: "Ir al ultimo registro",
      imgSrc: "/img/Arrow-Last.png",
      altText: "Ultimo",
      onClick: () => navigateRecord(API_ROUTES.LAST_RECORD),
    },
  ];

  // Render the buttons dynamically
  return (
    <div className="button-group-wrapper">
      <div className="btn-group">
        {buttonConfigs.map((button) => (
          <button
            key={button.id}
            type="button"
            className="btn"
            id={button.id}
            title={button.title}
            aria-label={button.ariaLabel}
            onClick={button.onClick}
          >
            <img src={button.imgSrc} alt={button.altText} />
          </button>
        ))}
      </div>
    </div>
  );
};

export default ButtonGroup;
