import { NavLink } from "react-router-dom";

/**
 * Sidebar Component
 * Provides main navigation for the system
 */
function Sidebar() {
  return (
    <aside className="sidebar">
      <div className="sidebar-brand">
        <h2>IT ASSET MS</h2>
        <p>Asset Management System</p>
      </div>

      <nav className="sidebar-nav">
        <div className="sidebar-section-label">General</div>
        <NavLink to="/" className="nav-link">
          <span className="nav-icon">📊</span>
          <span>Dashboard</span>
        </NavLink>

        <div className="sidebar-section-label">Inventory</div>
        <NavLink to="/assets" className="nav-link">
          <span className="nav-icon">💻</span>
          <span>Asset List</span>
        </NavLink>

        <div className="sidebar-section-label">Resources</div>
        <NavLink to="/users" className="nav-link">
          <span className="nav-icon">👥</span>
          <span>Employees</span>
        </NavLink>

        <div className="sidebar-section-label">Operations</div>
        <NavLink to="/assignments" className="nav-link">
          <span className="nav-icon">📝</span>
          <span>Assignments</span>
        </NavLink>
        <NavLink to="/maintenance" className="nav-link">
          <span className="nav-icon">🔧</span>
          <span>Maintenance</span>
        </NavLink>
      </nav>
    </aside>
  );
}

export default Sidebar;
