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
import PetitionDependentModal from "./PetitionDependentModal";

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
  fetchData,
  fetchAndSetData,
  fetchFormData,
  handleSubmit,
  eventDeleteHandler,
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

// INITIAL STATE
const initialFormData = {
  petitionId: null,
  processDate: "",
  client: null,
  petitioner: null,
  beneficiary: null,
  priorityDate: "",
  category: null,
  uscisNumber: "",
  uscisOffice: null,
  uscisPayment: "",
  uscisPaymentDate: "",
  uscisApprovalDate: "",
  nvcNumber: "",
  invoiceNumber: "",
  nvcPayment: "",
  nvcPaymentDate: "",
  ds260Form: false,
  petitionerAffidavit: false,
  sponsorAffidavit: false,
  nvcApprovalDate: "",
  consularAppointmentDate: "",
  vaccineAppointmentDate: "",
  petitionNotes: "",
  petitionStatus: null,
};

/******** CONFIGURATION CONSTANTS **********/
const formName = "Petition";
const pageTitle =
  formName.replace(/([a-z])([A-Z])/g, "$1 $2") + " Registration";
const classname = null; // className: "form-group-flex", Default: "form-group".
const url_form = API_ROUTES.PETITION_FORM;
const url_list = API_ROUTES.PETITION_LIST;

/******* DROPDOWN CONFIGURATION LIST ********/
const dropdownFieldConfigs = [
  {
    key: "client",
    dropdownUrl: API_ROUTES.CUSTOMER_ALL,
    modalUrl: null,
  },
  {
    key: "petitioner",
    dropdownUrl: API_ROUTES.CUSTOMER_ALL,
    modalUrl: null,
  },
  {
    key: "beneficiary",
    dropdownUrl: API_ROUTES.CUSTOMER_ALL,
    modalUrl: null,
  },
  {
    key: "category",
    dropdownUrl: API_ROUTES.CATEGORY_LIST,
    modalUrl: API_ROUTES.CATEGORY_FORM,
  },
  {
    key: "uscisOffice",
    dropdownUrl: API_ROUTES.USCIS_OFFICE_LIST,
    modalUrl: API_ROUTES.USCIS_OFFICE_FORM,
  },
  {
    key: "petitionStatus",
    dropdownUrl: API_ROUTES.STATUS_LIST,
    modalUrl: null,
  },
  {
    key: "dependentTypeOptions",
    dropdownUrl: API_ROUTES.PETITION_DEPENDENT_TYPE_LIST,
    modalUrl: null,
  },
  {
    key: "documentOptions",
    dropdownUrl: API_ROUTES.DOCUMENT_LIST,
    modalUrl: null,
  },
];

