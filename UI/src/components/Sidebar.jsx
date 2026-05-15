// Sidebar.jsx - Navigation sidebar ng buong application
// Ito ang nakalagay sa kaliwa ng screen - navigation links dito

import { useLocation, useNavigate } from "react-router-dom";

// Listahan ng navigation items - para madaling mag-add ng bagong pages
const navItems = [
  { path: "/",           icon: "📊", label: "Dashboard"   },
  { path: "/assets",     icon: "💻", label: "Assets"      },
  { path: "/users",      icon: "👥", label: "Employees"   },
  { path: "/assignments",icon: "🔗", label: "Assignments" },
];

function Sidebar() {
  const location = useLocation(); // Para malaman kung saang page tayo ngayon
  const navigate = useNavigate(); // Para mag-navigate sa ibang pages

  return (
    <aside className="sidebar">
      {/* Brand/Logo section */}
      <div className="sidebar-brand">
        <h2>🖥️ IT Asset MS</h2>
        <p>Asset Management System</p>
      </div>

      {/* Navigation links */}
      <nav className="sidebar-nav">
        <p className="sidebar-section-label">Main Menu</p>

        {/* I-map ang navItems para gumawa ng nav links */}
        {navItems.map((item) => (
          <button
            key={item.path}
            className={`nav-link ${location.pathname === item.path ? "active" : ""}`}
            onClick={() => navigate(item.path)}
          >
            <span className="nav-icon">{item.icon}</span>
            {item.label}
          </button>
        ))}
      </nav>

      {/* Footer ng sidebar */}
      <div style={{ padding: "16px 20px", borderTop: "1px solid var(--border)" }}>
        <p style={{ fontSize: "11px", color: "var(--text-muted)" }}>
          Backend: localhost:8080
        </p>
      </div>
    </aside>
  );
}

export default Sidebar;
