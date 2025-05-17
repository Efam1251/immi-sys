import React, { forwardRef } from "react";
import Select from "react-select";
import { isoformatDate } from "../Utils/commonUtils";

// RENDER SELECTFIELD.
export const renderSelectField = (field, index) => (
  <SelectField
    key={index}
    label={field.label}
    id={field.id}
    name={field.name}
    value={field.value}
    options={field.options}
    onChange={field.onChange}
    openModal={field.openModal}
    idField={field.idField || "id"} // <-- default to 'id'
    displayFields={field.displayFields || ["name"]} // <-- default display
    disabled={field.disabled || false} // <-- default to false
    className={field.className || "form-group"} // <-- default to 'form-group'
  />
);
export const SelectField = ({
  label,
  id,
  name,
  value,
  options,
  onChange,
  openModal,
  idField = "id",
  displayFields = ["name"],
  disabled = false,
  className,
}) => {
  return (
    <div className={className}>
      <label htmlFor={id}>{label}</label>
      <div className="select-group">
        <select
          id={id}
          name={name}
          value={value}
          onChange={onChange}
          disabled={disabled}
        >
          <option value="" disabled>
            Select {label}
          </option>
          {Array.isArray(options) && options.length > 0 ? (
            options.map((option, index) =>
              typeof option === "string" ? (
                <option key={index} value={option}>
                  {option}
                </option>
              ) : (
                <option key={option[idField] || index} value={option[idField]}>
                  {displayFields.map((field) => option[field] || "").join(" ")}
                </option>
              )
            )
          ) : (
            <option>No options available</option>
          )}
        </select>
        {openModal && (
          <button
            type="button"
            className="add-button"
            onClick={() => {
              openModal(); // <- this must be a function
            }}
          >
            <img
              src="/img/Add-48.png"
              alt="new"
              style={{ width: "25px", height: "25px" }}
            />
          </button>
        )}
      </div>
    </div>
  );
};

// RENDER REACT-SELECT FIELD.
export const renderReactSelectField = (field, index) => (
  <FormSelect
    key={index}
    label={field.label}
    id={field.id}
    name={field.name}
    value={field.value}
    options={field.options}
    onChange={field.onChange}
    openModal={field.openModal}
    className={field.className || "form-group"} // <-- default to 'form-group'
  />
);
export function FormSelect({
  label,
  id,
  name,
  options,
  value,
  onChange,
  className,
}) {
  const customStyles = {
    /*control: (provided) => ({
      ...provided,
      fontFamily: "'Times New Roman', monospace", // Change this to any font you want
    }),
    option: (provided, state) => ({
      ...provided,
      fontFamily: "'Times New Roman', monospace",
    }),
    singleValue: (provided) => ({
      ...provided,
      fontFamily: "'Times New Roman', monospace",
    }),*/
  };
  return (
    <div className={className}>
      <label htmlFor={id}>{label}</label>
      <Select
        inputId={id}
        name={name}
        options={options}
        value={value}
        onChange={onChange}
        isSearchable
        isClearable={true}
        styles={customStyles}
      />
    </div>
  );
}

// RENDER CHECKBOX.
export const renderCheckboxField = (
  field,
  index,
  handleInputChange,
  setFormData
) => (
  <CheckboxField
    key={index}
    id={field.id}
    name={field.name}
    label={field.label}
    checked={field.checked || false}
    onChange={(e) =>
      handleInputChange(setFormData)({
        target: {
          name: field.name,
          value: e.target.checked,
        },
      })
    }
    required={field.required || false}
    disabled={field.disabled || false}
  />
);

/**
 * Reusable Checkbox Field Component
 */
export const CheckboxField = forwardRef(
  (
    {
      label,
      id,
      name,
      checked,
      onChange,
      onBlur,
      required = false,
      disabled = false,
    },
    ref
  ) => (
    <div className="form-group">
      {/* Only render the label if the label is provided */}
      {label && (
        <label htmlFor={id} className={required ? "required-label" : ""}>
          {label}
        </label>
      )}
      <input
        ref={ref} // ✅ Attach ref to input
        type="checkbox"
        id={id}
        name={name}
        checked={checked} // Whether checkbox is checked
        onChange={onChange} // Handle checkbox change
        onBlur={onBlur} // Handle blur event
        required={required} // Make it a required field
        disabled={disabled} // Optionally disable the checkbox
      />
    </div>
  )
);