/********************************************/
/************* FORM DECLARATION *************/
/********************************************/
const Petition = () => {
  /***** Standard constants. *****/
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

  const [modalConfig, setModalConfig] = useState(false);
  const [showPetitionDependentModal, setShowPetitionDependentModal] =
    useState(false);
  const [showDocumentModal, setShowDocumentModal] = useState(false);
  const [processEvents, setProcessEvents] = useState([]);
  const [showUscisEventProcessModal, setShowUscisEventProcessModal] =
    useState(false);
  const [selectedEvent, setSelectedEvent] = useState(null);

  const caseNumber = formData.uscisNumber
    ? formData.uscisNumber
    : formData.nvcNumber;
  const modalReference = caseNumber;

  const config = {
    idField: "petitionId",
    url_List: `/api/immigration/dependent/petitionDependent-list?petitionId=${formData.petitionId}`,
    url_Save: `/api/immigration/dependent/save`,
    url_Delete: `/api/immigration/dependent/delete/${formData.petitionId}/${formData.dependentId}`,
  };
  useEffect(() => {
    setModalConfig(config);
  }, [formData.petitionId, formData.dependentId]);

  /**************** FORM FIELDS ***************/
  const fieldsConfig = [
    {
      label: "Petition Id",
      id: "petitionId",
      readOnly: true,
      mainId: true,
    },
    {
      label: "Process Date",
      id: "processDate",
      type: FIELD_TYPES.DATE,
      required: true,
      ref: focusFieldRef,
    },
    {
      label: "Client",
      id: "client",
      idField: "customerId",
      displayValue: ["firstName", "lastName"],
      type: FIELD_TYPES.SELECT_REACT,
      required: true,
    },
    {
      label: "Petitioner",
      id: "petitioner",
      idField: "customerId",
      displayValue: ["firstName", "lastName"],
      type: FIELD_TYPES.SELECT_REACT,
      required: true,
    },
    {
      label: "Beneficiary",
      id: "beneficiary",
      idField: "customerId",
      displayValue: ["firstName", "lastName"],
      type: FIELD_TYPES.SELECT_REACT,
      required: true,
    },
    {
      label: "Priority Date",
      id: "priorityDate",
      type: FIELD_TYPES.DATE,
      required: true,
    },
    {
      label: "Category",
      id: "category",
      type: FIELD_TYPES.SELECT,
      required: true,
    },
    {
      label: "USCIS Number",
      id: "uscisNumber",
      required: true,
    },
    {
      label: "USCIS Office",
      id: "uscisOffice",
      type: FIELD_TYPES.SELECT,
      required: true,
    },
    {
      label: "USCIS Payment",
      id: "uscisPayment",
      type: FIELD_TYPES.NUMBER,
    },
    {
      label: "USCIS Payment Date",
      id: "uscisPaymentDate",
      type: FIELD_TYPES.DATE,
    },
    {
      label: "USCIS Approval Date",
      id: "uscisApprovalDate",
      type: FIELD_TYPES.DATE,
    },
    {
      label: "NVC Number",
      id: "nvcNumber",
    },

    {
      label: "Invoice Number",
      id: "invoiceNumber",
    },
    {
      label: "NVC Payment",
      id: "nvcPayment",
      type: FIELD_TYPES.NUMBER,
    },
    {
      label: "NVC Payment Date",
      id: "nvcPaymentDate",
      type: FIELD_TYPES.DATE,
    },
    {
      label: "DS260 Form",
      id: "ds260Form",
      type: FIELD_TYPES.CHECKBOX,
    },
    {
      label: "Petitioner Affidavit",
      id: "petitionerAffidavit",
      type: FIELD_TYPES.CHECKBOX,
    },
    {
      label: "Sponsor Affidavit",
      id: "sponsorAffidavit",
      type: FIELD_TYPES.CHECKBOX,
    },
    {
      label: "NVC Approval Date",
      id: "nvcApprovalDate",
      type: FIELD_TYPES.DATE,
    },
    {
      label: "Consular Appointment Date",
      id: "consularAppointmentDate",
      type: FIELD_TYPES.DATE,
    },
    {
      label: "Vaccine Appointment Date",
      id: "vaccineAppointmentDate",
      type: FIELD_TYPES.DATE,
    },
    {
      label: "Petition Status",
      id: "petitionStatus",
      type: "select",
      idField: "value",
    },
    {
      label: "Petition Decision",
      id: "petitionDecision",
      type: "select",
      idField: "value",
    },
    {
      label: "Petition Notes",
      id: "petitionNotes",
      type: FIELD_TYPES.TEXTAREA,
    },
  ];

  const renderAdditionalContent = () => (
    <div className="button-group-wrapper">
      <ModalFormButton
        onClick={() => setShowPetitionDependentModal(true)}
        imageSrc="/img/dependiente.png"
        altText="new"
        label="Add Dependents"
      />
      <ModalFormButton
        onClick={() => setShowDocumentModal(true)}
        imageSrc="/img/document.jpg"
        altText="new"
        label="Add Document"
      />
    </div>
  );

  /************* GET PRIMARY ID FIELD *************/
  const primaryId = getPrimaryId(
    fieldsConfig,
    formData,
    formatToCamelCase(formName)
  );

  /************* LIST TABLE CONFIG ************/
  const columns = generateTableColumns(fieldsConfig);

  /************* DROPDOWN STATE *************/
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
    //console.log("Form data before submission:", formData);
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
      <PetitionDependentModal
          showModal={showPetitionDependentModal}
          closeModal={() => setShowPetitionDependentModal(false)}
          petitionId={formData.petitionId}
          dependentTypeOptions={dropdownOptions["dependentTypeOptions"]}
          modalConfig={modalConfig}
        />
      {/* Document Modal */}
      <DocumentModal
        showModal={showDocumentModal}
        closeModal={() => setShowDocumentModal(false)}
        token={token}
        recordId={formData[primaryId]}
        reference={modalReference}
        type={DOCUMENT_TYPE.USCIS_PROCESS}
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

export default Petition;
