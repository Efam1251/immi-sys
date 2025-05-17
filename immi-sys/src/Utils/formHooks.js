import { useEffect, useMemo, useCallback, useRef, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";
import { API_ROUTES } from "../comp/config";

// Form constants hook
export function useFormConstants(initialFormData) {
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

// Filtered items hook
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

// Row click handler hook
export const useHandleRowClick = (
  focusFieldRef,
  currentRecordIdRef,
  loadData
) => {
  return useCallback(
    (param_id) => {
      focusFieldRef.current.focus();
      currentRecordIdRef.current = param_id;
      loadData(API_ROUTES.CURRENT_RECORD);
    },
    [focusFieldRef, currentRecordIdRef, loadData]
  );
};

// FORM DROPDOWNS AND SETTERS GENERATION
export const useDropdownFields = (dropdownFieldConfigs) => {
  // State to hold dropdown options
  const [dropdownOptions, setDropdownOptions] = useState(
    dropdownFieldConfigs.reduce((acc, { key }) => {
      acc[key] = [];
      return acc;
    }, {})
  );

  // Map to track which fields have modals
  const dropdownModalMap = dropdownFieldConfigs.reduce(
    (acc, { key, modalUrl }) => {
      acc[key] = !!modalUrl;
      return acc;
    },
    {}
  );

  // Setters for updating dropdown options
  const getFieldSetters = (fieldConfigs) => {
    return fieldConfigs.reduce((acc, { key }) => {
      acc[key] = (options) => {
        setDropdownOptions((prev) => {
          if (JSON.stringify(prev[key]) !== JSON.stringify(options)) {
            return {
              ...prev,
              [key]: options,
            };
          }
          return prev;
        });
      };
      return acc;
    }, {});
  };

  const fieldSetters = useRef(getFieldSetters(dropdownFieldConfigs)).current;

  // Generate fetch config
  const dropdownFetchConfig = useMemo(() => {
    return dropdownFieldConfigs.map(({ key, dropdownUrl }) => ({
      url: dropdownUrl,
      setState: fieldSetters[key],
    }));
  }, [dropdownFieldConfigs]);

  // Fetch options
  useFetchDropdownOptions(dropdownFetchConfig);

  return {
    dropdownOptions,
    dropdownModalMap,
    fieldSetters,
  };
};

// Fetch dropdown options hook
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

// Dynamic dropdown options hook
export const useDynamicDropdownOptions = (configList) => {
  const [dropdownOptions, setDropdownOptions] = useState({});
  const fieldSetters = useMemo(() => {
    const setters = {};
    configList.forEach(({ key }) => {
      setters[key] = (options) =>
        setDropdownOptions((prev) => ({ ...prev, [key]: options }));
    });
    return setters;
  }, [configList]);

  const dropdownFetchConfig = useMemo(() => {
    return configList.map(({ key, url }) => ({
      url,
      setState: fieldSetters[key],
    }));
  }, [configList, fieldSetters]);

  useFetchDropdownOptions(dropdownFetchConfig);

  return { dropdownOptions, fieldSetters };
};
