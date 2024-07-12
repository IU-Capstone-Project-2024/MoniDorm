import './App.css';
import Navbar from './components/Navbar';
import Drawer from './components/Drawer';
import Home from './pages/Home';
import Administrators from './pages/Administrators';
import Reports from './pages/Reports';
import Failures from './pages/Failures';
import Settings from './pages/Settings';
import {
    BrowserRouter as Router,
    Routes,
    Route,
	Navigate
} from "react-router-dom";
function App() {
    return (
		<Router>
			<div>
				{/* <Navbar />
				<Drawer /> */}
				<Routes>
					<Route path="/" element={<Navigate to="/dashboard" replace />} />
					<Route path="/dashboard" element={<Home />} />
					<Route path="/administrators" element={<Administrators />} />
					<Route path="/reports" element={<Reports />} />
					<Route path="/failures" element={<Failures />} />
					<Route path="/settings" element={<Settings />} />
				</Routes>
			</div>
		</Router>

    );
}
 
export default App;
