// src/components/ui/Dropdown.tsx
import { useState, useRef, useEffect } from "react";

type DropdownProps = {
  children: React.ReactNode;
  trigger: React.ReactNode;
  align?: "left" | "right";
};

const Dropdown = ({ trigger, children, align = "right" }: DropdownProps) => {
  const [open, setOpen] = useState(false);
  const ref = useRef<HTMLDivElement>(null);

  // close on outside click
  useEffect(() => {
    const handler = (e: MouseEvent) => {
      if (ref.current && !ref.current.contains(e.target as Node)) {
        setOpen(false);
      }
    };
    document.addEventListener("mousedown", handler);
    return () => document.removeEventListener("mousedown", handler);
  }, []);

  return (
    <div className="relative inline-block" ref={ref}>
      <div onClick={() => setOpen((o) => !o)}>{trigger}</div>

      {open && (
        <div
          className={`
            absolute top-full mt-1.5 z-20
            ${align === "right" ? "right-0" : "left-0"}
            min-w-35
            bg-white border border-[#e5e7eb] rounded-xl
            shadow-[0_4px_16px_rgba(0,0,0,.1)]
            overflow-hidden
            animate-in fade-in slide-in-from-top-1 duration-100
          `}
          onClick={() => setOpen(false)}
        >
          {children}
        </div>
      )}
    </div>
  );
};

export default Dropdown;