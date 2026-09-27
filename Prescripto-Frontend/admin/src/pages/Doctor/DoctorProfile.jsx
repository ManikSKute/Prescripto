// import axios from "axios";
// import { useContext, useEffect, useState } from "react";
// import { toast } from "react-toastify";
// import { AppContext } from "../../context/AppContext";
// import { DoctorContext } from "../../context/DoctorContext";

// const DoctorProfile = () => {
//   const { dToken, profileData, setProfileData, getProfileData, backendUrl } =
//     useContext(DoctorContext);
//   const { currency } = useContext(AppContext);

//   const [isEdit, setIsEdit] = useState(false);

//   const updateProfile = async () => {
//     try {
//       const updateData = {
//         fees: profileData.fees,
//         addressLine1: profileData.addressLine1,
//         addressLine2: profileData.addressLine2,
//         available: profileData.available,
//       };

//       const { data } = await axios.post(
//         backendUrl + "/api/doctor/update-profile",
//         updateData,
//         { headers: { dToken } },
//       );
//       if (data.success) {
//         toast.success(data.message);
//         setIsEdit(false);
//         getProfileData();
//       } else {
//         toast.error(data.message);
//       }
//     } catch (error) {
//       toast.error(error.message);
//     }
//   };

//   useEffect(() => {
//     if (dToken) {
//       getProfileData();
//     }
//   }, [dToken]);

//   return (
//     profileData && (
//       <div>
//         <div className="flex flex-col gap-4 m-5">
//           <div>
//             <img
//               className="bg-primary/80 w-full sm:max-w-64 rounded-lg"
//               src={profileData.image}
//               alt=""
//             />
//           </div>

//           <div className="flex-1 border border-stone-100 rounded-lg p-8 py-7 bg-white">
//             <p className="flex items-center gap-2 text-3xl font-medium text-gray-700">
//               {profileData.name}
//             </p>
//             <div className="flex items-center gap-2 mt-1 text-gray-600">
//               <p>
//                 {profileData.degree} - {profileData.speciality}
//               </p>
//               <button className="py-0.5 px-2 border text-xs rounded-full">
//                 {profileData.experience}
//               </button>
//             </div>

//             <div>
//               <p className="flex items-center gap-1 text-sm font-medium text-neutral-800 mt-3">
//                 About:
//               </p>
//               <p className="text-sm text-gray-600 max-w-175 mt-1">
//                 {profileData.about}
//               </p>
//             </div>

//             <p className="text-gray-600 font-medium mt-4">
//               Appointment fee:{" "}
//               <span className="text-gray-800">
//                 {currency}{" "}
//                 {isEdit ? (
//                   <input
//                     type="number"
//                     onChange={(e) =>
//                       setProfileData((prev) => ({
//                         ...prev,
//                         fees: e.target.value,
//                       }))
//                     }
//                     value={profileData.fees}
//                   />
//                 ) : (
//                   profileData.fees
//                 )}
//               </span>
//             </p>

//             <div className="flex gap-2 py-2">
//               <p>Address:</p>
//               <p className="text-sm">
//                 {isEdit ? (
//                   <input
//                     type="text"
//                     onChange={(e) =>
//                       setProfileData((prev) => ({
//                         ...prev,
//                         addressLine1: e.target.value,
//                       }))
//                     }
//                     value={profileData.addressLine1}
//                   />
//                 ) : (
//                   profileData.addressLine1
//                 )}
//                 <br />
//                 {isEdit ? (
//                   <input
//                     type="text"
//                     onChange={(e) =>
//                       setProfileData((prev) => ({
//                         ...prev,
//                         addressLine2: e.target.value,
//                       }))
//                     }
//                     value={profileData.addressLine2}
//                   />
//                 ) : (
//                   profileData.addressLine2
//                 )}
//               </p>
//             </div>

//             <div className="flex gap-1 pt-2">
//               <input
//                 onChange={() =>
//                   isEdit &&
//                   setProfileData((prev) => ({
//                     ...prev,
//                     available: !prev.available,
//                   }))
//                 }
//                 checked={profileData.available}
//                 type="checkbox"
//               />
//               <label htmlFor="">Available</label>
//             </div>

//             {isEdit ? (
//               <button
//                 onClick={updateProfile}
//                 className="px-4 py-1 border border-primary text-sm rounded-full mt-5 hover:bg-primary hover:text-white transition-all cursor-pointer"
//               >
//                 Save
//               </button>
//             ) : (
//               <button
//                 onClick={() => setIsEdit(true)}
//                 className="px-4 py-1 border border-primary text-sm rounded-full mt-5 hover:bg-primary hover:text-white transition-all cursor-pointer"
//               >
//                 Edit
//               </button>
//             )}
//           </div>
//         </div>
//       </div>
//     )
//   );
// };

