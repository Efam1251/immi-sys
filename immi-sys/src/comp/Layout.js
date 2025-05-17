// Layout.js
import React from 'react';
import { Outlet } from 'react-router-dom';
import Menu from './Menu';
import '../css/Layout.css';

const Layout = () => {
  return (
    <div className="layout-wrapper">
      <div className="menu-overlay">
        <Menu />
      </div>
      <div className="main-content">
        <Outlet />
      </div>
    </div>
  );
};

export default Layout;
