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

import { TableList } from "./FormFields";
import "../css/UtilList.css";

const CustomerList = () => {
  const { navigate, formList, setFormList, searchTerm, setSearchTerm } = useFormConstants();
  
  const formName = "VisaAplication";
  const id_field = "id";
  const url_list = API_ROUTES.VISA_APPLICATION_LIST;
  const url_redirect = API_ROUTES.REDIRECT_TO_VISAAPPLICATIONFORM;

  const columns = [
    { key: "customerName", label: "Customer", width: "45%" },
    { key: "visaType", label: "Visa Type", width: "25%" },
    { key: "reference", label: "Reference", width: "15%" },
    { key: "status", label: "Status", width: "15%" },
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

export default CustomerList;
