import { Route, BrowserRouter as Router, Routes } from 'react-router-dom';
import Home from './Components/Home'; // Your home component
import LoginSuccess from './Components/LoginSuccess'; // Component to show after successful login

const App = () => {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/auth/login/success" element={<LoginSuccess />} />
                {/* Add other routes as needed */}
            </Routes>
        </Router>
    );
};

export default App;
