import React from "react";

// ProcessEventsTable Component
const ProcessEventsTable = ({
  columns,
  processEvents,
  handleEventRowClick,
  handleDeleteEvent,
}) => {
  return (
    <table className="event-table" style={{ width: "100%", tableLayout: "fixed" }}>
      <thead>
        <tr>
          {columns.map((column) => (
            <th key={column.key} style={{ width: column.width }}>
              {column.label}
            </th>
          ))}
        </tr>
      </thead>
      <tbody>
        {processEvents.map((ev, index) => (
          <tr
            key={index}
            onClick={() => handleEventRowClick(ev)}
            className="list-clickable-row"
          >
            {columns.map((column) => (
              <td key={column.key}>
                {column.key === "eventDate"
                  ? new Date(ev[column.key]).toLocaleString() // format eventDate
                  : column.key === "actions" ? (
                      <button
                        onClick={(e) => {
                          e.stopPropagation(); // prevent row click
                          handleDeleteEvent(ev.id);
                        }}
                      >
                        <img
                          src="/img/TrashBin.png"
                          alt="Remove"
                          className="btn"
                          style={{ width: "20px", height: "20px" }}
                        />
                      </button>
                    ) : (
                      ev[column.key]?.name || ev[column.key] // handle possible nested objects
                    )}
              </td>
            ))}
          </tr>
        ))}
      </tbody>
    </table>
  );
};

export default ProcessEventsTable;