// RENDER TEXTAREA FIELD.
export const renderTextareaField = (
  field,
  index,
  handleInputChange,
  setFormData
) => {
  const value = field.value || "";
  const isAddress =
    (field.id && field.id.toLowerCase().includes("address")) ||
    (field.name && field.name.toLowerCase().includes("address"));

  const showMapLink = isAddress && value.trim().length > 10;

  return (
    <div key={index} className="textarea-container">
      {showMapLink ? (
        <label htmlFor={field.id}>
          <a
            href={`https://www.google.com/maps/search/?api=1&query=${encodeURIComponent(
              value
            )}`}
            target="_blank"
            rel="noopener noreferrer"
            className="map-label-link"
          >
            {field.label} 📍
          </a>
        </label>
      ) : (
        <label htmlFor={field.id}>{field.label}</label>
      )}

      <textarea
        id={field.id}
        name={field.name}
        value={value}
        onChange={handleInputChange(setFormData)}
        placeholder={field.placeholder}
        required={field.required}
        readOnly={field.readOnly}
      />
    </div>
  );
};

export const TextareaField = ({
  label,
  id,
  name,
  value,
  onChange,
  required = false,
  rows = 3,
}) => (
  <div className="form-group-textArea">
    <label htmlFor={id} className={required ? "required-label" : ""}>
      {label}
    </label>
    <textarea
      id={id}
      name={name}
      value={value}
      onChange={onChange}
      rows={rows}
      required={required}
    />
  </div>
);

// RENDER RADIO FIELD.
export const renderRadioField = (field, index) => (
  <FormRadioGroup
    key={index}
    label={field.label}
    id={field.id}
    name={field.name}
    options={field.options}
    value={field.value}
    onChange={field.onChange}
  />
);

export function FormRadioGroup({ label, id, name, options, value, onChange }) {
  return (
    <div className="form-group">
      <label className="form-label" style={{ fontSize: "14px" }}>
        {label}
      </label>
      <div id={id}>
        {options.map((option) => (
          <div
            key={option.id}
            className="form-check"
            style={{
              display: "flex",
              alignItems: "center",
              marginBottom: "4px",
            }}
          >
            <input
              className="form-check-input"
              type="radio"
              id={`${id}-${option.id}`}
              name={name}
              value={option.id}
              checked={value === option.id}
              onChange={onChange}
              style={{
                width: "14px",
                height: "14px",
                marginRight: "6px",
              }}
            />
            <label
              className="form-check-label"
              htmlFor={`${id}-${option.id}`}
              style={{ fontSize: "13px" }}
            >
              {option.name}
            </label>
          </div>
        ))}
      </div>
    </div>
  );
}

// RENDER FILE FIELD.
export const renderFileField = (field, index, fileInputRef, setFormData) => {
  return (
    <div key={field.id || index} className="form-group">
      <label htmlFor={field.name}>{field.label || field.name}</label>
      <div className="select-group">
        <input
          type="file"
          name={field.name}
          id={field.id}
          ref={fileInputRef} // Add the ref here to manage file input
          onChange={(e) => {
            const file = e.target.files[0];
            setFormData((prevData) => ({ ...prevData, file })); // Set the file to the state
            field.onChange?.(e); // Run the provided onChange logic
          }}
        />
        <button
          type="button"
          className="add-button"
          onClick={() => {
            if (fileInputRef.current) {
              fileInputRef.current.value = ""; // Clear the file input field
            }
            // Reset file state in the formData when the Clear button is clicked
            setFormData((prevData) => ({ ...prevData, file: null }));
          }}
        >
          Clear
        </button>
      </div>
    </div>
  );
};

