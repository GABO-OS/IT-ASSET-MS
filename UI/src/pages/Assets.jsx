// Assets.jsx - Page para sa pag-manage ng IT Assets
// Dito pwedeng mag-add, edit, delete, at tingnan ang lahat ng assets

import { useState, useEffect } from "react";
import {
  getAllAssets, createAsset, updateAsset, deleteAsset,
} from "../api/assetApi";
import Modal from "../components/Modal";

// Options para sa mga select dropdowns
const ASSET_TYPES   = ["LAPTOP","DESKTOP","MONITOR","PRINTER","KEYBOARD","MOUSE","UPS","SERVER","NETWORKING","OTHER"];
const ASSET_STATUSES = ["AVAILABLE","IN_USE","UNDER_REPAIR","RETIRED","LOST"];

// Helper function para sa badge color ayon sa status
const statusBadge = (status) => {
  const map = {
    AVAILABLE: "available", IN_USE: "in-use",
    UNDER_REPAIR: "repair", RETIRED: "retired", LOST: "lost",
  };
  return `badge badge-${map[status] || "retired"}`;
};

// Default na laman ng form (para sa add modal)
const emptyForm = {
  name: "", type: "LAPTOP", serialNumber: "", brand: "",
  model: "", purchaseDate: "", purchasePrice: "", status: "AVAILABLE", remarks: "",
};

