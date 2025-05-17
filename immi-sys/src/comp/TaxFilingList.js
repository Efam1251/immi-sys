import React, { useEffect } from "react";
import { API_ROUTES } from "../comp/config";
// Import form-related hooks
import {
  useFormConstants,
  useFilteredItems,
} from "../Utils/formHooks";

// Import API-related functions
import {
  fetchFormData,
} from "../Utils/apiUtils";

// Import form utility functions
import { TableList } from "./FormFields";
import "../css/UtilList.css";

const TaxFilingList = () => {
  const { navigate, formList, setFormList, searchTerm, setSearchTerm } = useFormConstants();
  
  const formName = "TaxFiling";
  const id_field = "taxFilingId";
  const url_list = API_ROUTES.TAX_FILING_LIST;
  const url_redirect = API_ROUTES.REDIRECT_TO_TAX_FILING_FORM;

  const columns = [
    { key: "customerName", label: "Customer", type: "text", width: "35%" },
    { key: "taxYear", label: "Tax Year", type: "text", width: "12%" },
    { key: "filingDate", label: "Filing Date", type: "date", width: "15%" },
    { key: "totalIncome", label: "Total Income", type: "number", width: "12%" },
    { key: "taxOutcomeAmount", label: "Outcome Amount", type: "number", width: "12%" },
    { key: "status", label: "Status", type: "text", width: "14%" },
    // Add more columns if needed
  ];

  /************************************************************/
  /***** STANDARD FUNCTIONS. DO NOT EDIT. *****/
  useEffect(() => {
    fetchFormData(url_list, setFormList);
  }, []);

  const handleEdit = (id) => {
    navigate(`${url_redirect}?id=${id}`);
  };

  const filteredItems = useFilteredItems(formList, columns, searchTerm);

  return (
    <div>
      {/* TABLE LIST SCROLLABLE */}
      {columns && columns.length > 0 && (
        <TableList
          formName={formName}
          columns={columns}
          filteredItems={filteredItems}
          searchTerm={searchTerm}
          setSearchTerm={setSearchTerm}
          handleRowClick={handleEdit}
          idField={id_field}
        />
      )}
    </div>
  );
};

export default TaxFilingList;
