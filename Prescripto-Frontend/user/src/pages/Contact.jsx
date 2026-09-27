import { assets } from "../assets/assets";

function Contact() {
  return (
    <div>
      <div className="text-center text-2xl pt-10 text-[#707070]">
        <p>
          CONTACT <span className="text-gray-700 font-semibold">US</span>
        </p>
      </div>

      <div className="my-10 flex flex-col justify-center md:flex-row gap-10 mb-28 text-sm">
        <img src={assets.contact_image} alt="" className="w-full md:max-w-90" />

        <div className="flex flex-col justify-center items-start gap-6">
          <p className=" font-semibold text-lg text-gray-600">OUR OFFICE</p>

          <p className=" text-gray-500">
            Flat No. 302, Sunrise Apartments <br /> MG Road, Shivajinagar, Pune
          </p>

          <p className=" text-gray-500">
            Tel: (000) 000-0000 <br /> Email: prescripto@gmail.com
          </p>

          <p className=" font-semibold text-lg text-gray-600">
            CAREERS AT PRESCRIPTO
          </p>

          <p className=" text-gray-500">
            Learn more about our teams and job openings.{" "}
          </p>

          <button className="border border-black px-8 py-4 text-sm hover:bg-black hover:text-white transition-all duration-500 cursor-pointer">
            Explore Jobs
          </button>
        </div>
      </div>
    </div>
  );
}

export default Contact;
