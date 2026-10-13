import { useContext, useEffect } from "react";
import { assets } from "../../assets/assets";
import { AdminContext } from "../../context/AdminContext";
import { AppContext } from "../../context/AppContext";

const AllAppointments = () => {
  const { aToken, appointments, getAllAppointments, cancelAppointment } =
    useContext(AdminContext);
  const { calculateAge, slotDateFormat, currency } = useContext(AppContext);

  useEffect(() => {
    if (aToken) {
      getAllAppointments();
    }
  }, [aToken]);

  return (
    <div className="p-3 sm:p-6 w-full max-w-6xl">
      <p className="mb-3 text-lg font-medium text-gray-800">All Appointments</p>
      <div className="bg-white border rounded text-sm max-h-[80vh] min-h-[50vh] overflow-y-auto overflow-x-auto shadow-sm">
        <div className="min-w-[650px]">
          <div className="grid grid-cols-[0.5fr_2.5fr_1fr_2.5fr_2.5fr_1fr_1fr] gap-2 py-3 px-6 border-b text-gray-700 font-medium bg-gray-50">
            <p>#</p>
            <p>Patient</p>
            <p>Age</p>
            <p>Date & Time</p>
            <p>Doctor</p>
            <p>Fees</p>
            <p>Actions</p>
          </div>

          {appointments.map((item, index) => (
            <div
              className="grid grid-cols-[0.5fr_2.5fr_1fr_2.5fr_2.5fr_1fr_1fr] gap-2 items-center text-gray-600 py-3 px-6 border-b hover:bg-gray-50 transition-colors"
              key={index}
            >
              <p>{index + 1}</p>
              <div className="flex items-center gap-2">
                <img
                  className="w-8 h-8 rounded-full object-cover"
                  src={item.user.image}
                  alt=""
                />
                <p className="truncate font-medium">{item.user.name}</p>
              </div>
              <p>{calculateAge(item.user.dob)}</p>
              <p className="truncate">
                {slotDateFormat(item.slotDate)}, {item.slotTime}
              </p>
              <div className="flex items-center gap-2">
                <img
                  className="w-8 h-8 rounded-full bg-gray-200 object-cover"
                  src={item.doctor.image}
                  alt=""
                />
                <p className="truncate font-medium">{item.doctor.name}</p>
              </div>
              <p>
                {currency}
                {item.amount}
              </p>
              {item.cancelled ? (
                <p className="text-red-400 text-xs font-medium">Cancelled</p>
              ) : (
                <img
                  onClick={() => cancelAppointment(item.id)}
                  className="w-8 cursor-pointer hover:opacity-75 transition-opacity"
                  src={assets.cancel_icon}
                  alt="Cancel"
                  title="Cancel Appointment"
                />
              )}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default AllAppointments;
