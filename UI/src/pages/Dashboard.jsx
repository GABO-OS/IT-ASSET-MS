// Dashboard.jsx - Overview page ng buong system
// Dito makikita ang summary/statistics ng lahat ng assets, users, at assignments

import { useState, useEffect } from "react";
import { getAllAssets } from "../api/assetApi";
import { getAllUsers } from "../api/userApi";
import { getAllAssignments, getActiveAssignments } from "../api/assignmentApi";

function Dashboard() {
  // State para sa statistics data
  const [stats, setStats] = useState({
    totalAssets: 0,
    availableAssets: 0,
    inUseAssets: 0,
    underRepair: 0,
    lostAssets: 0,
    totalUsers: 0,
    activeAssignments: 0,
    totalAssignments: 0,
  });

  const [recentAssignments, setRecentAssignments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // I-fetch ang data kapag na-load ang page
  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      setLoading(true);

      // I-call ang lahat ng APIs sabay-sabay para mas mabilis (parallel)
      const [assetsRes, usersRes, assignmentsRes, activeAssignmentsRes] =
        await Promise.all([
          getAllAssets(),
          getAllUsers(),
          getAllAssignments(),
          getActiveAssignments(),
        ]);

      const assets = assetsRes.data;
      const users = usersRes.data;
      const assignments = assignmentsRes.data;
      const activeAssignments = activeAssignmentsRes.data;

      // Kalkulahin ang statistics mula sa fetched data
      setStats({
        totalAssets: assets.length,
        availableAssets: assets.filter((a) => a.status === "AVAILABLE").length,
        inUseAssets: assets.filter((a) => a.status === "IN_USE").length,
        underRepair: assets.filter((a) => a.status === "UNDER_REPAIR").length,
        lostAssets: assets.filter((a) => a.status === "LOST").length,
        totalUsers: users.length,
        activeAssignments: activeAssignments.length,
        totalAssignments: assignments.length,
      });

      // Ipakita ang pinaka-recent na 5 assignments
      setRecentAssignments(assignments.slice(-5).reverse());
    } catch (err) {
      setError("Hindi ma-load ang data. Siguraduhing naka-run ang backend!");
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div className="loading">⏳ Loading dashboard...</div>;

  return (
    <div>
      {/* Page header */}
      <div className="page-header">
        <h1 className="page-title">📊 Dashboard</h1>
        <p className="page-subtitle">Overview ng buong IT Asset Management System</p>
      </div>

      {/* Error message kung hindi ma-connect sa backend */}
      {error && (
        <div className="alert alert-error">
          ⚠️ {error}
        </div>
      )}

      {/* Statistics cards - 2 rows */}
      <p className="sidebar-section-label" style={{ marginBottom: "12px" }}>ASSETS</p>
      <div className="stats-grid">
        <div className="stat-card">
          <div className="stat-icon">💻</div>
          <div className="stat-number">{stats.totalAssets}</div>
          <div className="stat-label">Total Assets</div>
        </div>
        <div className="stat-card success">
          <div className="stat-icon">✅</div>
          <div className="stat-number" style={{ color: "var(--success)" }}>
            {stats.availableAssets}
          </div>
          <div className="stat-label">Available</div>
        </div>
        <div className="stat-card warning">
          <div className="stat-icon">🔄</div>
          <div className="stat-number" style={{ color: "var(--warning)" }}>
            {stats.inUseAssets}
          </div>
          <div className="stat-label">In Use</div>
        </div>
        <div className="stat-card danger">
          <div className="stat-icon">🔧</div>
          <div className="stat-number" style={{ color: "var(--repair)" }}>
            {stats.underRepair}
          </div>
          <div className="stat-label">Under Repair</div>
        </div>
        <div className="stat-card purple">
          <div className="stat-icon">❌</div>
          <div className="stat-number" style={{ color: "var(--lost)" }}>
            {stats.lostAssets}
          </div>
          <div className="stat-label">Lost</div>
        </div>
      </div>

      <p className="sidebar-section-label" style={{ margin: "20px 0 12px" }}>PEOPLE & ASSIGNMENTS</p>
      <div className="stats-grid" style={{ gridTemplateColumns: "repeat(auto-fit, minmax(200px, 1fr))" }}>
        <div className="stat-card">
          <div className="stat-icon">👥</div>
          <div className="stat-number">{stats.totalUsers}</div>
          <div className="stat-label">Total Employees</div>
        </div>
        <div className="stat-card success">
          <div className="stat-icon">🔗</div>
          <div className="stat-number" style={{ color: "var(--success)" }}>
            {stats.activeAssignments}
          </div>
          <div className="stat-label">Active Assignments</div>
        </div>
        <div className="stat-card">
          <div className="stat-icon">📋</div>
          <div className="stat-number">{stats.totalAssignments}</div>
          <div className="stat-label">Total Assignment History</div>
        </div>
      </div>

      {/* Recent assignments table */}
      <div className="card" style={{ marginTop: "28px" }}>
        <h3 style={{ marginBottom: "16px", fontSize: "15px" }}>
          🕐 Recent Assignments
        </h3>
        {recentAssignments.length === 0 ? (
          <div className="empty-state">
            <div className="empty-icon">📋</div>
            <p>Wala pang assignments</p>
          </div>
        ) : (
          <div className="table-container">
            <table>
              <thead>
                <tr>
                  <th>Asset</th>
                  <th>Employee</th>
                  <th>Assigned By</th>
                  <th>Date</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {recentAssignments.map((a) => (
                  <tr key={a.id}>
                    <td>{a.asset?.name || "—"}</td>
                    <td>{a.user?.fullName || "—"}</td>
                    <td>{a.assignedBy || "—"}</td>
                    <td>{a.assignedDate}</td>
                    <td>
                      <span className={`badge badge-${a.status?.toLowerCase()}`}>
                        {a.status}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}

export default Dashboard;
