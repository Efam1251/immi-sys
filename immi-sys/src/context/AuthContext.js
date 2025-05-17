import React, { createContext, useState, useContext } from 'react';

// Create a context
const AuthContext = createContext();

// AuthProvider component to wrap your app and provide auth state
export const AuthProvider = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(localStorage.getItem("isAuthenticated") === 'true');

  const login = () => {
    setIsAuthenticated(true);
    localStorage.setItem("isAuthenticated", 'true');
  };

  const logout = () => {
    setIsAuthenticated(false);
    localStorage.setItem("isAuthenticated", 'false');
  };

  return (
    <AuthContext.Provider value={{ isAuthenticated, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

// Custom hook to access auth context
export const useAuth = () => useContext(AuthContext);
