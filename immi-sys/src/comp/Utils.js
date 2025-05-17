import { useEffect, useMemo, useCallback, useRef, useState } from "react";
import axios from "axios";
import { API_ROUTES } from './config';
import { useLocation, useNavigate } from 'react-router-dom';

// Utility hook for handling constants and states
export function useFormConstants(initialFormData) {
  // Get the location for search params (if needed)
  const location = useLocation();
  const navigate = useNavigate();
  const searchParams = new URLSearchParams(location.search);

  // Refs for non-state variables
  const focusFieldRef = useRef(null);
  const fileInputRef = useRef(null);
  const currentRecordIdRef = useRef(0);
  const lastFetchedId = useRef(null);
  const isFirstLoad = useRef(true);

  // States for form handling
  const [searchTerm, setSearchTerm] = useState("");
  const [message, setMessage] = useState("");
  const [messageType, setMessageType] = useState("success");
  const [isPopupOpen, setPopupOpen] = useState(false);
  const [formData, setFormData] = useState(initialFormData);
  const [formList, setFormList] = useState([]);

  // Token from local storage
  const token = localStorage.getItem("token");

  // Return all the necessary constants and state variables
  return {
    location,
    navigate,
    searchParams,
    focusFieldRef,
    fileInputRef,
    currentRecordIdRef,
    lastFetchedId,
    isFirstLoad,
    searchTerm,
    setSearchTerm,
    message,
    setMessage,
    messageType,
    setMessageType,
    isPopupOpen,
    setPopupOpen,
    formData,
    setFormData,
    formList,
    setFormList,
    token,
  };
}

/**  
 * ✅ UNIVERSAL DATA FETCHING FUNCTION  
 * Fetch data from a given URL with optional search params and bearer token.  
 */
export async function fetchData(url, searchParams = null, token = null) {
  try {
    // Check if searchParams exists and has values
    const queryString = searchParams && searchParams.toString() ? `?${searchParams.toString()}` : "";
    const dynamicUrl = `${url}${queryString}`;

    const config = {
      headers: {
        'Content-Type': 'application/json',
        ...(token && { Authorization: `Bearer ${token}` }), // Add Authorization only if token is provided
      },
    };

    const response = await axios.get(dynamicUrl, config);
    return response.data || null;
  } catch (error) {
    console.error("Error fetching data:", error.response?.data || error.message);
    return null;
  }
}

/**  
 * ✅ FETCH LIST OF DATA FOR DROPDOWNS OR STATIC LISTS  
 */
export function fetchFormData(apiRoute, setFormList) {
  fetch(apiRoute)
    .then((response) => response.json())
    .then((data) => {
      Array.isArray(data) ? setFormList(data) : setFormList([]);
    })
    .catch((error) => console.error("Error fetching data:", error));
}

/**  
 * ✅ FETCH AND SET FORM DATA FUNCTION  
 * Handles form population with navigation logic (prev/next records).  
 */
export const fetchAndSetData = async ({ url, setFormState, currentRecordIdRef, direction, setMessage, setPopupOpen, idField = "id", token }) => {
  try {
    const params = new URLSearchParams({ direction: direction.toString(), id: currentRecordIdRef.current });
    const data = await fetchData(url, params, token);

    if (data) {
      const updatedData = {
        ...data,
        ...(Object.prototype.hasOwnProperty.call(data, "isChecked") && {
          isChecked: data.isChecked === null || data.isChecked === undefined ? false : data.isChecked,
        }),
      };

      setFormState(updatedData);
      currentRecordIdRef.current = data[idField];
    } else {
      setMessage("Record not found.");
      setPopupOpen(true);
    }
  } catch (error) {
    console.error("Error loading data:", error);
    setMessage("An error occurred while fetching data.");
    setPopupOpen(true);
  }
};

export async function fetchDocuments(token, documentModalConfig, setDocuments) {
  try {
    const response = await fetch(documentModalConfig.url_List, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      throw new Error("Failed to fetch documents");
    }

    const data = await response.json();
    setDocuments(data);
  } catch (error) {
    console.error("fetchDocuments error:", error);
  }
};

export function validateAndPrepareFile(e, reference, documentId) {
  const selectedFile = e.target.files[0];

  if (!selectedFile) return { error: "No file selected" };

  const fileExtension = selectedFile.name.split(".").pop().toLowerCase();
  const allowedExtensions = ["pdf", "jpg", "jpeg", "png", "docx"];

  if (!allowedExtensions.includes(fileExtension)) {
    return { error: "Unsupported file type selected." };
  }

  const structuredFileName = `${reference}-${documentId}.${fileExtension}`;
  return { file: selectedFile, fileName: structuredFileName };
};

