// APPLICATION LIST FORM
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

const UscisProcessList = () => {
  const { navigate, formList, setFormList, searchTerm, setSearchTerm } = useFormConstants();

  const formName = "ImmigrationApplic.";
  const id_field = "id"
  const url_list = API_ROUTES.APPLICATION_LIST;
  const url_redirect = API_ROUTES.REDIRECT_TO_APPLICATIONFORM;

  const columns = [
    /*{ key: "id", label: "Id", width: "10%"  },*/
    { key: "customerName", label: "Customer Name", width: "35%"  },
    { key: "formTypeName", label: "Form Type", width: "25%"  },
    { key: "receiptNumber", label: "Receipt Number", width: "15%"  },
    { key: "applicationDate", label: "Appl. Date", type: "date", width: "10%"  },
    { key: "status", label: "Status", width: "15%"  },
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

export default UscisProcessList;
