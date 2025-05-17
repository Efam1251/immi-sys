import React, { useState, useEffect, useRef } from "react";
import Select from "react-select";
import "../css/DocumentModal.css";
import { API_ROUTES } from "./config";
import Modal from "./Modal";

// Import API-related functions
import { fetchFormData, fetchDocuments } from "../Utils/apiUtils";
import { createDocumentFormData } from "../Utils/formUtils";
import { validateAndPrepareFile } from "../Utils/fileUtils";
import { CONFIG_SETTING } from "../Utils/commonUtils";

const DocumentModal = ({
  showModal,
  closeModal,
  token,
  recordId,
  reference,
  type,
}) => {
  const [isDragOver, setIsDragOver] = useState(false);
  const [documentId, setDocumentId] = useState("");
  const [file, setFile] = useState(null);
  const [fileName, setFileName] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [documents, setDocuments] = useState([]);
  const [documentOptions, setDocumentOptions] = useState([]);
  const [showDocumentModal, setShowDocumentModal] = useState(false);
  const fileInputRef = useRef();
  const url_list = `${API_ROUTES.DOCUMENTS_LIST}?type=${type}&reference=${reference}`;
  const url_delete = API_ROUTES.DOCUMENTS_DELETE;

  /*****************************************************************************/
  const clearFieldsModal = () => {
    setDocumentId("");
    setFile(null);
    setFileName("");
    setError("");
    if (fileInputRef.current) {
      fileInputRef.current.value = "";
    }
  };

  const handleClose = () => {
    clearFieldsModal();
    setDocuments([]); // Clear the list
    closeModal();     // Notify parent to hide modal
  };  

  function fetchDropdownData() {
    fetchFormData(API_ROUTES.DOCUMENTS_DROPDOWN, setDocumentOptions);
  }

  useEffect(() => {
    if (showModal) {
      fetchDropdownData();
      fetchDocuments(token, url_list, setDocuments);
      clearFieldsModal();
    }
  }, [showModal]);

  const handleFileChange = (e) => {
    const selectedFile = e.target.files[0];
    if (selectedFile) {
      setFile(selectedFile);
    }
  };

  const handleAddModal = () => {
    setShowDocumentModal(true);
  };

  const handleAddDocument = async () => {
    if (!documentId && !file) {
      setError("Please select a document type and upload a file.");
      return;
    } else if (!documentId) {
      setError("Please select a document type.");
      return;
    } else if (!file) {
      setError("Please upload a file.");
      return;
    }

    const result = validateAndPrepareFile(file, reference, documentId);
    if (result.error) {
      setError(result.error);
      setFile(null);
      setFileName("");
      return;
    }

    setLoading(true);
    setError("");

    const formData = createDocumentFormData(
      type,
      recordId,
      documentId,
      result.fileName,
      result.file
    );

    try {
      const response = await fetch(API_ROUTES.DOCUMENT_SAVE_UPLOAD, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
        },
        body: formData,
      });

      if (response.ok) {
        await fetchDocuments(token, url_list, setDocuments);
        clearFieldsModal();
      } else {
        const result = await response.text();
        setError(result);
      }
    } catch (error) {
      setError("Failed to upload file.");
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteDocument = async (id) => {
    if (!window.confirm("Are you sure you want to delete this document?")) {
      return;
    }

    try {
      const response = await fetch(`${url_delete}/${id}`, {
        method: "DELETE",
      });

      if (response.ok) {
        setDocuments((prevDocs) => prevDocs.filter((doc) => doc.id !== id));
      } else {
        const errorData = await response.json();
        setError(errorData.message || "Failed to delete document.");
      }
    } catch (error) {
      setError("Error deleting document. Please try again.");
    }
  };

  // Convert documentOptions into the format React Select expects
  const optionsFormatted = documentOptions.map((doc) => ({
    value: doc.id,
    label: doc.name,
  }));

  return (
    showModal && (
      <div className="document-modal">
        <div className="document-modal-content">
          <span className="document-modal-close" onClick={handleClose}>
            ×
          </span>
          <h3 className="document-modal-header">Add New Document</h3>
          <fieldset>
            <div className="document-modal-row">
              <div className="document-modal-input-group">
                <label>ID:</label>
                <span>{recordId}</span>
              </div>
              <div className="document-modal-input-group">
                <label>Reference:</label>
                <span>{reference}</span>
              </div>
            </div>
            <div className="document-modal-flex-select">
              <label>Document Type:</label>
              <Select
                options={optionsFormatted}
                value={optionsFormatted.find(
                  (option) => option.value === documentId
                )}
                onChange={(selectedOption) =>
                  setDocumentId(selectedOption ? selectedOption.value : "")
                }
                isClearable
                isSearchable
                isDisabled={loading}
                styles={{
                  control: (base) => ({
                    ...base,
                    width: "100%",
                    minWidth: "350px",
                  }),
                  menu: (base) => ({
                    ...base,
                    zIndex: 9999,
                    position: "absolute",
                  }),
                  menuPortal: (base) => ({
                    ...base,
                    zIndex: 9999,
                  }),
                }}
              />
              <button
                type="button"
                className="document-add-button"
                onClick={handleAddModal}
                aria-label="Add new document type"
                title="Add new document type"
              >
                <img
                  src="/img/Add-48.png"
                  alt="new"
                  style={{ width: "20px", height: "20px" }}
                />
              </button>
            </div>
            <div
              className={`document-modal-dropzone ${
                isDragOver ? "drag-over" : ""
              }`}
              onDragOver={(e) => {
                e.preventDefault();
                setIsDragOver(true);
              }}
              onDragLeave={() => setIsDragOver(false)}
              onDrop={(e) => {
                e.preventDefault();
                setIsDragOver(false);
                if (e.dataTransfer.files.length > 0) {
                  const droppedFile = e.dataTransfer.files[0];
                  setFile(droppedFile);
                  fileInputRef.current.value = ""; // Clear input in case user drops again
                }
              }}
            >
              <p>Drag & drop a file here, or click to select one</p>
              <input
                type="file"
                ref={fileInputRef}
                onChange={handleFileChange}
                accept=".pdf,.jpg,.jpeg,.png,.docx"
                disabled={loading}
                style={{ display: "none" }}
                id="hiddenFileInput"
              />
              <button
                type="button"
                onClick={() => fileInputRef.current.click()}
                className="select-file-button"
              >
                Browse
              </button>
              {file && <p className="file-selected">Selected: {file.name}</p>}
            </div>

            {error && <p className="error-message">{error}</p>}
            <div className="document-modal-buttons">
              <button onClick={handleAddDocument} disabled={loading}>
                {loading ? "Uploading..." : "Upload File"}
              </button>
              <button onClick={handleClose} disabled={loading}>
                Close
              </button>
            </div>
          </fieldset>
          {documents.length > 0 && (
            <div className="document-modal-table-container">
              <table>
                <thead>
                  <tr>
                    <th>Document Type</th>
                    <th>File Name</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {documents.map((doc) => (
                    <tr key={doc.id}>
                      <td>{doc.document.name}</td>
                      <td>{doc.fileName}</td>
                      <td>
                        <a
                          href={`${CONFIG_SETTING.LOCAL_HOST}/api/immigration/document/openDocument?fileName=${doc.fileName}`}
                          target="_blank"
                          rel="noopener noreferrer"
                        >
                          View
                        </a>
                        <button
                          className="delete-button"
                          onClick={() => handleDeleteDocument(doc.id)}
                        >
                          Delete
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}

          {/* Modal for adding new options */}
          {showDocumentModal && (
            <Modal
              showModal={showDocumentModal}
              closeModal={() => setShowDocumentModal(false)}
              onAddOption={() => fetchDropdownData()}
              field="document"
              url={API_ROUTES.DOCUMENT_SAVE}
            />
          )}
        </div>
      </div>
    )
  );
};

export default DocumentModal;
