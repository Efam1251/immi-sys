import React, { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import axios from "axios";

const GenericFormModal = ({
  showModal,
  onClose,
  onAddOption,
  initialData = {},
  fieldsConfig = [],
  field,
  title = "Form",
  url = "",
}) => {
  const [formData, setFormData] = useState({});
  const [newOption, setNewOption] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const location = useLocation();

  useEffect(() => {
    // Load initial data into form
    const initialValues = {};
    fieldsConfig.forEach((field) => {
      initialValues[field.name] = initialData[field.name] || "";
    });
    setFormData(initialValues);
  }, [initialData, fieldsConfig, showModal]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  function validateForm() {
    const errors = [];

    fieldsConfig.forEach((field) => {
      const value = formData[field.name];
      // Skip validation if field is optional (add a `required: false` in config if needed)
      if (field.required === false) return;

      // For checkboxes
      if (field.type === "checkbox" && !value) {
        errors.push(`${field.label} must be checked.`);
      }

      // For inputs, selects, textareas
      if (
        field.type !== "checkbox" &&
        (!value || value.toString().trim() === "")
      ) {
        errors.push(`${field.label} is required.`);
      }
    });

    return errors;
  }

  async function handleSubmit(e) {
    e.preventDefault();

    const validationErrors = validateForm();
    if (validationErrors.length > 0) {
      alert(validationErrors.join("\n")); // Or set a state to show inline errors
      return;
    }
    setLoading(true);
    try {
      // Use provided URL if available; otherwise, construct default API URL
      const apiUrl = url;
      const token = localStorage.getItem("token");
      console.log("Form Data: ", formData);
      // Axios POST request
      const response = await axios.post(apiUrl, formData, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        withCredentials: true, // Include cookies if needed
      });

      if (response.status === 200 || response.status === 201) {
        onAddOption(field, newOption);
        setNewOption("");
        onClose();
      } else {
        setError(response.data.message || "Error adding option on the server.");
      }
    } catch (err) {
      setError("Error adding option from the try. Please try again.");
    } finally {
      setLoading(false);
    }
  }

  if (!showModal) return null;

  return (
    <div className="modal">
      <div className="modal-content max-w-xl p-6 bg-white rounded shadow">
        <span className="close" onClick={onClose}>
          &times;
        </span>
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-xl font-semibold">{title}</h2>
        </div>

        <form onSubmit={handleSubmit} className="space-y-4">
          {fieldsConfig.map((field) => {
            const value = formData[field.name] || "";
            switch (field.type) {
              case "select":
                return (
                  <div key={field.name}>
                    <label className="block font-medium mb-1">
                      {field.label}
                    </label>
                    <select
                      name={field.name}
                      value={value}
                      onChange={handleChange}
                      className="w-full border rounded px-3 py-2"
                    >
                      <option value="">-- Select --</option>
                      {field.options?.map((opt) => (
                        <option key={opt.id} value={opt.id}>
                          {opt.name}
                        </option>
                      ))}
                    </select>
                  </div>
                );
              case "textarea":
                return (
                  <div key={field.name}>
                    <label className="block font-medium mb-1">
                      {field.label}
                    </label>
                    <textarea
                      name={field.name}
                      value={value}
                      onChange={handleChange}
                      className="w-full border rounded px-3 py-2"
                      placeholder={field.placeholder || ""}
                    />
                  </div>
                );
              case "checkbox":
                return (
                  <div key={field.name} className="flex items-center space-x-2">
                    <input
                      type="checkbox"
                      name={field.name}
                      checked={formData[field.name] || false}
                      onChange={handleChange}
                    />
                    <label className="font-medium">{field.label}</label>
                  </div>
                );
              default:
                return (
                  <div key={field.name}>
                    <label className="block font-medium mb-1">
                      {field.label}
                    </label>
                    <input
                      type={field.type || "text"}
                      name={field.name}
                      value={value}
                      onChange={handleChange}
                      placeholder={field.placeholder || ""}
                      className="w-full border rounded px-3 py-2"
                    />
                  </div>
                );
            }
          })}

          {/*} Button container */}
          <div className="button-container">
            <button type="submit" className="modal-btn">
              Save
            </button>
            <button type="button" onClick={onClose} className="modal-btn">
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default GenericFormModal;
