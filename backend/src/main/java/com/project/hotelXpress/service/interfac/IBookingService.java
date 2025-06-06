package com.project.hotelXpress.service.interfac;

import com.project.hotelXpress.dto.Response;
import com.project.hotelXpress.entity.Booking;

public interface IBookingService {

    Response saveBooking(Long roomId, Long userId, Booking bookingRequest);
    Response findBookingByConfirmationCode(String confirmationCode);
    Response getAllBookings();
    Response cancelBooking(Long bookingId);
    Response updatePaymentStatus(Long bookingId, Booking.PaymentStatus status);
}
