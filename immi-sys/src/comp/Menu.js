// Menu.js
import React, { useState, useEffect, useRef } from 'react';
import { Link } from 'react-router-dom';
import { API_ROUTES } from '../comp/config';
import '../css/Menu.css'; // Import the CSS file

const Menu = () => {
  const [activeSubmenu, setActiveSubmenu] = useState(null);
  const menuRef = useRef(null);

  const toggleSubmenu = (submenuName) => {
    setActiveSubmenu(activeSubmenu === submenuName ? null : submenuName);
  };

  const closeSubmenu = () => {
    setActiveSubmenu(null);
  };

  useEffect(() => {
    const adjustSubmenuPosition = () => {
      if (!menuRef.current) return;
      
      const submenus = menuRef.current.querySelectorAll('ul');
      submenus.forEach((submenu) => {
        const rect = submenu.getBoundingClientRect();
        const windowWidth = window.innerWidth;
        
        if (rect.right > windowWidth) {
          submenu.classList.add('left-align'); // Apply CSS to shift left
        } else {
          submenu.classList.remove('left-align'); // Reset if not needed
        }
      });
    };

    adjustSubmenuPosition();
    window.addEventListener('resize', adjustSubmenuPosition);
    return () => window.removeEventListener('resize', adjustSubmenuPosition);
  }, [activeSubmenu]);

  return (
    <div className="menu-container" id="menu-container" ref={menuRef}>
      {/* Logo Section */}
      <img src="/img/logo-company.png" className="logo" alt="Company Logo" width="100" />

      {/* Menu */}
      <div className="menu-subcontainer">
        <ul id="nav">
          <li className="current">
            <Link to="/dashboard" onClick={closeSubmenu}>Home</Link>
          </li>
          <li>
            <Link to="#" onClick={() => toggleSubmenu('registro')}>Registration</Link>
            <ul className={activeSubmenu === 'registro' ? 'visible' : 'hidden'}>
              <li><Link to={API_ROUTES.REDIRECT_TO_CUSTOMERFORM} onClick={closeSubmenu}>Customer</Link></li>
              <li><Link to={API_ROUTES.REDIRECT_TO_SERVICEFORM} onClick={closeSubmenu}>Services</Link></li>
              <li className="separator"></li>  {/* Styled separator */}
              <li><Link to="/dashboard/forms/form" onClick={closeSubmenu}>Forms/Templates</Link></li>
            </ul>
          </li>
          <li>
            <Link to="#" onClick={() => toggleSubmenu('procesos')}>Transactions</Link>
            <ul className={activeSubmenu === 'procesos' ? 'visible' : 'hidden'}>
              <li><Link to={API_ROUTES.REDIRECT_TO_USCISPETITIONFORM} onClick={closeSubmenu}>Family Petition</Link></li>
              <li><Link to={API_ROUTES.REDIRECT_TO_APPLICATIONFORM} onClick={closeSubmenu}>Immigration Application</Link></li>
              <li><Link to={API_ROUTES.REDIRECT_TO_TAX_FILING_FORM} onClick={closeSubmenu}>Tax Filing</Link></li>
              <li><Link to={API_ROUTES.REDIRECT_TO_SERVICE_REQUEST_FORM} onClick={closeSubmenu}>Service Request</Link></li>
              <li><Link to={API_ROUTES.REDIRECT_TO_INVOICEFORM} onClick={closeSubmenu}>Invoice</Link></li>
              <li><Link to={API_ROUTES.REDIRECT_TO_PETITIONFORM} onClick={closeSubmenu}>Petition</Link></li>
            </ul>
          </li>
          <li>
            <Link to="#" onClick={() => toggleSubmenu('listado')}>List</Link>
            <ul className={activeSubmenu === 'listado' ? 'visible' : 'hidden'}>
              <li><Link to={API_ROUTES.REDIRECT_TO_CUSTOMERLIST} onClick={closeSubmenu}>Customers</Link></li>
              <li><Link to={API_ROUTES.REDIRECT_TO_USCISPETITIONLIST} onClick={closeSubmenu}>Family Petition</Link></li>
              <li><Link to={API_ROUTES.REDIRECT_TO_APPLICATIONLIST} onClick={closeSubmenu}>Immigration Applic.</Link></li>
              <li><Link to={API_ROUTES.REDIRECT_TO_TAX_FILING_LIST} onClick={closeSubmenu}>Tax Filings</Link></li>
              <li><Link to={API_ROUTES.REDIRECT_TO_SERVICE_REQUEST_LIST} onClick={closeSubmenu}>Service Requests</Link></li>
              <li><Link to={API_ROUTES.REDIRECT_TO_FORMSLIST} onClick={closeSubmenu}>Forms and Docs.</Link></li>
              <li><Link to={API_ROUTES.REDIRECT_TO_SERVICELIST} onClick={closeSubmenu}>Service</Link></li>
              <li><Link to={API_ROUTES.REDIRECT_TO_INVOICELIST} onClick={closeSubmenu}>Invoices</Link></li>
            </ul>
          </li>
          <li>
            <Link to="#" onClick={() => toggleSubmenu('listado')}>Reports</Link>
            <ul className={activeSubmenu === 'reportes' ? 'visible' : 'hidden'}>
              <li><Link to={API_ROUTES.REDIRECT_TO_PROFIT_REPORT} onClick={closeSubmenu}>Profit Report</Link></li>
            </ul>
          </li>
          <li>
            <Link to="/" onClick={closeSubmenu}>Log Out</Link>
          </li>
        </ul>
      </div>
    </div>
  );
};

export default Menu;
