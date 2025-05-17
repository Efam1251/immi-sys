import React, { useEffect } from "react";
import { API_ROUTES } from "./config";
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

const CitizenshipApplicationList = () => {
  const { navigate, formList, setFormList, searchTerm, setSearchTerm } = useFormConstants();
  
  const formName = "CitizenshipApplication";
  const id_field = "citizenshipId";
  const url_list = API_ROUTES.CITIZENSHIP_APPLICATION_LIST;
  const url_redirect = API_ROUTES.REDIRECT_TO_CITIZENSHIPAPPLICATIONFORM;

  const columns = [
    { key: "customerName", label: "Customer", width: "35%" },
    { key: "receiptNumber", label: "Receipt Number", width: "15%" },
    { key: "biometricAppointmentDate", label: "Bio. App. Date", width: "25%" },
    { key: "currentStatus", label: "Status", width: "25%" },
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

export default CitizenshipApplicationList;
