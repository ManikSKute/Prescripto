import { useContext } from "react";
import { Navigate, Route, Routes } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import Navbar from "./components/Navbar";
import Sidebar from "./components/Sidebar";
import { AdminContext } from "./context/AdminContext";
import { DoctorContext } from "./context/DoctorContext";
import AddDoctor from "./pages/Admin/AddDoctor";
import AllAppointments from "./pages/Admin/AllAppointments";
import Dashboard from "./pages/Admin/Dashboard";
import DoctorsList from "./pages/Admin/DoctorsList";
import DoctorAppointments from "./pages/Doctor/DoctorAppointments";
import DoctorDashboard from "./pages/Doctor/DoctorDashboard";
import DoctorProfile from "./pages/Doctor/DoctorProfile";
import Login from "./pages/Login";

const App = () => {
  const { aToken } = useContext(AdminContext);
  const { dToken } = useContext(DoctorContext);

  return aToken || dToken ? (
    <div className="bg-[#F8F9FD]">
      <ToastContainer />
      <Navbar />
      <div className="flex items-start">
        <Sidebar />
        <div className="flex-1 w-full min-w-0">
          <Routes>
            {/* Root / Default Redirect */}
            <Route
              path="/"
              element={
                aToken ? (
                  <Navigate to="/admin/dashboard" replace />
                ) : (
                  <Navigate to="/doctor/dashboard" replace />
                )
              }
            />
            <Route
              path="/admin"
              element={<Navigate to="/admin/dashboard" replace />}
            />
            <Route
              path="/doctor"
              element={<Navigate to="/doctor/dashboard" replace />}
            />

            {/* Admin Routes */}
            <Route path="/admin/dashboard" element={<Dashboard />} />
            <Route path="/admin/appointments" element={<AllAppointments />} />
            <Route path="/admin/doctors/add" element={<AddDoctor />} />
            <Route path="/admin/doctors" element={<DoctorsList />} />

            {/* Doctor Routes */}
            <Route path="/doctor/dashboard" element={<DoctorDashboard />} />
            <Route
              path="/doctor/appointments"
              element={<DoctorAppointments />}
            />
            <Route path="/doctor/profile" element={<DoctorProfile />} />

            {/* Catch-all redirect */}
            <Route
              path="*"
              element={
                aToken ? (
                  <Navigate to="/admin/dashboard" replace />
                ) : (
                  <Navigate to="/doctor/dashboard" replace />
                )
              }
            />
          </Routes>
        </div>
      </div>
    </div>
  ) : (
    <>
      <Login />
      <ToastContainer />
    </>
  );
};

export default App;
