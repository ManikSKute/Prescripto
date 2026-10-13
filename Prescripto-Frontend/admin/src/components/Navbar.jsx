import { useContext } from "react";
import { useNavigate } from "react-router-dom";
import { assets } from "../assets/assets";
import { AdminContext } from "../context/AdminContext";
import { DoctorContext } from "../context/DoctorContext";

const Navbar = () => {
  const { aToken, setAToken } = useContext(AdminContext);
  const { dToken, setDToken } = useContext(DoctorContext);

  const navigate = useNavigate();

  const logout = () => {
    navigate("/");
    aToken && setAToken("");
    aToken && localStorage.removeItem("aToken");
    dToken && setDToken("");
    dToken && localStorage.removeItem("dToken");
  };

  return (
    <div className="flex justify-between items-center px-4 sm:px-10 py-3 border-b bg-white">
      <div className="flex items-center gap-2 text-xs">
        <img
          className="w-32 sm:w-40 cursor-pointer"
          src={assets.admin_logo}
          alt=""
        />
        <p className="border px-2.5 py-0.5 rounded-full border-gray-500 text-gray-600">
          {aToken ? "Admin" : "Doctor"}
        </p>
      </div>
      <div className="flex items-center gap-2 sm:gap-4">
        {import.meta.env.VITE_USER_URL && (
          <button
            onClick={() => window.open(import.meta.env.VITE_USER_URL, "_blank")}
            className="border border-gray-400 px-3 sm:px-5 py-1.5 sm:py-2 rounded-full font-light text-xs cursor-pointer hover:bg-gray-100 transition-all"
          >
            Patient Site
          </button>
        )}
        <button
          onClick={logout}
          className="bg-primary text-white text-xs sm:text-sm px-6 sm:px-10 py-1.5 sm:py-2 rounded-full cursor-pointer"
        >
          Logout
        </button>
      </div>
    </div>
  );
};

export default Navbar;
