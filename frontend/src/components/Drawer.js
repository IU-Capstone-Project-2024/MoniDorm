import React, { useState } from 'react';
import {Link, useLocation } from 'react-router-dom';
 
const Drawer = () => {
  const location = useLocation();
  const [selectedOption, setSelectedOption] = useState('');

  const handleSelectChange = (event) => {
    setSelectedOption(event.target.value);
  };

  const generateIframeSrc = (panelId) => {
    return `http://10.90.137.18:3000/d-solo/fdpa1canapou8a/monidorm-dashboard?orgId=1&from=1721031065935&to=1721635865935&theme=light&panelId=${panelId}`;
  };

  // Function to determine if the link is active based on the current location
  // 10.90.137.18
  const isActive = (path) => location.pathname.includes(path);
    return (
        <div className="drawer lg:drawer-open">
  <input id="my-drawer-2" type="checkbox" className="drawer-toggle" />
  <div className="drawer-content flex flex-col justify-center bg-gray-100">
    <div className='items-center justify-normal' style={{ transform: 'translateY(-30px) translateX(30px)' }}>
    <select className="select select-bordered max-w-xs items" onChange={handleSelectChange} value={selectedOption}>
      <option disabled selected value=''>Reports per hour</option>
      <option value="1">All</option>
      <option value="4">Dorm 1</option>
      <option value="5">Dorm 2</option>
      <option value="6">Dorm 3</option>
      <option value="10">Dorm 4</option>
      <option value="9">Dorm 5</option>
      <option value="7">Dorm 6</option>
      <option value="8">Dorm 7</option>
      </select>
    </div>
  <div className="App flex items-center justify-center flex-row">
  {selectedOption && (
        <iframe
          src={generateIframeSrc(selectedOption)}
          title='dashboard'
          width="450"
          height="400"
        ></iframe>
        )}
    {/* <iframe src="http://10.90.137.18:3000/d-solo/fdpa1canapou8a/monidorm-dashboard?orgId=1&from=1720122555995&to=1720727355995&theme=light&panelId=1" title="dashboard" width="450" height="400"></iframe> */}
    <iframe className='pl-2' src="http://10.90.137.18:3000/d-solo/fdpa1canapou8a/monidorm-dashboard?orgId=1&from=1720312200000&to=1720427400000&theme=light&panelId=2" title='dashboard' width="450" height="400" frameborder="0"></iframe>
		</div>
    <label htmlFor="my-drawer-2" className="btn btn-primary drawer-button lg:hidden">
      Open drawer
    </label>
  </div>
  <div className="drawer-side">
    <label htmlFor="my-drawer-2" aria-label="close sidebar" className="drawer-overlay"></label>
    <ul className="menu text-base-content min-h-full w-80 p-4">
      {/* Sidebar content here */}
      <li className={`font-medium text-xl py-3 ${isActive('dashboard') ? 'text-blue' : 'text-grey'}`}><Link to="/dashboard"><svg width="31" height="28" viewBox="0 0 31 28" fill="text-blue" xmlns="http://www.w3.org/2000/svg">
  <g clip-path="url(#clip0_1_309)">
    <path d="M29.5732 12.0887C29.5725 12.0881 29.5719 12.0875 29.5712 12.0868L17.2465 0.749795C16.7212 0.26633 16.0228 0 15.2798 0C14.5369 0 13.8385 0.266118 13.3129 0.749583L0.994742 12.0809C0.990593 12.0847 0.986444 12.0887 0.982295 12.0926C-0.0964869 13.0907 -0.0946428 14.7101 0.987597 15.7056C1.48204 16.1607 2.13507 16.4242 2.83328 16.4518C2.86163 16.4543 2.89022 16.4556 2.91903 16.4556H3.41025V24.799C3.41025 26.45 4.87052 27.7933 6.66572 27.7933H11.4875C11.9762 27.7933 12.3727 27.4288 12.3727 26.979V20.4378C12.3727 19.6844 13.0388 19.0716 13.8578 19.0716H16.7018C17.5208 19.0716 18.187 19.6844 18.187 20.4378V26.979C18.187 27.4288 18.5833 27.7933 19.0722 27.7933H23.894C25.6892 27.7933 27.1494 26.45 27.1494 24.799V16.4556H27.6049C28.3476 16.4556 29.0461 16.1895 29.5719 15.706C30.6552 14.7088 30.6557 13.0866 29.5732 12.0887Z" fill={isActive('dashboard') ? '#1814F3' : '#B1B1B1'}  />
  </g>
  <defs>
    <clipPath id="clip0_1_309">
      <rect width="30.2133" height="27.7933" fill="white" transform="translate(0.175354)" />
    </clipPath>
  </defs>
</svg><div className='pl-2'>Dashboard</div></Link></li>
      <li className={`font-medium text-xl py-3 ${isActive('administrators') ? 'text-blue' : 'text-grey'}`}><Link to="/administrators"><svg width="31" height="28" viewBox="0 0 31 28" fill="none" xmlns="http://www.w3.org/2000/svg">
  <g clip-path="url(#clip0_1_298)">
    <path d="M14.8913 13.3881C16.8908 13.3881 18.6221 12.7284 20.0368 11.4269C21.4514 10.1256 22.1685 8.53333 22.1685 6.69384C22.1685 4.85499 21.4514 3.26253 20.0365 1.96078C18.6217 0.659672 16.8905 0 14.8913 0C12.8917 0 11.1608 0.659672 9.74614 1.96099C8.33151 3.26231 7.61417 4.85477 7.61417 6.69384C7.61417 8.53333 8.33151 10.1258 9.74637 11.4271C11.1612 12.7282 12.8924 13.3881 14.8913 13.3881Z" fill={isActive('administrators') ? '#1814F3' : '#B1B1B1'}  />
    <path d="M27.6244 21.3716C27.5836 20.83 27.5011 20.2393 27.3796 19.6154C27.257 18.9869 27.0991 18.3928 26.9101 17.8497C26.7148 17.2885 26.4493 16.7342 26.121 16.203C25.7803 15.6517 25.3802 15.1716 24.9311 14.7766C24.4616 14.3633 23.8867 14.031 23.2219 13.7886C22.5594 13.5476 21.8253 13.4254 21.0399 13.4254C20.7315 13.4254 20.4332 13.5418 19.8572 13.8868C19.5027 14.0995 19.088 14.3455 18.6251 14.6175C18.2293 14.8495 17.6932 15.0669 17.0309 15.2636C16.3848 15.456 15.7288 15.5535 15.0813 15.5535C14.4338 15.5535 13.778 15.456 13.1312 15.2636C12.4696 15.0671 11.9334 14.8497 11.5381 14.6177C11.0796 14.3482 10.6647 14.1023 10.3049 13.8866C9.72954 13.5416 9.43104 13.4252 9.12261 13.4252C8.33704 13.4252 7.6031 13.5476 6.94085 13.7889C6.27652 14.0308 5.7014 14.3631 5.23139 14.7768C4.78259 15.172 4.3822 15.6519 4.04197 16.203C3.71395 16.7342 3.44841 17.2882 3.25293 17.8499C3.06415 18.393 2.90625 18.9869 2.78362 19.6154C2.66214 20.2384 2.57962 20.8294 2.53882 21.3722C2.49871 21.904 2.47842 22.456 2.47842 23.0135C2.47842 24.4643 2.97978 25.6388 3.96843 26.505C4.94487 27.3597 6.23687 27.7934 7.80802 27.7934H22.3559C23.9271 27.7934 25.2186 27.36 26.1953 26.505C27.1841 25.6394 27.6855 24.4647 27.6855 23.0132C27.6853 22.4532 27.6648 21.9009 27.6244 21.3716Z" fill={isActive('administrators') ? '#1814F3' : '#B1B1B1'}  />
  </g>
  <defs>
    <clipPath id="clip0_1_298">
      <rect width="30.2133" height="27.7933" fill="white" />
    </clipPath>
  </defs>
</svg><div className='pl-2'>Administrators</div></Link></li>
    <li className={`font-medium text-xl py-3 ${isActive('reports') ? 'text-blue' : 'text-grey'}`}><Link to="/reports"><svg width="31" height="28" viewBox="0 0 31 28" fill="none" xmlns="http://www.w3.org/2000/svg">
  <g clip-path="url(#clip0_1_239)">
    <path d="M29.9538 1.39015L28.7021 0.238633C28.4411 -0.00151837 28.0456 -0.0673102 27.71 0.0727417L20.9867 2.80305C20.7221 2.91379 20.5337 3.13651 20.4819 3.39903C20.4304 3.66225 20.5215 3.9315 20.7269 4.12046L25.734 8.72648C25.9394 8.91544 26.2321 8.9992 26.5183 8.95192C26.8036 8.90421 27.0458 8.73099 27.1661 8.48752L30.1341 2.30266C30.2864 1.99395 30.2148 1.63019 29.9538 1.39015Z" fill={isActive('reports') ? '#1814F3' : '#B1B1B1'}  />
    <path d="M5.71807 17.9272L0.776558 22.4729C-0.258893 23.4254 -0.258893 24.9749 0.776558 25.9274L2.02828 27.0789C3.06373 28.0314 4.74818 28.0314 5.78357 27.0789L10.7251 22.5332L5.71807 17.9272ZM5.16638 24.2036C4.82041 24.5218 4.26046 24.5218 3.91454 24.2036C3.56868 23.8853 3.56868 23.3702 3.91454 23.0521L6.35255 20.8094C6.69852 20.4911 7.25841 20.4911 7.60427 20.8094C7.95025 21.1276 7.95025 21.6426 7.60427 21.9608L5.16638 24.2036Z" fill={isActive('reports') ? '#1814F3' : '#B1B1B1'}  />
    <path d="M14.4804 19.0787L9.47337 14.4726C8.78325 13.8378 7.65993 13.8378 6.96986 14.4726C6.27974 15.1075 6.27974 16.1408 6.96986 16.7756L11.9769 21.3816C12.6669 22.0165 13.7903 22.0165 14.4804 21.3816C15.1706 20.747 15.1706 19.7135 14.4804 19.0787Z" fill={isActive('reports') ? '#1814F3' : '#B1B1B1'} />
    <path d="M23.2306 8.72643L20.727 6.42339L13.8545 12.7454C13.1632 12.1094 12.0424 12.1094 11.351 12.7454L10.7251 13.3212L15.7322 17.9272L16.3581 17.3514C17.0494 16.7154 17.0494 15.6844 16.3581 15.0484L23.2306 8.72643Z" fill={isActive('reports') ? '#1814F3' : '#B1B1B1'}  />
    <path d="M28.6599 19.453C27.223 18.1311 25.4951 17.8469 23.5125 18.1728L19.4883 14.4714L18.5274 15.3554C18.8438 16.4492 18.5361 17.6501 17.6093 18.5026L16.9847 19.0772L19.7541 21.6246C19.4749 23.062 19.5011 24.2812 20.4716 25.6035C21.5915 27.1301 23.5515 27.9977 25.6049 27.7377C25.9444 27.6948 26.2279 27.4782 26.3371 27.1794C26.4462 26.8806 26.362 26.5507 26.1198 26.3278L24.9025 25.2099V22.9072H27.4083L28.6189 24.0209C28.8616 24.2442 29.2212 24.321 29.5463 24.2199C29.8714 24.1187 30.1061 23.8566 30.1518 23.5436C30.3679 22.0626 29.8412 20.5391 28.6599 19.453Z" fill={isActive('reports') ? '#1814F3' : '#B1B1B1'}  />
    <path d="M10.447 6.1505C10.7983 4.34573 10.5139 2.75804 9.05536 1.41539C8.06298 0.502558 6.73525 0 5.31677 0C5.07795 0 4.84168 0.0143309 4.60847 0.0427213C4.26792 0.0843025 3.98284 0.30046 3.87273 0.599835C3.76262 0.899101 3.8467 1.23001 4.08959 1.45345L5.31205 2.5668V4.88548H2.79626L1.5829 3.75366C1.34054 3.53072 0.981583 3.45358 0.656673 3.55417C0.331881 3.65476 0.0969618 3.91586 0.0502847 4.22826C-0.219392 6.03466 0.631476 7.86983 2.3675 8.9479C3.81041 9.84217 5.14369 9.86432 6.6946 9.60712L9.47521 12.1692L10.1001 11.5944C11.0269 10.7418 12.3323 10.4589 13.5213 10.7499L14.4819 9.86627L10.447 6.1505Z" fill={isActive('reports') ? '#1814F3' : '#B1B1B1'}  />
  </g>
  <defs>
    <clipPath id="clip0_1_239">
      <rect width="30.2133" height="27.7933" fill="white" />
    </clipPath>
  </defs>
</svg><div className='pl-2'>Reports</div></Link></li>
<li className={`font-medium text-xl py-3 ${isActive('failures') ? 'text-blue' : 'text-grey'}`}><Link to="/failures">
<svg className='ml-0s' width="35" height="35" viewBox="0 0 35 35" fill="none" xmlns="http://www.w3.org/2000/svg">
  <circle cx="17.5" cy="17.5" r="16.5" transform="rotate(180 17.5 17.5)" stroke="#B1B1B1" stroke-width="2.7" />
  <path d="M17.399 24.351C17.7309 24.683 18.2691 24.683 18.601 24.351L24.0104 18.9417C24.3424 18.6097 24.3424 18.0715 24.0104 17.7396C23.6785 17.4076 23.1403 17.4076 22.8083 17.7396L18 22.5479L13.1917 17.7396C12.8597 17.4076 12.3215 17.4076 11.9896 17.7396C11.6576 18.0715 11.6576 18.6097 11.9896 18.9417L17.399 24.351ZM17.15 10L17.15 23.75L18.85 23.75L18.85 10L17.15 10Z" fill={isActive('failures') ? '#1814F3' : '#B1B1B1'} />
</svg><div className='pl-2'>Failures</div></Link></li>
    </ul>
  </div>
</div>
    );
}
export default Drawer;