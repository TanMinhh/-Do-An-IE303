import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import ApiService from '../../service/ApiService';
    import './AdminPage.css';
const AdminPage = () => {
    const [adminName, setAdminName] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchAdminName = async () => {
            try {
                const response = await ApiService.getUserProfile();
                setAdminName(response.user.name);
            } catch (error) {
                console.error('Error fetching admin details:', error.message);
            }
        };

        fetchAdminName();
    }, []);

    const handleLogout = () => {
        localStorage.removeItem("token");
        navigate("/login");
    };

    return (
        <div className="admin-page ">
            <div ></div>
            <h1 className="text-3xl font-bold mb-6">Welcome back, {adminName} 👋</h1>

            <div className="grid grid-cols-2 gap-6">
                <button onClick={() => navigate('/admin/manage-rooms')} className="btn-admin">
                    🏨 Manage Rooms
                </button>
                <button onClick={() => navigate('/admin/manage-bookings')} className="btn-admin">
                    📅 Manage Bookings
                </button>
                <button onClick={() => navigate('/admin/manage-users')} className="btn-admin">
                    👤 Manage Users
                </button>

            </div>
        </div>
    );
};

export default AdminPage;
