import axios from "axios";

// Universal data fetching function
export async function fetchData(url, searchParams = null, token = null) {
  try {
    const queryString =
      searchParams && searchParams.toString()
        ? `?${searchParams.toString()}`
        : "";
    const dynamicUrl = `${url}${queryString}`;

    const config = {
      headers: {
        "Content-Type": "application/json",
        ...(token && { Authorization: `Bearer ${token}` }),
      },
    };

    const response = await axios.get(dynamicUrl, config);

    return response.data || null;
  } catch (error) {
    console.error(
      "Error fetching data:",
      error.response?.data || error.message
    );
    return null;
  }
}

// Fetch list of data for dropdowns or static lists
/*export function fetchFormData(apiRoute, setFormList) {
  fetch(apiRoute)
    .then((response) => response.json())
    .then((data) => {
      Array.isArray(data) ? setFormList(data) : setFormList([]);
    })
    .catch((error) => console.error("Error fetching data:", error));
}*/
export async function fetchFormData(apiRoute, setFormList) {
  try {
    const response = await axios.get(apiRoute);
    Array.isArray(response.data) ? setFormList(response.data) : setFormList([]);
  } catch (error) {
    console.error("Error fetching data:", error);
  }
}



// Fetch and set form data function
export const fetchAndSetData = async ({
  url,
  setFormState,
  currentRecordIdRef,
  direction,
  setMessage,
  setPopupOpen,
  idField = "id",
  token,
}) => {
  try {
    const params = new URLSearchParams({
      direction: direction.toString(),
      id: currentRecordIdRef.current,
    });
    const data = await fetchData(url, params, token);

    if (data) {
      const updatedData = {
        ...data,
        ...(Object.prototype.hasOwnProperty.call(data, "isChecked") && {
          isChecked:
            data.isChecked === null || data.isChecked === undefined
              ? false
              : data.isChecked,
        }),
      };
      setFormState(updatedData);
      currentRecordIdRef.current = data[idField];
    } else {
      const messages = {
        Next: "No more records available.",
        Previous: "No previous records available.",
        Current: "No record found.",
        First: "No first record found.",
      };

      if (direction !== "Last") {
        setMessage(messages[direction] || "No record found.");
        setPopupOpen(true);
      }
    }
  } catch (error) {
    console.error("Error loading data:", error);
    setMessage("An error occurred while fetching data.");
    setPopupOpen(true);
  }
};

// Fetch documents
export async function fetchDocuments(token, url_list, setDocuments) {
  try {
    const response = await fetch(url_list, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      throw new Error("Failed to fetch documents");
    }

    const data = await response.json();
    setDocuments(data);
  } catch (error) {
    console.error("fetchDocuments error:", error);
  }
}

// Handle form submission (Create/Update)
export async function handleSubmit(e, formData, url_form, primaryId) {
  e.preventDefault();

  try {
    let request;
    const hasFile = formData.file instanceof File;

    if (hasFile) {
      const multipartData = new FormData();
      multipartData.append(
        "record",
        new Blob([JSON.stringify(formData)], { type: "application/json" })
      );
      multipartData.append("file", formData.file);

      const config = {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      };

      request = formData[primaryId]
        ? axios.put(`${url_form}/${formData[primaryId]}`, multipartData, config)
        : axios.post(url_form, multipartData, config);
    } else {
      request = formData[primaryId]
        ? axios.put(`${url_form}/${formData[primaryId]}`, formData)
        : axios.post(url_form, formData);
    }

    await request;

    return formData[primaryId]
      ? "Record updated successfully"
      : "Record saved successfully";
  } catch (error) {
    const errorMessage =
      error.response?.data?.error ||
      error.response?.data?.message ||
      (typeof error.response?.data === "string" ? error.response.data : null) ||
      error.message ||
      "An error occurred. Please try again.";
    return String(errorMessage);
  }
}

// Handle delete action
export const eventDeleteHandler = async ({
  eventId,
  token,
  apiUrl,
  confirmationMessage = "Are you sure you want to delete this event?",
  errorMessage = "Error deleting event.",
}) => {
  const confirmDelete = window.confirm(confirmationMessage);
  if (!confirmDelete) return;

  try {
    await axios.delete(apiUrl, {
      params: {
        id: eventId,
      },
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
  } catch (error) {
    console.error(error);
    alert(errorMessage);
  }
};
