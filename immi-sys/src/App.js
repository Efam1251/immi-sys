import React, { useState } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from './context/AuthContext';
import { API_ROUTES } from './comp/config';
import PrivateRoute from './comp/PrivateRoute';
import Layout from "./comp/Layout";
import LandingPage from "./comp/LandingPage";
import Dashboard from "./comp/Dashboard";
import Login from "./comp/Login";
import CustomerForm from "./comp/CustomerForm";
import CustomerList from "./comp/CustomerList";
import ApplicationForm from "./comp/ApplicationForm";
import ApplicationList from "./comp/ApplicationList";
import UscisPetitionForm from "./comp/UscisPetitionForm";
import UscisPetitionList from "./comp/UscisPetitionList";
import PetitionForm from "./comp/PetitionForm";
import PetitionList from "./comp/PetitionList";
import ServiceForm from "./comp/ServiceForm";
import ServiceList from "./comp/ServiceList";
import InvoiceForm from "./comp/InvoiceForm";
import InvoiceList from "./comp/InvoiceList";
import VisaApplicationForm from "./comp/VisaApplicationForm";
import VisaApplicationList from "./comp/VisaApplicationList";
import FormsForm from "./comp/FormsForm";
import FormList from "./comp/FormList";
import StateForm from "./comp/StateForm";
import StateList from "./comp/StateList";
import UserRegistrationForm from "./comp/UserRegistrationForm";
import HotelList from "./comp/HotelList";
import TaxFilingForm from "./comp/TaxFilingForm";
import TaxFilingList from "./comp/TaxFilingList";
import CitizenshipForm from "./comp/CitizenshipApplicationForm";
import CitizenshipList from "./comp/CitizenshipApplicationList";
import InvoiceProfitReport from "./comp/InvoiceProfitReport";
import ServiceRequestForm from "./comp/ServiceRequestForm";
import ServiceRequestList from "./comp/ServiceRequestList";

import HotelForm from "./comp/HotelForm";

function App() {

  return (
    <AuthProvider>
        <Routes>
          {/* No layout for login */}
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<UserRegistrationForm />} />

          {/* ✅ LandingPage at root and homepage */}
          <Route path="/" element={<LandingPage />} />
          <Route path="/homepage" element={<LandingPage />} />
          
          {/* ✅ Protected app routes with layout */}
          <Route
            path="/dashboard"
            element={
              <PrivateRoute>
                <Layout />
              </PrivateRoute>
            }
          >
            {/* These routes are protected */}
            <Route index element={<Dashboard />} />
            <Route path={API_ROUTES.REDIRECT_TO_CUSTOMERFORM} element={<CustomerForm />} />
            <Route path={API_ROUTES.REDIRECT_TO_CUSTOMERLIST} element={<CustomerList />} />
            <Route path={API_ROUTES.REDIRECT_TO_APPLICATIONFORM} element={<ApplicationForm />} />
            <Route path={API_ROUTES.REDIRECT_TO_APPLICATIONLIST} element={<ApplicationList />} />
            <Route path={API_ROUTES.REDIRECT_TO_PETITIONFORM} element={<PetitionForm />} />
            <Route path={API_ROUTES.REDIRECT_TO_PETITIONLIST} element={<PetitionList />} />
            <Route path={API_ROUTES.REDIRECT_TO_USCISPETITIONFORM} element={<UscisPetitionForm />} />
            <Route path={API_ROUTES.REDIRECT_TO_USCISPETITIONLIST} element={<UscisPetitionList />} />
            <Route path={API_ROUTES.REDIRECT_TO_SERVICEFORM} element={<ServiceForm />} />
            <Route path={API_ROUTES.REDIRECT_TO_SERVICELIST} element={<ServiceList />} />
            <Route path={API_ROUTES.REDIRECT_TO_INVOICEFORM} element={<InvoiceForm />} />
            <Route path={API_ROUTES.REDIRECT_TO_INVOICELIST} element={<InvoiceList />} />
            <Route path={API_ROUTES.REDIRECT_TO_VISAAPPLICATIONFORM} element={<VisaApplicationForm />} />
            <Route path={API_ROUTES.REDIRECT_TO_VISAAPPLICATIONLIST} element={<VisaApplicationList />} />
            <Route path={API_ROUTES.REDIRECT_TO_CITIZENSHIPAPPLICATIONFORM} element={<CitizenshipForm />} />
            <Route path={API_ROUTES.REDIRECT_TO_CITIZENSHIPAPPLICATIONLIST} element={<CitizenshipList />} />
            <Route path={API_ROUTES.REDIRECT_TO_SERVICE_REQUEST_FORM} element={<ServiceRequestForm />} />
            <Route path={API_ROUTES.REDIRECT_TO_SERVICE_REQUEST_LIST} element={<ServiceRequestList />} />
            
            <Route path="forms/form" element={<FormsForm />} />
            <Route path="forms/list" element={<FormList />} />
            <Route path={API_ROUTES.REDIRECT_TO_STATEFORM} element={<StateForm />} />
            <Route path={API_ROUTES.REDIRECT_TO_STATELIST} element={<StateList />} />
            <Route path="/dashboard/hotel/list" element={<HotelList />} />
            <Route path={API_ROUTES.REDIRECT_TO_TAX_FILING_FORM} element={<TaxFilingForm />} />
            <Route path={API_ROUTES.REDIRECT_TO_TAX_FILING_LIST} element={<TaxFilingList />} />
            <Route path="/dashboard/hotel/form" element={<HotelForm />} />
            <Route path={API_ROUTES.REDIRECT_TO_PROFIT_REPORT} element={<InvoiceProfitReport />} />
          </Route>
        </Routes>
    </AuthProvider>
  );
}

export default App;
