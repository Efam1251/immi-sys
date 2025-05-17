import React, { useState, useEffect } from "react";
import Select from "react-select";
import "../css/UscisEvent.css";
import { API_ROUTES } from "./config";

const ApplicationEventModal = ({
  showModal,
  closeModal,
  id,
  reference,
  eventTypeOptions,
  statusOptions,
  token,
  initialData,
  sourceType,
}) => {
  const [eventType, setEventType] = useState("");
  const [eventDate, setEventDate] = useState("");
  const [location, setLocation] = useState("");
  const [status, setStatus] = useState("");
  const [notes, setNotes] = useState("");
  const [processId, setProcessId] = useState("");
  const [eventId, setEventId] = useState(null);
  const [processEvents, setProcessEvents] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const getLocalDateTimeString = () => {
    const now = new Date();
    const offset = now.getTimezoneOffset();
    const localDate = new Date(now.getTime() - offset * 60000);
    return localDate.toISOString().slice(0, 16);
  };

  const clearFieldsModal = () => {
    setEventId(null);
    setEventType(null);
    setEventDate(getLocalDateTimeString());
    setLocation("");
    setStatus(null);
    setNotes("");
    setError("");
  };

  useEffect(() => {
    if (showModal) {
      setProcessId(id);
  
      if (initialData) {
        // Editing existing event
        setEventId(initialData.id);
        setEventType(initialData.eventType?.id || initialData.eventType || null);
        setEventDate(initialData.eventDate || "");
        setStatus(initialData.status?.id || initialData.status || null);
        setLocation(initialData.location || "");
        setNotes(initialData.notes || "");
        setError("");
      } else {
        // New event
        clearFieldsModal();
      }
  
    }
  }, [showModal, initialData, id]);  
  
  const handleSubmitEvent = async () => {
    if (!eventType || !eventDate) {
      setError("Please fill all required fields.");
      return;
    }
  
    setLoading(true);
    setError("");
  
    const eventData = {
      id: eventId,
      processId,
      eventType,
      eventDate,
      location,
      status,
      notes,
    };
  
    try {
      const response = await fetch(`${API_ROUTES.APPLICATION_EVENT_SAVE}?sourceType=${sourceType}&id=${id}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(eventData),
      });
  
      if (response.ok) {
        clearFieldsModal();
        closeModal();
      } else {
        const result = await response.text();
        setError(result || "Failed to submit the event.");
      }
    } catch (error) {
      setError("Failed to submit the event.");
    } finally {
      setLoading(false);
    }
  };  

  return (
    showModal && (
      <div className="event-modal">
        <div className="event-modal-content">
          <span className="event-close" onClick={closeModal}>
            ×
          </span>
          <h3 className="event-modal-header">Add New Event</h3>
          <fieldset>
            {/* Top Info Row */}
            <div className="event-modal-row two-cols">
              <div className="info-pair">
                <label>Process Id:</label>
                <span>{id}</span>
              </div>
              <div className="info-pair">
                <label>Reference:</label>
                <span>{reference}</span>
              </div>
            </div>

            {/* Event Type & Date */}
            <div className="event-modal-row two-cols">
              <div className="form-control">
                <label>Event Type:</label>
                <Select
                  options={eventTypeOptions}
                  getOptionLabel={(option) => option.name}
                  getOptionValue={(option) => option.value}
                  value={
                    eventTypeOptions.find((opt) => opt.value === eventType) ||
                    null
                  }
                  onChange={(selectedOption) =>
                    setEventType(selectedOption ? selectedOption.value : null)
                  }
                  isClearable
                  isSearchable
                  isDisabled={loading}
                />
              </div>
              <div className="form-control">
                <label>Event Status:</label>
                <Select
                  options={statusOptions}
                  getOptionLabel={(option) => option.name}
                  getOptionValue={(option) => option.value}
                  value={
                    statusOptions.find((opt) => opt.value === status) || null
                  }
                  onChange={(selectedOption) =>
                    setStatus(selectedOption ? selectedOption.value : null)
                  }
                  isClearable
                  isSearchable
                  isDisabled={loading}
                />
              </div>
            </div>

            {/* Status & Location */}
            <div className="event-modal-row two-cols">
            <div className="form-control">
                <label>Event Date:</label>
                <input
                  type="datetime-local"
                  value={eventDate}
                  onChange={(e) => setEventDate(e.target.value)}
                  disabled={loading}
                  required
                />
              </div>
              <div className="form-control">
                <label>Reference:</label>
                <input
                  type="text"
                  value={location}
                  onChange={(e) => setLocation(e.target.value)}
                  disabled={loading}
                />
              </div>
            </div>

            {/* Notes */}
            <div className="event-modal-row">
              <div className="form-control full-width">
                <label>Notes:</label>
                <textarea
                  value={notes}
                  onChange={(e) => setNotes(e.target.value)}
                  disabled={loading}
                />
              </div>
            </div>

            {/* Buttons */}
            <div className="event-modal-buttons">
              <button onClick={handleSubmitEvent} disabled={loading}>
                {loading ? "Submitting..." : "Submit Event"}
              </button>
              <button onClick={closeModal} disabled={loading}>
                Close
              </button>
            </div>
          </fieldset>
        </div>
      </div>
    )
  );
};

export default ApplicationEventModal;
