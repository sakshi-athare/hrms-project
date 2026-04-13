import { useNavigate } from "react-router-dom";
import { useUpdateUserStatus } from "../hooks/useUpdateUserStatus";
import Toggle from "./Toggle";

const EmployeeTable = ({ data }: any) => {
  const navigate = useNavigate();
  const { mutate, isPending } = useUpdateUserStatus();

  return (
    <table className="w-full text-sm">

      <thead className="bg-gray-50 text-gray-400 text-xs uppercase tracking-wide">
        <tr>
          <th className="p-5 text-left">Employee</th>
          <th>Role</th>
          <th>Status</th>
          <th></th>
        </tr>
      </thead>

      <tbody>
        {data.map((u: any) => (
          <tr
            key={u.id}
            className="hover:bg-gray-50 transition cursor-pointer"
            onClick={() => navigate(`/employees/${u.id}`)}
          >

            {/* EMPLOYEE */}
            <td className="py-5 px-5">
              <div className="flex items-center gap-4">

                <div className="w-11 h-11 rounded-full bg-blue-100 text-blue-600 flex items-center justify-center font-semibold">
                  {u.username?.[0]}
                </div>

                <div>
                  <p className="font-medium text-gray-900">
                    {u.username}
                  </p>
                  <p className="text-xs text-gray-400">
                    {u.email}
                  </p>
                </div>

              </div>
            </td>

            {/* ROLE */}
            <td>
              <span className="px-3 py-1 text-xs rounded-full bg-blue-50 text-blue-600 font-medium">
                {u.role}
              </span>
            </td>

            {/* STATUS */}
            <td onClick={(e) => e.stopPropagation()}>
              <div className="flex items-center gap-3">

                <Toggle
                  checked={u.isActive}
                  onChange={(val) => {
                    if (isPending) return;
                    mutate({ userId: u.id, isActive: val });
                  }}
                />

                <span
                  className={`text-xs font-medium px-3 py-1 rounded-full ${
                    u.isActive
                      ? "bg-green-50 text-green-600"
                      : "bg-red-50 text-red-600"
                  }`}
                >
                  {u.isActive ? "Active" : "Inactive"}
                </span>

              </div>
            </td>

            {/* ACTION */}
            <td className="text-right pr-5">
              <button
                className="text-sm text-blue-600 hover:underline"
                onClick={(e) => {
                  e.stopPropagation();
                  navigate(`/employees/edit/${u.id}`);
                }}
              >
                Edit
              </button>
            </td>

          </tr>
        ))}
      </tbody>
    </table>
  );
};

export default EmployeeTable;