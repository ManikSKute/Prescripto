import { useContext } from "react";
import { NavLink } from "react-router-dom";
import { assets } from "../assets/assets";
import { AdminContext } from "../context/AdminContext";
import { DoctorContext } from "../context/DoctorContext";

const Sidebar = () => {
  const { aToken } = useContext(AdminContext);
  const { dToken } = useContext(DoctorContext);

  const navLinkClass = ({ isActive }) =>
    `w-full flex items-center justify-center md:justify-start gap-3 py-3.5 px-3 md:px-6 cursor-pointer ${
      isActive ? "bg-[#F2F3FF] border-r-4 border-primary" : ""
    }`;

  return (
    <aside className="min-h-screen bg-white border-r shrink-0 w-16 sm:w-18 md:w-64 lg:w-72">
      {aToken && (
        <ul className="text-[#515151] mt-5">
          <NavLink
            title="Dashboard"
            className={navLinkClass}
            to={"/admin/dashboard"}
          >
            <img className="w-5 min-w-5" src={assets.home_icon} alt="" />
            <p className="hidden md:block">Dashboard</p>
          </NavLink>
          <NavLink
            title="Appointments"
            className={navLinkClass}
            to={"/admin/appointments"}
          >
            <img className="w-5 min-w-5" src={assets.appointment_icon} alt="" />
            <p className="hidden md:block">Appointments</p>
          </NavLink>
          <NavLink
            title="Add Doctor"
            className={navLinkClass}
            to={"/admin/doctors/add"}
          >
            <img className="w-5 min-w-5" src={assets.add_icon} alt="" />
            <p className="hidden md:block">Add Doctor</p>
          </NavLink>
          <NavLink
            title="Doctors List"
            className={navLinkClass}
            to={"/admin/doctors"}
            end
          >
            <img className="w-5 min-w-5" src={assets.people_icon} alt="" />
            <p className="hidden md:block">Doctors List</p>
          </NavLink>
        </ul>
      )}

      {dToken && (
        <ul className="text-[#515151] mt-5">
          <NavLink
            title="Dashboard"
            className={navLinkClass}
            to={"/doctor/dashboard"}
          >
            <img className="w-5 min-w-5" src={assets.home_icon} alt="" />
            <p className="hidden md:block">Dashboard</p>
          </NavLink>
          <NavLink
            title="Appointments"
            className={navLinkClass}
            to={"/doctor/appointments"}
          >
            <img className="w-5 min-w-5" src={assets.appointment_icon} alt="" />
            <p className="hidden md:block">Appointments</p>
          </NavLink>
          <NavLink
            title="Profile"
            className={navLinkClass}
            to={"/doctor/profile"}
          >
            <img className="w-5 min-w-5" src={assets.people_icon} alt="" />
            <p className="hidden md:block">Profile</p>
          </NavLink>
        </ul>
      )}
    </aside>
  );
};

export default Sidebar;
