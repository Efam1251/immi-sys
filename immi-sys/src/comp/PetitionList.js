import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { API_ROUTES } from '../comp/config';
// Import API-related functions
import {
  fetchFormData,
} from "../Utils/apiUtils";

import { TableList } from "./FormFields";
import "../css/PetitionList.css";

const PetitionList = () => {
  const formName = "Petition";
  const url_list = API_ROUTES.PETITION_LIST; // API endpoint for petitions
  const url_redirect = API_ROUTES.REDIRECT_TO_PETITIONFORM; // URL to navigate for petition form

  const columns = [
    { key: "petitioner", label: "Petitioner", width: "27%"   },
    { key: "beneficiary", label: "Beneficiary", width: "27%"   },
    { key: "uscisNumber", label: "USCIS Number", width: "15%"   },
    { key: "caseNumber", label: "Case Number", width: "15%"   },
    { key: "category", label: "Category", width: "16%"   },
  ];


  
  /********************************************/
  /***** STANDARD FUNCTIONS. DO NOT EDIT. *****/
  const [petitions, setPetitions] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    fetchFormData(url_list, setPetitions); // Fetch petitions data from API
  }, []);

  const handleEdit = (id) => {
    navigate(`${url_redirect}?id=${id}`);
  };

  const filteredItems = petitions.filter((petition) =>
    columns.some((column) => {
      const value = petition[column.key];
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

export default PetitionList;
