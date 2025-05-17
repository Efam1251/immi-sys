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

  const formName = "ServiceRequest";
  const id_field = "id"
  const url_list = API_ROUTES.SERVICE_REQUEST_LIST;
  const url_redirect = API_ROUTES.REDIRECT_TO_SERVICE_REQUEST_FORM;

  const columns = [
    /*{ key: "id", label: "Id", width: "10%"  },*/
    { key: "serviceName", label: "Service", width: "20%"  },
    { key: "customerFullName", label: "Customer", width: "35%"  },
    { key: "requestDate", label: "Req. Date", type: "date", width: "15%"  },
    { key: "referenceCode", label: "Reference", width: "15%"  },
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
