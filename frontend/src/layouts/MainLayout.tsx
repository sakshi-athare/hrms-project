import Sidebar from "../components/navigation/Sidebar";
import Navbar from "../components/navigation/Navbar";
import { Outlet } from "react-router-dom";

const MainLayout = () => {
  return (
    <div className="flex h-screen bg-bg">

      <Sidebar />

      <div className="flex flex-col flex-1 overflow-hidden">

        <Navbar />

        <main className="flex-1 overflow-y-auto p-6">
          <div className="max-w-350 mx-auto space-y-6">
            <Outlet />
          </div>
        </main>

      </div>
    </div>
  );
};

export default MainLayout;