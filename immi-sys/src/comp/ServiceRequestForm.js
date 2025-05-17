// 🔁 React Core Imports
import React, { useState, useEffect, useCallback } from "react";

// 🌐 API & Configuration
import { API_ROUTES } from "./config";

// 🧩 Component Imports
import ButtonsBar from "../Utils/ButtonsBar";
import ModalFormButton from "../Utils/ModalFormButton";
import MessagePopup from "./MessagePopup";
import Modal from "./Modal";
import DocumentModal from "./DocumentModal";

// 🧾 Form Field Renderers
import * as FormFields from "./FormFields";

// 🛠️ Custom Hooks (Form Related)
import {
  useFormConstants,
  useFilteredItems,
  useHandleRowClick,
  useFetchDropdownOptions,
} from "../Utils/formHooks";

// 📡 API Utility Functions
import {
  fetchAndSetData,
  fetchFormData,
  handleSubmit,
} from "../Utils/apiUtils";

// 🧮 Form Utility Functions
import {
  FIELD_TYPES,
  DOCUMENT_TYPE,
  getPrimaryId,
  FormRenderer,
  generateFormFields,
  renderField,
  getMissingRequiredFields,
  generateTableColumns,
  handleNavigateRecord,
  addNewOption,
  handleInputChange,
  useDropdownManager,
  initializeAndHandleModalStates,
} from "../Utils/formUtils";

// 🔤 Common Utilities
import { formatToCamelCase } from "../Utils/commonUtils";

// 🎨 Styles
import "../css/UtilForm.css";

const initialFormData = {
  id: null,
  service: null,
  customer: null,
  requestDate: "",
  notes: "",
  status: null,
  createdBy: "",
  referenceCode: "",
};

/******** CONFIGURATION CONSTANTS **********/
const formName = "ServiceRequest";
const pageTitle = formName.replace(/([a-z])([A-Z])/g, "$1 $2") + " Registration";
const classname = null; // className: "form-group-flex", Default: "form-group".
const url_form = API_ROUTES.SERVICE_REQUEST_FORM;
const url_list = API_ROUTES.SERVICE_REQUEST_LIST;

/******* DROPDOWN CONFIGURATION LIST ********/
const dropdownFieldConfigs = [
  {
    key: "service",
    dropdownUrl: API_ROUTES.SERVICE_LIST,
    modalUrl: null,
  },
  {
    key: "customer",
    dropdownUrl: API_ROUTES.CUSTOMER_ALL,
    modalUrl: null,
  },
  {
    key: "status",
    dropdownUrl: API_ROUTES.STATUS_LIST,
    modalUrl: null,
  },
];

/********************************************/
/************* FORM DECLARATION *************/
/********************************************/

