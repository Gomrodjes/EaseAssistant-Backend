package com.example.demo.services.implement;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Address;
import com.example.demo.entities.Booking;
import com.example.demo.entities.Job;
import com.example.demo.entities.User;
import com.example.demo.entities.UserServiceAssignment;
import com.example.demo.enums.StateBooking;
import com.example.demo.enums.UserRole;
import com.example.demo.models.booking.BookingCancellationDTO;
import com.example.demo.models.booking.BookingResponseDTO;
import com.example.demo.models.booking.BookingSaveDTO;
import com.example.demo.models.booking.BookingUpdateDTO;
import com.example.demo.repositories.AddressRepository;
import com.example.demo.repositories.BookingRepository;
import com.example.demo.repositories.JobRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.repositories.UserServiceAssignmentRepository;
import com.example.demo.services.BookingService;

@Service("bookingService")
public class BookingServiceImpl implements BookingService {

    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    @Qualifier("bookingRepository")
    private BookingRepository bookingRepository;

    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;

    @Autowired
    @Qualifier("jobRepository")
    private JobRepository jobRepository;

    @Autowired
    @Qualifier("addressRepository")
    private AddressRepository addressRepository;

    @Autowired
    @Qualifier("userServiceAssignmentRepository")
    private UserServiceAssignmentRepository userServiceAssignmentRepository;

    @Override
    public BookingResponseDTO saveBooking(BookingSaveDTO bookingSaveDTO) {
        User customer = userRepository.findById(bookingSaveDTO.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer does not exist"));

        User worker = userRepository.findById(bookingSaveDTO.getWorkerId())
                .orElseThrow(() -> new IllegalArgumentException("Worker does not exist"));

        Job job = jobRepository.findById(bookingSaveDTO.getJobId())
                .orElseThrow(() -> new IllegalArgumentException("Service does not exist"));

        Address address = addressRepository.findById(bookingSaveDTO.getAddressId())
                .orElseThrow(() -> new IllegalArgumentException("Address does not exist"));

        if (customer.getRole() != UserRole.CLIENT) {
            throw new IllegalArgumentException("Customer must have CLIENT role");
        }

        if (worker.getRole() != UserRole.ASSISTANT) {
            throw new IllegalArgumentException("Worker must have ASSISTANT role");
        }

        if (customer.getId().equals(worker.getId())) {
            throw new IllegalArgumentException("Customer and worker must be different users");
        }

        if (address.getUser() == null || !address.getUser().getId().equals(customer.getId())) {
            throw new IllegalArgumentException("Address must belong to the customer");
        }

        UserServiceAssignment assignment = userServiceAssignmentRepository
                .findByUserIdAndCategoryId(worker.getId(), job.getCategory().getId())
                .orElseThrow(() -> new IllegalArgumentException("Worker is not assigned to this service"));

        if (!assignment.isActive()) {
            throw new IllegalArgumentException("Worker assignment for this service is inactive");
        }

        Booking booking = new Booking();
        booking.setDateBooking(bookingSaveDTO.getDateBooking());
        booking.setStartTime(bookingSaveDTO.getStartTime());
        booking.setEndTime(calculateEndTime(bookingSaveDTO.getStartTime(), job.getDurationMinutes()));
        booking.setTotalPrice(job.getPrice());
        booking.setState(StateBooking.WAITING);
        booking.setClientNote(bookingSaveDTO.getClientNote());
        booking.setCancellationReason(null);
        booking.setCustomer(customer);
        booking.setWorker(worker);
        booking.setJob(job);
        booking.setAddress(address);

        validateNoConflicts(booking, null);

        Booking savedBooking = bookingRepository.save(booking);
        return toResponseDTO(savedBooking);
    }

    @Override
    public BookingResponseDTO updateBooking(Long bookingId, BookingUpdateDTO bookingUpdateDTO) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking does not exist to update"));

        if (booking.getState() != StateBooking.WAITING) {
            throw new IllegalArgumentException("Only waiting bookings can be updated");
        }

        Address address = addressRepository.findById(bookingUpdateDTO.getAddressId())
                .orElseThrow(() -> new IllegalArgumentException("Address does not exist"));

        booking.setDateBooking(bookingUpdateDTO.getDateBooking());
        booking.setStartTime(bookingUpdateDTO.getStartTime());
        booking.setEndTime(calculateEndTime(bookingUpdateDTO.getStartTime(), booking.getJob().getDurationMinutes()));
        booking.setClientNote(bookingUpdateDTO.getClientNote());
        booking.setAddress(address);
        booking.setTotalPrice(booking.getJob().getPrice());

        validateNoConflicts(booking, booking.getId());

        Booking updatedBooking = bookingRepository.save(booking);
        return toResponseDTO(updatedBooking);
    }

