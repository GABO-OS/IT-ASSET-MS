// Users.jsx - Page para sa pag-manage ng Employees
// Dito pwedeng mag-add, edit, deactivate ng mga empleyado

import { useState, useEffect } from "react";
import { getAllUsers, createUser, updateUser, deactivateUser, deleteUser } from "../api/userApi";
import Modal from "../components/Modal";

const ROLES       = ["EMPLOYEE", "ADMIN"];
const DEPARTMENTS = ["IT", "HR", "Finance", "Operations", "Marketing", "Admin", "Other"];

const emptyForm = {
  fullName: "", email: "", department: "IT",
  position: "", contactNumber: "", role: "EMPLOYEE",
};

function Users() {
  const [users, setUsers]         = useState([]);
  const [filtered, setFiltered]   = useState([]);
  const [loading, setLoading]     = useState(true);
  const [error, setError]         = useState(null);
  const [search, setSearch]       = useState("");
  const [showModal, setShowModal] = useState(false);
  const [editMode, setEditMode]   = useState(false);
  const [selectedId, setSelectedId] = useState(null);
  const [form, setForm]           = useState(emptyForm);
  const [formError, setFormError] = useState(null);

  useEffect(() => { fetchUsers(); }, []);

  // I-filter ang users kapag nag-change ang search
  useEffect(() => {
    const q = search.toLowerCase();
    setFiltered(
      users.filter(
        (u) =>
          u.fullName.toLowerCase().includes(q) ||
          u.email.toLowerCase().includes(q) ||
          u.department.toLowerCase().includes(q)
      )
    );
  }, [search, users]);

  const fetchUsers = async () => {
    try {
      setLoading(true);
      const res = await getAllUsers();
      setUsers(res.data);
    } catch {
      setError("Hindi ma-load ang users. Siguraduhing naka-run ang backend.");
    } finally {
      setLoading(false);
    }
  };

  const openAddModal = () => {
    setForm(emptyForm);
    setEditMode(false);
    setFormError(null);
    setShowModal(true);
  };

  const openEditModal = (user) => {
    setForm({
      fullName: user.fullName, email: user.email,
      department: user.department, position: user.position || "",
      contactNumber: user.contactNumber || "", role: user.role,
    });
    setSelectedId(user.id);
    setEditMode(true);
    setFormError(null);
    setShowModal(true);
  };

  const handleChange = (e) =>
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));

  const handleSubmit = async () => {
    if (!form.fullName || !form.email || !form.department) {
      setFormError("Kailangan ng Full Name, Email, at Department!");
      return;
    }
    try {
      if (editMode) {
        await updateUser(selectedId, form);
      } else {
        await createUser(form);
      }
      setShowModal(false);
      fetchUsers();
    } catch (err) {
      setFormError(err.response?.data?.message || "May error. Subukan ulit.");
    }
  };

  // Soft delete - hindi ganap na tatanggalin, inactive lang ang magiging status
  const handleDeactivate = async (id, name) => {
    if (!confirm(`I-deactivate ang "${name}"? Hindi siya mabubura, inactive lang siya.`)) return;
    try {
      await deactivateUser(id);
      fetchUsers();
    } catch {
      alert("Hindi ma-deactivate ang user.");
    }
  };

  const handleDelete = async (id, name) => {
    if (!confirm(`PERMANENTLY i-delete ang "${name}"? Hindi na ito maibabalik!`)) return;
    try {
      await deleteUser(id);
      fetchUsers();
    } catch {
      alert("Hindi ma-delete. Baka may active assignments pa ang user na ito.");
    }
  };

  if (loading) return <div className="loading">⏳ Loading employees...</div>;

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">👥 Employees</h1>
        <p className="page-subtitle">Manage lahat ng empleyado na may access sa IT assets</p>
      </div>

      {error && <div className="alert alert-error">⚠️ {error}</div>}

      <div className="toolbar">
        <div className="search-box">
          <span className="search-icon">🔍</span>
          <input
            placeholder="Maghanap ng empleyado..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>
        <button className="btn btn-primary" onClick={openAddModal}>
          ＋ Add Employee
        </button>
      </div>

      <div className="card">
        {filtered.length === 0 ? (
          <div className="empty-state">
            <div className="empty-icon">👥</div>
            <p>Walang employees na nahanap</p>
          </div>
        ) : (
          <div className="table-container">
            <table>
              <thead>
                <tr>
                  <th>#</th>
                  <th>Full Name</th>
                  <th>Email</th>
                  <th>Department</th>
                  <th>Position</th>
                  <th>Role</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {filtered.map((user, idx) => (
                  <tr key={user.id}>
                    <td style={{ color: "var(--text-muted)" }}>{idx + 1}</td>
                    <td style={{ fontWeight: 500 }}>{user.fullName}</td>
                    <td style={{ color: "var(--text-secondary)", fontSize: "13px" }}>{user.email}</td>
                    <td>{user.department}</td>
                    <td style={{ color: "var(--text-secondary)" }}>{user.position || "—"}</td>
                    <td>
                      <span className={`badge badge-${user.role?.toLowerCase()}`}>
                        {user.role}
                      </span>
                    </td>
                    <td>
                      <span className={`badge ${user.active ? "badge-available" : "badge-retired"}`}>
                        {user.active ? "Active" : "Inactive"}
                      </span>
                    </td>
                    <td>
                      <div className="action-buttons">
                        <button
                          className="btn btn-secondary btn-sm"
                          onClick={() => openEditModal(user)}
                        >
                          ✏️ Edit
                        </button>
                        {user.active && (
                          <button
                            className="btn btn-secondary btn-sm"
                            onClick={() => handleDeactivate(user.id, user.fullName)}
                            style={{ color: "var(--warning)", borderColor: "var(--warning)" }}
                          >
                            ⛔
                          </button>
                        )}
                        <button
                          className="btn btn-danger btn-sm"
                          onClick={() => handleDelete(user.id, user.fullName)}
                        >
                          🗑️
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {/* Add/Edit User Modal */}
      {showModal && (
        <Modal
          title={editMode ? "✏️ Edit Employee" : "＋ Add Employee"}
          onClose={() => setShowModal(false)}
          footer={
            <>
              <button className="btn btn-secondary" onClick={() => setShowModal(false)}>
                Cancel
              </button>
              <button className="btn btn-primary" onClick={handleSubmit}>
                {editMode ? "Save Changes" : "Add Employee"}
              </button>
            </>
          }
        >
          {formError && <div className="alert alert-error">⚠️ {formError}</div>}

          <div className="form-grid-2">
            <div className="form-group">
              <label className="form-label">Full Name *</label>
              <input name="fullName" className="form-control"
                placeholder="hal. Juan dela Cruz" value={form.fullName} onChange={handleChange} />
            </div>
            <div className="form-group">
              <label className="form-label">Email *</label>
              <input name="email" type="email" className="form-control"
                placeholder="juan@company.com" value={form.email} onChange={handleChange} />
            </div>
            <div className="form-group">
              <label className="form-label">Department *</label>
              <select name="department" className="form-control" value={form.department} onChange={handleChange}>
                {DEPARTMENTS.map((d) => <option key={d} value={d}>{d}</option>)}
              </select>
            </div>
            <div className="form-group">
              <label className="form-label">Role</label>
              <select name="role" className="form-control" value={form.role} onChange={handleChange}>
                {ROLES.map((r) => <option key={r} value={r}>{r}</option>)}
              </select>
            </div>
            <div className="form-group">
              <label className="form-label">Position</label>
              <input name="position" className="form-control"
                placeholder="hal. IT Specialist" value={form.position} onChange={handleChange} />
            </div>
            <div className="form-group">
              <label className="form-label">Contact Number</label>
              <input name="contactNumber" className="form-control"
                placeholder="hal. 09XX-XXX-XXXX" value={form.contactNumber} onChange={handleChange} />
            </div>
          </div>
        </Modal>
      )}
    </div>
  );
}

export default Users;
