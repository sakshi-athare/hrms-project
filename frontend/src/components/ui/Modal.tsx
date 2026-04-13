// src/components/ui/Modal.tsx
import { useEffect } from "react";

type ModalProps = {
  open: boolean;
  title: string;
  description?: string;
  confirmLabel?: string;
  confirmVariant?: "danger" | "primary";
  onClose: () => void;
  onConfirm: () => void;
};

const Modal = ({
  open,
  title,
  description,
  confirmLabel = "Confirm",
  confirmVariant = "danger",
  onClose,
  onConfirm,
}: ModalProps) => {
  // close on Escape
  useEffect(() => {
    const handler = (e: KeyboardEvent) => {
      if (e.key === "Escape") onClose();
    };
    if (open) document.addEventListener("keydown", handler);
    return () => document.removeEventListener("keydown", handler);
  }, [open, onClose]);

  if (!open) return null;

  const confirmStyles =
    confirmVariant === "danger"
      ? "bg-[#ef4444] hover:bg-[#dc2626] text-white"
      : "bg-[#3B6FF0] hover:bg-[#2d5edf] text-white";

  return (
    <div
      className="fixed inset-0 bg-black/40 backdrop-blur-[2px] flex items-center justify-center z-50"
      onClick={onClose}
    >
      <div
        className="bg-white w-full max-w-sm rounded-2xl shadow-[0_10px_40px_rgba(0,0,0,.16)] p-6"
        onClick={(e) => e.stopPropagation()}
      >
        {/* Icon */}
        {confirmVariant === "danger" && (
          <div className="w-10 h-10 rounded-full bg-[#fee2e2] flex items-center justify-center mb-4">
            <svg
              className="w-5 h-5 text-[#ef4444]"
              fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}
            >
              <path strokeLinecap="round" strokeLinejoin="round"
                d="M12 9v3.75m-9.303 3.376c-.866 1.5.217 3.374 1.948 3.374h14.71c1.73 0 2.813-1.874 1.948-3.374L13.949 3.378c-.866-1.5-3.032-1.5-3.898 0L2.697 16.126zM12 15.75h.007v.008H12v-.008z" />
            </svg>
          </div>
        )}

        <h2 className="text-base font-semibold text-[#111827] mb-1">{title}</h2>

        {description && (
          <p className="text-sm text-[#6b7280] mb-5">{description}</p>
        )}

        <div className="flex justify-end gap-2 mt-5">
          <button
            onClick={onClose}
            className="px-4 py-2 text-sm font-medium text-[#374151] border border-[#e5e7eb] rounded-lg hover:bg-[#f9fafb] transition"
          >
            Cancel
          </button>

          <button
            onClick={onConfirm}
            className={`px-4 py-2 text-sm font-semibold rounded-lg transition active:scale-[.98] ${confirmStyles}`}
          >
            {confirmLabel}
          </button>
        </div>
      </div>
    </div>
  );
};

export default Modal;