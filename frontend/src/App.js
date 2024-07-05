import './App.css';
import Navbar from './components/Navbar';
import Drawer from './components/Drawer';
import Home from './pages/Home';
import Administrators from './pages/Administrators';
import Reports from './pages/Reports';
import Settings from './pages/Settings';
import {
    BrowserRouter as Router,
    Routes,
    Route,
} from "react-router-dom";
function App() {
    return (
		<Router>
			<div>
				{/* <Navbar />
				<Drawer /> */}
				<Routes>
					<Route path="/" element={<Home />} />
					<Route path="/pages/administrators" element={<Administrators />} />
					<Route path="/pages/reports" element={<Reports />} />
					<Route path="/pages/settings" element={<Settings />} />
				</Routes>
			</div>
		</Router>

    );
}
 
export default App;