// formUtils.js

export function createDocumentFormData(recordId, documentId, file, fileName, idField) {
  const formData = new FormData();
  formData.append(`${idField}`, recordId);
  formData.append("documentId", documentId);
  formData.append("file", file);
  formData.append("fileName", fileName);
  return formData;
}


/**  
 * ✅ RESET FORM TO INITIAL STATE  
 */
export function resetFormData(setFormData, initialFormData) {
  setFormData(() => ({ ...initialFormData }));
}

/**  
 * ✅ NAVIGATE THROUGH RECORDS (New, Next, Prev)  
 */
export function handleNavigateRecord(direction, loadData, setFormData, initialFormData, focusFieldRef) {
  if (direction === "New") {
    resetFormData(setFormData, initialFormData);
    focusFieldRef.current?.focus();
    return;
  }
  loadData(direction);
}

/**  
 * ✅ HANDLE FORM SUBMISSION (Create/Update)  
 */
export async function handleSubmit(e, formData, url_form, primaryId) {
  e.preventDefault();

  try {
    let request;

    const hasFile = formData.file instanceof File;
    
    if (hasFile) {
      const multipartData = new FormData();

      // Send model data as a Blob in "record" field
      multipartData.append(
        "record",
        new Blob([JSON.stringify(formData)], { type: "application/json" })
      );

      // Append file only if it's a File instance
      multipartData.append("file", formData.file);

      const config = {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      };
      // Determine request type
      request = formData[primaryId]
        ? axios.put(`${url_form}/${formData[primaryId]}`, multipartData, config)
        : axios.post(url_form, multipartData, config);
    } else {
      // Standard JSON request
      request = formData[primaryId]
        ? axios.put(`${url_form}/${formData[primaryId]}`, formData)
        : axios.post(url_form, formData);
    }

    await request;

    return formData[primaryId]
      ? "Record updated successfully"
      : "Record saved successfully";
  } catch (error) {
    const errorMessage =
      error.response?.data?.error ||
      error.response?.data?.message ||
      (typeof error.response?.data === "string" ? error.response.data : null) ||
      error.message ||
      "An error occurred. Please try again.";
    return String(errorMessage);
  }
};

/**  
 * ✅ CLEAN FORM DATA (Replaces empty strings with `null`)  
 */
export const cleanFormData = (data) => {
  return Object.fromEntries(Object.entries(data).map(([key, value]) => [key, value === "" ? null : value]));
};

/**
 * ✅ HANDLE DROPDOWN OPTIONS FETCHING  
 */
export const useFetchDropdownOptions = (configs) => {
  useEffect(() => {
    const fetchOptions = async () => {
      try {
        await Promise.all(
          configs.map(async ({ url, setState, options }) => {
            if (url) {
              const res = await axios.get(url);
              setState(res.data || []);
            } else if (options && options.length > 0) {
              // Directly set the options if no URL is given (enum or static list)
              setState(options);
            }
          })
        );
      } catch (error) {
        console.error(
          "Error fetching dropdown options:",
          error.response?.data || error.message
        );
      }
    };

    fetchOptions();
  }, [configs]);
};

/**  
 * ✅ FUNCTION TO TRANSFORM DATA FOR REACT SELECT FIELD
 */
export function transformOptions(options, idField = "id", displayFields = "name") {
  if (!Array.isArray(options)) return [];
  return options.map((option) => {
    const label = Array.isArray(displayFields)
      ? displayFields.map((field) => option?.[field] ?? "").join(" ").trim()
      : option?.[displayFields] ?? "";

    return {
      value: option?.[idField] ?? "",
      label,
      raw: option, // Keep original object for later use
    };
  });
};

/**  
 * ✅ FORMAT DATE TO YYYY-MM-DD  
 */
export const formatDate = (date) => {
  const d = new Date(date);
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
};

/**  
 * ✅ ADD DAYS TO DATE  
 */
export const addDaysToDate = (date, days) => {
  const d = new Date(date);
  d.setDate(d.getDate() + days);
  return d;
};

/**
 * ✅ ADD NEW OPTION TO DROPDOWN LIST
 */
export function addNewOption(field, newOptionWithId, fieldSetters) {
  if (fieldSetters[field]) {
    fieldSetters[field]((prevOptions) => {
      const updatedOptions = [...prevOptions, newOptionWithId];
      return updatedOptions;
    });
  } else {
    console.warn(`Invalid field: ${field}`);
  }
};

