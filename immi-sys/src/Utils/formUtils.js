import { useState, useMemo, useRef } from "react";

import { processFileChange } from "../Utils/fileUtils";

// Create form field Type.
export const FIELD_TYPES = {
  TEXT: "text",
  NUMBER: "number",
  EMAIL: "email",
  SELECT: "select",
  SELECT_REACT: "select_react",
  DATE: "date",
  DATE_TIME: "datetime-local",
  TEXTAREA: "textarea",
  CHECKBOX: "checkbox",
  RADIO: "radio",
  FILE: "file",
};

export const DOCUMENT_TYPE = {
  PETITION_DOCUMENT: "petition_document",
  TAX_DOCUMENT: "tax_document",
  APPLICATION_DOCUMENT: "application_document",
  SERVICE_REQUEST_DOCUMENT: "service_request_document",
};

export function clearFormDataExceptCopy(
  formData,
  fieldsConfig,
  focusFieldRef,
  primaryId = "id"
) {
  const updated = { ...formData };

  fieldsConfig.forEach((field) => {
    if (field.copyRecord && field.id in updated) {
      updated[field.id] = field.id === primaryId ? null : "";
    }
  });

  focusFieldRef?.current?.focus();
  return updated;
}

export function getPrimaryId(fields, formData, formName) {
  // Priority 1: explicitly marked mainId in Fields List.
  const mainIdField = fields.find((field) => field.mainId === true);
  if (mainIdField) return mainIdField.id;

  // Priority 2: check for convention-based ID in fields.
  const expectedId = `${formName}Id`;
  const fallbackIdField = fields.find((field) => field.id === expectedId);
  if (fallbackIdField) return fallbackIdField.id;

  // Priority 3: scan formData for typical ID field names
  for (const key in formData) {
    const lowerKey = key.toLowerCase();
    if (lowerKey === "id" || lowerKey === expectedId.toLowerCase()) {
      return key;
    }
  }
  // No ID field found
  return null;
}

export function getFormHeaderLabel(formData, primaryId, formName) {
  return formData[primaryId] ? `Edit ${formName}` : `Add New ${formName}`;
}

export function FormRenderer({
  formData,
  primaryId,
  formName,
  formFields,
  renderField,
  submitForm,
  legendTitle = "Main Information",
  renderAdditionalContent = () => null, // default to empty function
}) {
  return (
    <form onSubmit={submitForm}>
      <label className="form-state-label">
        {getFormHeaderLabel(formData, primaryId, formName)}
      </label>

      <fieldset>
        <legend>{legendTitle}</legend>
        <div className="fieldset-container">
          {formFields.map((field, index) => renderField(field, index))}
          {renderAdditionalContent()}
        </div>
      </fieldset>
    </form>
  );
}

