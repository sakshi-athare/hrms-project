type Props = {
  total: number;
  active: number;
  inactive: number;
  managers: number;
};

const Stat = ({ label, value }: any) => (
  <div className="bg-white rounded-2xl p-5 shadow-sm hover:shadow-md transition">
    <p className="text-xs text-gray-400">{label}</p>
    <p className="text-2xl font-semibold text-gray-900 mt-1">
      {value}
    </p>
  </div>
);

const EmployeeStats = ({ total, active, inactive, managers }: Props) => {
  return (
    <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
      <Stat label="Total Employees" value={total} />
      <Stat label="Active" value={active} />
      <Stat label="Inactive" value={inactive} />
      <Stat label="Managers" value={managers} />
    </div>
  );
};

export default EmployeeStats;