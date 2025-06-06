package com.project.hotelXpress.service.impl;

import com.project.hotelXpress.dto.BookingDTO;
import com.project.hotelXpress.dto.Response;
import com.project.hotelXpress.entity.Booking;
import com.project.hotelXpress.entity.Room;
import com.project.hotelXpress.entity.User;
import com.project.hotelXpress.exception.OurException;
import com.project.hotelXpress.repo.*;
import com.project.hotelXpress.service.interfac.IBookingService;
import com.project.hotelXpress.service.interfac.IRoomService;
import com.project.hotelXpress.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service

public class BookingService implements IBookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private IRoomService roomService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Response saveBooking(Long roomId, Long userId, Booking bookingRequest) {

        Response response = new Response();

        try {
            if (bookingRequest.getCheckInDate().isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Cannot book a room for a past date");
            }
            if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
                throw new IllegalArgumentException("Check in date must be after check out date");
            }
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room not found"));
            User user = userRepository.findById(userId).orElseThrow(() -> new OurException("User not found"));

            List<Booking> existingBookings = room.getBookings();

            if (!roomIsAvailable(bookingRequest, existingBookings)){
                throw new OurException("Room is not available for selected date range!");
            }

            bookingRequest.setRoom(room);
            bookingRequest.setUser(user);
            String bookingConfirmationCode = Utils.generateRandomConfirmationCode(10);
            bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);
            bookingRepository.save(bookingRequest);
            response.setStatusCode(200);
            response.setMessage("Booking successful");
            response.setBookingConfirmationCode(bookingConfirmationCode);
        } catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error saving a booking: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response findBookingByConfirmationCode(String confirmationCode) {

        Response response = new Response();

        try {
            Booking booking = bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(() -> new OurException("Booking not found"));
            BookingDTO bookingDTO = Utils.mapBookingEntityToBookingDTOPlusBookedRooms(booking, true);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setBooking(bookingDTO);
        } catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error finding a booking: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllBookings() {

        Response response = new Response();

        try {
            List<Booking> bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<BookingDTO> bookingDTOList = Utils.mapBookingListEntityToBookingListDTO(bookingList);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setBookingList(bookingDTOList);
        } catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting all booking: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response cancelBooking(Long bookingId) {

        Response response = new Response();

        try {
            Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new OurException("Booking not found"));
            bookingRepository.deleteById(bookingId);
            response.setStatusCode(200);
            response.setMessage("Canceling successful");
        } catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error canceling all booking: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updatePaymentStatus(Long bookingId, Booking.PaymentStatus status) {
        Response response = new Response();
        try {
            Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new OurException("Booking not found"));
            booking.setPaymentStatus(status);
            bookingRepository.save(booking);
            response.setStatusCode(200);
            response.setMessage("Payment status updated successfully");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating payment status: " + e.getMessage());
        }
        return response;
    }

    private boolean roomIsAvailable(Booking bookingRequest, List<Booking> existingBookings) {
        return existingBookings.stream().noneMatch(existingBooking ->
                bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                        || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())

                        || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                        && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))

                        || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())
                        && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))

                        || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())
                        && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                        || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                        && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                        || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                        && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
        );
    }
}
