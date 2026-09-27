import React from 'react'
import { assets } from '../assets/assets'

const Footer = () => {
  return (
    <div className='md:mx-10'>
        <div className='flex flex-col sm:grid grid-cols-[3fr_1fr_1fr] gap-14 my-10  mt-40 text-sm'>

            {/*-------- Left Section ------- */}
            <div>
                <img src={assets.logo} alt="" className='mb-5 w-40' />

                <p className='w-full md:w-2/3 text-gray-600 leading-6'>Lorem ipsum dolor sit amet consectetur, adipisicing elit. Expedita culpa nulla minus pariatur voluptatum illo magnam, ratione assumenda. Molestiae eligendi commodi odit est harum ducimus ipsam blanditiis unde facere quod?</p>
            </div>

            {/*-------- Center Section ------- */}
            <div>
                <p className='text-xl font-medium mb-5'>COMPANY</p>
                <ul className='flex flex-col gap-2 text-gray-600'>
                    <li>Home</li>
                    <li>About Us</li>
                    <li>Delivery</li>
                    <li>Privacy policy</li>
                </ul>
            </div>

            {/*-------- Right Section ------- */}
            <div>
                <p className='text-xl font-medium mb-5'>GET IN TOUCH</p>
                <ul className='flex flex-col gap-2 text-gray-600'>
                    <li>+0-000-000-000</li>
                    <li>abc@gmail.com</li>
                </ul>
            </div>
        </div>

        {/*-------- Bottom Section ------- */}
        <div>
            <hr />
            <p className='py-5 text-sm text-center'>Copyright 2026 @ Mk.dev - All Right Reserved.</p>
        </div>
    </div>
  )
}

export default Footer