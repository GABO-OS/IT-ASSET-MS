// App.jsx - Main component ng buong application
// Dito nakalagay ang routing - kung anong component ang ipapakita per URL

import { BrowserRouter, Routes, Route } from "react-router-dom";
import Sidebar    from "./components/Sidebar";
import Dashboard  from "./pages/Dashboard";
import Assets     from "./pages/Assets";
import Users      from "./pages/Users";
import Assignments from "./pages/Assignments";

function App() {
  return (
    // BrowserRouter - para gumana ang routing ng React Router
    <BrowserRouter>
      <div className="app-layout">

        {/* Sidebar - laging visible kahit mag-navigate sa ibang pages */}
        <Sidebar />

        {/* Main content area - dito nagbabago ang content depende sa URL */}
        <main className="main-content">
          <Routes>
            {/* Default route - Dashboard ang unang makikita */}
            <Route path="/"            element={<Dashboard />}   />
            <Route path="/assets"      element={<Assets />}      />
            <Route path="/users"       element={<Users />}       />
            <Route path="/assignments" element={<Assignments />} />

            {/* 404 - Kung mag-navigate sa hindi existing na page */}
            <Route path="*" element={
              <div style={{ textAlign: "center", padding: "80px", color: "var(--text-muted)" }}>
                <div style={{ fontSize: "64px" }}>🔍</div>
                <h2 style={{ marginTop: "16px" }}>Page Not Found</h2>
                <p>Ang page na hinahanap mo ay wala.</p>
              </div>
            } />
          </Routes>
        </main>

      </div>
    </BrowserRouter>
  );
}

export default App;
