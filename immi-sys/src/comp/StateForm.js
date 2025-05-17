import React, {
  useState,
  useEffect,
  useCallback,
  useMemo,
  useRef,
} from "react";

// COMPONENT IMPORTS
import { API_ROUTES } from "./config";
import ButtonGroup from "../Utils/ButtonGroup";
import MessagePopup from "./MessagePopup";
import Modal from "./Modal";

// FORM FIELD RENDERERS
import {
  renderSelectField,
  renderReactSelectField,
  renderCheckboxField,
  renderTextareaField,
  renderInputField,
  TableList,
} from "./FormFields";

// UTILITIES
import {
  useFormConstants,
  fetchAndSetData,
  fetchFormData,
  handleNavigateRecord,
  handleSubmit,
  addNewOption,
  useFetchDropdownOptions,
  handleSelectChange,
  handleReactSelectChange,
  handleInputChange,
  useFilteredItems,
  useHandleRowClick,
  transformOptions,
} from "./Utils";

// STYLES  ---> Edit here
import "../css/UtilForm.css";

// INITIAL STATE  ---> Edit here
const initialFormData = {
  id: null,
  name: "",
  code: "",
  country: null,
};

/******** CONFIGURATION CONSTANTS **********/
const formName = "State";
const pageTitle = formName + " Registration";
const url_form = API_ROUTES.STATE_FORM;
const url_list = API_ROUTES.STATE_LIST;

/******* DROPDOWN CONFIGURATION LIST ********/
const dropdownFieldConfigs = [
  {
    key: "country",
    dropdownUrl: API_ROUTES.COUNTRY_LIST,
    modalUrl: API_ROUTES.COUNTRY_FORM,
  },
];

