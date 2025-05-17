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

const CustomerList = () => {
  const { navigate, formList, setFormList, searchTerm, setSearchTerm } = useFormConstants();

  const formName = "Customer";
  const id_field = "customerId";
  const url_list = API_ROUTES.CUSTOMER_LIST;
  const url_redirect = API_ROUTES.REDIRECT_TO_CUSTOMERFORM;

  const columns = [
    { key: "firstName", label: "First Name", width: "25%" },
    { key: "lastName", label: "Last Name", width: "25%" },
    { key: "phone", label: "Phone", width: "15%" },
    { key: "email", label: "Email", width: "35%" },
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
