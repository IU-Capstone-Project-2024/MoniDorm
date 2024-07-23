import React from 'react';
import { useCookies } from 'react-cookie';
import { useNavigate } from 'react-router-dom';
 
const Navbar = () => {
  const [cookies, setCookie, removeCookie] = useCookies(['is_authorized']);
  const navigate = useNavigate();

  const handleLogout = () => {
    removeCookie('is_authorized', { path: '/' }); // Delete the is_authorized cookie
    navigate('/login'); // Redirect to login page
  };

    return (
		<div className="navbar bg-base-100">
  <div className="flex-1">
    <a className="btn btn-ghost text-xl" href='/'>MoniDorm</a>
  </div>
  <div className="flex-none">
    <div className="dropdown dropdown-end">
      <div tabIndex={0} role="button" className="btn btn-ghost btn-circle avatar">
        <div className="rounded-full">
        <svg width="31" height="28" viewBox="0 0 31 28" fill="none" xmlns="http://www.w3.org/2000/svg">
  <g clip-path="url(#clip0_1_298)">
    <path d="M14.8913 13.3881C16.8908 13.3881 18.6221 12.7284 20.0368 11.4269C21.4514 10.1256 22.1685 8.53333 22.1685 6.69384C22.1685 4.85499 21.4514 3.26253 20.0365 1.96078C18.6217 0.659672 16.8905 0 14.8913 0C12.8917 0 11.1608 0.659672 9.74614 1.96099C8.33151 3.26231 7.61417 4.85477 7.61417 6.69384C7.61417 8.53333 8.33151 10.1258 9.74637 11.4271C11.1612 12.7282 12.8924 13.3881 14.8913 13.3881Z" fill="#B1B1B1" />
    <path d="M27.6244 21.3716C27.5836 20.83 27.5011 20.2393 27.3796 19.6154C27.257 18.9869 27.0991 18.3928 26.9101 17.8497C26.7148 17.2885 26.4493 16.7342 26.121 16.203C25.7803 15.6517 25.3802 15.1716 24.9311 14.7766C24.4616 14.3633 23.8867 14.031 23.2219 13.7886C22.5594 13.5476 21.8253 13.4254 21.0399 13.4254C20.7315 13.4254 20.4332 13.5418 19.8572 13.8868C19.5027 14.0995 19.088 14.3455 18.6251 14.6175C18.2293 14.8495 17.6932 15.0669 17.0309 15.2636C16.3848 15.456 15.7288 15.5535 15.0813 15.5535C14.4338 15.5535 13.778 15.456 13.1312 15.2636C12.4696 15.0671 11.9334 14.8497 11.5381 14.6177C11.0796 14.3482 10.6647 14.1023 10.3049 13.8866C9.72954 13.5416 9.43104 13.4252 9.12261 13.4252C8.33704 13.4252 7.6031 13.5476 6.94085 13.7889C6.27652 14.0308 5.7014 14.3631 5.23139 14.7768C4.78259 15.172 4.3822 15.6519 4.04197 16.203C3.71395 16.7342 3.44841 17.2882 3.25293 17.8499C3.06415 18.393 2.90625 18.9869 2.78362 19.6154C2.66214 20.2384 2.57962 20.8294 2.53882 21.3722C2.49871 21.904 2.47842 22.456 2.47842 23.0135C2.47842 24.4643 2.97978 25.6388 3.96843 26.505C4.94487 27.3597 6.23687 27.7934 7.80802 27.7934H22.3559C23.9271 27.7934 25.2186 27.36 26.1953 26.505C27.1841 25.6394 27.6855 24.4647 27.6855 23.0132C27.6853 22.4532 27.6648 21.9009 27.6244 21.3716Z" fill="#B1B1B1" />
  </g>
  <defs>
    <clipPath id="clip0_1_298">
      <rect width="30.2133" height="27.7933" fill="white" />
    </clipPath>
  </defs>
</svg>
        </div>
      </div>
      <ul
        tabIndex={0}
        className="menu menu-sm dropdown-content bg-base-100 rounded-box z-[1] mt-3 w-52 p-2 shadow">
        <li><a onClick={handleLogout}>Logout</a></li>
      </ul>
    </div>
  </div>
</div>
    );
};
 
export default Navbar;







