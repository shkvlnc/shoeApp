import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import OrdersPage from "./pages/OrdersPage";
import PaymentsPage from "./pages/PaymentsPage";
import AdminPage from "./pages/AdminPage";

function App() {
  return (
    <Router>
      <nav style={{ padding: "1rem", background: "#eee" }}>
        <Link to="/orders">Orders</Link> |{" "}
        <Link to="/payments">Payments</Link> |{" "}
        <Link to="/admin">Admin</Link>
      </nav>
      <Routes>
        <Route path="/orders" element={<OrdersPage />} />
        <Route path="/payments" element={<PaymentsPage />} />
        <Route path="/admin" element={<AdminPage />} />
      </Routes>
    </Router>
  );
}

export default App;
