import React, { useState, useEffect, useRef } from 'react';
import '../css/Modal.css';

function Modal({ showModal, closeModal, onAddOption, field, url = ""}) {
  const [newOption, setNewOption] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const focusFieldRef = useRef(null);

  // To convert the field name as a Header Name formatted.
  const formattedField = field.replace(/([a-z])([A-Z])/g, '$1 $2').replace(/^./, field[0].toUpperCase());

  // Reset the input field when the modal opens
  useEffect(() => {
    if (showModal) {
      setNewOption(''); // Reset newOption state when modal is shown
      setError('');
      focusFieldRef.current.focus();
    }
  }, [showModal]); // Dependency array ensures this runs when showModal changes

  async function handleAddOption(url) {
    if (newOption.trim() !== '') {
      setLoading(true);
      setError(''); // Reset previous errors
  
      try {
        // Use provided URL if available; otherwise, construct default API URL
        const response = await fetch(url, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ name: newOption }), // Send 'name' instead of 'option'
        });
        
        const data = await response.json(); // Parse the response  
                
        if (response.ok) {
          onAddOption(field, data); // Update parent component with the new option, including its ID
          setNewOption('');
          closeModal();
          setError(''); // Clear any error message if successful
          //alert(data.message); // Show success message in alert (for testing)
        } else {
          // If response is not ok, parse the error message from the response
          setError(data.message || 'Error adding option in the server. Please try again.'); // Set the error message to display
        }
      } catch (err) {
        console.error('Error in handleAddOption:', err);
        setError('Error adding option from the try. Please try again.');
      } finally {
        setLoading(false);
      }
    }
  };
  
  return (
    showModal && (
      <div className="modal">
        <div className="modal-content">
          <span className="close" onClick={closeModal}>×</span>
          <h2>Add New {formattedField}</h2>
          
          <input
            ref={focusFieldRef}
            type="text"
            value={newOption}
            onChange={(e) => setNewOption(e.target.value)}
            placeholder={`Enter new ${field}`}
            disabled={loading}  // Disable input while loading
          />
          
          {error && <p className="error-message">{error}</p>}

          <div className="button-container">
            <button className="modal-btn" onClick={() => handleAddOption(url)} disabled={loading}>
              {loading ? 'Adding...' : 'Add'}
            </button>
            <button className="modal-btn" onClick={closeModal} disabled={loading}>Cancel</button>
          </div>
          
        </div>
      </div>
    )
  );
}

export default Modal;
