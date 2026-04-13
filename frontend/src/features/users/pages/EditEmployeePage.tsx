import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { useMutation, useQueryClient } from "@tanstack/react-query";

import { useUserById } from "../hooks/useUserById";
import { useUsers } from "../hooks/useUsers";
import { updateUser } from "../api/userApi";

import Card from "../../../components/ui/Card";
import Input from "../../../components/ui/Input";

const EditEmployeePage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const userId = Number(id);

  const { data: rawUser, isLoading } = useUserById(userId);
  const { data: usersData } = useUsers({ page: 1, limit: 100 });

  // ✅ normalize responses
  const user = (rawUser as any)?.data ?? rawUser;
  const users = usersData?.data || [];

  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [isActive, setIsActive] = useState(true);
  const [managerId, setManagerId] = useState<number | "">("");

  useEffect(() => {
    if (user) {
      setUsername(user.username);
      setEmail(user.email);
      setIsActive(user.isActive ?? true);
    }
  }, [user]);

  const managerOptions = users.filter((u) => {
    if (!user) return false;
    if (u.id === user.id) return false;

    if (user.role === "HR") return u.role === "CEO";
    if (user.role === "MANAGER") return u.role === "HR";
    if (user.role === "EMPLOYEE") return u.role === "MANAGER";

    return false;
  });

  const { mutate, isPending } = useMutation({
    mutationFn: () =>
      updateUser(userId, {
        username,
        email,
        isActive,
        managerId: managerId || undefined,
      }),

    onSuccess: () => {
      toast.success("Employee updated");
      queryClient.invalidateQueries({ queryKey: ["users"] });
      queryClient.invalidateQueries({ queryKey: ["user", userId] });
      navigate("/employees");
    },

    onError: (err: any) => {
      toast.error(err?.response?.data?.message || "Update failed");
    },
  });

  if (isLoading) return <div className="p-6">Loading...</div>;
  if (!user) return null;

  return (
    <div className="p-6 max-w-lg">

      <Card className="p-6 space-y-6">

        <h2 className="text-lg font-semibold">Edit Employee</h2>

        {/* Name */}
        <Input
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          placeholder="Name"
        />

        {/* Email */}
        <Input
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          placeholder="Email"
        />

        {/* Manager */}
        <select
          className="w-full border p-2 rounded"
          value={managerId}
          onChange={(e) =>
            setManagerId(
              e.target.value === "" ? "" : Number(e.target.value)
            )
          }
        >
          <option value="">Select Manager</option>
          {managerOptions.map((m) => (
            <option key={m.id} value={m.id}>
              {m.username}
            </option>
          ))}
        </select>

        {/* Status */}
        <div className="flex items-center justify-between">
          <span>Status</span>

          <button
            onClick={() => setIsActive((prev) => !prev)}
            className={`w-12 h-6 rounded-full ${
              isActive ? "bg-green-500" : "bg-gray-300"
            } relative`}
          >
            <span
              className={`absolute top-1 left-1 w-4 h-4 bg-white rounded-full transition ${
                isActive ? "translate-x-6" : ""
              }`}
            />
          </button>
        </div>

        {/* Actions */}
        <div className="flex gap-3">
          <button
            onClick={() => navigate(-1)}
            className="flex-1 border py-2 rounded"
          >
            Cancel
          </button>

          <button
            onClick={() => mutate()}
            disabled={isPending}
            className="flex-1 bg-blue-600 text-white py-2 rounded"
          >
            {isPending ? "Saving..." : "Save"}
          </button>
        </div>

      </Card>

    </div>
  );
};

export default EditEmployeePage;