    @Override
    public void cancellationBooking(Long id, BookingCancellationDTO bookingCancellationDTO) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking does not exist to cancel"));

        if (booking.getState() == StateBooking.CANCELED) {
            throw new IllegalArgumentException("Booking is already canceled");
        }

        if (booking.getState() == StateBooking.COMPLETED) {
            throw new IllegalArgumentException("Completed bookings cannot be canceled");
        }

        booking.setState(StateBooking.CANCELED);
        booking.setCancellationReason(bookingCancellationDTO.getCancellationReason());
        bookingRepository.save(booking);
    }

    @Override
    public BookingResponseDTO getBookingById(Long Id) {
        Booking booking = bookingRepository.findById(Id).orElse(null);
        if (booking == null) {
            return null;
        }
        return toResponseDTO(booking);
    }

    @Override
    public List<BookingResponseDTO> getAllBookingByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User does not exist"));

        List<BookingResponseDTO> bookings = new ArrayList<>();
        List<Booking> userBookings;

        if (user.getRole() == UserRole.ADMIN) {
            userBookings = bookingRepository.findAll();
        } else {
            userBookings = bookingRepository.findByCustomer_IdOrWorker_Id(userId, userId);
        }

        userBookings.sort(Comparator
                .comparing(Booking::getDateBooking)
                .thenComparing(Booking::getStartTime));

        for (Booking booking : userBookings) {
            bookings.add(toResponseDTO(booking));
        }

        return bookings;
    }

    private BookingResponseDTO toResponseDTO(Booking booking) {
        BookingResponseDTO response = modelMapper.map(booking, BookingResponseDTO.class);
        response.setAddressId(booking.getAddress().getId());
        return response;
    }

    private LocalTime calculateEndTime(LocalTime startTime, int durationMinutes) {
        return startTime.plusMinutes(durationMinutes);
    }

    private void validateNoConflicts(Booking booking, Long bookingIdToExclude) {
        for (Booking existingBooking : bookingRepository.findByDateBookingAndStateNot(booking.getDateBooking(),
                StateBooking.CANCELED)) {
            if (bookingIdToExclude != null && bookingIdToExclude.equals(existingBooking.getId())) {
                continue;
            }

            boolean sameWorker = existingBooking.getWorker() != null
                    && existingBooking.getWorker().getId().equals(booking.getWorker().getId());
            boolean sameCustomer = existingBooking.getCustomer() != null
                    && existingBooking.getCustomer().getId().equals(booking.getCustomer().getId());

            if ((sameWorker || sameCustomer) && timeRangesOverlap(
                    booking.getStartTime(),
                    booking.getEndTime(),
                    existingBooking.getStartTime(),
                    existingBooking.getEndTime())) {
                throw new IllegalArgumentException("Booking time overlaps with another booking for this user");
            }
        }
    }

    private boolean timeRangesOverlap(LocalTime startA, LocalTime endA, LocalTime startB, LocalTime endB) {
        return startA.isBefore(endB) && endA.isAfter(startB);
    }
}
