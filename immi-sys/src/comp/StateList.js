import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { API_ROUTES } from '../comp/config';
// Import API-related functions
import {
  fetchFormData,
} from "../Utils/apiUtils";

import { TableList } from "./FormFields";
import "../css/StateList.css";

const StateList = () => {
  const formName = "State";
  const url_list = API_ROUTES.STATE_LIST;
  const url_redirect = API_ROUTES.REDIRECT_TO_STATEFORM;

  const columns = [
    { key: "id", label: "Id" },
    { key: "name", label: "State" },
    { key: "code", label: "Code" },
    { key: "country", label: "Country", type: "select" },
  ];


  
  /********************************************/
  /***** STANDARD FUNCTIONS. DO NOT EDIT. *****/

  const [states, setStates] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    fetchFormData(url_list, setStates);
  }, []);

  const handleEdit = (id) => {
    navigate(`${url_redirect}?id=${id}`);
  };

  const filteredItems = states.filter((item) =>
    columns.some((column) => {
      const value = column.type === "select" ? item[column.key]?.name : item[column.key];
      return typeof value === "string" && value.toLowerCase().includes(searchTerm.toLowerCase());
    })
  );

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
        />
      )}
    </div>
  );
};

export default StateList;
