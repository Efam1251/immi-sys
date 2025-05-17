import React, { useState, useEffect } from "react";
import Select from 'react-select';
import "../css/PetitionDocument.css";
import { API_ROUTES } from "./config";

const PetitionDependentModal = ({
  showModal,
  closeModal,
  petitionId,
  dependentTypeOptions,
}) => {
  // STATE MANAGEMENT
  const [dependentName, setDependentName] = useState("");
  const [customerList, setCustomerList] = useState([]);
  const [error, setError] = useState("");
  const [dependents, setDependents] = useState([]);

  const [dependentId, setDependent] = useState(null); // Store full customer object
  const [dependentTypeId, setDependentType] = useState(null); // Store full dependent type object
  const [filteredCustomers, setFilteredCustomers] = useState([]);

  // UTILITY FUNCTIONS
  const clearFieldsModal = () => {
    setDependentName("");
    setDependent(null); // Reset the dependent field
    setDependentType(null); // Reset the dependent field
    setFilteredCustomers([]); // Clear filtered customers
  };

  useEffect(() => {
    if (showModal) {
      clearFieldsModal();
      fetchCustomers();
      fetchDependents();
    }
  }, [showModal, petitionId]); // Depend on petitionId to fetch dependents

  // FETCHING DATA
  const fetchCustomers = async () => {
    try {
      const response = await fetch("/api/common/customers/dropdown");
      if (!response.ok) throw new Error("Failed to fetch customers");
      const data = await response.json();
      setCustomerList(data);
    } catch (error) {
      console.error(error);
      setError("Error loading customers.");
    }
  };

  const fetchDependents = async () => {
    try {
      const response = await fetch(
        `${API_ROUTES.PETITION_DEPENDENT_LIST}?petitionId=${petitionId}`
      );
      if (!response.ok) throw new Error("Failed to fetch dependents");
      const data = await response.json();
      setDependents(data);
    } catch (error) {
      console.error(error);
      setError("Error loading dependents.");
    }
  };

  // FORM HANDLING FUNCTIONS
  const handleSearchCustomer = (e) => {
    const inputValue = e.target.value;
    setDependentName(inputValue);

    // Filter customers based on the input value (case insensitive)
    const filtered = customerList.filter((cust) =>
      cust.name.toLowerCase().includes(inputValue.toLowerCase())
    );
    setFilteredCustomers(filtered);

    if (filtered.length === 1) {
      // If there's only one match, automatically select it
      setDependent(filtered[0]);
      setDependentName(filtered[0].name);
    }
  };

  const handleKeyDown = (e) => {
    if (e.key === "Tab" && filteredCustomers.length > 0) {
      // When Tab is pressed, select the first filtered option
      const firstCustomer = filteredCustomers[0];
      setDependent(firstCustomer);
      setDependentName(firstCustomer.name);
    }
  };

  const handleTypeSelection = (e) => {
    const selectedType = dependentTypeOptions.find(
      (type) => type.id.toString() === e.target.value
    );
    if (selectedType) {
      setDependentType(selectedType);
    }
  };

  const handleAddDependent = async () => {
    if (!dependentId || !dependentTypeId) {
      setError("Please select a dependent and dependent type.");
      return;
    }

    const dependentData = {
      petitionId: petitionId,
      dependentId: dependentId.value,
      dependentTypeId: dependentTypeId.id,
    };
    try {
      const response = await fetch("/api/immigration/dependent/save", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(dependentData),
      });

      if (response.ok) {
        fetchDependents(); // Refresh list
        clearFieldsModal();
      } else {
        const errorMsg = await response.text();
        setError(`Failed to save dependent: ${errorMsg}`);
      }
    } catch (error) {
      setError("Error saving dependent.");
    }
  };

  const handleDeleteDependent = async (dependentId) => {
    try {
      const response = await fetch(
        `/api/immigration/dependent/delete/${petitionId}/${dependentId}`,
        {
          method: "DELETE",
        }
      );

      if (!response.ok) {
        throw new Error("Failed to delete dependent");
      }
      console.log("Eliminado exitosamente.");
      // After successful deletion, update the state to remove the deleted dependent from the list
      setDependents(
        dependents.filter((dependent) => dependent.dependentId !== dependentId)
      );
    } catch (error) {
      console.error("Error deleting dependent:", error);
      setError("Error deleting dependent.");
    }
  };

  // Modify your handleCustomerSelection function
  const handleCustomerSelection = (selectedOption) => {
    setDependent(selectedOption); // Save the selected customer object
    setDependentName(selectedOption?.label || ""); // Update the displayed name
  };

  const customStyles = {
    control: (provided) => ({
      ...provided,
      width: '538px', // Set your desired width here
    }),
  };

  // JSX RETURN
  return (
    showModal && (
      <div className="petition-modal">
        <div className="petition-modal-content">
          <span className="petition-close" onClick={closeModal}>
            ×
          </span>
          <h3 className="petition-modal-header">Add Dependent</h3>

          <fieldset>
            <div className="petition-modal-input-group">
              <label>Petition ID:</label>
              <input type="text" value={petitionId} readOnly />
            </div>

            <div className="petition-modal-input-group">
              <label>Dependent:</label>
              <Select
                value={dependentId}
                onChange={handleCustomerSelection}
                options={customerList.map(cust => ({ value: cust.id, label: cust.name}))}
                placeholder="Search customer..."
                isClearable
                styles={customStyles}
              />
            </div>     
            <div className="petition-modal-input-group">
              <label>Dependent Type:</label>
              <select
                value={dependentTypeId?.id || ""}
                onChange={handleTypeSelection}
              >
                <option value="">Select Type</option>
                {dependentTypeOptions.map((type) => (
                  <option key={type.id} value={type.id}>
                    {type.name}
                  </option>
                ))}
              </select>
            </div>

            {error && <p className="error-message">{error}</p>}

            <div className="petition-modal-buttons">
              <button
                className="petition-modal-btn-small"
                onClick={handleAddDependent}
              >
                Add Dependent
              </button>
              <button className="petition-modal-btn-small" onClick={closeModal}>
                Close
              </button>
            </div>
          </fieldset>

          <div className="petition-modal-dependents">
            <h4>Dependents List for Petition {petitionId}:</h4>
            {dependents.length > 0 ? (
              <table className="petition-dependents-table">
                <thead>
                  <tr>
                    <th>Dependent Name</th>
                    <th>Dependent Type</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {dependents.map((dependent) => (
                    <tr key={dependent.dependentId}>
                      <td>{dependent.dependentName}</td>
                      <td>{dependent.dependentTypeName}</td>
                      <td>
                        <button
                          className="delete-btn"
                          onClick={() =>
                            handleDeleteDependent(dependent.dependentId)
                          }
                        >
                          Delete
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            ) : (
              <p>No dependents added yet.</p>
            )}
          </div>
        </div>
      </div>
    )
  );
};

export default PetitionDependentModal;
