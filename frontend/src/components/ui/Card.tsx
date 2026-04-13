import React from "react";

// ── Types ─────────────────────────────────────────
type CardProps = {
  children: React.ReactNode;
  className?: string;
  hover?: boolean;
};

// ── Base Card ─────────────────────────────────────
const Card = ({
  children,
  className = "",
  hover = false,
}: CardProps) => (
  <div
    className={`
      bg-white border border-[#e5e7eb] rounded-xl
      shadow-[0_1px_3px_rgba(0,0,0,.07),0_1px_2px_rgba(0,0,0,.04)]
      overflow-hidden
      transition
      ${hover ? "hover:shadow-md hover:-translate-y-px" : ""}
      ${className}
    `}
  >
    {children}
  </div>
);

// ── Header ────────────────────────────────────────
export const CardHeader = ({
  children,
  className = "",
}: {
  children: React.ReactNode;
  className?: string;
}) => (
  <div
    className={`px-5 py-4 border-b border-[#e5e7eb] font-semibold ${className}`}
  >
    {children}
  </div>
);

// ── Content ───────────────────────────────────────
export const CardContent = ({
  children,
  className = "",
}: {
  children: React.ReactNode;
  className?: string;
}) => <div className={`p-5 ${className}`}>{children}</div>;

export default Card;