export const API_ROUTES = {
  // ===================== Location =====================
  COUNTRY_FORM: "/api/common/location/countries",
  COUNTRY_LIST: "/api/common/location/countries/list",
  STATE_FORM: "/api/common/location/states",
  STATE_LIST: "/api/common/location/states/list",

  // ===================== Forms =====================
  FORMS_FORM: "/api/common/forms",
  FORMS_LIST: "/api/common/forms/list",

  // ===================== Services =====================
  SERVICE_REQUEST_ALL: "/api/common/service-requests/all",
  SERVICE_REQUEST_FORM: "/api/common/service-requests",
  SERVICE_REQUEST_LIST: "/api/common/service-requests/list",

  // ===================== Services =====================
  SERVICE_ALL: "/api/common/services/all",
  SERVICE_FORM: "/api/common/services",
  SERVICE_LIST: "/api/common/services/list",
  SERVICETYPE_FORM: "/api/common/serviceTypes",
  SERVICETYPE_LIST: "/api/common/serviceTypes/list",

  // ===================== Customers =====================
  CUSTOMER_ALL: "/api/common/customers/all",
  CUSTOMER_DROPLIST: "/api/common/customers/droplist",
  CUSTOMER_FORM: "/api/common/customers",
  CUSTOMER_LIST: "/api/common/customers/list",
  CUSTOMER_SELECT: "/api/common/customers/dropdown",

  // ===================== Petitions =====================
  PETITION_FORM: "/api/immigration/petitions",
  PETITION_LIST: "/api/immigration/petitions/list",

  // ===================== USCIS Petitions =====================
  PETITION_EVENTS_DELETE: "/api/immigration/uscis-petitions/event-delete",
  PETITION_EVENTS_LIST: "/api/immigration/uscis-petitions/events",
  PETITION_EVENTS_SAVE: "/api/immigration/uscis-petitions/event-save",
  USCIS_PETITION_FORM: "/api/immigration/uscis-petitions",
  USCIS_PETITION_LIST: "/api/immigration/uscis-petitions/list",
  PETITION_EVENT_TYPE_LIST: "/api/common/enums/uscis-petition-event-type",

// ===================== Application =====================
  APPLICATION_FORM: "/api/immigration/application",
  APPLICATION_LIST: "/api/immigration/application/list",
  
  // ===================== Event Process =====================
  APPLICATION_EVENT_LIST: "/api/immigration/application-event/events",
  APPLICATION_EVENT_SAVE: "/api/immigration/application-event/event-save",
  APPLICATION_EVENT_DELETE: "/api/immigration/application-event/event-delete",
  APPLICATION_EVENT_TYPE_LIST: "/api/common/enums/application-event-type",
  
  // ===================== USCIS Process =====================
  USCIS_PROCESS_FORM: "/api/immigration/uscis-processes",
  USCIS_PROCESS_LIST: "/api/immigration/uscis-processes/list",

  // ===================== Visa Applications =====================
  VISA_APPLICATION_FORM: "/api/immigration/visaApplications",
  VISA_APPLICATION_LIST: "/api/immigration/visaApplications/list",

  // ===================== Citizenship Applications =====================
  CITIZENSHIP_APPLICATION_FORM: "/api/immigration/citizenship",
  CITIZENSHIP_APPLICATION_LIST: "/api/immigration/citizenship/list",

  // ===================== Invoices =====================
  INCOME_PROFIT_REPORT: "/api/common/invoice/invoice-profit",
  INVOICE_EMAIL: "/api/common/invoice/email-invoice",
  INVOICE_FORM: "/api/common/invoice/form",
  INVOICE_LAST: "/api/common/invoice/last-invoice",
  INVOICE_LIST: "/api/common/invoice/list",
  INVOICE_OPEN_BALANCE: "/api/common/invoice/open-balance",
  INVOICE_STATUS: "/api/common/invoice/invoice-status-list",
  INVOICE_STATUS_LIST: "/api/common/enums//invoice_Status_List",
  INVOICE_SUBMIT: "/api/common/invoice/submit",

  // ===================== Payments =====================
  PAYMENT_DELETE: "/api/common/payment/delete",
  PAYMENT_FORM: "/api/common/payment/payment",
  PAYMENT_LIST: "/api/common/payment/payment/list",
  PAYMENT_METHOD_LIST: "/api/common/payment/payment-methods",
  PAYMENT_SAVE: "/api/common/payment/save",
  PAYMENT_UPDATE: "/api/common/payment/update",
  PAYMENTS_BY_INVOICE: "/api/common/payment/payments-by-invoice",

  // ===================== Tax =====================
  TAX_FILING_FORM: "/api/tax/tax-filings",
  TAX_FILING_LIST: "/api/tax/tax-filings/list",

  // ===================== Documents =====================
  DOCUMENTS_DELETE: "/api/immigration/document/document-delete",
  DOCUMENTS_DROPDOWN: "/api/immigration/document/documents",
  DOCUMENTS_LIST: "/api/immigration/document/document-list",
  DOCUMENT_SAVE_UPLOAD: "/api/immigration/document/document-save-upload",
  DOCUMENT_SAVE: "/api/immigration/document/save-Document",
  PETITIONDOCUMENT_LIST: "/api/immigration/document/petitionDocuments",
  VISADOCUMENT_LIST: "/api/immigration/document/visaDocuments",

  // ===================== Dependents =====================
  DEPENDENT_LIST: "/api/immigration/dependent/dependentTypes",
  PETITION_DEPENDENT_DELETE: "/api/immigration/dependent/delete",
  PETITION_DEPENDENT_LIST: "/api/immigration/dependent/petitionDependent-list",
  PETITION_DEPENDENT_SAVE: "/api/immigration/dependent/save",
  PETITION_DEPENDENT_TYPE_LIST: "/api/immigration/dependent/dependentTypes",

  // ===================== Helper Entities =====================
  CATEGORY_FORM: "/api/common/categories",
  CATEGORY_LIST: "/api/common/categories/list",
  GENDER_FORM: "/api/common/genders",
  GENDER_LIST: "/api/common/genders/list",
  IMMIGRATION_STATUS_FORM: "/api/common/immigration-statuses",
  IMMIGRATION_STATUS_LIST: "/api/common/immigration-statuses/list",
  MARITAL_STATUS_FORM: "/api/common/marital-statuses",
  MARITAL_STATUS_LIST: "/api/common/marital-statuses/list",
  PETITION_STATUS_FORM: "/api/immigration/petitionstatus",
  PETITION_STATUS_LIST: "/api/immigration/petitionstatus/list",
  UNIT_OF_MEASURE_FORM: "/api/common/units-of-measure",
  UNIT_OF_MEASURE_LIST: "/api/common/units-of-measure/list",
  USCIS_OFFICE_FORM: "/api/immigration/uscisoffices",
  USCIS_OFFICE_LIST: "/api/immigration/uscisoffices/list",
  VISATYPE_FORM: "/api/immigration/visaTypes",
  VISATYPE_LIST: "/api/immigration/visaTypes/list",

  // ===================== Enums =====================
  CITIZENSHIP_STATUS_LIST: "/api/common/enums//citizenship_status_list",
  DECISION_LIST: "/api/common/enums/decision",
  FEE_TYPE_LIST: "/api/common/enums/fee_type",
  STATUS_LIST: "/api/common/enums/status",
  TAX_FILING_STATUS_LIST: "/api/common/enums/tax_filing_status",

  // ===================== Redirections =====================
  REDIRECT_TO_CITIZENSHIPAPPLICATIONFORM: "/dashboard/citizenship-application/form",
  REDIRECT_TO_CITIZENSHIPAPPLICATIONLIST: "/dashboard/citizenship-application/list",
  REDIRECT_TO_CUSTOMERFORM: "/dashboard/customer/form",
  REDIRECT_TO_CUSTOMERLIST: "/dashboard/customer/list",
  REDIRECT_TO_CUSTOMER_REPORT: "/dashboard/customer/report",
  REDIRECT_TO_APPLICATIONFORM: "/dashboard/application/form",
  REDIRECT_TO_APPLICATIONLIST: "/dashboard/application/list",
  REDIRECT_TO_FORMSFORM: "/dashboard/forms/form",
  REDIRECT_TO_FORMSLIST: "/dashboard/forms/list",
  REDIRECT_TO_INVOICEFORM: "/dashboard/invoice/form",
  REDIRECT_TO_INVOICELIST: "/dashboard/invoice/list",
  REDIRECT_TO_PROFIT_REPORT: "/dashboard/invoice/profit_report",
  REDIRECT_TO_PETITIONFORM: "/dashboard/petition/form",
  REDIRECT_TO_PETITIONLIST: "/dashboard/petition/list",
  REDIRECT_TO_USCISPETITIONFORM: "/dashboard/uscis-petition/form",
  REDIRECT_TO_USCISPETITIONLIST: "/dashboard/uscis-petition/list",
  REDIRECT_TO_SERVICEFORM: "/dashboard/service/form",
  REDIRECT_TO_SERVICELIST: "/dashboard/service/list",
  REDIRECT_TO_STATEFORM: "/dashboard/state/form",
  REDIRECT_TO_STATELIST: "/dashboard/state/list",
  REDIRECT_TO_TAX_FILING_FORM: "/dashboard/tax-filing/form",
  REDIRECT_TO_TAX_FILING_LIST: "/dashboard/tax-filing/list",
  REDIRECT_TO_USCIS_PROCESS_FORM: "/dashboard/uscis-process/form",
  REDIRECT_TO_USCIS_PROCESS_LIST: "/dashboard/uscis-process/list",
  REDIRECT_TO_VISAAPPLICATIONFORM: "/dashboard/visa-application/form",
  REDIRECT_TO_VISAAPPLICATIONLIST: "/dashboard/visa-application/list",
  REDIRECT_TO_SERVICE_REQUEST_FORM: "/dashboard/service-request/form",
  REDIRECT_TO_SERVICE_REQUEST_LIST: "/dashboard/service-request/list",

  // ===================== Navigation =====================
  CURRENT_RECORD: "Current",
  FIRST_RECORD: "First",
  LAST_RECORD: "Last",
  LIST_RECORD: "List",
  NEW_RECORD: "New",
  NEXT_RECORD: "Next",
  PREV_RECORD: "Previous"
};
