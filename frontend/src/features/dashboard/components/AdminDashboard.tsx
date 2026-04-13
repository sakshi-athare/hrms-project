import Card from "../../../components/ui/Card";

type Props = {
  data: {
    totalEmployees: number;
    activeEmployees: number;
    inactiveEmployees: number;
    newJoineesThisMonth: number;
    employeesOnLeave: number;
    pendingLeaveRequests: number;
  };
};

const AdminDashboard = ({ data }: Props) => {
  return (
    <div className="space-y-6">

      {/* Header */}
      <div>
        <h2 className="text-2xl font-semibold text-gray-900">
          Admin Dashboard
        </h2>
        <p className="text-sm text-gray-500">
          Overview of company statistics
        </p>
      </div>

      {/* Stats Grid */}
      <div className="grid gap-5 sm:grid-cols-2 lg:grid-cols-3">

        <Card className="p-5 hover:shadow-md transition">
          <Stat label="Total Employees" value={data.totalEmployees} />
        </Card>

        <Card className="p-5 hover:shadow-md transition">
          <Stat label="Active Employees" value={data.activeEmployees} />
        </Card>

        <Card className="p-5 hover:shadow-md transition">
          <Stat label="Inactive Employees" value={data.inactiveEmployees} />
        </Card>

        <Card className="p-5 hover:shadow-md transition">
          <Stat label="New Joinees (This Month)" value={data.newJoineesThisMonth} />
        </Card>

        <Card className="p-5 hover:shadow-md transition">
          <Stat label="Employees On Leave" value={data.employeesOnLeave} />
        </Card>

        <Card className="p-5 hover:shadow-md transition">
          <Stat label="Pending Leave Requests" value={data.pendingLeaveRequests} />
        </Card>

      </div>

    </div>
  );
};

const Stat = ({ label, value }: { label: string; value: number }) => {
  return (
    <div>
      <p className="text-sm text-gray-500">{label}</p>
      <p className="text-3xl font-bold text-gray-900 mt-1">
        {value}
      </p>
    </div>
  );
};

export default AdminDashboard;