function Assets() {
  const [assets, setAssets]       = useState([]);   // Listahan ng assets
  const [filtered, setFiltered]   = useState([]);   // Filtered list (para sa search)
  const [loading, setLoading]     = useState(true);
  const [error, setError]         = useState(null);
  const [search, setSearch]       = useState("");   // Search input value
  const [showModal, setShowModal] = useState(false); // Ipakita/itago ang modal
  const [editMode, setEditMode]   = useState(false); // Add mode o Edit mode
  const [selectedId, setSelectedId] = useState(null); // ID ng asset na ine-edit
  const [form, setForm]           = useState(emptyForm); // Form data
  const [formError, setFormError] = useState(null); // Error sa form

  // I-fetch ang assets kapag na-load ang page
  useEffect(() => { fetchAssets(); }, []);

  // I-filter ang assets kapag nag-change ang search input
  useEffect(() => {
    const q = search.toLowerCase();
    setFiltered(
      assets.filter(
        (a) =>
          a.name.toLowerCase().includes(q) ||
          a.serialNumber.toLowerCase().includes(q) ||
          (a.brand || "").toLowerCase().includes(q)
      )
    );
  }, [search, assets]);

  const fetchAssets = async () => {
    try {
      setLoading(true);
      const res = await getAllAssets();
      setAssets(res.data);
    } catch {
      setError("Hindi ma-load ang assets. Baka hindi pa naka-run ang backend.");
    } finally {
      setLoading(false);
    }
  };

  // Buksan ang modal para mag-add ng bagong asset
  const openAddModal = () => {
    setForm(emptyForm);
    setEditMode(false);
    setFormError(null);
    setShowModal(true);
  };

  // Buksan ang modal para mag-edit ng existing asset
  const openEditModal = (asset) => {
    setForm({
      name: asset.name, type: asset.type,
      serialNumber: asset.serialNumber, brand: asset.brand || "",
      model: asset.model || "", purchaseDate: asset.purchaseDate || "",
      purchasePrice: asset.purchasePrice || "", status: asset.status,
      remarks: asset.remarks || "",
    });
    setSelectedId(asset.id);
    setEditMode(true);
    setFormError(null);
    setShowModal(true);
  };

  // I-handle ang pagbabago sa form fields
  const handleChange = (e) =>
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));

  // I-submit ang form (add o edit)
  const handleSubmit = async () => {
    // Basic validation - kailangan may pangalan at serial number
    if (!form.name || !form.serialNumber) {
      setFormError("Kailangan ng Name at Serial Number!");
      return;
    }
    try {
      if (editMode) {
        await updateAsset(selectedId, form);
      } else {
        await createAsset(form);
      }
      setShowModal(false);
      fetchAssets(); // I-refresh ang listahan
    } catch (err) {
      setFormError(err.response?.data?.message || "May error na nangyari. Subukan ulit.");
    }
  };

  // I-delete ang asset pagkatapos ng confirmation
  const handleDelete = async (id, name) => {
    if (!confirm(`Sure ka bang i-delete ang "${name}"?`)) return;
    try {
      await deleteAsset(id);
      fetchAssets();
    } catch {
      alert("Hindi ma-delete. Baka may assignment pa ang asset na ito.");
    }
  };

  if (loading) return <div className="loading">⏳ Loading assets...</div>;

  return (
    <div>
      {/* Page header */}
      <div className="page-header">
        <h1 className="page-title">💻 Assets</h1>
        <p className="page-subtitle">Manage lahat ng IT assets ng kompanya</p>
      </div>

      {error && <div className="alert alert-error">⚠️ {error}</div>}

      {/* Search bar at Add button */}
      <div className="toolbar">
        <div className="search-box">
          <span className="search-icon">🔍</span>
          <input
            placeholder="Maghanap ng asset..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>
        <button className="btn btn-primary" onClick={openAddModal}>
          ＋ Add Asset
        </button>
      </div>

      {/* Assets table */}
      <div className="card">
        {filtered.length === 0 ? (
          <div className="empty-state">
            <div className="empty-icon">💻</div>
            <p>Walang assets na nahanap</p>
          </div>
        ) : (
          <div className="table-container">
            <table>
              <thead>
                <tr>
                  <th>#</th>
                  <th>Name</th>
                  <th>Type</th>
                  <th>Brand / Model</th>
                  <th>Serial No.</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {filtered.map((asset, idx) => (
                  <tr key={asset.id}>
                    <td style={{ color: "var(--text-muted)" }}>{idx + 1}</td>
                    <td style={{ fontWeight: 500 }}>{asset.name}</td>
                    <td>{asset.type}</td>
                    <td style={{ color: "var(--text-secondary)" }}>
                      {asset.brand} {asset.model && `· ${asset.model}`}
                    </td>
                    <td style={{ fontFamily: "monospace", fontSize: "12px" }}>
                      {asset.serialNumber}
                    </td>
                    <td>
                      <span className={statusBadge(asset.status)}>
                        {asset.status?.replace("_", " ")}
                      </span>
                    </td>
                    <td>
                      <div className="action-buttons">
                        <button
                          className="btn btn-secondary btn-sm"
                          onClick={() => openEditModal(asset)}
                        >
                          ✏️ Edit
                        </button>
                        <button
                          className="btn btn-danger btn-sm"
                          onClick={() => handleDelete(asset.id, asset.name)}
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

      {/* Add/Edit Asset Modal */}
      {showModal && (
        <Modal
          title={editMode ? "✏️ Edit Asset" : "＋ Add New Asset"}
          onClose={() => setShowModal(false)}
          footer={
            <>
              <button className="btn btn-secondary" onClick={() => setShowModal(false)}>
                Cancel
              </button>
              <button className="btn btn-primary" onClick={handleSubmit}>
                {editMode ? "Save Changes" : "Add Asset"}
              </button>
            </>
          }
        >
          {formError && <div className="alert alert-error">⚠️ {formError}</div>}

          <div className="form-grid-2">
            <div className="form-group">
              <label className="form-label">Asset Name *</label>
              <input name="name" className="form-control"
                placeholder="hal. Dell Laptop" value={form.name} onChange={handleChange} />
            </div>
            <div className="form-group">
              <label className="form-label">Type *</label>
              <select name="type" className="form-control" value={form.type} onChange={handleChange}>
                {ASSET_TYPES.map((t) => <option key={t} value={t}>{t}</option>)}
              </select>
            </div>
            <div className="form-group">
              <label className="form-label">Serial Number *</label>
              <input name="serialNumber" className="form-control"
                placeholder="hal. SN-12345" value={form.serialNumber} onChange={handleChange} />
            </div>
            <div className="form-group">
              <label className="form-label">Status</label>
              <select name="status" className="form-control" value={form.status} onChange={handleChange}>
                {ASSET_STATUSES.map((s) => <option key={s} value={s}>{s.replace("_"," ")}</option>)}
              </select>
            </div>
            <div className="form-group">
              <label className="form-label">Brand</label>
              <input name="brand" className="form-control"
                placeholder="hal. Dell, HP" value={form.brand} onChange={handleChange} />
            </div>
            <div className="form-group">
              <label className="form-label">Model</label>
              <input name="model" className="form-control"
                placeholder="hal. Latitude 5420" value={form.model} onChange={handleChange} />
            </div>
            <div className="form-group">
              <label className="form-label">Purchase Date</label>
              <input name="purchaseDate" type="date" className="form-control"
                value={form.purchaseDate} onChange={handleChange} />
            </div>
            <div className="form-group">
              <label className="form-label">Purchase Price (₱)</label>
              <input name="purchasePrice" type="number" className="form-control"
                placeholder="0.00" value={form.purchasePrice} onChange={handleChange} />
            </div>
          </div>
          <div className="form-group">
            <label className="form-label">Remarks</label>
            <input name="remarks" className="form-control"
              placeholder="Mga notes tungkol sa asset..." value={form.remarks} onChange={handleChange} />
          </div>
        </Modal>
      )}
    </div>
  );
}

export default Assets;
