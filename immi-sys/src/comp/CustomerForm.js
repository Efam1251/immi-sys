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

// Initial State.
const initialFormData = {
  customerId: null,
  firstName: "",
  lastName: "",
  address: "",
  phone: "",
  email: "",
  dateOfBirth: "",
  gender: null,
  maritalStatus: null,
  immigrationStatus: null,
  immigrationNumber: "",
  socialSecurity: "",
  localId: "",
  passportNumber: "",
  notes: "",
};

/******** CONFIGURATION CONSTANTS **********/
const formName = "Customer";
const pageTitle =
  formName.replace(/([a-z])([A-Z])/g, "$1 $2") + " Registration";
const classname = null;  // className: "form-group-flex", Default: "form-group".
const url_form = API_ROUTES.CUSTOMER_FORM; // Url to load the data in the form.
const url_list = API_ROUTES.CUSTOMER_ALL; // Url for rendering a list of records at the bottom.

/******* DROPDOWN CONFIGURATION LIST ********/
const dropdownFieldConfigs = [
  {
    key: "gender", // Dropdown field key.
    dropdownUrl: API_ROUTES.GENDER_LIST, // Url for populating the dropdown.
    modalUrl: API_ROUTES.GENDER_FORM, // if no url, it wont be auxiliar button added.
  },
  {
    key: "maritalStatus",
    dropdownUrl: API_ROUTES.MARITAL_STATUS_LIST,
    modalUrl: API_ROUTES.MARITAL_STATUS_FORM,
  },
  {
    key: "immigrationStatus",
    dropdownUrl: API_ROUTES.IMMIGRATION_STATUS_LIST,
    modalUrl: API_ROUTES.IMMIGRATION_STATUS_FORM,
  },
];

/********************************************/
/************* FORM DECLARATION *************/
/********************************************/
const CustomerForm = () => {
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

  /**************** Form Fields config ***************/
  const fieldsConfig = [
    {
      label: "Customer Id",
      id: "customerId",
      readOnly: true,
      mainId: true,
      copyRecord: true,
    },
    {
      label: "First Name",
      id: "firstName",
      required: true,
      ref: focusFieldRef,
      useInList: false,
      width: "25%",
      copyRecord: true,
    },
    {
      label: "Last Name",
      id: "lastName",
      required: true,
      useInList: false,
      width: "25%",
      copyRecord: true,
    },
    {
      label: "Address",
      id: "address",
      type: FIELD_TYPES.TEXTAREA,
      required: true,
      openInMap: true,
    },
    {
      label: "Phone",
      id: "phone",
      required: true,
      useInList: false,
      width: "15%",
    },
    {
      label: "Email",
      id: "email",
      type: FIELD_TYPES.EMAIL,
      required: true,
      useInList: false,
      width: "35%",
    },
    {
      label: "Date of Birth",
      id: "dateOfBirth",
      type: FIELD_TYPES.DATE,
      required: true,
      copyRecord: true,
    },
    { label: "Gender", id: "gender", type: FIELD_TYPES.SELECT },
    { label: "Marital Status", id: "maritalStatus", type: FIELD_TYPES.SELECT },
    {
      label: "Immigration Status",
      id: "immigrationStatus",
      type: FIELD_TYPES.SELECT,
    },
    { label: "Immigration Number", id: "immigrationNumber" },
    { label: "Social Security", id: "socialSecurity" },
    { label: "Local ID", id: "localId" },
    { label: "Passport Number", id: "passportNumber" },
    { label: "Notes", id: "notes", type: FIELD_TYPES.TEXTAREA },
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

export default CustomerForm;
