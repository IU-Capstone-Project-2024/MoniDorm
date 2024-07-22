import './App.css';
import Navbar from './components/Navbar';
import Drawer from './components/Drawer';
import Home from './pages/Home';
import Administrators from './pages/Administrators';
import Reports from './pages/Reports';
import Failures from './pages/Failures';
import Settings from './pages/Settings';
import Login from './pages/Login';
import { useCookies } from 'react-cookie';
import {
    BrowserRouter as Router,
    Routes,
    Route,
	Navigate
} from "react-router-dom";

// ProtectedRoute component
const ProtectedRoute = ({ children }) => {
	const [cookies] = useCookies(['is_authorized']); // Access cookies
	const isAuthorized = cookies.is_authorized; // Check if 'is_authorized' cookie exists
  
	// If authorized, render the children components, otherwise redirect to login
	return isAuthorized ? children : <Navigate to="/login" replace />;
  };

  function App() {
    return (
        <Router>
            <div>
                {/* <Navbar />
                <Drawer /> */}
                <Routes>
                    <Route path="/" element={<Navigate to="/dashboard" replace />} />
                    <Route path="/dashboard" element={<ProtectedRoute><Home /></ProtectedRoute>} />
                    <Route path="/administrators" element={<ProtectedRoute><Administrators /></ProtectedRoute>} />
                    <Route path="/reports" element={<ProtectedRoute><Reports /></ProtectedRoute>} />
                    <Route path="/failures" element={<ProtectedRoute><Failures /></ProtectedRoute>} />
                    <Route path="/settings" element={<ProtectedRoute><Settings /></ProtectedRoute>} />
                    <Route path="/login" element={<Login />} />
                </Routes>
            </div>
        </Router>
    );
}
 
export default App;
