import { BrowserRouter, Routes, Route } from "react-router-dom";
import Sidebar    from "./components/Sidebar";
import Dashboard  from "./pages/Dashboard";
import Assets     from "./pages/Assets";
import Users      from "./pages/Users";
import Assignments from "./pages/Assignments";
import Maintenance from "./pages/Maintenance";

/**
 * Main Application Component
 * Handles routing and global layout
 */
function App() {
  return (
    <BrowserRouter>
      <div className="app-layout">

        {/* Sidebar remains visible across all pages */}
        <Sidebar />

        {/* Main content area dynamically changes based on the current URL */}
        <main className="main-content">
          <Routes>
            {/* Navigation Routes */}
            <Route path="/"            element={<Dashboard />}   />
            <Route path="/assets"      element={<Assets />}      />
            <Route path="/users"       element={<Users />}       />
            <Route path="/assignments" element={<Assignments />} />
            <Route path="/maintenance" element={<Maintenance />} />

            {/* 404 - Page Not Found Handler */}
            <Route path="*" element={
              <div style={{ textAlign: "center", padding: "80px", color: "var(--text-muted)" }}>
                <div style={{ fontSize: "64px" }}>🔍</div>
                <h2 style={{ marginTop: "16px" }}>Page Not Found</h2>
                <p>The page you are looking for does not exist.</p>
              </div>
            } />
          </Routes>
        </main>

      </div>
    </BrowserRouter>
  );
}

export default App;
