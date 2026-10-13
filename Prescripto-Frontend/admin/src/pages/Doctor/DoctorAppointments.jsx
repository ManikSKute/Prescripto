import { useContext, useEffect } from "react";
import { assets } from "../../assets/assets";
import { AppContext } from "../../context/AppContext";
import { DoctorContext } from "../../context/DoctorContext";

const DoctorAppointments = () => {
  const {
    dToken,
    appointments,
    getAppointments,
    completeAppointment,
    cancelAppointment,
  } = useContext(DoctorContext);
  const { calculateAge, slotDateFormat, currency } = useContext(AppContext);

  useEffect(() => {
    if (dToken) {
      getAppointments();
    }
  }, [dToken]);

  return (
    <div className="p-3 sm:p-6 w-full max-w-6xl">
      <p className="mb-3 text-lg font-medium text-gray-800">
        Doctor Appointments
      </p>

      <div className="bg-white border rounded text-sm max-h-[80vh] min-h-[50vh] overflow-y-auto overflow-x-auto shadow-sm">
        <div className="min-w-162.5">
          <div className="grid grid-cols-[0.5fr_2.5fr_1fr_1fr_3fr_1fr_1fr] gap-2 py-3 px-6 border-b text-gray-700 font-medium bg-gray-50">
            <p>#</p>
            <p>Patient</p>
            <p>Payment</p>
            <p>Age</p>
            <p>Date & Time</p>
            <p>Fees</p>
            <p>Action</p>
          </div>

          {appointments.map((item, index) => (
            <div
              className="grid grid-cols-[0.5fr_2.5fr_1fr_1fr_3fr_1fr_1fr] gap-2 items-center text-gray-600 py-3 px-6 border-b hover:bg-gray-50 transition-colors"
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
              <div>
                <p className="text-xs inline border border-primary text-primary px-2 py-0.5 rounded-full">
                  {item.payment ? "Online" : "CASH"}
                </p>
              </div>
              <p>{calculateAge(item.user.dob)}</p>
              <p className="truncate">
                {slotDateFormat(item.slotDate)}, {item.slotTime}
              </p>
              <p>
                {currency}
                {item.amount}
              </p>
              {item.cancelled ? (
                <p className="text-red-400 text-xs font-medium">Cancelled</p>
              ) : item.isCompleted ? (
                <p className="text-green-500 text-xs font-medium">Completed</p>
              ) : (
                <div className="flex items-center gap-1.5">
                  <img
                    onClick={() => cancelAppointment(item.id)}
                    className="w-8 cursor-pointer hover:opacity-75 transition-opacity"
                    src={assets.cancel_icon}
                    alt="Cancel"
                    title="Cancel Appointment"
                  />
                  <img
                    onClick={() => completeAppointment(item.id)}
                    className="w-8 cursor-pointer hover:opacity-75 transition-opacity"
                    src={assets.tick_icon}
                    alt="Complete"
                    title="Complete Appointment"
                  />
                </div>
              )}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default DoctorAppointments;
