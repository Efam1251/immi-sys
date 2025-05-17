import React, {
  useState,
  useEffect,
  useCallback,
  useMemo,
  useRef,
} from "react";

// COMPONENT IMPORTS
import { API_ROUTES } from "./config";
import ButtonsBar from "../Utils/ButtonsBar";
import MessagePopup from "./MessagePopup";
import Modal from "./Modal";
import DocumentModal from "./DocumentModal";

// FORM FIELD RENDERERS
import * as FormFields from "./FormFields";

// UTILITIES
// Import form-related hooks
import {
  useFormConstants,
  useFilteredItems,
  useHandleRowClick,
  useFetchDropdownOptions,
} from "../Utils/formHooks";

// Import API-related functions
import {
  fetchAndSetData,
  fetchFormData,
  handleSubmit,
} from "../Utils/apiUtils";

// Import form utility functions
import {
  FIELD_TYPES,
  DOCUMENT_TYPE,
  getPrimaryId,
  generateFormFields,
  getMissingRequiredFields,
  generateTableColumns,
  handleNavigateRecord,
  addNewOption,
  handleInputChange,
} from "../Utils/formUtils";

import {
  formatToCamelCase,
} from "../Utils/commonUtils";

// STYLES
import "../css/UtilForm.css";

// INITIAL STATE
const initialFormData = {
  id: null,
  date: "",
  customer: null,
  visaType: null,
  country: null,
  reference: "",
  submissionDate: "",
  biometricAppointment: "",
  consularAppointment: "",
  cost: 0,
  status: null,
  notes: "",
};

/******** CONFIGURATION CONSTANTS **********/
const formName = "VisaApplication";
const pageTitle = formName.replace(/([a-z])([A-Z])/g, '$1 $2') + " Registration";
const url_form = API_ROUTES.VISA_APPLICATION_FORM;
const url_list = API_ROUTES.VISA_APPLICATION_LIST;
/******* DROPDOWN CONFIGURATION LIST ********/
const dropdownFieldConfigs = [
  {
    key: "customer",
    dropdownUrl: API_ROUTES.CUSTOMER_ALL,
    modalUrl: null,
  },
  {
    key: "visaType",
    dropdownUrl: API_ROUTES.VISATYPE_LIST,
    modalUrl: API_ROUTES.VISATYPE_FORM,
  },
  {
    key: "country",
    dropdownUrl: API_ROUTES.COUNTRY_LIST,
    modalUrl: API_ROUTES.COUNTRY_FORM,
  },
  {
    key: "status",
    dropdownUrl: API_ROUTES.STATUS_LIST,
    modalUrl: null,
  },
  {
    key: "document",
    dropdownUrl: API_ROUTES.DOCUMENTS_DROPDOWN,
    modalUrl: null,
  },
];

