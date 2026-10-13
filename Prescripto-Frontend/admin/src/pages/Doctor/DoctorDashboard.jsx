import { useContext, useEffect } from "react";
import { assets } from "../../assets/assets";
import { AppContext } from "../../context/AppContext";
import { DoctorContext } from "../../context/DoctorContext";

const DoctorDashboard = () => {
  const {
    dToken,
    dashData,
    getDashData,
    completeAppointment,
    cancelAppointment,
  } = useContext(DoctorContext);
  const { currency, slotDateFormat } = useContext(AppContext);

  useEffect(() => {
    if (dToken) {
      getDashData();
    }
  }, [dToken]);

  return (
    dashData && (
      <div className="p-3 sm:p-6 w-full max-w-6xl">
        <div className="flex flex-wrap gap-3">
          <div className="flex items-center gap-2 bg-white p-4 min-w-52 rounded border-2 border-gray-100 cursor-pointer hover:scale-105 transition-all shadow-sm">
            <img className="w-14" src={assets.earning_icon} alt="" />
            <div>
              <p className="text-xl font-semibold text-gray-600">
                {currency} {dashData.earnings}
              </p>
              <p className="text-gray-400">Earnings</p>
            </div>
          </div>

          <div className="flex items-center gap-2 bg-white p-4 min-w-52 rounded border-2 border-gray-100 cursor-pointer hover:scale-105 transition-all shadow-sm">
            <img className="w-14" src={assets.appointments_icon} alt="" />
            <div>
              <p className="text-xl font-semibold text-gray-600">
                {dashData.appointments}
              </p>
              <p className="text-gray-400">Appointments</p>
            </div>
          </div>

          <div className="flex items-center gap-2 bg-white p-4 min-w-52 rounded border-2 border-gray-100 cursor-pointer hover:scale-105 transition-all shadow-sm">
            <img className="w-14" src={assets.patients_icon} alt="" />
            <div>
              <p className="text-xl font-semibold text-gray-600">
                {dashData.patients}
              </p>
              <p className="text-gray-400">Patients</p>
            </div>
          </div>
        </div>

        <div className="bg-white mt-10 rounded border shadow-sm">
          <div className="flex items-center gap-2.5 px-4 py-4 border-b">
            <img src={assets.list_icon} alt="" />
            <p className="font-semibold text-gray-800">Latest Bookings</p>
          </div>

          <div className="pt-2">
            {dashData.latestAppointments.map((item, index) => (
              <div
                className="flex items-center px-6 py-3 gap-3 hover:bg-gray-50 border-b last:border-b-0 transition-colors"
                key={index}
              >
                <img
                  className="rounded-full w-10 h-10 object-cover"
                  src={item.user.image}
                  alt=""
                />
                <div className="flex-1 text-sm">
                  <p className="text-gray-800 font-medium">{item.user.name}</p>
                  <p className="text-gray-600">
                    {slotDateFormat(item.slotDate)}
                  </p>
                </div>
                {item.cancelled ? (
                  <p className="text-red-400 text-xs font-medium">Cancelled</p>
                ) : item.isCompleted ? (
                  <p className="text-green-500 text-xs font-medium">
                    Completed
                  </p>
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
    )
  );
};

export default DoctorDashboard;
