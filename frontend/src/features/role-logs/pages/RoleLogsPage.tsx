import { useRoleLogs } from "../hooks/useRoleLogs";

const RoleLogsPage = () => {
  const { data, isLoading } = useRoleLogs(1, 10);

  if (isLoading) {
    return (
      <div className="flex justify-center items-center h-[50vh] text-sm text-gray-500">
        Loading role logs...
      </div>
    );
  }

  return (
    <div className="space-y-6 max-w-3xl">

      {/* HEADER */}
      <div>
        <h1 className="text-2xl font-semibold text-gray-900">
          Role Activity
        </h1>
        <p className="text-sm text-gray-500">
          See how employee roles are changing
        </p>
      </div>

      {/* LIST */}
      <div className="space-y-3">

        {data.data.map((log: any) => {

          return (
            <div
              key={log.id}
              className="bg-white px-5 py-4 rounded-xl shadow-sm hover:shadow-md transition"
            >
              <div className="flex items-center justify-between">

                {/* LEFT */}
                <div className="space-y-1">
                  <p className="text-sm text-gray-900">
                    <span className="font-semibold">{log.userName}</span>{" "}
                    was changed from{" "}
                    <span className="text-gray-600">{log.oldRole}</span>{" "}
                    to{" "}
                    <span className="font-semibold text-blue-600">
                      {log.newRole}
                    </span>
                  </p>

                  <p className="text-xs text-gray-500">
                    Changed by <span className="font-medium">{log.changedByName}</span>
                  </p>
                </div>

                {/* RIGHT */}
                <p className="text-xs text-gray-400">
                  {new Date(log.createdAt).toLocaleString()}
                </p>

              </div>
            </div>
          );
        })}

      </div>
    </div>
  );
};

export default RoleLogsPage;