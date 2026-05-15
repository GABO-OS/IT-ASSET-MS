// Modal.jsx - Reusable modal component
// Ginagamit ito para sa add/edit forms - para hindi kailangang gumawa ng bagong modal per page

function Modal({ title, onClose, children, footer }) {
  return (
    // Dark overlay - kapag ni-click ang labas ng modal, magsasara
    <div className="modal-overlay" onClick={onClose}>

      {/* I-stop ang click propagation para hindi magsara kapag ni-click ang loob */}
      <div className="modal" onClick={(e) => e.stopPropagation()}>

        {/* Header ng modal - may title at close button */}
        <div className="modal-header">
          <h3 className="modal-title">{title}</h3>
          <button className="modal-close" onClick={onClose}>✕</button>
        </div>

        {/* Content ng modal - dinidictate ng parent component */}
        <div className="modal-body">
          {children}
        </div>

        {/* Footer ng modal - buttons (Save, Cancel, etc.) */}
        {footer && (
          <div className="modal-footer">
            {footer}
          </div>
        )}
      </div>
    </div>
  );
}

export default Modal;
