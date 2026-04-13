// src/pages/employees/CreateEmployeePage.tsx
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuthStore } from "../../auth/model/authStore";
import { useUsers } from "../hooks/useUsers";
import { useCreateUser } from "../hooks/useCreateUser";
import Card from "../../../components/ui/Card";
import Input from "../../../components/ui/Input";

const ROLES = ["HR", "MANAGER", "EMPLOYEE"];

const CreateEmployeePage = () => {
  const navigate = useNavigate();

  const user = useAuthStore((s) => s.user);

  const isAdmin =
    user?.role === "CEO" || user?.role === "HR";

  const { data } = isAdmin
    ? useUsers({ search: "", page: 1, limit: 100 })
    : { data: { data: [] } };
  const users = data?.data ?? [];



  const { mutate: createUser, isPending } = useCreateUser();

  const [form, setForm] = useState({
    username: "",
    email: "",
    password: "",
    role: "EMPLOYEE",
    managerId: "",
  });

  const managers = users.filter((u) => {
    if (form.role === "EMPLOYEE") return u.role === "MANAGER";
    if (form.role === "MANAGER") return u.role === "HR";
    if (form.role === "HR") return u.role === "CEO";
    return false;
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    createUser({
      username: form.username,
      email: form.email,
      password: form.password,
      role: form.role,
      managerId: form.managerId ? Number(form.managerId) : undefined,
    });
  };

  const field = (key: keyof typeof form) => ({
    value: form[key],
    onChange: (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) =>
      setForm({ ...form, [key]: e.target.value }),
  });

  return (
    <div className="p-6 max-w-xl space-y-5">

      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold tracking-tight text-[#111827]">
            Create Employee
          </h1>
          <p className="text-sm text-[#6b7280] mt-0.5">
            Add a new team member to your organisation
          </p>
        </div>

        <button
          onClick={() => navigate(-1)}
          className="text-sm font-medium text-[#374151] border border-[#e5e7eb] px-3 py-1.5 rounded-lg hover:bg-[#f9fafb] transition inline-flex items-center gap-1.5"
        >
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" className="w-3.5 h-3.5">
            <path d="M19 12H5M12 19l-7-7 7-7" />
          </svg>
          Back
        </button>
      </div>

      <Card>
        <form onSubmit={handleSubmit} className="px-6 py-5 space-y-4">

          <Input
            label="Full Name"
            placeholder="e.g. Sarah Brown"
            required
            {...field("username")}
            onChange={(e) => setForm({ ...form, username: e.target.value })}
          />

          <Input
            label="Email Address"
            type="email"
            placeholder="e.g. sarah@company.com"
            required
            {...field("email")}
            onChange={(e) => setForm({ ...form, email: e.target.value })}
          />

          <Input
            label="Password"
            type="password"
            placeholder="Minimum 8 characters"
            required
            {...field("password")}
            onChange={(e) => setForm({ ...form, password: e.target.value })}
          />

          {/* Role select */}
          <div className="space-y-1">
            <label className="block text-sm font-medium text-[#374151]">
              Role
            </label>
            <select
              value={form.role}
              onChange={(e) => setForm({ ...form, role: e.target.value, managerId: "" })}
              className="w-full px-3 py-2 border border-[#e5e7eb] rounded-lg text-sm text-[#111827] bg-white focus:outline-none focus:ring-2 focus:ring-[#3B6FF0]/30 focus:border-[#3B6FF0]"
            >
              {ROLES.map((r) => (
                <option key={r} value={r}>{r}</option>
              ))}
            </select>
          </div>

          {/* Manager select */}
          <div className="space-y-1">
            <label className="block text-sm font-medium text-[#374151]">
              Reporting Manager
            </label>
            <select
              value={form.managerId}
              onChange={(e) => setForm({ ...form, managerId: e.target.value })}
              className="w-full px-3 py-2 border border-[#e5e7eb] rounded-lg text-sm text-[#111827] bg-white focus:outline-none focus:ring-2 focus:ring-[#3B6FF0]/30 focus:border-[#3B6FF0]"
            >
              <option value="">Select manager (optional)</option>
              {managers.map((m) => (
                <option key={m.id} value={m.id}>
                  {m.username} — {m.role}
                </option>
              ))}
            </select>
            {managers.length === 0 && (
              <p className="text-xs text-[#9ca3af] mt-1">
                No available managers for this role
              </p>
            )}
          </div>

          <div className="flex gap-3 pt-1">
            <button
              type="submit"
              disabled={isPending}
              className="flex-1 bg-[#3B6FF0] text-white text-sm font-semibold py-2.5 rounded-lg hover:bg-[#2d5edf] transition active:scale-[.98] disabled:opacity-60 inline-flex items-center justify-center gap-2"
            >
              {isPending ? (
                <>
                  <svg className="animate-spin w-4 h-4" viewBox="0 0 24 24" fill="none">
                    <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" />
                    <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8z" />
                  </svg>
                  Creating…
                </>
              ) : (
                <>
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" className="w-4 h-4">
                    <path d="M12 5v14M5 12h14" />
                  </svg>
                  Create Employee
                </>
              )}
            </button>

            <button
              type="button"
              onClick={() => navigate(-1)}
              className="px-4 py-2.5 text-sm font-medium text-[#374151] border border-[#e5e7eb] rounded-lg hover:bg-[#f9fafb] transition"
            >
              Cancel
            </button>
          </div>
        </form>
      </Card>
    </div>
  );
};

export default CreateEmployeePage;