// FormFieldRenderer.js
export const renderField = (
  field,
  index,
  { classname = null, handleInputChange, setFormData, fileInputRef, FormFields }
) => {
  if (!field.className && classname) {
    field.className = classname;
  }

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

// Function to get the id field value
function getFieldValue(data, key) {
  const value = data[key];
  return value?.id || value || "";
}
// Function to get the React Select value
function getReactSelectValue(options, formData, field, idField = "id") {
  const formObject = formData[field];
  const selectedId = formObject?.[idField] ?? null;
  return options.find((opt) => opt.value === selectedId) || null;
}

export function generateFormFields({
  fieldsConfig,
  formData,
  dropdownOptions,
  dropdownModalMap,
  setFormData,
  setModalState,
  setFile,
  setFileName,
}) {
  return fieldsConfig.map(
    ({
      id,
      name = id,
      type,
      idField = "id",
      displayValue = "name",
      ...rest
    }) => {
      let options = [];
      let value =
        type === FIELD_TYPES.CHECKBOX
          ? formData[id] ?? false
          : getFieldValue(formData, id);

      if (type === FIELD_TYPES.SELECT_REACT) {
        options = transformOptions(dropdownOptions[id], idField, displayValue);
        value = getReactSelectValue(options, formData, id, idField);
      } else if (type === FIELD_TYPES.SELECT) {
        options = dropdownOptions[id] || [];
      }

      const hasModal = dropdownModalMap[id];
      const openModal = hasModal ? () => setModalState(id, true) : false;

      return {
        id,
        name,
        type,
        value,
        checked: type === FIELD_TYPES.CHECKBOX ? value : undefined,
        placeholder:
          type === FIELD_TYPES.SELECT
            ? undefined
            : `Enter ${String(rest.label || name).toLowerCase()}`,

        // Handle select field type
        ...(type === FIELD_TYPES.SELECT && {
          options,
          onChange: (e) =>
            handleSelectChange(e, dropdownOptions, setFormData, idField),
          openModal,
        }),

        // Handle React select field type
        ...(type === FIELD_TYPES.SELECT_REACT && {
          options,
          onChange: (selectedOption, actionMeta) =>
            handleReactSelectChange(
              selectedOption,
              actionMeta,
              dropdownOptions,
              setFormData,
              idField
            ),
          openModal,
        }),

        // Handle checkbox field type
        ...(type === FIELD_TYPES.CHECKBOX && {
          onChange: (e) =>
            handleInputChange(setFormData)({
              target: {
                name: id,
                value: e.target.checked,
              },
            }),
        }),

        ...(type === FIELD_TYPES.RADIO && {
          options,
          onChange: (e) => handleInputChange(setFormData)(e),
        }),

        // Handle file field type
        ...(type === FIELD_TYPES.FILE && {
          onChange: (e) =>
            processFileChange(
              e,
              formData.name,
              ({ file, fileName, location }) => {
                setFile(file);
                setFileName(fileName);
                setFormData((prev) => ({ ...prev, location }));
              }
            ),
        }),

        // Default input field (text, number, etc.)
        ...(type !== FIELD_TYPES.SELECT &&
          type !== FIELD_TYPES.SELECT_REACT &&
          type !== FIELD_TYPES.CHECKBOX &&
          type !== FIELD_TYPES.FILE && {
            onChange: (e) => handleInputChange(setFormData)(e),
          }),

        ...rest,
      };
    }
  );
}

export const getMissingRequiredFields = (formData, fieldsConfig) => {
  const requiredFields = fieldsConfig
    .filter((f) => f.required)
    .map((f) => f.name || f.id);

  return requiredFields.filter((field) => {
    const value = formData[field];
    return (
      value === undefined ||
      value === null ||
      value === "" ||
      (typeof value === "object" && Object.keys(value).length === 0)
    );
  });
};

export function generateTableColumns(fieldsConfig) {
  return fieldsConfig
    .filter((field) => field.useInList) // Only include fields flagged for list use
    .map((field) => ({
      key: field.id,
      label: field.label,
      width: field.width || "20%", // Default width
      type: field.type || "text", // Default type
      options: field.options || [], // Optional for select-type fields
    }));
}

// Reset form to initial state
export function resetFormData(setFormData, initialFormData) {
  setFormData(() => ({ ...initialFormData }));
}

// Navigate through records (New, Next, Prev)
export function handleNavigateRecord(
  direction,
  loadData,
  setFormData,
  initialFormData,
  focusFieldRef
) {
  if (direction === "New") {
    resetFormData(setFormData, initialFormData);
    focusFieldRef.current?.focus();
    return;
  }
  loadData(direction);
}

// Create document form data
export function createDocumentFormData(
  form_Type, // e.g., visa, petition, tax, etc.
  recordId,
  documentId,
  fileName,
  file
) {
  // Validate all required fields
  if (
    !form_Type ||
    form_Type.trim() === "" ||
    !recordId ||
    !documentId ||
    !fileName ||
    fileName.trim() === "" ||
    !file
  ) {
    throw new Error("All document fields are required and must not be empty.");
  }

  const formData = new FormData();
  formData.append("type", form_Type);
  formData.append("recordId", recordId);
  formData.append("documentId", documentId);
  formData.append("fileName", fileName);
  formData.append("file", file);
  return formData;
}

// Clean form data (Replaces empty strings with `null`)
export const cleanFormData = (data) => {
  return Object.fromEntries(
    Object.entries(data).map(([key, value]) => [
      key,
      value === "" ? null : value,
    ])
  );
};

// Format phone number (XXX-XXX-XXXX)
export function formatPhoneNumber(input) {
  let formattedInput = input.replace(/\D/g, ""); // Remove non-digit characters
  if (formattedInput.length > 3 && formattedInput.length <= 6) {
    formattedInput = `${formattedInput.slice(0, 3)}-${formattedInput.slice(3)}`;
  } else if (formattedInput.length > 6) {
    formattedInput = `${formattedInput.slice(0, 3)}-${formattedInput.slice(
      3,
      6
    )}-${formattedInput.slice(6, 10)}`;
  }
  return formattedInput;
}

// Format social security number (XXX-XX-XXXX)
export function formatSocialSecurity(input) {
  let formattedInput = input.replace(/\D/g, ""); // Remove non-digit characters
  if (formattedInput.length > 3 && formattedInput.length <= 5) {
    formattedInput = `${formattedInput.slice(0, 3)}-${formattedInput.slice(3)}`;
  } else if (formattedInput.length > 5) {
    formattedInput = `${formattedInput.slice(0, 3)}-${formattedInput.slice(
      3,
      5
    )}-${formattedInput.slice(5, 9)}`;
  }
  return formattedInput;
}

// Generic input change handler with validation & formatting
export const handleInputChange = (setFormData) => (e) => {
  const { name, value } = e.target;
  let updatedValue = value;

  if (name === "phone") {
    updatedValue = formatPhoneNumber(value);
    const isValidPhone = /^\d{3}-\d{3}-\d{4}$/.test(updatedValue);
    if (!isValidPhone && updatedValue.length >= 12) {
      console.warn("Invalid phone number format! It should be XXX-XXX-XXXX.");
      return;
    }
  }
  if (name === "email") {
    const isValidEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value);
    if (!isValidEmail && value.length > 0) {
      console.warn("Invalid email format! Use a valid email address.");
    }
  }
  if (name === "socialSecurity") {
    updatedValue = formatSocialSecurity(value);
    const isValidSSN = /^\d{3}-\d{2}-\d{4}$/.test(updatedValue);
    if (!isValidSSN && updatedValue.length >= 11) {
      console.warn("Invalid SSN format! Use XXX-XX-XXXX.");
    }
  }
  // Update the form data state
  setFormData((prev) => ({ ...prev, [name]: updatedValue }));
};

