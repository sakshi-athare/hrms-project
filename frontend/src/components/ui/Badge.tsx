// src/components/ui/Badge.tsx

export type BadgeVariant = "default" | "success" | "danger" | "gray" | "warning";

type BadgeProps = {
  children: React.ReactNode;
  variant?: BadgeVariant;
};

const variantStyles: Record<BadgeVariant, string> = {
  default: "bg-[#dbeafe] text-[#1d4ed8]",
  success: "bg-[#dcfce7] text-[#15803d]",
  danger:  "bg-[#fee2e2] text-[#b91c1c]",
  gray:    "bg-[#f3f4f6] text-[#374151]",
  warning: "bg-[#fef9c3] text-[#a16207]",
};


const Badge = ({ children, variant = "default" }: BadgeProps) => {
  return (
    <span
      className={`
        inline-flex items-center px-2.5 py-0.5 rounded-md
        text-xs font-semibold tracking-wide
        ${variantStyles[variant]}
      `}
    >
      {children}
    </span>
  );
};

export default Badge;