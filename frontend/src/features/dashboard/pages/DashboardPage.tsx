import { useDashboard } from "../hooks/useDashboard";
import AdminDashboard from "../components/AdminDashboard";
import EmployeeDashboard from "../components/EmployeeDashboard";
import { useAuthStore } from "../../auth/model/authStore";

const DashboardPage = () => {
  const { data, isLoading, isError } = useDashboard();

  const user = useAuthStore((s) => s.user);
  const authLoading = useAuthStore((s) => s.isLoading);

  const isAdmin = user?.role === "CEO" || user?.role === "HR";

  // 🔵 Loading UI
  if (authLoading || isLoading) {
    return (
      <div className="flex items-center justify-center h-[60vh]">
        <div className="flex flex-col items-center gap-3">
          <div className="w-8 h-8 border-4 border-blue-500 border-t-transparent rounded-full animate-spin" />
          <p className="text-sm text-gray-500">Loading dashboard...</p>
        </div>
      </div>
    );
  }

  // 🔴 Error UI
  if (isError || !data) {
    return (
      <div className="flex items-center justify-center h-[60vh]">
        <div className="bg-white border border-red-200 rounded-xl p-6 text-center shadow-sm">
          <p className="text-red-500 font-medium">Failed to load dashboard</p>
          <p className="text-sm text-gray-500 mt-1">
            Please try again or contact support
          </p>
        </div>
      </div>
    );
  }

  // 🔒 Unauthorized
  if (!user) {
    return (
      <div className="flex items-center justify-center h-[60vh]">
        <p className="text-gray-500">Unauthorized access</p>
      </div>
    );
  }

  // ✅ Main Content
  return (
    <div className="space-y-6">
      {isAdmin ? (
        <AdminDashboard data={data} />
      ) : (
        <EmployeeDashboard data={data} />
      )}
    </div>
  );
};

export default DashboardPage;