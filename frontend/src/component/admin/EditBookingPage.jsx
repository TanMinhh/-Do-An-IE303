import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import ApiService from '../../service/ApiService'; // Assuming your service is in a file called ApiService.js

const EditBookingPage = () => {
    const navigate = useNavigate();
    const { bookingCode } = useParams();
    const [bookingDetails, setBookingDetails] = useState(null); // State variable for booking details
    const [error, setError] = useState(null); // Track any errors
    const [success, setSuccessMessage] = useState(null); // Track any errors



    useEffect(() => {
        const fetchBookingDetails = async () => {
            try {
                const response = await ApiService.getBookingByConfirmationCode(bookingCode);
                setBookingDetails(response.booking);
            } catch (error) {
                setError(error.message);
            }
        };

        fetchBookingDetails();
    }, [bookingCode]);


    const achieveBooking = async (bookingId) => {
        if (!window.confirm('Are you sure you want to Achieve this booking?')) {
            return; // Do nothing if the user cancels
        }

        try {
            const response = await ApiService.cancelBooking(bookingId);
            if (response.statusCode === 200) {
                setSuccessMessage("The booking was successfully Achieved")
                
                setTimeout(() => {
                    setSuccessMessage('');
                    navigate('/admin/manage-bookings');
                }, 3000);
            }
        } catch (error) {
            setError(error.response?.data?.message || error.message);
            setTimeout(() => setError(''), 5000);
        }
    };

    return (
        <div className="find-booking-page">
            <h2>Booking Detail</h2>
            {error && <p className='error-message'>{error}</p>}
            {success && <p className='success-message'>{success}</p>}
            {bookingDetails && (
                <div className="booking-details">
                    <h3>Booking Details</h3>
                    <p>Confirmation Code: {bookingDetails.bookingConfirmationCode}</p>
                    <p>Check-in Date: {bookingDetails.checkInDate}</p>
                    <p>Check-out Date: {bookingDetails.checkOutDate}</p>
                    <p>Num Of Adults: {bookingDetails.numOfAdults}</p>
                    <p>Num Of Children: {bookingDetails.numOfChildren}</p>
                    <p>Guest Email: {bookingDetails.user.email}</p>
                    <p><strong>Payment Status:</strong> {bookingDetails.paymentStatus || 'PENDING'}</p>
                    <div style={{ margin: '10px 0' }}>
                        <button onClick={async () => {
                            try {
                                await ApiService.updatePaymentStatus(bookingDetails.id, 'PAID');
                                setSuccessMessage('Payment status set to PAID');
                                setTimeout(() => setSuccessMessage(''), 2000);
                                // Refresh booking details
                                const response = await ApiService.getBookingByConfirmationCode(bookingCode);
                                setBookingDetails(response.booking);
                            } catch (error) {
                                setError(error.response?.data?.message || 'Failed to update payment status. Please try again.');
                                setTimeout(() => setError(''), 5000);
                            }
                        }}>Set Paid</button>
                        <button onClick={async () => {
                            try {
                                await ApiService.updatePaymentStatus(bookingDetails.id, 'FAILED');
                                setSuccessMessage('Payment status set to FAILED');
                                setTimeout(() => setSuccessMessage(''), 2000);
                                const response = await ApiService.getBookingByConfirmationCode(bookingCode);
                                setBookingDetails(response.booking);
                            } catch (error) {
                                setError(error.response?.data?.message || 'Failed to update payment status. Please try again.');
                                setTimeout(() => setError(''), 5000);
                            }
                        }} style={{ marginLeft: 8 }}>Set Failed</button>
                        <button onClick={async () => {
                            try {
                                await ApiService.updatePaymentStatus(bookingDetails.id, 'PENDING');
                                setSuccessMessage('Payment status set to PENDING');
                                setTimeout(() => setSuccessMessage(''), 2000);
                                const response = await ApiService.getBookingByConfirmationCode(bookingCode);
                                setBookingDetails(response.booking);
                            } catch (error) {
                                setError(error.response?.data?.message || 'Failed to update payment status. Please try again.');
                                setTimeout(() => setError(''), 5000);
                            }
                        }} style={{ marginLeft: 8 }}>Set Pending</button>
                    </div>

                    <br />
                    <hr />
                    <br />
                    <h3>Booker Details</h3>
                    <div>
                        <p> Name: {bookingDetails.user.name}</p>
                        <p> Email: {bookingDetails.user.email}</p>
                        <p> Phone Number: {bookingDetails.user.phoneNumber}</p>
                    </div>

                    <br />
                    <hr />
                    <br />
                    <h3>Room Details</h3>
                    <div>
                        <p> Room Type: {bookingDetails.room.roomType}</p>
                        <p> Room Price: {bookingDetails.room.roomPrice.toLocaleString('vi-VN')} VND</p>
                        <p> Room Description: {bookingDetails.room.roomDescription}</p>
                        <img src={bookingDetails.room.roomPhotoUrl} alt="" sizes="" srcSet="" />
                    </div>
                    <button
                        className="achieve-booking"
                        onClick={() => achieveBooking(bookingDetails.id)}>Achieve Booking
                    </button>
                </div>
            )}
        </div>
    );
};

export default EditBookingPage;