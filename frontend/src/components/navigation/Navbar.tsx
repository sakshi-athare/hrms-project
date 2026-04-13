import { useAuthStore } from "../../features/auth/model/authStore";
import { useLocation } from "react-router-dom";
import { api } from "../../shared/api/axios";
import { Bell, Search } from "lucide-react";
import Input from "../ui/Input";

const PAGE_TITLES: Record<string, string> = {
  "/dashboard": "Dashboard",
  "/employees": "Employees",
  "/attendance": "Attendance",
  "/leave": "Leave",
  "/payroll": "Payroll",
  "/role-logs": "Role Logs",
  "/settings": "Settings",
};

const Navbar = () => {
  const logout = useAuthStore((s) => s.logout);
  const user = useAuthStore((s) => s.user);
  const { pathname } = useLocation();

  const title =
    Object.entries(PAGE_TITLES).find(([key]) =>
      pathname.startsWith(key)
    )?.[1] ?? "Dashboard";

  const handleLogout = async () => {
    try {
      await api.post("/auth/logout");
    } catch {}
    logout();
  };

  return (
    <header className="h-16 bg-white shadow-sm  px-6 flex items-center justify-between">

  {/* TITLE */}
  <h1 className="text-lg font-semibold text-gray-900">{title}</h1>

  {/* RIGHT */}
  <div className="flex items-center gap-4">

    <div className="w-72">
      <Input
        placeholder="Search..."
        leftIcon={<Search size={16} />}
      />
    </div>

    <button className="p-2 rounded-lg hover:bg-gray-100 transition">
      <Bell size={18} />
    </button>

    <div className="flex items-center gap-3 pl-4 border-l border-border">

      <div className="w-9 h-9 rounded-lg bg-blue-600 text-white flex items-center justify-center text-sm font-semibold">
        {user?.username?.slice(0, 2).toUpperCase() || "U"}
      </div>

      <div>
        <p className="text-sm font-medium">{user?.username}</p>
        <p className="text-xs text-gray-400">{user?.role}</p>
      </div>

    </div>

    <button
      onClick={handleLogout}
      className="text-sm text-red-500 font-medium hover:text-red-600"
    >
      Logout
    </button>

  </div>
</header>
  );
};

export default Navbar;