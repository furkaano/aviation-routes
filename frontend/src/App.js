import './App.css';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Sidebar from './components/Sidebar';
import LocationsPage from "./pages/LocationsPage";
import TransportationsPage from "./pages/TransportationsPage";

function App() {
  return (
      <Router>
        <div style={{ display: 'flex' }}>
          <Sidebar />
          <div style={{ flex: 1, padding: '10px' }}>
            <Routes>
                <Route path="/locations" element={<LocationsPage />} />
                <Route path="/transportations" element={<TransportationsPage />} />
                <Route path="/" element={<h2>Hoş geldiniz!</h2>} />
                <Route path="*" element={<h2>404 - Sayfa Bulunamadı</h2>} />
            </Routes>
          </div>
        </div>
      </Router>
  );
}

export default App;
