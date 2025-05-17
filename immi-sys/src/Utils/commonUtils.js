export const CONFIG_SETTING = {
  LOCAL_HOST: "http://localhost:8080",
}
// Format date to YYYY-MM-DD
export const sqlformatDate = (date) => {
  const d = new Date(date);
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(
    2,
    "0"
  )}-${String(d.getDate()).padStart(2, "0")}`;
};

// Format date to MM/DD/YYYY
export const isoformatDate = (iso) => {
  if (!iso) return "All Dates";
  const [y, m, d] = iso.split("-");
  return `${m}/${d}/${y}`;
};

export const toLocalISO = (date) => {
  const y = date.getFullYear();
  const m = String(date.getMonth() + 1).padStart(2, "0");
  const d = String(date.getDate()).padStart(2, "0");
  return `${y}-${m}-${d}`;
};

// Add days to date
export const addDaysToDate = (date, days) => {
  const d = new Date(date);
  d.setDate(d.getDate() + days);
  return d;
};

export function formatToCamelCase(input) {
  return input
    .replace(/([A-Z])/g, ' $1') // Insert space before each capital letter
    .trim()
    .split(' ')
    .map((word, index) =>
      index === 0 ? word.toLowerCase() : word.charAt(0).toUpperCase() + word.slice(1)
    )
    .join('');
}

export const formatCurrency = (num) => {
  return new Intl.NumberFormat("en-US", {
    style: "currency",
    currency: "USD",
  }).format(num);
};