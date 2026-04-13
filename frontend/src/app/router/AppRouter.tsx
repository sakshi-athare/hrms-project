import { Routes, Route, Navigate } from "react-router-dom";
import { useAuthStore } from "../../features/auth/model/authStore";

import ProtectedRoute from "./ProtectedRoute";
import PublicRoute from "./PublicRoute";
import PrivateLayout from "./PrivateLayout";

import DashboardPage from "../../features/dashboard/pages/DashboardPage";
import LoginPage from "../../features/auth/pages/LoginPage";

import EmployeePage from "../../features/users/pages/EmployeePage";
import CreateEmployeePage from "../../features/users/pages/CreateEmployeePage";
import EditEmployeePage from "../../features/users/pages/EditEmployeePage";
import EmployeeDetailsPage from "../../features/users/pages/EmployeeDetailsPage";
import RoleLogsPage from "../../features/role-logs/pages/RoleLogsPage";

const AppRouter = () => {
  const isAuthenticated = useAuthStore((s) => s.isAuthenticated);
  const isLoading = useAuthStore((s) => s.isLoading);

  if (isLoading) return null;

  return (
    <Routes>

      <Route
        path="/"
        element={
          isAuthenticated
            ? <Navigate to="/dashboard" replace />
            : <Navigate to="/login" replace />
        }
      />

      <Route
        path="/login"
        element={
          <PublicRoute>
            <LoginPage />
          </PublicRoute>
        }
      />

      <Route element={<ProtectedRoute />}>
        <Route element={<PrivateLayout />}>
          <Route path="/dashboard" element={<DashboardPage />} />
          <Route path="/employees" element={<EmployeePage />} />
          <Route path="/employees/create" element={<CreateEmployeePage />} />
          <Route path="/employees/:id" element={<EmployeeDetailsPage />} />
          <Route path="/employees/edit/:id" element={<EditEmployeePage />} />
          <Route path="/role-logs" element={<RoleLogsPage/>}></Route>
        </Route>
      </Route>

      <Route path="/unauthorized" element={<div>Unauthorized</div>} />

    </Routes>
  );
};

export default AppRouter;