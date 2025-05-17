import React from 'react';
import '../css/Dashboard.css'; // Assuming you have a separate CSS file for styles

const Dashboard = () => {
  return (
    <div className="dashboard-page">
      {/* Logo Section */}
      <img src="/img/logo-company.png" alt="Company Logo" className="dashboard-logo-image" />
      <div className="dashboard-text-container">
        <h1>Immigration and Travel System!</h1>
        <p>We specialize in K-1 fiancé visas, marriage green cards,
          and all types of family-based immigration.
          Choose expert lawyer support or our guided application service,
          both with affordable payment options and a track record of success.</p>
      </div>
    </div>
  );
};

export default Dashboard;
