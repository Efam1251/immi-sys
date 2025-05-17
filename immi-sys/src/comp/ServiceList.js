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

const ServiceList = () => {
  const { navigate, formList, setFormList, searchTerm, setSearchTerm } = useFormConstants();

  const formName = "Service";
  const id_field = "serviceId"
  const url_list = API_ROUTES.SERVICE_LIST;
  const url_redirect = API_ROUTES.REDIRECT_TO_SERVICEFORM;

  const columns = [
    { key: "serviceId", label: "Id", width: "15%"  },
    { key: "serviceName", label: "Service Name", width: "45%"  },
    { key: "unitPrice", label: "Unit Price", type: "number", width: "15%"  },
    { key: "serviceType", label: "Service Type", type: "select", width: "25%"  },
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

export default ServiceList;
