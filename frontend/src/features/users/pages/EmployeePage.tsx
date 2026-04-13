import { useState } from "react";
import { useNavigate } from "react-router-dom";
import EmployeeFilters from "../components/EmployeeFilters";
import EmployeeTable from "../components/EmployeeTable";
import EmployeeStats from "../components/EmployeeStats";
import { useUsers } from "../hooks/useUsers";
import { useDebounce } from "../hooks/useDebounce";
import Button from "../../../components/ui/Button";
import { usePermission } from "../../../shared/hooks/usePermission";

const EmployeePage = () => {
  const navigate = useNavigate();
  const { has } = usePermission();

  const canCreate = has("USER_CREATE");

  const [search, setSearch] = useState("");
  const [role, setRole] = useState("");
  const [status, setStatus] = useState("");
  const [page, setPage] = useState(1);

  const debouncedSearch = useDebounce(search, 400);

  const { data, isLoading } = useUsers({
    search: debouncedSearch,
    role,
    status: status === "" ? undefined : status === "true",
    page,
    limit: 10,
  });

  const employees = data?.data || [];

  return (
    <div className="p-8 bg-gray-50 min-h-screen space-y-8">

      {/* HEADER */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-semibold text-gray-900">
            Employees
          </h1>
          <p className="text-sm text-gray-500 mt-1">
            Manage your organization members
          </p>
        </div>

        {canCreate && (
          <Button
            onClick={() => navigate("/employees/create")}
            className="bg-blue-600 text-white hover:bg-blue-700 px-5 py-2.5 rounded-xl shadow-sm"
          >
            + Add Employee
          </Button>
        )}
      </div>

      {/* STATS */}
      <EmployeeStats
        total={employees.length}
        active={employees.filter((e) => e.isActive).length}
        inactive={employees.filter((e) => !e.isActive).length}
        managers={employees.filter((e) => e.role === "MANAGER").length}
      />

      {/* FILTERS */}
      <EmployeeFilters
        search={search}
        setSearch={setSearch}
        role={role}
        setRole={setRole}
        status={status}
        setStatus={setStatus}
      />

      {/* TABLE */}
      <div className="bg-white rounded-2xl shadow-sm overflow-hidden">
        {isLoading ? (
          <div className="text-center py-10 text-gray-500">
            Loading employees...
          </div>
        ) : (
          <EmployeeTable data={employees} />
        )}
      </div>

      {/* PAGINATION */}
      <div className="flex items-center justify-between">
        <button
          onClick={() => setPage((p) => Math.max(1, p - 1))}
          className="px-4 py-2 bg-white rounded-lg text-sm shadow-sm hover:bg-gray-100"
        >
          Prev
        </button>

        <span className="text-sm text-gray-500">
          Page {page}
        </span>

        <button
          onClick={() => setPage((p) => p + 1)}
          className="px-4 py-2 bg-white rounded-lg text-sm shadow-sm hover:bg-gray-100"
        >
          Next
        </button>
      </div>
    </div>
  );
};

export default EmployeePage;