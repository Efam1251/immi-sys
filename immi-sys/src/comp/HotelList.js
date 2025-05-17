import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "../css/HotelList.css";

function HotelList() {
  const [hotels, setHotels] = useState([]);
  const [searchTerm, setSearchTerm] = useState(""); // Track the search term
  const navigate = useNavigate();

  // Fetch hotel data
  useEffect(() => {
    fetch("/travel/forms/hotels/list") // Replace with your API endpoint for hotels
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
      })
      .then((data) => {
        if (Array.isArray(data)) {
          setHotels(data);
        } else {
          console.error("Data received is not an array:", data);
          setHotels([]);
        }
      })
      .catch((error) => {
        console.error("Error fetching hotels:", error);
      });
  }, []);

  const handleRowClick = (id) => {
    navigate(`/dashboard/hotel/form?direction=Current&id=${id}`);
  };

  // Filter hotels based on search term
  const filteredHotels = hotels.filter((hotel) => {
    const fullName = `${hotel.name ?? ""} ${hotel.address ?? ""}`.toLowerCase();
    const lowerSearchTerm = searchTerm.toLowerCase(); // Convert searchTerm to lowercase once

    return (
      fullName.includes(lowerSearchTerm) ||
      hotel.id?.toString().includes(lowerSearchTerm) ||
      hotel.address?.toLowerCase().includes(lowerSearchTerm)
    );
  });

  return (
    <div className="hotel-list-container">
      <h1 className="hotel-list-header">Hotel List</h1>

      {/* Search Field */}
      <div className="list-search-input-container">
        <input
          type="text"
          placeholder="Search by Name, Address, or ID"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="list-search-input"
        />
      </div>

      {/* Header Table */}
      <table className="list-table">
        <thead>
          <tr>
            <th className="hotel-id">Id</th>
            <th className="hotel-name">Name</th>
            <th className="hotel-address">Address</th>
          </tr>
        </thead>
      </table>

      {/* Scrollable Body Table */}
      <div className="list-table-container">
        <table className="list-table">
          <tbody>
            {filteredHotels.map((hotel) => (
              <tr
                key={hotel.id}
                className="list-clickable-row"
                onClick={() => handleRowClick(hotel.id)}
              >
                <td className="hotel-id">{hotel.id}</td>
                <td className="hotel-name">{hotel.name}</td>
                <td className="hotel-address">{hotel.address}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default HotelList;