// export default DoctorProfile;

import axios from "axios";
import { useContext, useEffect, useState } from "react";
import { toast } from "react-toastify";
import { assets } from "../../assets/assets";
import { AppContext } from "../../context/AppContext";
import { DoctorContext } from "../../context/DoctorContext";

const DoctorProfile = () => {
  const { dToken, profileData, setProfileData, getProfileData, backendUrl } =
    useContext(DoctorContext);
  const { currency } = useContext(AppContext);

  const [isEdit, setIsEdit] = useState(false);
  const [image, setImage] = useState(false);

  const updateProfile = async () => {
    try {
      const formData = new FormData();

      const docData = {
        name: profileData.name,
        speciality: profileData.speciality,
        degree: profileData.degree,
        experience: profileData.experience,
        about: profileData.about,
        fees: Number(profileData.fees),
        addressLine1: profileData.addressLine1,
        addressLine2: profileData.addressLine2,
        available: profileData.available,
      };

      formData.append(
        "docData",
        new Blob([JSON.stringify(docData)], { type: "application/json" }),
      );

      if (image) {
        formData.append("image", image);
      }

      const { data } = await axios.post(
        backendUrl + "/api/doctor/update-profile",
        formData,
        {
          headers: { dToken, "Content-Type": "multipart/form-data" },
        },
      );

      if (data.success) {
        toast.success(data.message);
        setIsEdit(false);
        setImage(false);
        getProfileData();
      } else {
        toast.error(data.message);
      }
    } catch (error) {
      console.log(error);
      toast.error(error.message);
    }
  };

  useEffect(() => {
    if (dToken) {
      getProfileData();
    }
  }, [dToken]);

  return (
    profileData && (
      <div>
        <div className="flex flex-col gap-4 m-5">
          <div>
            {isEdit ? (
              <label htmlFor="doc-image">
                <div className="inline-block relative cursor-pointer">
                  <img
                    className="bg-primary/80 w-full sm:max-w-64 rounded-lg opacity-80"
                    src={image ? URL.createObjectURL(image) : profileData.image}
                    alt=""
                  />
                  <img
                    className="w-10 absolute bottom-12 right-12"
                    src={image ? "" : assets.upload_icon}
                    alt=""
                  />
                </div>
                <input
                  onChange={(e) => setImage(e.target.files[0])}
                  type="file"
                  id="doc-image"
                  hidden
                />
              </label>
            ) : (
              <img
                className="bg-primary/80 w-full sm:max-w-64 rounded-lg"
                src={profileData.image}
                alt=""
              />
            )}
          </div>

          <div className="flex-1 border border-stone-100 rounded-lg p-8 py-7 bg-white max-w-2xl">
            {/* ----- Doctor Name ----- */}
            {isEdit ? (
              <div className="mb-2">
                <p className="text-xs text-gray-500">Doctor Name:</p>
                <input
                  className="border rounded px-2 py-1 text-2xl font-medium text-gray-700 w-full"
                  type="text"
                  value={profileData.name || ""}
                  onChange={(e) =>
                    setProfileData((prev) => ({
                      ...prev,
                      name: e.target.value,
                    }))
                  }
                />
              </div>
            ) : (
              <p className="flex items-center gap-2 text-3xl font-medium text-gray-700">
                {profileData.name}
              </p>
            )}

            {/* ----- Degree, Speciality & Experience ----- */}
            <div className="flex flex-wrap items-center gap-2 mt-1 text-gray-600">
              {isEdit ? (
                <div className="flex flex-wrap gap-2 w-full my-2">
                  <div className="flex-1 min-w-36">
                    <p className="text-xs text-gray-500">Degree:</p>
                    <input
                      className="border rounded px-2 py-1 text-sm w-full"
                      type="text"
                      value={profileData.degree || ""}
                      onChange={(e) =>
                        setProfileData((prev) => ({
                          ...prev,
                          degree: e.target.value,
                        }))
                      }
                    />
                  </div>
                  <div className="flex-1 min-w-44">
                    <p className="text-xs text-gray-500">Speciality:</p>
                    <select
                      className="border rounded px-2 py-1 text-sm w-full"
                      value={profileData.speciality || "General physician"}
                      onChange={(e) =>
                        setProfileData((prev) => ({
                          ...prev,
                          speciality: e.target.value,
                        }))
                      }
                    >
                      <option value="General physician">
                        General physician
                      </option>
                      <option value="Gynecologist">Gynecologist</option>
                      <option value="Dermatologist">Dermatologist</option>
                      <option value="Pediatricians">Pediatricians</option>
                      <option value="Neurologist">Neurologist</option>
                      <option value="Gastroenterologist">
                        Gastroenterologist
                      </option>
                    </select>
                  </div>
                  <div className="min-w-28">
                    <p className="text-xs text-gray-500">Experience:</p>
                    <select
                      className="border rounded px-2 py-1 text-sm w-full"
                      value={profileData.experience || "1 Year"}
                      onChange={(e) =>
                        setProfileData((prev) => ({
                          ...prev,
                          experience: e.target.value,
                        }))
                      }
                    >
                      <option value="1 Year">1 Year</option>
                      <option value="2 Years">2 Years</option>
                      <option value="3 Years">3 Years</option>
                      <option value="4 Years">4 Years</option>
                      <option value="5 Years">5 Years</option>
                      <option value="6 Years">6 Years</option>
                      <option value="7 Years">7 Years</option>
                      <option value="8 Years">8 Years</option>
                      <option value="9 Years">9 Years</option>
                      <option value="10 Years">10 Years</option>
                    </select>
                  </div>
                </div>
              ) : (
                <>
                  <p>
                    {profileData.degree} - {profileData.speciality}
                  </p>
                  <button className="py-0.5 px-2 border text-xs rounded-full">
                    {profileData.experience}
                  </button>
                </>
              )}
            </div>

            {/* ----- About Doctor ----- */}
            <div>
              <p className="flex items-center gap-1 text-sm font-medium text-neutral-800 mt-3">
                About:
              </p>
              {isEdit ? (
                <textarea
                  className="border rounded w-full p-2 text-sm text-gray-600 mt-1"
                  rows={4}
                  value={profileData.about || ""}
                  onChange={(e) =>
                    setProfileData((prev) => ({
                      ...prev,
                      about: e.target.value,
                    }))
                  }
                />
              ) : (
                <p className="text-sm text-gray-600 max-w-175 mt-1">
                  {profileData.about}
                </p>
              )}
            </div>

            {/* ----- Fees ----- */}
            <p className="text-gray-600 font-medium mt-4">
              Appointment fee:{" "}
              <span className="text-gray-800">
                {currency}{" "}
                {isEdit ? (
                  <input
                    className="border rounded px-2 py-0.5 max-w-28"
                    type="number"
                    onChange={(e) =>
                      setProfileData((prev) => ({
                        ...prev,
                        fees: e.target.value,
                      }))
                    }
                    value={profileData.fees}
                  />
                ) : (
                  profileData.fees
                )}
              </span>
            </p>

            {/* ----- Address ----- */}
            <div className="flex gap-2 py-2">
              <p>Address:</p>
              <p className="text-sm">
                {isEdit ? (
                  <input
                    className="border rounded px-2 py-0.5 mb-1 w-full"
                    type="text"
                    onChange={(e) =>
                      setProfileData((prev) => ({
                        ...prev,
                        addressLine1: e.target.value,
                      }))
                    }
                    value={profileData.addressLine1 || ""}
                  />
                ) : (
                  profileData.addressLine1
                )}
                <br />
                {isEdit ? (
                  <input
                    className="border rounded px-2 py-0.5 w-full"
                    type="text"
                    onChange={(e) =>
                      setProfileData((prev) => ({
                        ...prev,
                        addressLine2: e.target.value,
                      }))
                    }
                    value={profileData.addressLine2 || ""}
                  />
                ) : (
                  profileData.addressLine2
                )}
              </p>
            </div>

            {/* ----- Available Checkbox ----- */}
            <div className="flex gap-1 pt-2">
              <input
                onChange={() =>
                  isEdit &&
                  setProfileData((prev) => ({
                    ...prev,
                    available: !prev.available,
                  }))
                }
                checked={profileData.available || false}
                type="checkbox"
                id="avail-check"
              />
              <label htmlFor="avail-check" className="cursor-pointer">
                Available
              </label>
            </div>

            {/* ----- Action Buttons ----- */}
            {isEdit ? (
              <button
                onClick={updateProfile}
                className="px-6 py-2 border border-primary text-sm rounded-full mt-5 hover:bg-primary hover:text-white transition-all"
              >
                Save
              </button>
            ) : (
              <button
                onClick={() => setIsEdit(true)}
                className="px-6 py-2 border border-primary text-sm rounded-full mt-5 hover:bg-primary hover:text-white transition-all"
              >
                Edit
              </button>
            )}
          </div>
        </div>
      </div>
    )
  );
};

export default DoctorProfile;
