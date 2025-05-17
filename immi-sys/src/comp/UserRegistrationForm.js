import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from 'axios';
import "../css/UserRegistrationForm.css";

const UserRegisterForm = () => {
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    password: "",
    confirmPassword: "",
    role: "user",
  });

  const navigate = useNavigate();
  const [errors, setErrors] = useState({});
  const [successMessage, setSuccessMessage] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  const validateForm = () => {
    const newErrors = {};

    if (!formData.firstName.trim()) {
      newErrors.firstName = "First name is required";
    }

    if (!formData.lastName.trim()) {
      newErrors.lastName = "Last name is required";
    }

    if (!formData.email.trim()) {
      newErrors.email = "Email is required";
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = "Email is invalid";
    }

    if (!formData.password.trim()) {
      newErrors.password = "Password is required";
    } else if (formData.password.length < 8) {
      newErrors.password = "Password must be at least 8 characters";
    }

    if (formData.password !== formData.confirmPassword) {
      newErrors.confirmPassword = "Passwords do not match";
    }

    if (!formData.role) {
      newErrors.role = "Role is required";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleCancel = () => {
    navigate("/homepage");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
  
    if (validateForm()) {
      console.log('Form data submitted:', formData);
  
      setIsSubmitting(true);
  
      try {
        const response = await axios.post('/users/register', formData, {
          headers: {
            'Content-Type': 'application/json',
          },
        });
  
        console.log('Registration successful:', response.data);
        setSuccessMessage(response.data.message || 'User registered successfully!');
        setFormData({
          firstName: '',
          lastName: '',
          email: '',
          password: '',
          confirmPassword: '',
          role: 'user',
        });
        setErrors({});
        navigate('/homepage');
        window.location.reload();
      } catch (error) {
        console.error('Error:', error.response?.data || error.message);
        setErrors({ apiError: error.response?.data || error.message });
      } finally {
        setIsSubmitting(false);
      }
    } else {
      console.log('Form validation failed');
    }
  };

  return (
    <div className="user-register-container">
      <h2>Register a New User</h2>
      <form className="user-register-form" onSubmit={handleSubmit}>
        <div className="user-register-group">
          <label htmlFor="firstName">First Name</label>
          <input
            type="text"
            id="firstName"
            name="firstName"
            value={formData.firstName}
            onChange={handleChange}
            className={`user-register-input ${errors.firstName ? "error" : ""}`}
          />
          {errors.firstName && (
            <p className="user-register-error-message">{errors.firstName}</p>
          )}
        </div>

        <div className="user-register-group">
          <label htmlFor="lastName">Last Name</label>
          <input
            type="text"
            id="lastName"
            name="lastName"
            value={formData.lastName}
            onChange={handleChange}
            className={`user-register-input ${errors.lastName ? "error" : ""}`}
          />
          {errors.lastName && (
            <p className="user-register-error-message">{errors.lastName}</p>
          )}
        </div>

        <div className="user-register-group">
          <label htmlFor="email">Email</label>
          <input
            type="email"
            id="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            className={`user-register-input ${errors.email ? "error" : ""}`}
          />
          {errors.email && (
            <p className="user-register-error-message">{errors.email}</p>
          )}
        </div>

        <div className="user-register-group">
          <label htmlFor="password">Password</label>
          <input
            type="password"
            id="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            className={`user-register-input ${errors.password ? "error" : ""}`}
          />
          {errors.password && (
            <p className="user-register-error-message">{errors.password}</p>
          )}
        </div>

        <div className="user-register-group">
          <label htmlFor="confirmPassword">Confirm Password</label>
          <input
            type="password"
            id="confirmPassword"
            name="confirmPassword"
            value={formData.confirmPassword}
            onChange={handleChange}
            className={`user-register-input ${
              errors.confirmPassword ? "error" : ""
            }`}
          />
          {errors.confirmPassword && (
            <p className="user-register-error-message">
              {errors.confirmPassword}
            </p>
          )}
        </div>

        <div className="user-register-group">
          <label htmlFor="role">Role</label>
          <select
            id="role"
            name="role"
            value={formData.role}
            onChange={handleChange}
            className={`user-register-select ${errors.role ? "error" : ""}`}
          >
            <option value="user">User</option>
            <option value="admin">Admin</option>
          </select>
          {errors.role && (
            <p className="user-register-error-message">{errors.role}</p>
          )}
        </div>

        <button
          type="submit"
          className="user-register-submit"
          disabled={isSubmitting}
        >
          {isSubmitting ? "Submitting..." : "Register"}
        </button>
        <button type="button" className="login-button" onClick={handleCancel}>
          Cancel
        </button>
      </form>
      {successMessage && (
        <p className="user-register-success-message">{successMessage}</p>
      )}
    </div>
  );
};

export default UserRegisterForm;
