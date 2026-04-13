import { NavLink } from "react-router-dom";
import {
  LayoutDashboard,
  Users,
  CalendarCheck,
  FileText,
  DollarSign,
  Settings,
  ShieldCheck,
} from "lucide-react";
import { useAuthStore } from "../../features/auth/model/authStore";

const Sidebar = () => {
  const user = useAuthStore((s) => s.user);

  if (!user) return null;

  const role = user.role;

  const isAdmin = role === "CEO" || role === "HR";
  const isManager = role === "MANAGER";

  const menu = [
    { label: "Dashboard", path: "/dashboard", icon: LayoutDashboard, show: true },
    { label: "Employees", path: "/employees", icon: Users, show: isAdmin || isManager },
    { label: "Attendance", path: "/attendance", icon: CalendarCheck, show: true },
    { label: "Leave", path: "/leave", icon: FileText, show: true },
    { label: "Payroll", path: "/payroll", icon: DollarSign, show: isAdmin },
    { label: "Role Logs", path: "/role-logs", icon: ShieldCheck, show: isAdmin },
    { label: "Settings", path: "/settings", icon: Settings, show: true },
  ];

  return (
    <aside className="w-64 bg-white  flex flex-col">

  {/* LOGO */}
  <div className="h-16 shadow-sm flex items-center px-6 ">
    <p className="text-lg font-semibold">
      HRMS <span className="text-xs text-gray-400 ml-1">HR</span>
    </p>
  </div>

  <nav className="flex-1 px-3 py-4 space-y-6">

    {/* SECTION */}
    <div>
      <p className="px-3 text-xs font-semibold text-gray-400 uppercase mb-2">
        Overview
      </p>

      <div className="space-y-1">
        {menu.filter(i => i.show).slice(0, 4).map(item => (
          <NavItem key={item.path} {...item} />
        ))}
      </div>
    </div>

    <div>
      <p className="px-3 text-xs font-semibold text-gray-400 uppercase mb-2">
        HR
      </p>

      <div className="space-y-1">
        {menu.filter(i => i.show).slice(4).map(item => (
          <NavItem key={item.path} {...item} />
        ))}
      </div>
    </div>

  </nav>

  {/* USER */}
  <div className="p-4 shadow-sm flex items-center gap-3">
    <div className="w-9 h-9 rounded-lg bg-gray-900 text-white flex items-center justify-center text-sm font-semibold">
      {user.username?.slice(0, 1)}
    </div>
    <div>
      <p className="text-sm font-medium">{user.username}</p>
      <p className="text-xs text-gray-400">Managing</p>
    </div>
  </div>

</aside>
  );
};

const NavItem = ({
  label,
  path,
  icon: Icon,
}: {
  label: string;
  path: string;
  icon: React.ElementType;
}) => (
  <NavLink
    to={path}
    className={({ isActive }) =>
      `flex items-center gap-3 px-3 py-2 rounded-lg text-sm transition
      ${
        isActive
          ? "bg-blue-50 text-blue-600 font-semibold"
          : "text-gray-600 hover:bg-gray-100"
      }`
    }
  >
    <Icon size={18} />
    {label}
  </NavLink>
);

export default Sidebar;