import './App.css';
function App() {
	
	return (
		<div>
			<div class="navbar bg-base-100">
				<div class="flex-none">
					<div class="drawer">
						<input id="my-drawer" type="checkbox" class="drawer-toggle" />
						<div class="drawer-content">
							<label for="my-drawer" class="btn btn-square btn-ghost drawer-button">
								<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" class="inline-block w-5 h-5 stroke-current"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"></path></svg>
							</label>
						</div>
						<div class="drawer-side">
				<label for="my-drawer" aria-label="close sidebar" class="drawer-overlay"></label>
				<ul class="menu p-4 w-80 min-h-full bg-base-200 text-base-content">
					<li>Element 1</li>
					<li>Element 2</li>
				</ul>
			</div>
				</div>
			</div>
			<div class="flex-1"></div>
			<div class="flex-none">
				<label className="flex cursor-pointer gap-2 items-center justify-center">
  					<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><circle cx="12" cy="12" r="5"/><path d="M12 1v2M12 21v2M4.2 4.2l1.4 1.4M18.4 18.4l1.4 1.4M1 12h2M21 12h2M4.2 19.8l1.4-1.4M18.4 5.6l1.4-1.4"/></svg>
  					<input type="checkbox" value="dark" className="toggle theme-controller"/>
  					<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"></path></svg>
				</label>
			</div>
		</div>

		<div className="App flex items-center justify-center p-2">
			<iframe src="http://10.90.138.215:3000/d-solo/fdpa1canapou8a/monidorm-dashboard?orgId=1&from=1718219692383&to=1718824492383&panelId=1" width="650" height="400" frameborder="0"></iframe>
		</div>

		<div className="App flex items-center justify-center p-2">
			<iframe src="http://10.90.138.215:3000/d-solo/fdpa1canapou8a/monidorm-dashboard?orgId=1&from=1718745134263&to=1719349934263&panelId=2" width="650" height="400" frameborder="0"></iframe>
		</div>


			{/* <label className="cursor-pointer grid place-items-center">
  				<input type="checkbox" value="dark" className="toggle theme-controller bg-base-content row-start-1 col-start-1 col-span-2"/>
  				<svg className="col-start-1 row-start-1 stroke-base-100 fill-base-100" xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><circle cx="12" cy="12" r="5"/><path d="M12 1v2M12 21v2M4.2 4.2l1.4 1.4M18.4 18.4l1.4 1.4M1 12h2M21 12h2M4.2 19.8l1.4-1.4M18.4 5.6l1.4-1.4"/></svg>
  				<svg className="col-start-2 row-start-1 stroke-base-100 fill-base-100" xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"></path></svg>
			</label> */}
	</div>

	);
}

export default App;