/********************************************/
/************* FORM DECLARATION *************/
/********************************************/
const StateForm = () => {
  /***** STANDARD CONSTANTS. DO NOT EDIT. *****/
  const {
    location,
    navigate,
    searchParams,
    focusFieldRef,
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
  } = useFormConstants(initialFormData);
  
  /**************** FORM FIELDS ***************/
  const fieldsConfig = [
    { label: "State Id", id: "id", readOnly: true, useInList: true, width: "10%" },
    { label: "State Name", id: "name", required: true, ref: focusFieldRef, useInList: true, width: "40%" },
    { label: "Code", id: "code", required: true, useInList: true, width: "10%" },
    { label: "Country", id: "country", type: "select", useInList: true, width: "40%" },
  ];

  /************* DROPDOWN STATE *************/
  const [dropdownOptions, setDropdownOptions] = useState(
    dropdownFieldConfigs.reduce((acc, { key }) => {
      acc[key] = []; // Initialize each option list to an empty array
      return acc;
    }, {})
  );

  const dropdownModalMap = dropdownFieldConfigs.reduce((acc, { key, modalUrl }) => {
    acc[key] = !!modalUrl; // true if modalUrl exists
    return acc;
  }, {});  

  // Initialize the fieldSetters once; no need for useMemo here
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

  // Dynamically create the dropdownFetchConfig
  const dropdownFetchConfig = useMemo(() => {
    return dropdownFieldConfigs.map(({ key, dropdownUrl }) => ({
      url: dropdownUrl,
      setState: fieldSetters[key],
    }));
  }, [dropdownFieldConfigs]); // Only recompute when `dropdownFieldConfigs` changes

  // Use the fetch config with your custom hook
  useFetchDropdownOptions(dropdownFetchConfig);

  /********************************************/
  /******** FORM FIELDS CONFIGURATION *********/
  /********************************************/
  // Function to get the correct field value
  const getFieldValue = (data, key) => {
    const value = data[key];
    return value?.id || value || "";
  };

  // Function to get the React Select value
  function getReactSelectValue(options, formData, field, idField = "id") {
    const formObject = formData[field];
    const selectedId = formObject?.[idField] ?? null;
    return options.find((opt) => opt.value === selectedId) || null;
  }

  const formFields = fieldsConfig.map(
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
        type === "checkbox"
          ? formData[id] ?? false
          : getFieldValue(formData, id);

      if (type === "select_react") {
        options = transformOptions(dropdownOptions[id], idField, displayValue);
        value = getReactSelectValue(options, formData, id, idField);
      } else if (type === "select") {
        options = dropdownOptions[id] || [];
      }
      const hasModal = dropdownModalMap[id];
      const openModal = hasModal ? () => setModalState(id, true) : false;

      return {
        id,
        name,
        type,
        value,
        checked: type === "checkbox" ? value : undefined,
        placeholder:
          type === "select"
            ? undefined
            : `Enter ${String(rest.label || name).toLowerCase()}`,
        ...(type === "select" && {
          options,
          onChange: (e) =>
            handleSelectChange(e, dropdownOptions, setFormData, idField),
          openModal,
        }),
        ...(type === "select_react" && {
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
        ...(type === "checkbox" && {
          onChange: (e) =>
            handleInputChange(setFormData)({
              target: {
                name: id,
                value: e.target.checked,
              },
            }),
        }),
        ...(type !== "select" &&
          type !== "select_react" &&
          type !== "checkbox" && {
            onChange: (e) => handleInputChange(setFormData)(e),
          }),
        ...rest,
      };
    }
  );

  /********************************************/
  /*************** MODALS FORMS ***************/
  /********************************************/
  const [modalStates, setModalStates] = useState(
    Object.fromEntries(dropdownFieldConfigs.map(({ key }) => [key, false]))
  );

  // Function to toggle modal visibility
  const setModalState = (field, isOpen) => {
    setModalStates((prev) => {
      const newState = Object.fromEntries(
        Object.keys(prev).map((key) => [key, key === field ? isOpen : false])
      );
      return newState;
    });
  };

  // Dynamically create modal forms
  const modalForms = dropdownFieldConfigs
    .filter(({ modalUrl }) => modalUrl)
    .map(({ key, modalUrl }) => ({
      id: `${key}Modal`,
      showModal: modalStates[key],
      closeModal: () => setModalState(key, false),
      onAddOption: handleAddOption,
      field: key,
      url: modalUrl,
    }));

  /********************************************/
  /*************** TABLE CONFIG ***************/
  /********************************************/
  // --------- Edit Here ---------
  const columns = fieldsConfig
    .filter(field => field.useInList) // Filter fields that should appear in the list
    .map(field => ({
      key: field.id,
      label: field.label,
      width: field.width || "20%", // Default width if not specified
      type: field.type || "text", // Default to text if no type is specified
      options: field.options || [], // Include options for select fields
    }));

  /********************************************/
  /************* LOGIC FUNCTIONS **************/
  /********************************************/
  const primaryId = findPrimaryId(initialFormData, formName.toLowerCase());
  
  function findPrimaryId(formData, modelName) {
    const expectedId = `${modelName}Id`;
    return (
      Object.keys(formData).find(
        (key) => key.toLowerCase() === expectedId.toLowerCase()
      ) || "id"
    );
  }
  /********************************************/
  const loadData = useCallback(
    async (direction) => {
      try {
        await fetchAndSetData({
          url: url_form,
          setFormState: setFormData,
          currentRecordIdRef,
          direction,
          setMessage,
          setMessageType,
          setPopupOpen,
          idField: primaryId,
          token: localStorage.getItem("token"),
        });
      } catch (error) {
        console.error("Error loading data:", error);
      }
    },
    [setFormData, currentRecordIdRef, setMessage, setMessageType, setPopupOpen]
  );
  /********************************************/
  function handleAddOption(field, newOptionWithId) {
    addNewOption(field, newOptionWithId, fieldSetters);
  }
  /********************************************/
  const filteredItems = useFilteredItems(formList, columns, searchTerm);
  /********************************************/
  const handleRowClick = useHandleRowClick(
    focusFieldRef,
    currentRecordIdRef,
    loadData
  );
  /********************************************/
  const navigateRecord = (direction) => {
    handleNavigateRecord(
      direction,
      loadData,
      setFormData,
      initialFormData,
      focusFieldRef
    );
  };
  /********************************************/
  useEffect(() => {
    const recordId = searchParams.get("id");
    const parsedId = recordId ? parseInt(recordId, 10) : null;
    if (!token) return;
    const loadRecord = async () => {
      try {
        if (parsedId !== null) {
          currentRecordIdRef.current = parsedId;
          lastFetchedId.current = parsedId;
          await loadData(API_ROUTES.CURRENT_RECORD);
        } else if (isFirstLoad.current) {
          isFirstLoad.current = false;
          await loadData(API_ROUTES.FIRST_RECORD);
        }
        fetchFormData(url_list, setFormList);
      } catch (error) {
        console.error("Error loading data:", error);
      }
    };
    loadRecord();
  }, [location.search, loadData]);
  /********************************************/
  const submitForm = async (e) => {
    e.preventDefault();

    // Dynamically get required field ids
    const requiredFields = fieldsConfig
      .filter((f) => f.required)
      .map((f) => f.name || f.id);

    // Check for missing values
    const missingFields = requiredFields.filter((field) => {
      const value = formData[field];
      return (
        value === undefined ||
        value === null ||
        value === "" ||
        (typeof value === "object" && Object.keys(value).length === 0)
      );
    });

    if (missingFields.length > 0) {
      setMessage(`Please fill out the required fields marked with *`);
      setMessageType("error");
      setPopupOpen(true);
      return;
    }

    try {
      const message = await handleSubmit(e, formData, url_form, primaryId);

      if (formData[primaryId] == null) {
        loadData(API_ROUTES.LAST_RECORD);
      } else {
        currentRecordIdRef.current = formData[primaryId];
      }

      fetchFormData(url_list, setFormList);
      setMessage(message);
      setMessageType("success");
      setPopupOpen(true);
    } catch (error) {
      console.error("Form submission failed:", error);
      setMessage("An error occurred while submitting the form.");
      setMessageType("error");
      setPopupOpen(true);
    }
  };
  /********************************************/
  const renderField = (field, index) => {
    switch (field.type) {
      case "select":
        return renderSelectField(field, index);
      case "select_react":
        return renderReactSelectField(
          field,
          index,
          handleInputChange,
          setFormData
        );
      case "checkbox":
        return renderCheckboxField(
          field,
          index,
          handleInputChange,
          setFormData
        );
      case "textarea":
        return renderTextareaField(
          field,
          index,
          handleInputChange,
          setFormData
        );
      default:
        return renderInputField(field, index, handleInputChange, setFormData);
    }
  };

  /********************************************/
  /************* HTML FORM RENDER *************/
  /********************************************/
  return (
    <div className="form-container">
      <h1 className="embossed-text">{pageTitle}</h1>
      <ButtonGroup
        onNewRecord={() => navigateRecord(API_ROUTES.NEW_RECORD)}
        onSave={submitForm}
        onCancel={() => navigateRecord(API_ROUTES.CURRENT_RECORD)}
        onFirstRecord={() => navigateRecord(API_ROUTES.FIRST_RECORD)}
        onPreviousRecord={() => navigateRecord(API_ROUTES.PREV_RECORD)}
        onNextRecord={() => navigateRecord(API_ROUTES.NEXT_RECORD)}
      />
      <form onSubmit={submitForm}>
        <label className="form-state-label">
          {formData[primaryId] ? "Edit " + formName : "Add New " + formName}
        </label>

        <fieldset>
          <legend>Main Information</legend>
          <div className="fieldset-container">
            {formFields.map((field, index) => renderField(field, index))}
          </div>
        </fieldset>
      </form>
      {/* TABLE LIST SCROLLABLE */}
      {columns && columns.length > 0 && (
        <TableList
          formName={formName}
          columns={columns}
          filteredItems={filteredItems}
          searchTerm={searchTerm}
          setSearchTerm={setSearchTerm}
          handleRowClick={handleRowClick}
          idField={primaryId}
        />
      )}
      {modalForms.map((modal) => (
        <Modal key={modal.id} {...modal} />
      ))}
      <MessagePopup
        message={message}
        isPopupOpen={isPopupOpen}
        setPopupOpen={setPopupOpen}
        type={messageType}
      />{" "}
    </div>
  );
};

export default StateForm;