/***** Standard constants. *****/
const ServiceRequestForm = () => {
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

  const modalReference = formData.referenceCode;

  /**************** Form Fields config ***************/
  const fieldsConfig = [
    {
      label: "Request ID",
      id: "id",
      type: FIELD_TYPES.TEXT,
      readOnly: true,
      mainId: true,
      required: false,
    },
    {
      label: "Service",
      id: "service",
      type: FIELD_TYPES.SELECT_REACT,
      idField: "serviceId",
      displayValue: ["serviceName"],
      required: true,
      ref: focusFieldRef,
    },
    {
      label: "Customer",
      id: "customer",
      type: FIELD_TYPES.SELECT_REACT,
      idField: "customerId",
      displayValue: ["firstName", "lastName"],
      required: true,
    },
    {
      label: "Request Date",
      id: "requestDate",
      type: FIELD_TYPES.DATE,
      required: true,
    },
    {
      label: "Status",
      id: "status",
      type: FIELD_TYPES.SELECT,
      idField: "value",
      required: true,
    },
    {
      label: "Created By",
      id: "createdBy",
      type: FIELD_TYPES.TEXT,
      required: false,
    },
    {
      label: "Notes",
      id: "notes",
      type: FIELD_TYPES.TEXTAREA,
      required: false,
    },
    {
      label: "Reference Id",
      id: "referenceCode",
      type: FIELD_TYPES.TEXT,
      readOnly: true,
      required: false,
    },
  ];

  /************* AUXILIAR BUTTONS CONFIGURATION *************/
  const renderAdditionalContent = () => (
    <div className="button-group-wrapper">
      <ModalFormButton
        onClick={() => setShowDocumentModal(true)}
        imageSrc="/img/document.jpg"
        altText="new"
        label="Add Document"
      />
    </div>
  );

  /************* PRIMARY ID FIELD *************/
  const primaryId = getPrimaryId(
    fieldsConfig,
    formData,
    formatToCamelCase(formName)
  );

  /************* FORM DATA TABLE COLUMN CONFIGURATION *************/
  const columns = generateTableColumns(fieldsConfig);

  /************* DROPDOWN STATE MANAGEMENT *************/
  const {
    dropdownOptions, // Options for dropdown fields
    dropdownModalMap, // Map linking dropdowns to their modals
    fieldSetters, // Setters for each dropdown field
    dropdownFetchConfig, // Config for fetching dropdown data
  } = useDropdownManager(dropdownFieldConfigs);

  // Fetch initial dropdown options
  useFetchDropdownOptions(dropdownFetchConfig);

  /************* MODAL FORM STATE MANAGEMENT *************/
  const [modalStates, setModalStates] = useState(
    Object.fromEntries(dropdownFieldConfigs.map(({ key }) => [key, false]))
  );

  // Setup modal forms and their toggle function
  const { modalForms, setModalState } = initializeAndHandleModalStates(
    dropdownFieldConfigs,
    modalStates,
    setModalStates,
    handleAddOption
  );

  /************* FORM FIELD CONFIGURATION *************/
  const formFields = generateFormFields({
    fieldsConfig, // Configurations for each form field
    formData, // Current form data state
    dropdownOptions, // Populated dropdown choices
    dropdownModalMap, // Modal trigger mappings
    setFormData, // Function to update form data
    setModalState, // Function to open/close modals
  });

  /************* LOGIC FUNCTIONS **************/

  /** Load form data and associated events based on direction (next, previous, etc.) */
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

  /** Handle adding a new option to a dropdown and update form data */
  function handleAddOption(field, newOptionWithId) {
    const currentOptions = dropdownOptions[field] || [];
    addNewOption(field, newOptionWithId, fieldSetters, currentOptions);
    setFormData((prev) => ({
      ...prev,
      [field]: newOptionWithId.id,
    }));
  }

  /************* FILTERING & SEARCH **************/

  /** Filter table/list items based on search input and defined columns */
  const filteredItems = useFilteredItems(formList, columns, searchTerm);

  /************* ROW CLICK HANDLING **************/

  /** Handle table row click to load record data */
  const handleRowClick = useHandleRowClick(
    focusFieldRef,
    currentRecordIdRef,
    loadData
  );

  /************* RECORD NAVIGATION **************/

  /** Navigate through form records (e.g., next/prev) */
  const navigateRecord = (direction) => {
    handleNavigateRecord(
      direction,
      loadData,
      setFormData,
      initialFormData,
      focusFieldRef
    );
  };

  /************* INITIAL DATA LOAD ON MOUNT *************/
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

  /************* FORM SUBMISSION HANDLER *************/
  const submitForm = async (e) => {
    e.preventDefault();

    // Validate required fields
    const missingFields = getMissingRequiredFields(formData, fieldsConfig);
    if (missingFields.length > 0) {
      setMessage("Please fill out the required fields marked with *");
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

  /************* RENDER FORM FIELD CONFIGURATION *************/
  const renderConfiguredField = (field, index) =>
    renderField(field, index, {
      classname,
      handleInputChange,
      setFormData,
      fileInputRef,
      FormFields,
    });

  /************* HTML FORM RENDER *************/
  return (
    <div className="form-container">
      <h1 className="embossed-text">{pageTitle}</h1>
      {/* BUTTONS BAR */}
      <ButtonsBar navigateRecord={navigateRecord} submitForm={submitForm} />
      {/* FORM FIELDS */}
      <FormRenderer
        formData={formData}
        primaryId={primaryId}
        formName={formName}
        formFields={formFields}
        renderField={renderConfiguredField}
        submitForm={submitForm}
        renderAdditionalContent={renderAdditionalContent}
      />
      {/* REGULAR TABLE LIST SCROLLABLE */}
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
      {/* Modal Forms */}
      {modalForms.map((modal) => (
        <Modal key={modal.id} {...modal} />
      ))}
      {/* USCIS Event Process Modal */}
      {/* Document Modal */}
      <DocumentModal
        showModal={showDocumentModal}
        closeModal={() => setShowDocumentModal(false)}
        token={token}
        recordId={formData[primaryId]}
        reference={modalReference}
        type={DOCUMENT_TYPE.SERVICE_REQUEST_DOCUMENT}
      />
      {/* Message Popup */}
      <MessagePopup
        message={message}
        isPopupOpen={isPopupOpen}
        setPopupOpen={setPopupOpen}
        type={messageType}
      />{" "}
    </div>
  );
};

export default ServiceRequestForm;
