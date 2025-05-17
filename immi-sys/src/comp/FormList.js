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

const FormList = () => {
  const { navigate, formList, setFormList, searchTerm, setSearchTerm } = useFormConstants();

  const formName = "Forms";
  const id_field = "id"
  const url_list = API_ROUTES.FORMS_LIST;
  const url_redirect = API_ROUTES.REDIRECT_TO_FORMSFORM;

  const columns = [
    { key: "id", label: "Id", width: "20%"  },
    { key: "name", label: "Name", width: "20%"  },
    { key: "location", label: "Location", width: "60%"  },
  ];

  /********************************************/
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

export default FormList;