// RENDER INPUT FIELD.
export const renderInputField = (
  field,
  index,
  handleInputChange,
  setFormData
) => (
  <InputField
    key={index}
    label={field.label}
    id={field.id}
    name={field.name}
    type={field.type || "text"} // Default to 'text' if field.type is missing
    value={field.value}
    onChange={handleInputChange(setFormData)}
    placeholder={field.placeholder}
    required={field.required || false} // Default to false if field.required is missing
    readOnly={field.readOnly || false} // Default to false if field.readOnly is missing
    ref={field.ref}
    className={field.className || "form-group"} // Default to 'form-group' if field.className is missing
  />
);
export const InputField = forwardRef(
  (
    {
      label,
      id,
      name,
      value,
      onChange,
      onBlur,
      type = "text",
      required = false,
      placeholder = "",
      readOnly = false,
      className,
    },
    ref
  ) => (
    <div className={className}>
      {/* Only render the label if type is not "hidden" */}
      {type !== "hidden" && (
        <label htmlFor={id} className={required ? "required-label" : ""}>
          {label}
        </label>
      )}
      <input
        ref={ref} // ✅ Attach ref to input
        type={type}
        id={id}
        name={name}
        value={value}
        onChange={onChange}
        onBlur={onBlur}
        placeholder={placeholder}
        readOnly={readOnly}
        required={required}
      />
    </div>
  )
);

// RENDER TABLE LIST FIELD.
export const TableList = forwardRef(
  (
    {
      formName,
      columns,
      filteredItems,
      searchTerm,
      setSearchTerm,
      handleRowClick,
      idField = "id", // Default to 'id', but you can pass any name
    },
    ref
  ) => {
    if (!columns || columns.length === 0) return null;

    return (
      <div className="table-container" ref={ref}>
        <h1 className="embossed-text" style={{ textAlign: "center" }}>
          {formName} List
        </h1>

        {/* Search Field */}
        {searchTerm !== undefined && setSearchTerm && (
          <div className="list-search-input-container">
            <input
              id="search"
              type="text"
              placeholder="Search"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="list-search-input"
            />
          </div>
        )}

        {/* Header Table */}
        <table className="list-table">
          <thead>
            <tr>
              {columns.map((col) => (
                <th
                  key={col.key}
                  style={{ width: col.width || "auto" }}
                  className={`${formName.toLowerCase()}-${col.key.toLowerCase()}`}
                >
                  {col.label}
                </th>
              ))}
            </tr>
          </thead>
        </table>

        {/* Scrollable Body Table */}
        <div className="list-table-container">
          <table className="list-table">
            <tbody>
              {filteredItems.map((item) => (
                <tr
                  key={item[idField]}
                  className={`list-clickable-row ${
                    handleRowClick ? "clickable" : ""
                  }`}
                  {...(handleRowClick && {
                    onClick: () => handleRowClick(item[idField]),
                  })}
                >
                  {columns.map((col) => (
                    <td
                      key={col.key}
                      style={{
                        width: col.width || "auto",
                        textAlign:
                          col.type === "number"
                            ? "right"
                            : col.type === "date"
                            ? "center"
                            : "left",
                      }}
                      className={`${formName.toLowerCase()}-${col.key.toLowerCase()}`}
                    >
                      {col.type === "select" ? (
                        item[col.key]?.name
                      ) : col.key.toLowerCase().includes("id") ? (
                        item[col.key]
                      ) : col.type === "number" ? (
                        item[col.key] != null ? (
                          new Intl.NumberFormat("en-US", {
                            minimumFractionDigits: 2,
                            maximumFractionDigits: 2,
                          }).format(item[col.key])
                        ) : (
                          ""
                        )
                      ) : col.type === "date" && item[col.key] ? (
                        isoformatDate(item[col.key])
                      ) : typeof item[col.key] === "string" &&
                        (/^https?:\/\//.test(item[col.key]) ||
                          /\.(pdf|docx?|xlsx?|xls|pptx?)$/i.test(
                            item[col.key]
                          )) ? (
                        <a
                          href={item[col.key]}
                          target="_blank"
                          rel="noopener noreferrer"
                          className="list-link"
                        >
                          {item[col.key]}
                        </a>
                      ) : (
                        item[col.key]
                      )}
                    </td>
                  ))}
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    );
  }
);