/********************************************/
/************* FORM DECLARATION *************/
/********************************************/
const VisaApplicationForm = () => {
  /***** STANDARD CONSTANTS. DO NOT EDIT. *****/
  const {
    location,
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
  } = useFormConstants(initialFormData);

  const [showDocumentModal, setShowDocumentModal] = useState(false);

  /**************** FORM FIELDS ***************/
  const fieldsConfig = [
    {
      label: "Applic. Id",
      id: "id",
      readOnly: true,
      mainId: true,
    },
    {
      label: "Date",
      id: "date",
      type: FIELD_TYPES.DATE,
      required: true,
      ref: focusFieldRef,
    },
    {
      label: "Customer",
      id: "customer",
      idField: "customerId",
      displayValue: ["firstName", "lastName"],
      type: FIELD_TYPES.SELECT_REACT,
      required: true,
    },
    {
      label: "Visa Type",
      id: "visaType",
      type: FIELD_TYPES.SELECT,
      required: true,
    },
    {
      label: "Country",
      id: "country",
      type: FIELD_TYPES.SELECT,
      required: true,
    },
    {
      label: "Reference",
      id: "reference",
      required: true,
    },
    {
      label: "Submission Date",
      id: "submissionDate",
      type: FIELD_TYPES.DATE,
      required: true,
    },
    {
      label: "Biometric App.",
      id: "biometricAppointment",
      type: FIELD_TYPES.DATE_TIME,
    },
    {
      label: "Consular App.",
      id: "consularAppointment",
      type: FIELD_TYPES.DATE_TIME,
    },
    { label: "Cost", id: "cost" },
    {
      label: "Status",
      id: "status",
      type: FIELD_TYPES.SELECT,
      idField: "value",
    }, // idField = Value when is loaded from Enums.
    {
      label: "Notes",
      id: "notes",
      required: true,
      type: FIELD_TYPES.TEXTAREA,
    },
  ];
  const renderAdditionalContent = () => (
    <div className="button-group-wrapper">
      {/* Open Document Modal on Click */}
      <div
        className="image-button"
        onClick={() => setShowDocumentModal(true)}
        style={{ cursor: "pointer" }}
      >
        <img
          src="/img/document.jpg"
          alt="new"
          style={{ width: "75px", height: "75px" }}
        />
        <span>Documents</span>
      </div>
    </div>
  );

  const modalReference = formData.reference;


  /************* GET PRIMARY ID FIELD *************/
  const primaryId = getPrimaryId(fieldsConfig, formData, formatToCamelCase(formName));
  
  /************* LIST TABLE CONFIG ************/
  const columns = generateTableColumns(fieldsConfig);

  /************* DROPDOWN STATE *************/
  const [dropdownOptions, setDropdownOptions] = useState(
    dropdownFieldConfigs.reduce((acc, { key }) => {
      acc[key] = []; // Initialize each option list to an empty array
      return acc;
    }, {})
  );

  const dropdownModalMap = dropdownFieldConfigs.reduce(
    (acc, { key, modalUrl }) => {
      acc[key] = !!modalUrl; // true if modalUrl exists
      return acc;
    },
    {}
  );

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
  /******** FORM FIELDS CONFIGURATION *********/
  /********************************************/
  const formFields = generateFormFields({
    fieldsConfig,
    formData,
    dropdownOptions,
    dropdownModalMap,
    setFormData,
    setModalState,
  });

  /********************************************/
  /************* LOGIC FUNCTIONS **************/
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
    const currentOptions = dropdownOptions[field] || [];
    addNewOption(field, newOptionWithId, fieldSetters, currentOptions);
    setFormData((prev) => ({
      ...prev,
      [field]: newOptionWithId.id,
    }));
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
          await loadData(API_ROUTES.LAST_RECORD);
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
    const missingFields = getMissingRequiredFields(formData, fieldsConfig);
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
        return FormFields.renderFileField(field, index, fileInputRef, setFormData);
      default:
        return FormFields.renderInputField(
          field,
          index,
          handleInputChange,
          setFormData
        );
    }
  };  

  /********************************************/
  /************* HTML FORM RENDER *************/
  /********************************************/
  return (
    <div className="form-container">
      <h1 className="embossed-text">{pageTitle}</h1>
      <ButtonsBar navigateRecord={navigateRecord} submitForm={submitForm} />
      <form onSubmit={submitForm}>
        <label className="form-state-label">
          {formData[primaryId] ? "Edit " + formName : "Add New " + formName}
        </label>

        <fieldset>
          <legend>Main Information</legend>
          <div className="fieldset-container">
            {formFields.map((field, index) => renderField(field, index))}
            {renderAdditionalContent()}
          </div>
        </fieldset>
      </form>
      {/* TABLE LIST SCROLLABLE */}
      {columns && columns.length > 0 && (
        <FormFields.TableList
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
      <DocumentModal
        showModal={showDocumentModal}
        closeModal={() => setShowDocumentModal(false)}
        token={token}
        recordId={formData[primaryId]}
        reference={modalReference}
        type={DOCUMENT_TYPE.VISA}
      />
      <MessagePopup
        message={message}
        isPopupOpen={isPopupOpen}
        setPopupOpen={setPopupOpen}
        type={messageType}
      />{" "}
    </div>
  );
};

export default VisaApplicationForm;
