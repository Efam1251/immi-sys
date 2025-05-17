import React from "react";
import PropTypes from "prop-types"; // ✅ Added for type checking
import "../css/ButtonGroup.css";

/**
 * ButtonGroup Component
 * 
 * Renders a set of buttons for record navigation and management.
 * 
 * @param {Function} onNewRecord - Callback for creating a new record
 * @param {Function} onSave - Callback for saving a record
 * @param {Function} onCancel - Callback for canceling the current action
 * @param {Function} onFirstRecord - Callback for navigating to the first record
 * @param {Function} onPreviousRecord - Callback for navigating to the previous record
 * @param {Function} onNextRecord - Callback for navigating to the next record
 */
const ButtonGroup = ({ 
  onNewRecord, 
  onSave, 
  onCancel,
  onFirstRecord, 
  onPreviousRecord, 
  onNextRecord 
}) => {
  
  return (
    <div className="button-group-wrapper">
      <div className="btn-group">
        {/* New Record Button */}
        <button 
          type="button" 
          className="btn" 
          onClick={onNewRecord} 
          id="newRecordBtn" 
          title="Nuevo Registro"
          aria-label="Crear nuevo registro"
        >
          <img src="/img/New-48.png" alt="Nuevo Registro" />
        </button>

        {/* Save Button */}
        <button 
          type="button" 
          className="btn" 
          onClick={onSave} 
          id="saveButton" 
          title="Guardar"
          aria-label="Guardar registro"
        >
          <img src="/img/Save-48.png" alt="Guardar" />
        </button>

        {/* Cancel Button */}
        <button 
          type="button" 
          className="btn" 
          onClick={onCancel}
          id="cancelButton"
          title="Cancelar"
          aria-label="Cancelar acción"
        >
          <img src="/img/UndoIcon01.png" alt="Cancelar" />
        </button>

        {/* First Record Button */}
        <button 
          type="button" 
          className="btn" 
          onClick={onFirstRecord} 
          id="firstRecordBtn" 
          title="Primer registro"
          aria-label="Ir al primer registro"
        >
          <img src="/img/Home-btn.png" alt="Primer Registro" />
        </button>

        {/* Previous Record Button */}
        <button 
          type="button" 
          className="btn" 
          onClick={onPreviousRecord} 
          id="previousRecordBtn" 
          title="Anterior"
          aria-label="Ir al registro anterior"
        >
          <img src="/img/Arrow-Left-Blue-48.png" alt="Anterior" />
        </button>

        {/* Next Record Button */}
        <button 
          type="button" 
          className="btn" 
          onClick={onNextRecord} 
          id="nextRecordBtn" 
          title="Siguiente"
          aria-label="Ir al siguiente registro"
        >
          <img src="/img/Arrow-Right-Blue-48.png" alt="Siguiente" />
        </button>
      </div>
    </div>
  );
};

// ✅ Define PropTypes for validation
ButtonGroup.propTypes = {
  onNewRecord: PropTypes.func,
  onSave: PropTypes.func,
  onCancel: PropTypes.func,
  onFirstRecord: PropTypes.func,
  onPreviousRecord: PropTypes.func,
  onNextRecord: PropTypes.func,
};

export default ButtonGroup;
