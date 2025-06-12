import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import ApiService from '../../service/ApiService';
import './ProfilePage.css';

const ProfilePage = () => {
    const [user, setUser] = useState(null);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);
    const navigate = useNavigate();
    const location = useLocation();

    useEffect(() => {
        const fetchUserProfile = async () => {
            try {
                const response = await ApiService.getUserProfile();
                // Fetch user bookings using the fetched user ID
                const userPlusBookings = await ApiService.getUserBookings(response.user.id);
                setUser(userPlusBookings.user)
            } catch (error) {
                setError(error.response?.data?.message || error.message);
            }
        };

        fetchUserProfile();

        // Check for payment status in URL
        const searchParams = new URLSearchParams(location.search);
        const paymentStatus = searchParams.get('payment_status');
        
        if (paymentStatus) {
            if (paymentStatus === 'success') {
                setSuccess('Payment successful! Your booking has been confirmed.');
            } else if (paymentStatus === 'failed') {
                setError('Payment failed. Please try again.');
            } else if (paymentStatus === 'error') {
                setError('An error occurred during payment. Please contact support.');
            }
            
            // Remove the payment_status from URL
            window.history.replaceState({}, document.title, '/profile');
        }
    }, [location]);

    const handleLogout = () => {
        ApiService.logout();
        navigate('/home');
    };

    const handleEditProfile = () => {
        navigate('/edit-profile');
    };

    const handleCancelAndBookAgain = async (bookingId) => {
        if (!window.confirm('Are you sure you want to cancel this booking and book again?')) {
            return;
        }

        try {
            const response = await ApiService.cancelBooking(bookingId);
            if (response.statusCode === 200) {
                setSuccess('Booking cancelled successfully. Redirecting to rooms page...');
                // Refresh user data
                const userResponse = await ApiService.getUserProfile();
                const userPlusBookings = await ApiService.getUserBookings(userResponse.user.id);
                setUser(userPlusBookings.user);
                
                // Navigate to rooms page after a short delay
                setTimeout(() => {
                    navigate('/rooms');
                }, 2000);
            }
        } catch (error) {
            setError(error.response?.data?.message || 'Failed to cancel booking. Please try again.');
            setTimeout(() => setError(''), 5000);
        }
    };

    return (
        <div className="profile-page">
            {user && <h2>Welcome, {user.name}</h2>}
            <div className="profile-actions">
                <button className="edit-profile-button" onClick={handleEditProfile}>Edit Profile</button>
                <button className="logout-button" onClick={handleLogout}>Logout</button>
            </div>
            {error && <p className="error-message">{error}</p>}
            {success && <p className="success-message">{success}</p>}
            {user && (
                <div className="profile-details">
                    <h3>My Profile Details</h3>
                    <p><strong>Email:</strong> {user.email}</p>
                    <p><strong>Phone Number:</strong> {user.phoneNumber}</p>
                </div>
            )}
            <div className="bookings-section">
                <h3>My Booking History</h3>
                <div className="booking-list">
                    {user && user.bookings.length > 0 ? (
                        user.bookings.map((booking) => (
                            <div key={booking.id} className="booking-item">
                                <div className="booking-item-info">
                                    <p><strong>Booking Code:</strong> {booking.bookingConfirmationCode}</p>
                                    <p><strong>Check-in Date:</strong> {booking.checkInDate}</p>
                                    <p><strong>Check-out Date:</strong> {booking.checkOutDate}</p>
                                    <p><strong>Total Guests:</strong> {booking.totalNumOfGuest}</p>
                                    <p><strong>Room Type:</strong> {booking.room.roomType}</p>
                                    <p><strong>Payment Status:</strong> {booking.paymentStatus || 'PENDING'}</p>
                                    {booking.paymentStatus === 'FAILED' && (
                                        <button 
                                            className="cancel-and-book-again-button"
                                            onClick={() => handleCancelAndBookAgain(booking.id)}
                                        >
                                            Cancel and Book Again
                                        </button>
                                    )}
                                </div>
                                <div className="booking-item-image">
                                    <img src={booking.room.roomPhotoUrl} alt="Room" className="room-photo" />
                                </div>
                            </div>
                        ))
                    ) : (
                        <p>No bookings found.</p>
                    )}
                </div>
            </div>
        </div>
    );
};

export default ProfilePage;