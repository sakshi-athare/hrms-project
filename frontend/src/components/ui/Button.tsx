// src/components/ui/Button.tsx
import React from "react";

type ButtonVariant = "primary" | "secondary" | "ghost" | "danger";
type ButtonSize = "sm" | "md" | "lg";

type ButtonProps = React.ButtonHTMLAttributes<HTMLButtonElement> & {
  variant?: ButtonVariant;
  size?: ButtonSize;
  loading?: boolean;
  leftIcon?: React.ReactNode;
};

const variantStyles: Record<ButtonVariant, string> = {
  primary:
    "bg-[#3B6FF0] text-white hover:bg-[#2d5edf] active:scale-[.98]",
  secondary:
    "bg-white text-[#374151] border border-[#e5e7eb] hover:bg-[#f9fafb]",
  ghost:
    "bg-transparent text-[#6b7280] hover:bg-[#f3f4f6] hover:text-[#111827]",
  danger:
    "bg-[#ef4444] text-white hover:bg-[#dc2626] active:scale-[.98]",
};

const sizeStyles: Record<ButtonSize, string> = {
  sm: "px-3 py-1.5 text-xs gap-1.5",
  md: "px-4 py-2 text-sm gap-2",
  lg: "px-5 py-2.5 text-sm gap-2",
};

const Button = React.forwardRef<HTMLButtonElement, ButtonProps>(
  (
    {
      variant = "primary",
      size = "md",
      loading = false,
      leftIcon,
      children,
      className = "",
      disabled,
      ...props
    },
    ref
  ) => {
    return (
      <button
        ref={ref}
        disabled={disabled || loading}
        className={`
          inline-flex items-center justify-center
          font-semibold rounded-lg
          transition-all duration-150
          focus:outline-none focus:ring-2 focus:ring-[#3B6FF0]/30
          disabled:opacity-50 disabled:cursor-not-allowed
          ${variantStyles[variant]}
          ${sizeStyles[size]}
          ${className}
        `}
        {...props}
      >
        {loading ? (
          <svg
            className="animate-spin w-4 h-4"
            fill="none"
            viewBox="0 0 24 24"
          >
            <circle
              className="opacity-25"
              cx="12" cy="12" r="10"
              stroke="currentColor" strokeWidth="4"
            />
            <path
              className="opacity-75"
              fill="currentColor"
              d="M4 12a8 8 0 018-8v8z"
            />
          </svg>
        ) : leftIcon}
        {children}
      </button>
    );
  }
);

Button.displayName = "Button";
export default Button;