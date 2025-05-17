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
import ApplicationEventModal from "./ApplicationEventModal";
import ProcessEventsTable from "../Utils/ProcessEventsTable";

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
  petitionStatus: null,
  petitionNotes: "",
};

/******** CONFIGURATION **********/
const formName = "Petition";
const pageTitle = formName.replace(/([a-z])([A-Z])/g, "$1 $2") + " Registration";
const classname = null; // className: "form-group-flex", Default: "form-group".
const sourceType = "Uscis_Petition"; // Source type for the process events
const url_form = API_ROUTES.USCIS_PETITION_FORM;
const url_list = API_ROUTES.USCIS_PETITION_LIST;
const url_delete = API_ROUTES.APPLICATION_EVENT_DELETE;
const url_eventList = API_ROUTES.APPLICATION_EVENT_LIST;

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
    key: "eventTypeOptions",
    dropdownUrl: API_ROUTES.APPLICATION_EVENT_TYPE_LIST,
    modalUrl: null,
  },
];

const PetitionEventsColumns = [
  { key: "eventType", label: "Type", width: "25%" },
  { key: "eventDate", label: "Date", width: "20%" },
  { key: "status", label: "Status", width: "25%" },
  { key: "location", label: "Reference", width: "20%" },
  { key: "actions", label: "Actions", width: "10%" },
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
  const [showPetitionDependentModal, setShowPetitionDependentModal] = useState(false);
  const [showDocumentModal, setShowDocumentModal] = useState(false);
  const [petitionEvents, setPetitionEvents] = useState([]);
  const [showUscisEventProcessModal, setShowUscisEventProcessModal] = useState(false);
  const [selectedEvent, setSelectedEvent] = useState(null);

  const caseNumber = formData.uscisNumber
    ? formData.uscisNumber
    : formData.nvcNumber;
  const modalReference = caseNumber;

  const config = {
    idField: "petitionId",
    url_List: `${API_ROUTES.PETITION_DEPENDENT_LIST}?petitionId=${formData.petitionId}`,
    url_Save: API_ROUTES.PETITION_DEPENDENT_SAVE,
    url_Delete: `${API_ROUTES.PETITION_DEPENDENT_DELETE}/${formData.petitionId}/${formData.dependentId}`,
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
      label: "Petition Status",
      id: "petitionStatus",
      type: "select",
      idField: "value",
    },
    {
      label: "Petition Notes",
      id: "petitionNotes",
      type: FIELD_TYPES.TEXTAREA,
    },
  ];

  /************* AUXILIAR BUTTONS CONFIGURATION *************/
  const renderAdditionalContent = () => (
    <div className="button-group-wrapper">
      <ModalFormButton
        onClick={() => setShowUscisEventProcessModal(true)}
        imageSrc="/img/Add-48.png"
        altText="new"
        label="Event"
      />
      <ModalFormButton
        onClick={() => setShowPetitionDependentModal(true)}
        imageSrc="/img/dependiente.png"
        altText="new"
        label="Dependents"
      />
      <ModalFormButton
        onClick={() => setShowDocumentModal(true)}
        imageSrc="/img/document.jpg"
        altText="new"
        label="Document"
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
  const {
    dropdownOptions,
    dropdownModalMap,
    fieldSetters,
    dropdownFetchConfig,
  } = useDropdownManager(dropdownFieldConfigs);

  useFetchDropdownOptions(dropdownFetchConfig);

  /************* MODAL STATE *************/
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

  /******** FORM FIELDS CONFIGURATION *********/
  const formFields = generateFormFields({
    fieldsConfig,
    formData,
    dropdownOptions,
    dropdownModalMap,
    setFormData,
    setModalState,
  });

  /************* LOAD DATA TO FORM **************/
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
        fetchProcessEvents();
      } catch (error) {
        console.error("Error loading data:", error);
      }
    },
    [setFormData, currentRecordIdRef, setMessage, setMessageType, setPopupOpen]
  );

  /************* HANDLE ADD TO DROPDOWNS **************/
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

  /** Handle click on an event row to display details in modal */
  const handleEventRowClick = (eventData) => {
    setSelectedEvent(eventData);
    setShowUscisEventProcessModal(true);
  };

  /************* NAVIGAT RECORDS IN FORM **************/
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

  /************* USCIS PROCESS EVENTS HANDLING *************/

  /** Fetch and update process events for current record */
  const fetchProcessEvents = async () => {
    if (!currentRecordIdRef.current) return;
  
    const params = new URLSearchParams({
      sourceType: sourceType,
      id: currentRecordIdRef.current,
    });
  
    const data = await fetchData(url_eventList, params, token);
    if (data) setPetitionEvents(data);
  };

  /** Refresh process events whenever the modal closes */
  useEffect(() => {
    if (!showUscisEventProcessModal) {
      fetchProcessEvents();
    }
  }, [showUscisEventProcessModal]);

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

  /************* EVENT DELETE HANDLER *************/
  const handleDeleteEvent = async (eventId) => {
    await eventDeleteHandler({
      eventId,
      sourceType,
      token,
      apiUrl: url_delete,
    });
    fetchProcessEvents();
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
      {/* USCIS PROCESS EVENTS TABLE */}
      <ProcessEventsTable
        columns={PetitionEventsColumns} // Pass columns dynamically
        processEvents={petitionEvents}
        handleEventRowClick={handleEventRowClick}
        handleDeleteEvent={handleDeleteEvent}
      />
      {/* Modal Forms */}
      {modalForms.map((modal) => (
        <Modal key={modal.id} {...modal} />
      ))}
      {/* USCIS Event Process Modal */}
      <ApplicationEventModal
        showModal={showUscisEventProcessModal}
        closeModal={() => {
          setShowUscisEventProcessModal(false);
          setSelectedEvent(null); // Clear selected event when closing
        }}
        id={formData[primaryId]}
        reference={modalReference}
        eventTypeOptions={dropdownOptions["eventTypeOptions"]}
        statusOptions={dropdownOptions["petitionStatus"]}
        token={token}
        initialData={selectedEvent} // <- pass data to modal
        sourceType={sourceType}
      />
      {/* Petition Dependent Modal */}
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
        type={DOCUMENT_TYPE.PETITION_DOCUMENT}
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
