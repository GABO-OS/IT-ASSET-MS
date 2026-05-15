// Assignments.jsx - Page para sa pag-assign at pag-return ng IT assets
// Ito yung pinaka-main na functionality ng system

import { useState, useEffect } from "react";
import {
  getAllAssignments, assignAsset, returnAsset, markAsLost,
} from "../api/assignmentApi";
import { getAllAssets } from "../api/assetApi";
import { getAllUsers } from "../api/userApi";
import Modal from "../components/Modal";

const statusBadge = (status) => {
  const map = { ACTIVE: "active", RETURNED: "returned", LOST: "lost" };
  return `badge badge-${map[status] || "retired"}`;
};

const emptyForm = { assetId: "", userId: "", assignedBy: "", notes: "", assignedDate: "" };

function Assignments() {
  const [assignments, setAssignments] = useState([]);
  const [filtered, setFiltered]       = useState([]);
  const [assets, setAssets]           = useState([]);   // Para sa assign dropdown
  const [users, setUsers]             = useState([]);   // Para sa assign dropdown
  const [loading, setLoading]         = useState(true);
  const [error, setError]             = useState(null);
  const [search, setSearch]           = useState("");
  const [filterStatus, setFilterStatus] = useState("ALL"); // Filter by status
  const [showModal, setShowModal]     = useState(false);
  const [form, setForm]               = useState(emptyForm);
  const [formError, setFormError]     = useState(null);

  // Kunin lang ang available assets para sa dropdown
  const availableAssets = assets.filter((a) => a.status === "AVAILABLE");

  useEffect(() => { fetchAll(); }, []);

  // I-filter ang assignments kapag nag-change ang search o status filter
  useEffect(() => {
    let result = assignments;

    // Filter by status
    if (filterStatus !== "ALL") {
      result = result.filter((a) => a.status === filterStatus);
    }

    // Filter by search
    const q = search.toLowerCase();
    if (q) {
      result = result.filter(
        (a) =>
          a.asset?.name?.toLowerCase().includes(q) ||
          a.user?.fullName?.toLowerCase().includes(q) ||
          (a.assignedBy || "").toLowerCase().includes(q)
      );
    }

    setFiltered(result);
  }, [search, filterStatus, assignments]);

  const fetchAll = async () => {
    try {
      setLoading(true);
      // I-fetch ang assignments, assets, at users sabay-sabay
      const [assignRes, assetRes, userRes] = await Promise.all([
        getAllAssignments(),
        getAllAssets(),
        getAllUsers(),
      ]);
      // Pinaka-bago ang nasa itaas
      setAssignments([...assignRes.data].reverse());
      setAssets(assetRes.data);
      setUsers(userRes.data);
    } catch {
      setError("Hindi ma-load ang assignments.");
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) =>
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));

  const handleAssign = async () => {
    if (!form.assetId || !form.userId || !form.assignedBy) {
      setFormError("Kailangan ng Asset, Employee, at Assigned By!");
      return;
    }
    try {
      await assignAsset({
        assetId: Number(form.assetId),
        userId: Number(form.userId),
        assignedBy: form.assignedBy,
        notes: form.notes,
        assignedDate: form.assignedDate || null,
      });
      setShowModal(false);
      fetchAll(); // I-refresh lahat
    } catch (err) {
      setFormError(err.response?.data?.message || "May error. Baka in-use na ang asset.");
    }
  };

  // I-return ang asset - kailangan ng confirmation
  const handleReturn = async (id, assetName) => {
    if (!confirm(`I-return ang asset na "${assetName}"?`)) return;
    try {
      await returnAsset(id);
      fetchAll();
    } catch {
      alert("Hindi ma-process ang return.");
    }
  };

  // I-mark ang asset na nawala
  const handleMarkLost = async (id, assetName) => {
    if (!confirm(`I-mark bilang LOST ang "${assetName}"? Mababago ang status ng asset.`)) return;
    try {
      await markAsLost(id);
      fetchAll();
    } catch {
      alert("Hindi ma-mark as lost.");
    }
  };

  if (loading) return <div className="loading">⏳ Loading assignments...</div>;

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">🔗 Assignments</h1>
        <p className="page-subtitle">Track kung sino ang may hawak ng anong IT asset</p>
      </div>

      {error && <div className="alert alert-error">⚠️ {error}</div>}

      {/* Toolbar - search, filter, at assign button */}
      <div className="toolbar">
        <div style={{ display: "flex", gap: "10px", flex: 1, flexWrap: "wrap" }}>
          <div className="search-box">
            <span className="search-icon">🔍</span>
            <input
              placeholder="Maghanap..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
            />
          </div>
          {/* Status filter dropdown */}
          <select
            className="form-control"
            style={{ width: "auto", padding: "8px 14px" }}
            value={filterStatus}
            onChange={(e) => setFilterStatus(e.target.value)}
          >
            <option value="ALL">All Status</option>
            <option value="ACTIVE">Active</option>
            <option value="RETURNED">Returned</option>
            <option value="LOST">Lost</option>
          </select>
        </div>
        <button
          className="btn btn-primary"
          onClick={() => { setForm(emptyForm); setFormError(null); setShowModal(true); }}
        >
          ＋ Assign Asset
        </button>
      </div>

      {/* Assignments table */}
      <div className="card">
        {filtered.length === 0 ? (
          <div className="empty-state">
            <div className="empty-icon">🔗</div>
            <p>Walang assignments na nahanap</p>
          </div>
        ) : (
          <div className="table-container">
            <table>
              <thead>
                <tr>
                  <th>#</th>
                  <th>Asset</th>
                  <th>Employee</th>
                  <th>Assigned By</th>
                  <th>Date Assigned</th>
                  <th>Date Returned</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {filtered.map((a, idx) => (
                  <tr key={a.id}>
                    <td style={{ color: "var(--text-muted)" }}>{idx + 1}</td>
                    <td style={{ fontWeight: 500 }}>{a.asset?.name || "—"}</td>
                    <td>{a.user?.fullName || "—"}</td>
                    <td style={{ color: "var(--text-secondary)" }}>{a.assignedBy || "—"}</td>
                    <td>{a.assignedDate || "—"}</td>
                    <td style={{ color: "var(--text-secondary)" }}>{a.returnedDate || "—"}</td>
                    <td>
                      <span className={statusBadge(a.status)}>{a.status}</span>
                    </td>
                    <td>
                      {/* Ipakita lang ang return/lost buttons kung ACTIVE ang assignment */}
                      {a.status === "ACTIVE" && (
                        <div className="action-buttons">
                          <button
                            className="btn btn-success btn-sm"
                            onClick={() => handleReturn(a.id, a.asset?.name)}
                          >
                            ↩️ Return
                          </button>
                          <button
                            className="btn btn-danger btn-sm"
                            onClick={() => handleMarkLost(a.id, a.asset?.name)}
                          >
                            ❌ Lost
                          </button>
                        </div>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {/* Assign Asset Modal */}
      {showModal && (
        <Modal
          title="🔗 Assign Asset to Employee"
          onClose={() => setShowModal(false)}
          footer={
            <>
              <button className="btn btn-secondary" onClick={() => setShowModal(false)}>
                Cancel
              </button>
              <button className="btn btn-primary" onClick={handleAssign}>
                Assign Now
              </button>
            </>
          }
        >
          {formError && <div className="alert alert-error">⚠️ {formError}</div>}

          <div className="form-group">
            <label className="form-label">Asset (Available lang ang pwedeng i-assign) *</label>
            <select name="assetId" className="form-control" value={form.assetId} onChange={handleChange}>
              <option value="">-- Pumili ng Asset --</option>
              {availableAssets.map((a) => (
                <option key={a.id} value={a.id}>
                  {a.name} · {a.type} · {a.serialNumber}
                </option>
              ))}
            </select>
            {availableAssets.length === 0 && (
              <p style={{ fontSize: "12px", color: "var(--warning)", marginTop: "4px" }}>
                ⚠️ Walang available na assets!
              </p>
            )}
          </div>

          <div className="form-group">
            <label className="form-label">Employee *</label>
            <select name="userId" className="form-control" value={form.userId} onChange={handleChange}>
              <option value="">-- Pumili ng Employee --</option>
              {users.filter((u) => u.active).map((u) => (
                <option key={u.id} value={u.id}>
                  {u.fullName} · {u.department}
                </option>
              ))}
            </select>
          </div>

          <div className="form-grid-2">
            <div className="form-group">
              <label className="form-label">Assigned By *</label>
              <input name="assignedBy" className="form-control"
                placeholder="Pangalan mo (Admin)" value={form.assignedBy} onChange={handleChange} />
            </div>
            <div className="form-group">
              <label className="form-label">Date Assigned</label>
              <input name="assignedDate" type="date" className="form-control"
                value={form.assignedDate} onChange={handleChange} />
            </div>
          </div>

          <div className="form-group">
            <label className="form-label">Notes</label>
            <input name="notes" className="form-control"
              placeholder="hal. Para sa project use lang..." value={form.notes} onChange={handleChange} />
          </div>
        </Modal>
      )}
    </div>
  );
}

export default Assignments;
