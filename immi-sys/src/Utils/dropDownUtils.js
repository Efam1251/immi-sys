import { API_ROUTES } from "../comp/config";

export const dropdownFieldConfigs = {
  gender: {
    dropdownUrl: API_ROUTES.GENDER_LIST,
    modalUrl: API_ROUTES.GENDER_FORM,
  },
  maritalStatus: {
    dropdownUrl: API_ROUTES.MARITAL_STATUS_LIST,
    modalUrl: API_ROUTES.MARITAL_STATUS_FORM,
  },
  immigrationStatus: {
    dropdownUrl: API_ROUTES.IMMIGRATION_STATUS_LIST,
    modalUrl: API_ROUTES.IMMIGRATION_STATUS_FORM,
  },
  // ...more fields
};
