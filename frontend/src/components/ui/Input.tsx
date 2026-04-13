// src/components/ui/Input.tsx
import React from "react";

type InputProps = React.InputHTMLAttributes<HTMLInputElement> & {
  label?: string;
  error?: string;
  leftIcon?: React.ReactNode;
};

const Input = React.forwardRef<HTMLInputElement, InputProps>(
  ({ label, error, leftIcon, className = "", ...props }, ref) => {
    return (
      <div className="w-full space-y-1">
        {label && (
          <label className="block text-sm font-medium text-[#374151]">
            {label}
          </label>
        )}

        <div className="relative">
          {leftIcon && (
            <span className="absolute left-3 top-1/2 -translate-y-1/2 text-[#9ca3af]">
              {leftIcon}
            </span>
          )}

          <input
            ref={ref}
            className={`
              w-full px-3 py-2 text-sm
              border border-[#e5e7eb] rounded-lg
              bg-white text-[#111827]
              placeholder:text-[#9ca3af]
              transition-colors duration-150
              focus:outline-none focus:ring-2 focus:ring-[#3B6FF0]/30 focus:border-[#3B6FF0]
              disabled:bg-[#f9fafb] disabled:cursor-not-allowed
              ${leftIcon ? "pl-9" : ""}
              ${error ? "border-red-400 focus:ring-red-200 focus:border-red-400" : ""}
              ${className}
            `}
            {...props}
          />
        </div>

        {error && (
          <p className="text-xs text-red-500">{error}</p>
        )}
      </div>
    );
  }
);

Input.displayName = "Input";

export default Input;