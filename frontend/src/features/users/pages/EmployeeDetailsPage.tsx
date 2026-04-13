import { useParams } from "react-router-dom";
import { useAuthStore } from "../../auth/model/authStore";
import { useUserById } from "../hooks/useUserById";
import { useMe } from "../../auth/hooks/useMe";
import { useState } from "react";

import Badge from "../../../components/ui/Badge";
import RoleHistoryTab from "../../role-logs/components/RoleHistoryTab";

const EmployeeDetailsPage = () => {
  const { id } = useParams();
  const currentUser = useAuthStore((s) => s.user);

  const userId = id ? Number(id) : null;

  const isSelf = currentUser?.id === userId;

  const { data, isLoading } = isSelf
    ? useMe()
    : useUserById(userId!, {
        enabled: !!userId,
      });

  const [activeTab, setActiveTab] = useState<"overview" | "roles">("overview");

  if (isLoading) return <div className="p-6">Loading...</div>;

  const emp = data;

  if (!emp) {
    return <div className="p-6 text-gray-500">No employee found</div>;
  }

  return (
    <div className="p-6 max-w-3xl space-y-6">

      {/* Tabs */}
      <div className="flex border-b">
        <button
          onClick={() => setActiveTab("overview")}
          className={`px-4 py-2 text-sm font-medium border-b-2 ${
            activeTab === "overview"
              ? "border-blue-600 text-blue-600"
              : "border-transparent text-gray-500"
          }`}
        >
          Overview
        </button>

        <button
          onClick={() => setActiveTab("roles")}
          className={`px-4 py-2 text-sm font-medium border-b-2 ${
            activeTab === "roles"
              ? "border-blue-600 text-blue-600"
              : "border-transparent text-gray-500"
          }`}
        >
          Role History
        </button>
      </div>

      {/* Overview */}
      {activeTab === "overview" && (
        <div className="bg-white rounded-2xl shadow p-6 space-y-6">

          <div className="flex justify-between items-start">
            <div className="flex gap-4 items-center">
              <div className="w-14 h-14 rounded-xl bg-blue-100 flex items-center justify-center font-bold text-blue-600">
                {emp.username?.slice(0, 2).toUpperCase()}
              </div>

              <div>
                <h2 className="text-lg font-semibold">{emp.username}</h2>
                <p className="text-sm text-gray-500">{emp.email}</p>
              </div>
            </div>

            <Badge variant={emp.isActive ? "success" : "danger"}>
              {emp.isActive ? "Active" : "Inactive"}
            </Badge>
          </div>

          <div className="grid grid-cols-2 gap-4 text-sm">
            <div>
              <p className="text-gray-400">Role</p>
              <p className="font-medium">{emp.role}</p>
            </div>

            <div>
              <p className="text-gray-400">Manager</p>
              <p className="font-medium">{emp.managerName || "-"}</p>
            </div>
          </div>
        </div>
      )}

      {/* Role Logs */}
      {activeTab === "roles" && (
        <div className="bg-white border rounded-2xl p-6 shadow-sm">
          <RoleHistoryTab userId={emp.id} />
        </div>
      )}
    </div>
  );
};

export default EmployeeDetailsPage;