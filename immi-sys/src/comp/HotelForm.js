import React, {
  useState,
  useEffect,
  useRef,
  useCallback,
  useMemo,
} from "react";
import axios from "axios";
import {
  fetchAndSetData,
  useFetchDropdownOptions,
  addNewOption,
  resetFormData,
} from "../comp/Utils";

import { useLocation } from "react-router-dom";
import { InputField, SelectField, CheckboxField } from "./FormFields";
import GenericFormModal from "./GenericFormModal";
import Modal from "../comp/Modal";
import MessagePopup from "../comp/MessagePopup";
import ButtonGroup from "../Utils/ButtonGroup";
import "../css/HotelForm.css";

const initialFormData = {
  id: null,
  name: "",
  description: "",
  state: null,
  address: null,
  amenities: [],
};

const HotelForm = () => {
  const [formData, setFormData] = useState(initialFormData);

  const resetForm = () => {
    setFormData(initialFormData);
  };

  const location = useLocation();

  // 2️⃣ Refs (For persisting values without re-renders)
  const focusFieldRef = useRef(null);
  const lastFetchedId = useRef(null); // Track the last fetched ID
  const currentRecordIdRef = useRef(0);
  const isFirstLoad = useRef(true); // Track if it's the first load

  const [amenityOptions, setAmenityOptions] = useState([]);
  const [statesOptions, setStatesOptions] = useState([]);
  const [countriesOptions, setCountriesOptions] = useState([]);
  const [addressOptions, setAddressOptions] = useState([]);
  const [searchTerm, setSearchTerm] = useState(""); // For filtering amenities

  // 5️⃣ Modal Visibility States
  const [showAddressModal, setShowAddressModal] = useState(false);
  const [showStateModal, setShowStateModal] = useState(false);
  const [showCountryModal, setShowCountryModal] = useState(false);
  const [showAmenityModal, setShowAmenityModal] = useState(false);

  //const [email, setEmail] = useState("");
  const [error, setError] = useState("");
  const [message, setMessage] = useState(""); // Stores response message
  const [isPopupOpen, setPopupOpen] = useState(false);

  const token = localStorage.getItem("token");

  // Fetch formData options
  const loadData = useCallback(
    async (direction) => {
      await fetchAndSetData({
        url: "/travel/forms/hotels/form",
        setFormState: setFormData,
        currentRecordIdRef,
        direction,
        setMessage,
        setPopupOpen,
        idField: "id",
        token: localStorage.getItem("token"),
      });
    },
    [setFormData, currentRecordIdRef, setMessage, setPopupOpen]
  );

  const handleNavigateRecord = useCallback(
    (direction) => {
      if (direction === "New") {
        resetFormData(setFormData, initialFormData);
        if (focusFieldRef.current) {
          focusFieldRef.current.focus();
        }
        return;
      }
      loadData(direction);
    },
    [setFormData, loadData]
  ); // Add dependencies here

  // Fetch initial formData options
  useEffect(() => {
    const searchParams = new URLSearchParams(location.search);
    const token = localStorage.getItem("token");
    const recordId = searchParams.get("id");
    const parsedId = recordId ? parseInt(recordId, 10) : null;

    if (parsedId !== currentRecordIdRef.current) {
      if (token) {
        if (parsedId && lastFetchedId.current !== parsedId) {
          currentRecordIdRef.current = parsedId;
          lastFetchedId.current = parsedId;
          loadData("Current");
        } else if (isFirstLoad.current) {
          isFirstLoad.current = false;
          handleNavigateRecord("First");
        }
      }
    }
  }, [location.search, loadData, handleNavigateRecord]);

  // Using the hook to fetch options for multiple dropdowns
  const dropdownFetchConfig = useMemo(
    () => [
      { url: "/travel/api/address/states", setState: setStatesOptions },
      { url: "/travel/api/address/countries", setState: setCountriesOptions },
      { url: "/travel/api/address/addresses", setState: setAddressOptions },
      { url: "/travel/api/amenity/amenities", setState: setAmenityOptions },
    ],
    []
  ); // ✅ Dependencies are empty because setState functions never change`
  useFetchDropdownOptions(dropdownFetchConfig);

  // Handler: Select field change
  const dropdownOptions = {
    state: statesOptions,
    country: countriesOptions,
    address: addressOptions,
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    if (name === "amenities") {
      const selectedAmenities = Array.from(
        e.target.selectedOptions,
        (option) => option.value
      );
      setFormData({ ...formData, amenities: selectedAmenities });
    } else {
      setFormData({
        ...formData,
        [name]: value,
      });
    }
  };

  const handleAddressSelect = (e) => {
    const selectedId = e.target.value;
    const selectedAddress = addressOptions.find(
      (addr) => addr.id == selectedId
    );
    if (selectedAddress) {
      setFormData((prev) => ({
        ...prev,
        address: {
          id: selectedAddress.id,
          street: selectedAddress.street,
          city: selectedAddress.city,
          postalCode: selectedAddress.zipCode,
          state: {
            id: selectedAddress.state?.id || "",  // Ensure the state object has id and name
            name: selectedAddress.state?.name || "",
          },
          country: {
            id: selectedAddress.country?.id || "",  // Ensure the country object has id and name
            name: selectedAddress.country?.name || "",
          },
        },
      }));
    }
  };

  // Handler: Add new options to helper tables.
  const fieldSetters = {
    amenity: setAmenityOptions,
    states: setStatesOptions,
    countries: setCountriesOptions,
  };

  // Function to add new options to helper dropdowns.
  function handleAddOption(field, newAddedItem) {
    addNewOption(field, newAddedItem, fieldSetters);
  }

  const handleSearchChange = (e) => {
    setSearchTerm(e.target.value);
  };

  const handleCheckboxChange = (e, amenityId) => {
    const isChecked = e.target.checked;
    // Find the amenity object in the amenitiesList by its id
    const amenity = amenityOptions.find((item) => item.id === amenityId);
    if (!amenity) {
      return; // If the amenity is not found, do nothing
    }
    // If checked, add the full amenity object to the array, otherwise remove it
    const updatedAmenities = isChecked
      ? [...formData.amenities, amenity] // Add the full amenity object
      : formData.amenities.filter((item) => item.id !== amenity.id); // Remove the amenity by id

    setFormData({
      ...formData,
      amenities: updatedAmenities, // Update the amenities array with full objects
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
  
    const payload = {
      id: formData.id,
      name: formData.name,
      description: formData.description,
      address: formData.address,
      amenities: formData.amenities,
    };

    const requestOptions = {
      method: formData.id ? "PUT" : "POST",  // Decide method based on whether it's update or create
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`, // Add Bearer token for authentication
      },
      body: JSON.stringify(payload),
    };

    const url = formData.id
      ? `/travel/forms/hotels/save/${formData.id}`  // Update URL if id exists (for update)
      : "/travel/forms/hotels/save";  // URL for creating a new hotel
  
    fetch(url, requestOptions)
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`); // Error if response status is not OK
        }
        return response.json(); // Parse JSON response
      })
      .then((data) => {
        if (data) {
          if (!formData.id) {
            setMessage("Hotel Created");
            setFormData({
              id: null,
              name: "",
              description: "",
              address: null,
              amenities: [], // Clear amenities
            });
            currentRecordIdRef.current = formData.id;
          } else {
            setMessage("Hotel Updated");
          }
        }
      })
      .catch((error) => {
        console.error("Fetch error:", error); // Log errors if the fetch fails
        setMessage("An error occurred. Please try again.");
      });
      setPopupOpen(true);
  };

  const handleDelete = () => {
    axios
      .delete(`/travel/forms/hotels/${formData.id}`)
      .then((res) => {
        alert("Hotel Deleted");
        setFormData({
          id: null,
          name: "",
          description: "",
          address: {},
          amenities: [],
        }); // Clear form
      })
      .catch((err) => console.error(err));
  };

  const filteredAmenities = amenityOptions.filter((amenity) =>
    amenity.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  // Function to transform address options into the required structure (id and name)
  function transformOptions(options) {
    return options.map((address) => ({
      id: address?.id ?? "",
      name: `${address?.street ?? ""} ${address?.city ?? ""}`.trim(),
    }));
  }

  // Helper function to convert string to Proper Case (capitalize first letter of each word)
  const toProperCase = (str) => {
    return str
      .toLowerCase() // Make all characters lowercase first
      .replace(/\b\w/g, (char) => char.toUpperCase()); // Capitalize the first letter of each word
  };

  // Concatenate address fields into two parts for each line and apply proper case formatting
  const addressLine1 =`${toProperCase(formData.address?.street || "")}, ${toProperCase(formData.address?.city || "")}`.trim();
  const addressLine2 = `${toProperCase(formData.address?.state.name || "")}, ${toProperCase(formData.address?.zipCode || "")}, ${toProperCase(formData.address?.country.name || "")}`.trim();

  // State Modal Form Fields.
  const stateFields = [
    {
      name: "name",
      label: "State Name",
      type: "text",
      placeholder: "Enter name",
    },
    { name: "code", label: "Code", type: "text", placeholder: "Enter code" },
    {
      name: "countryId",
      label: "Country",
      type: "select",
      options: countriesOptions,
    },
  ];
  // State Modal Form Fields.
  const countryFields = [
    {
      name: "name",
      label: "Country Name",
      type: "text",
      placeholder: "Enter name",
    },
  ];
  // State Modal Form Fields.
  const addressFields = [
    {
      name: "street",
      label: "Street Name",
      type: "text",
      placeholder: "Enter street name",
    },
    {
      name: "city",
      label: "City",
      type: "text",
      placeholder: "Enter city name",
    },
    {
      name: "stateId",
      label: "State",
      type: "select",
      options: statesOptions,
      helpBtn: true,
    },
    {
      name: "zipCode",
      label: "Zip Code",
      type: "text",
      placeholder: "Enter Zip Code",
    },
    {
      name: "countryId",
      label: "Country",
      type: "select",
      options: countriesOptions,
    },
  ];

  return (
    <div className="form-container">
      <h1 className="embossed-text">Hotel Registration</h1>
      <form onSubmit={handleSubmit}>
        {/* Buttons */}
        <div className="flex items-center justify-between pt-4">
          <ButtonGroup
            className="btn-group"
            onNewRecord={() => handleNavigateRecord("New")}
            onSave={handleSubmit}
            onDisplayRecord={() => handleNavigateRecord("Current")}
            onFirstRecord={() => handleNavigateRecord("First")}
            onPreviousRecord={() => handleNavigateRecord("Previous")}
            onNextRecord={() => handleNavigateRecord("Next")}
          />
        </div>
        <fieldset>
          <legend>Hotel Information</legend>
          <div className="fieldset-container-internal">
            {/*<InputField
              label="Hotel Id"
              id="id"
              name="id"
              value={formData.id || ""}
              onChange={handleInputChange}
              type="text"
              readOnly
            />*/}
            <InputField
              label="Hotel Name"
              id="name"
              name="name"
              value={formData.name || ""}
              onChange={handleInputChange}
              type="text"
              required
            />
            <InputField
              label="Description"
              id="description"
              name="description"
              value={formData.description || ""}
              onChange={handleInputChange}
              type="text"
              required
            />
            <SelectField
              label="Address"
              id="address"
              name="address"
              value={formData.address?.id || ""}
              options={transformOptions(addressOptions)}
              onChange={handleAddressSelect}
              openModal={() => setShowAddressModal(true)}
            />
            <div className="address-text">
              <div>{addressLine1 || "No address available"}</div>
              <div>{addressLine2 || ""}</div>
            </div>
          </div>
        </fieldset>

        <fieldset>
          <legend>Amenities</legend>
          <div className="fieldset-container-Amenity">
            <div className="amenity-search-container">
              <InputField
                label="Search Amenities"
                id="amenitiesSearch"
                name="amenitiesSearch"
                value={searchTerm}
                onChange={handleSearchChange}
                type="text"
              />
              <button
                type="button"
                className="add-button"
                onClick={() => setShowAmenityModal(true)}
              >
                <img
                  src="/img/Add-48.png"
                  alt="new"
                  style={{ width: "25px", height: "25px" }}
                />
              </button>
            </div>
            <div className="form-group-Amenity">
              <div className="form-checkbox">
                {filteredAmenities.map((amenity) => (
                  <label key={amenity.id}>
                    <input
                      type="checkbox"
                      id="amenity"
                      name="amenity"
                      checked={formData.amenities.some(
                        (a) => a.id === amenity.id
                      )}
                      onChange={(e) => handleCheckboxChange(e, amenity.id)}
                    />
                    {amenity.name}
                  </label>
                ))}
              </div>
            </div>
          </div>
        </fieldset>
      </form>

      {/* Modals for adding new options */}
      <GenericFormModal
        showModal={showAddressModal}
        onClose={() => setShowAddressModal(false)}
        onAddOption={handleAddOption}
        initialData={{
          street: "",
          city: "",
          stateId: "",
          zipCode: "",
          countryId: "",
        }}
        fieldsConfig={addressFields}
        field="address"
        title="Add New Address"
        url="/travel/api/address/address-save"
      />
      <GenericFormModal
        showModal={showStateModal}
        onClose={() => setShowStateModal(false)}
        onAddOption={handleAddOption}
        initialData={{ name: "", code: "", countryId: "" }}
        fieldsConfig={stateFields}
        field="state"
        title="Add New State"
        url="/api/states"
      />
      <GenericFormModal
        showModal={showCountryModal}
        onClose={() => setShowCountryModal(false)}
        onAddOption={handleAddOption}
        initialData={{ name: "" }}
        fieldsConfig={countryFields}
        field="country"
        title="Add New Country"
        url="/travel/api/address/country-save"
      />
      <Modal
        showModal={showAmenityModal}
        closeModal={() => setShowAmenityModal(false)}
        onAddOption={handleAddOption}
        url="/travel/api/amenity/save"
        field="amenity"
      />
      <MessagePopup
        message={message}
        isPopupOpen={isPopupOpen}
        setPopupOpen={setPopupOpen}
      />
    </div>
  );
};

export default HotelForm;
