// React Core
import React, { useState, useEffect, useCallback } from "react";

// Styles
import "../css/UtilForm.css";

// Configs
import { API_ROUTES } from "./config";

// Components
import ButtonsBar from "../Utils/ButtonsBar";
import MessagePopup from "./MessagePopup";
import Modal from "./Modal";

// Form Field Renderers
import * as FormFields from "./FormFields";

// Hooks - Form-Related
import {
  useFormConstants,
  useFilteredItems,
  useHandleRowClick,
  useFetchDropdownOptions,
} from "../Utils/formHooks";

// API Utilities
import {
  fetchAndSetData,
  fetchFormData,
  handleSubmit,
} from "../Utils/apiUtils";

// Form Utilities
import {
  FIELD_TYPES,
  clearFormDataExceptCopy,
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

// Common Utilities
import { formatToCamelCase } from "../Utils/commonUtils";

// INITIAL STATE
const initialFormData = {
  serviceId: null,
  serviceName: "",
  serviceDescription: "",
  serviceType: null,
  unitPrice: "",
  notes: "",
};

/******** CONFIGURATION CONSTANTS **********/
const formName = "Service";
const pageTitle = formName + " Registration";
const classname = null;  // className: "form-group-flex", Default: "form-group".
const url_form = API_ROUTES.SERVICE_FORM;
const url_list = API_ROUTES.SERVICE_LIST;

/******* DROPDOWN CONFIGURATION LIST ********/
const dropdownFieldConfigs = [
  {
    key: "serviceType",
    dropdownUrl: API_ROUTES.SERVICETYPE_LIST,
    modalUrl: API_ROUTES.SERVICETYPE_FORM,
  },
  {
    key: "isIncomeService",
    dropdownUrl: API_ROUTES.FEE_TYPE_LIST,
    modalUrl: null,
  },
];

/********************************************/
/************* FORM DECLARATION *************/
/********************************************/
const ServiceForm = () => {
  /***** STANDARD CONSTANTS. DO NOT EDIT. *****/
  const {
    location,
    searchParams,
    focusFieldRef,
    fileInputRef,
    currentRecordIdRef,
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
    {
      label: "Service Id",
      id: "serviceId",
      readOnly: true,
      useInList: true,
      width: "10%",
    },
    {
      label: "Service Name",
      id: "serviceName",
      required: true,
      ref: focusFieldRef,
      useInList: true,
      width: "40%",
    },
    {
      label: "Service Description",
      id: "serviceDescription",
      type: FIELD_TYPES.TEXTAREA,
      required: true,
    },
    {
      label: "Unit Price",
      id: "unitPrice",
      type: FIELD_TYPES.NUMBER,
      required: true,
      useInList: true,
      width: "20%",
    },
    {
      label: "Service Type",
      id: "serviceType",
      type: FIELD_TYPES.SELECT,
      useInList: true,
      width: "30%",
    },
    {
      label: "Service Fee",
      id: "isIncomeService",
      type: FIELD_TYPES.SELECT,
      idField: "value",
      required: true,
    },
    {
      label: "Notes",
      id: "notes",
      type: FIELD_TYPES.TEXTAREA,
    },
  ];


  /************* FORM DATA INITIALIZATION *************/

  /************* GET PRIMARY ID FIELD *************/
  const primaryId = getPrimaryId(
    fieldsConfig,
    formData,
    formatToCamelCase(formName)
  );

  /********************************************/
  const copyRecord = () => {
    const cleared = clearFormDataExceptCopy(
      formData,
      fieldsConfig,
      focusFieldRef,
      primaryId
    );
    setFormData(cleared);
  };

  /************* FORM DATA TABLE COLUMN CONFIGURATION *************/
  const columns = generateTableColumns(fieldsConfig);

  /********************************************/
  const {
    dropdownOptions,
    dropdownModalMap,
    fieldSetters,
    dropdownFetchConfig,
  } = useDropdownManager(dropdownFieldConfigs);

  useFetchDropdownOptions(dropdownFetchConfig);

  /********************************************/
  const [modalStates, setModalStates] = useState(
    Object.fromEntries(dropdownFieldConfigs.map(({ key }) => [key, false]))
  );

  // 🟢 Pass modalStates and setModalStates
  const { modalForms, setModalState } = initializeAndHandleModalStates(
    dropdownFieldConfigs,
    modalStates,
    setModalStates,
    handleAddOption
  );

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
  const renderConfiguredField = (field, index) =>
    renderField(field, index, {
      classname,
      handleInputChange,
      setFormData,
      fileInputRef,
      FormFields,
    });

  /********************************************/
  /************* HTML FORM RENDER *************/
  /********************************************/
  return (
    <div className="form-container">
      <h1 className="embossed-text">{pageTitle}</h1>
      {/* BUTTONS BAR */}
      <ButtonsBar
        navigateRecord={navigateRecord}
        submitForm={submitForm}
        copyRecord={copyRecord}
      />
      {/* FORM FIELDS */}
      <FormRenderer
        formData={formData}
        primaryId={primaryId}
        formName={formName}
        formFields={formFields}
        renderField={renderConfiguredField}
        submitForm={submitForm}
      />
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
      {/* MODAL FORMS */}
      {modalForms.map((modal) => (
        <Modal key={modal.id} {...modal} />
      ))}
      {/* MESSAGE POPUP */}
      <MessagePopup
        message={message}
        isPopupOpen={isPopupOpen}
        setPopupOpen={setPopupOpen}
        type={messageType}
      />{" "}
    </div>
  );
};

export default ServiceForm;
