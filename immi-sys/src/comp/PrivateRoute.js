import React from 'react';
import { Navigate } from 'react-router-dom'; // Import Navigate from react-router-dom

// PrivateRoute component to protect routes
function PrivateRoute({ children }) {
  const isAuthenticated = localStorage.getItem("isAuthenticated"); // Example, replace with real logic

  return isAuthenticated ? children : <Navigate to="/login" />; // Redirect to login if not authenticated
}

export default PrivateRoute;
