type Props = {
  search: string;
  setSearch: (v: string) => void;
  role: string;
  setRole: (v: string) => void;
  status: string;
  setStatus: (v: string) => void;
};

const EmployeeFilters = ({
  search,
  setSearch,
  role,
  setRole,
  status,
  setStatus,
}: Props) => {
  return (
    <div className="bg-white rounded-2xl shadow-sm p-4 flex items-center gap-3">

      <input
        value={search}
        onChange={(e) => setSearch(e.target.value)}
        placeholder="Search employees..."
        className="flex-1 px-4 py-2.5 rounded-xl bg-gray-100 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
      />

      <select
        value={role}
        onChange={(e) => setRole(e.target.value)}
        className="px-3 py-2.5 rounded-xl bg-gray-100 text-sm"
      >
        <option value="">All Roles</option>
        <option value="CEO">CEO</option>
        <option value="HR">HR</option>
        <option value="MANAGER">Manager</option>
        <option value="EMPLOYEE">Employee</option>
      </select>

      <select
        value={status}
        onChange={(e) => setStatus(e.target.value)}
        className="px-3 py-2.5 rounded-xl bg-gray-100 text-sm"
      >
        <option value="">All Status</option>
        <option value="true">Active</option>
        <option value="false">Inactive</option>
      </select>

    </div>
  );
};

export default EmployeeFilters;