import Card from "../../../components/ui/Card"; // FIXED PATH
import { useNavigate } from "react-router-dom";

const EmployeeDashboard = ({ data }: { data: any }) => {
  const navigate = useNavigate();

  return (
    <div className="space-y-5">

      <Card>
        <div className="p-5">
          <h2 className="text-xl font-semibold">
            Employee Dashboard
          </h2>
          <p className="text-sm text-gray-500">
            Welcome to your workspace
          </p>
        </div>
      </Card>

      <Card>
        <div className="p-5 flex gap-3">
          <button
            onClick={() => navigate("/employees")}
            className="px-4 py-2 bg-blue-500 text-white rounded"
          >
            View Employees
          </button>

          <button
            onClick={() => navigate("/leave")}
            className="px-4 py-2 border rounded"
          >
            Apply Leave
          </button>
        </div>
      </Card>

      {/* 👇 DATA (you were not using it at all) */}
      <Card>
        <div className="p-5">
          <p>Approved Leaves: {data?.employeesOnLeave}</p>
          <p>Pending Leaves: {data?.pendingLeaveRequests}</p>
        </div>
      </Card>

    </div>
  );
};

export default EmployeeDashboard;