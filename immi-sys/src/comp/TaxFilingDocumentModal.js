import React, { useState, useEffect } from "react";
import Modal from "../comp/Modal";
import Select from "react-select";
import "../css/VisaDocument.css";

const TaxFilingDocumentModal = ({
  showModal,
  closeModal,
  onAddOption,
  token,
  onSubmit,
  recordId,
  documentOptions,
  reference,
}) => {
  const [documentId, setDocumentId] = useState("");
  const [file, setFile] = useState(null);
  const [fileName, setFileName] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [documents, setDocuments] = useState([]);
  const [showDocumentModal, setShowDocumentModal] = useState(false);

  const clearFieldsModal = () => {
    setDocumentId("");
    setFile(null);
    setFileName("");
    setError("");
  };

  const fetchDocuments = async () => {
    try {
      const response = await fetch(
        `/api/immigration/document/taxDocuments?reference=${reference}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      if (!response.ok) throw new Error("Failed to fetch documents");
      const data = await response.json();
      setDocuments(data);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    if (showModal) {
      fetchDocuments();
      clearFieldsModal();
    }
  }, [showModal]);

  const handleFileChange = (e) => {
    const selectedFile = e.target.files[0];
    if (selectedFile) {
      const fileExtension = selectedFile.name.split(".").pop();
      const structuredFileName = `${reference}-${documentId}.${fileExtension}`;
      setFile(selectedFile);
      setFileName(structuredFileName);
    }
  };

  const handleAddModal = () => {
    setShowDocumentModal(true);
  };

  const handleAddDocument = async () => {
    if (!documentId || !file) {
      setError("Please select a document type and upload a file.");
      return;
    }

    setLoading(true);
    setError("");

    const formData = new FormData();
    formData.append("taxFilingId", recordId);
    formData.append("documentId", documentId);
    formData.append("file", file);
    formData.append("fileName", fileName);

    try {
      const response = await fetch(
        "/api/immigration/document/taxDocuments-save-upload",
        {
          method: "POST",
          body: formData,
        }
      );

      if (response.ok) {
        console.log("Document uploaded successfully");
        onSubmit(formData);
        fetchDocuments();
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
      const response = await fetch(
        `/api/immigration/document/taxDocuments-delete/${id}`,
        {
          method: "DELETE",
        }
      );

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
  const documentOptionsFormatted = documentOptions.map((doc) => ({
    value: doc.id,
    label: doc.name,
  }));

  return (
    showModal && (
      <div className="visa-modal">
        <div className="visa-modal-content">
          <span className="visa-close" onClick={closeModal}>
            ×
          </span>
          <h3 className="visa-modal-header">Add New Visa Document</h3>
          <fieldset>
            <div className="visa-modal-row">
              <div className="visa-modal-input-group">
                <label>Tax Filing ID:</label>
                <span>{recordId}</span>
              </div>
              <div className="visa-modal-input-group">
                <label>Reference:</label>
                <span>{reference}</span>
              </div>
            </div>
            <div className="visa-modal-flex-select">
              <label>Document Type:</label>
              <Select
                options={documentOptionsFormatted}
                value={documentOptionsFormatted.find(
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
                    zIndex: 9999, // Ensures dropdown is on top
                    position: "absolute",
                  }),
                  menuPortal: (base) => ({
                    ...base,
                    zIndex: 9999, // Ensures it appears above everything
                  }),
                }}
              />
              <button
                type="button"
                className="petition-add-button"
                onClick={handleAddModal}
              >
                <img
                  src="/img/Add-48.png"
                  alt="new"
                  style={{ width: "20px", height: "20px" }}
                />
              </button>
            </div>
            <div className="visa-modal-flex-select">
              <label>Upload File:</label>
              <input
                type="file"
                onChange={handleFileChange}
                accept=".pdf,.jpg,.jpeg,.png,.docx"
                disabled={loading}
              />
            </div>
            {error && <p className="error-message">{error}</p>}
            <div className="visa-modal-buttons">
              <button onClick={handleAddDocument} disabled={loading}>
                {loading ? "Uploading..." : "Upload File"}
              </button>
              <button onClick={closeModal} disabled={loading}>
                Close
              </button>
            </div>
          </fieldset>
          {documents.length > 0 && (
            <div className="visa-table-container">
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
                          href={`http://localhost:8080/api/immigration/document/openDocument?fileName=${doc.fileName}`}
                          target="_blank"
                          rel="noopener noreferrer"
                        >
                          View
                        </a>
                        <button onClick={() => handleDeleteDocument(doc.id)}>
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
              onAddOption={onAddOption}
              field="document"
              url="/api/immigration/document/save"
            />
          )}
        </div>
      </div>
    )
  );
};

export default TaxFilingDocumentModal;
