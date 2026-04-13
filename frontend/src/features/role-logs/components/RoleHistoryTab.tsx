import { useUserRoleLogs } from "../hooks/useRoleLogs";

const RoleHistoryTab = ({ userId }: { userId: number }) => {
  const { data, isLoading } = useUserRoleLogs(userId);

  if (isLoading) return <p>Loading role history...</p>;

  if (!data || data.length === 0) {
    return <p>No role changes found</p>;
  }

  return (
    <div>
      <h3>Role History</h3>

      <table>
        <thead>
          <tr>
            <th>Old Role</th>
            <th>New Role</th>
            <th>Changed By</th>
            <th>Date</th>
          </tr>
        </thead>

        <tbody>
          {data.map((log: any) => (
            <tr key={log.id}>
              <td>{log.oldRole}</td>
              <td>{log.newRole}</td>
              <td>{log.changedByName}</td>
              <td>{new Date(log.createdAt).toLocaleString()}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default RoleHistoryTab;