/**  
 * ✅ HANDLE DROPDOWN SELECT CHANGE  
 */
export function handleSelectChange(e, dropdownOptions, setFormData, idField = "id") {
  const { name, value } = e.target;

  const selectedOption = dropdownOptions[name]?.find(
    (option) => String(option[idField]) === value // For enums, make sure to convert to string
  );
  setFormData((prev) => ({
    ...prev,
    [name]: idField === 'value' ? selectedOption?.value || null : selectedOption || null
  }));
};

export function handleReactSelectChange(selectedOption, { name }, dropdownOptions, setFormData, idField = 'id') {
  const selectedValue = selectedOption ? selectedOption.value : null;
  const selectedData = dropdownOptions[name]?.find((option) => option[idField] === selectedValue);
  setFormData((prev) => ({ ...prev, [name]: selectedData || null }));
}

/**  
 * ✅ GENERIC INPUT CHANGE HANDLER WITH PHONE VALIDATION & FORMATTING
 */
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

/**  
 * ✅ FORMAT PHONE NUMBER (XXX-XXX-XXXX)  
 */
export function formatPhoneNumber(input) {
  let formattedInput = input.replace(/\D/g, ""); // Remove non-digit characters
  if (formattedInput.length > 3 && formattedInput.length <= 6) {
    formattedInput = `${formattedInput.slice(0, 3)}-${formattedInput.slice(3)}`;
  } else if (formattedInput.length > 6) {
    formattedInput = `${formattedInput.slice(0, 3)}-${formattedInput.slice(3, 6)}-${formattedInput.slice(6, 10)}`;
  }
  return formattedInput;
}

/**
 * ✅ FORMAT SOCIAL SECURITY NUMBER (XXX-XX-XXXX)
 */
export function formatSocialSecurity(input) {
  let formattedInput = input.replace(/\D/g, ""); // Remove non-digit characters
  if (formattedInput.length > 3 && formattedInput.length <= 5) {
    formattedInput = `${formattedInput.slice(0, 3)}-${formattedInput.slice(3)}`;
  } else if (formattedInput.length > 5) {
    formattedInput = `${formattedInput.slice(0, 3)}-${formattedInput.slice(3, 5)}-${formattedInput.slice(5, 9)}`;
  }
  return formattedInput;
}

export const useFilteredItems = (formList, columns, searchTerm) => {
  return useMemo(() => {
    if (!searchTerm) return formList;
    return formList.filter((item) =>
      columns.some((column) => {
        const value =
          column.type === "select" ? item[column.key]?.name : item[column.key];
        return (
          typeof value === "string" &&
          value.toLowerCase().includes(searchTerm.toLowerCase())
        );
      })
    );
  }, [searchTerm, formList, columns]);
};

export const useHandleRowClick = (focusFieldRef, currentRecordIdRef, loadData) => {
  return useCallback(
    (param_id) => {
      focusFieldRef.current.focus();
      currentRecordIdRef.current = param_id;
      loadData(API_ROUTES.CURRENT_RECORD);
    },
    [focusFieldRef, currentRecordIdRef, loadData] // Ensure these dependencies are passed correctly
  );
};

export const useDynamicDropdownOptions = (configList) => {
  // Store dropdown options dynamically by field name
  const [dropdownOptions, setDropdownOptions] = useState({});
  const fieldSetters = useMemo(() => {
    const setters = {};
    configList.forEach(({ key }) => {
      setters[key] = (options) =>
        setDropdownOptions((prev) => ({ ...prev, [key]: options }));
    });
    return setters;
  }, [configList]);

  // Prepare fetch config with correct setState functions
  const dropdownFetchConfig = useMemo(() => {
    return configList.map(({ key, url }) => ({
      url,
      setState: fieldSetters[key],
    }));
  }, [configList, fieldSetters]);

  useFetchDropdownOptions(dropdownFetchConfig);

  return { dropdownOptions };
};

export const processFileChange = (event, baseName, updateCallback) => {
  const selectedFile = event.target.files[0];
  
  if (selectedFile) {
    const fileExtension = selectedFile.name.split('.').pop();
    const structuredFileName = `${baseName}.${fileExtension}`;

    // Log the original and structured file names
    //console.log("Selected file name:", selectedFile.name);
    //console.log("Structured file name:", structuredFileName);

    updateCallback({
      file: selectedFile,
      fileName: structuredFileName,
      location: structuredFileName,
    });
  } else {
    console.log("No file selected.");
  }
};

