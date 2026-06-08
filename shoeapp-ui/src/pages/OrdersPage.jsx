import { useEffect, useState } from "react";

const OrdersPage = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Call your backend API
    fetch("http://localhost:8080/api/orders")
      .then(res => res.json())
      .then(data => setOrders(data))
      .catch(err => console.error("Error fetching orders:", err))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <p>Loading orders...</p>;

  return (
    <div style={{ padding: "2rem" }}>
      <h2>Orders</h2>
      {orders.length === 0 ? (
        <p>No orders found.</p>
      ) : (
        <ul>
          {orders.map(order => (
            <li key={order.id}>
              <strong>{order.customerName}</strong> — {order.productName} ({order.quantity})
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default OrdersPage;