// Function to transform data for React Select field
export function transformOptions(
  options,
  idField = "id",
  displayFields = "name"
) {
  if (!Array.isArray(options)) return [];
  return options.map((option) => {
    const label = Array.isArray(displayFields)
      ? displayFields
          .map((field) => option?.[field] ?? "")
          .join(" ")
          .trim()
      : option?.[displayFields] ?? "";

    return {
      value: option?.[idField] ?? "",
      label,
      raw: option, // Keep original object for later use
    };
  });
}

// Add new option to dropdown list
export function addNewOption(
  field,
  newOptionWithId,
  fieldSetters,
  currentOptions
) {
  if (fieldSetters[field]) {
    const updatedOptions = [...currentOptions, newOptionWithId];
    fieldSetters[field](updatedOptions);
  } else {
    console.warn(`Invalid field: ${field}`);
  }
}

// Handle dropdown select change
export function handleSelectChange(
  e,
  dropdownOptions,
  setFormData,
  idField = "id"
) {
  const { name, value } = e.target;

  const selectedOption = dropdownOptions[name]?.find(
    (option) => String(option[idField]) === value // For enums, make sure to convert to string
  );
  setFormData((prev) => ({
    ...prev,
    [name]:
      idField === "value"
        ? selectedOption?.value || null
        : selectedOption || null,
  }));
}

// Handle React Select change
export function handleReactSelectChange(
  selectedOption,
  { name },
  dropdownOptions,
  setFormData,
  idField = "id"
) {
  const selectedValue = selectedOption ? selectedOption.value : null;
  const selectedData = dropdownOptions[name]?.find(
    (option) => option[idField] === selectedValue
  );
  setFormData((prev) => ({ ...prev, [name]: selectedData || null }));
}

export function useDropdownManager(dropdownFieldConfigs) {
  // State for dropdown options
  const [dropdownOptions, setDropdownOptions] = useState(
    dropdownFieldConfigs.reduce((acc, { key }) => {
      acc[key] = [];
      return acc;
    }, {})
  );

  // State for modal visibility flags
  const dropdownModalMap = useMemo(
    () =>
      dropdownFieldConfigs.reduce((acc, { key, modalUrl }) => {
        acc[key] = !!modalUrl;
        return acc;
      }, {}),
    [dropdownFieldConfigs]
  );

  // Field setters
  const getFieldSetters = (fieldConfigs) => {
    return fieldConfigs.reduce((acc, { key }) => {
      acc[key] = (options) => {
        setDropdownOptions((prev) => {
          if (JSON.stringify(prev[key]) !== JSON.stringify(options)) {
            return { ...prev, [key]: options };
          }
          return prev;
        });
      };
      return acc;
    }, {});
  };

  const fieldSetters = useRef(getFieldSetters(dropdownFieldConfigs)).current;

  // Fetch config for external fetch function
  const dropdownFetchConfig = useMemo(() => {
    return dropdownFieldConfigs.map(({ key, dropdownUrl }) => ({
      url: dropdownUrl,
      setState: fieldSetters[key],
    }));
  }, [dropdownFieldConfigs]);

  return {
    dropdownOptions,
    dropdownModalMap,
    fieldSetters,
    dropdownFetchConfig,
    setDropdownOptions,
  };
}

// modalUtils.js

export function initializeAndHandleModalStates(
  configs,
  modalStates,
  setter,
  handleAddOption
) {
  const setModalState = (field, isOpen) => {
    setter((prev) =>
      Object.fromEntries(
        Object.keys(prev).map((key) => [key, key === field ? isOpen : false])
      )
    );
  };

  const modalForms = configs
    .filter(({ modalUrl }) => modalUrl)
    .map(({ key, modalUrl }) => ({
      id: `${key}Modal`,
      showModal: modalStates[key],
      closeModal: () => setModalState(key, false),
      onAddOption: handleAddOption,
      field: key,
      url: modalUrl,
    }));

  return {
    setModalState,
    modalForms,
  };
}
