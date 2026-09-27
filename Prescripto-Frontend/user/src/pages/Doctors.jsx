import { useContext, useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { AppContext } from "../context/AppContext";

function Doctors() {
  const { speciality } = useParams();

  const [filterDoc, setFilterDoc] = useState([]);

  const { doctors } = useContext(AppContext);

  const navigate = useNavigate();

  const applyFilter = () => {
    if (speciality) {
      setFilterDoc(doctors.filter((doc) => doc.speciality === speciality));
    } else {
      setFilterDoc(doctors);
    }
  };

  useEffect(() => {
    applyFilter();
  }, [doctors, speciality]);

  return (
    <div>
      <p className="text-gray-600">Browse through the doctors specialist.</p>

      <div className="flex flex-col sm:flex-row items-start gap-5 mt-5">
        <button className='py-1 px-3 border rounded text-sm  transition-all sm:hidden '>Filters</button>

        {/*------------ Left Side ------------- */}
        <div className="flex-col gap-4 text-sm text-gray-600 hidden sm:flex">
          <p
            onClick={() =>
              speciality === "General physician"
                ? navigate(`/doctors`)
                : navigate(`/doctors/General physician`)
            }
            className="w-[94vw] sm:w-auto pl-3 py-1.5 pr-16 border border-gray-300 rounded transition-all cursor-pointer "
          >
            General physician
          </p>
          <p
            onClick={() =>
              speciality === "Gynecologist"
                ? navigate(`/doctors`)
                : navigate(`/doctors/Gynecologist`)
            }
            className="w-[94vw] sm:w-auto pl-3 py-1.5 pr-16 border border-gray-300 rounded transition-all cursor-pointer "
          >
            Gynecologist
          </p>
          <p
            onClick={() =>
              speciality === "Dermatologist"
                ? navigate(`/doctors`)
                : navigate(`/doctors/Dermatologist`)
            }
            className="w-[94vw] sm:w-auto pl-3 py-1.5 pr-16 border border-gray-300 rounded transition-all cursor-pointer "
          >
            Dermatologist
          </p>
          <p
            onClick={() =>
              speciality === "Pediatricians"
                ? navigate(`/doctors`)
                : navigate(`/doctors/Pediatricians`)
            }
            className="w-[94vw] sm:w-auto pl-3 py-1.5 pr-16 border border-gray-300 rounded transition-all cursor-pointer "
          >
            Pediatricians
          </p>
          <p
            onClick={() =>
              speciality === "Neurologist"
                ? navigate(`/doctors`)
                : navigate(`/doctors/Neurologist`)
            }
            className="w-[94vw] sm:w-auto pl-3 py-1.5 pr-16 border border-gray-300 rounded transition-all cursor-pointer "
          >
            Neurologist
          </p>
          <p
            onClick={() =>
              speciality === "Gastroenterologist"
                ? navigate(`/doctors`)
                : navigate(`/doctors/Gastroenterologist`)
            }
            className="w-[94vw] sm:w-auto pl-3 py-1.5 pr-16 border border-gray-300 rounded transition-all cursor-pointer "
          >
            Gastroenterologist
          </p>
        </div>

        {/*------------ Right Side ------------- */}
        <div className="w-full grid grid-cols-auto gap-4 gap-y-6">
          {filterDoc.map((doctor, index) => (
            <div
              key={index}
              onClick={() => navigate(`/appointment/${doctor.id}`)}
              className="border border-[#C9D8FF] rounded-xl overflow-hidden cursor-pointer hover:-translate-y-2.5 transition-all duration-500"
            >
              <img src={doctor.image} alt="" className="bg-[#EAEFFF]" />
              <div className="p-4">
                <div className="flex items-center gap-2 text-sm text-center text-green-500">
                  <p className="w-2 h-2 rounded-full bg-green-500"></p>
                  <p>Available</p>
                </div>

                <p className="text-[#262626] text-lg font-medium">
                  {doctor.name}
                </p>
                <p className="text-[#5C5C5C] text-sm">{doctor.speciality}</p>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default Doctors;
