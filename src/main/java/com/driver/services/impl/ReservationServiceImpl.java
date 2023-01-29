package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
//        if(parkingLotRepository3.existsById(parkingLotId) && userRepository3.existsById(userId)) {
//            List<Spot> spotList = parkingLotRepository3.findById(parkingLotId).get().getSpotList();
//            int totalPrice = Integer.MAX_VALUE;
//            boolean availableToBook = false;
//            int spotId = -1;
//            for (Spot spot : spotList) {
//                if (spot.getOccupied() == false) {
//                    if (spot.getSpotType().equals(SpotType.OTHERS) && numberOfWheels > 4) {
//                        availableToBook = true;
//                        if (spot.getPricePerHour() * timeInHours < totalPrice) {
//                            totalPrice = spot.getPricePerHour() * timeInHours;
//                            spotId = spot.getId();
//                        }
//
//                    } else if (numberOfWheels > 2 && (spot.getSpotType().equals(SpotType.FOUR_WHEELER) || spot.getSpotType().equals(SpotType.OTHERS))) {
//                        availableToBook = true;
//                        if (spot.getPricePerHour() * timeInHours < totalPrice) {
//                            totalPrice = spot.getPricePerHour() * timeInHours;
//                            spotId = spot.getId();
//                        }
//                    } else {
//                        availableToBook = true;
//                        if (spot.getPricePerHour() * timeInHours < totalPrice) {
//                            totalPrice = spot.getPricePerHour() * timeInHours;
//                            spotId = spot.getId();
//                        }
//
//                    }
//
//                }
//            }
//            if(spotId!=-1){
//                Spot spot = spotRepository3.findById(spotId).get();
//                User user = userRepository3.findById(userId).get();
//                Reservation reservation = new Reservation();
//                reservation.setSpot(spot);
//                reservation.setUser(user);
//                spot.setOccupied(true);
//                reservation.setNumberOfHours(timeInHours);
//                spot.getReservationList().add(reservation);
//                user.getReservationList().add(reservation);
//                userRepository3.save(user);
//                spotRepository3.save(spot);
//                reservationRepository3.save(reservation);
//                return reservation;
//
//            }else throw new Exception("Cannot make reservation");
//        }
//        else{
//            throw new Exception("Cannot make reservation");
//        }
        try {
            if (!userRepository3.findById(userId).isPresent() || !parkingLotRepository3.findById(parkingLotId).isPresent()) {
                throw new Exception("Cannot make reservation");
            }
            User user = userRepository3.findById(userId).get();
            ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();

            List<Spot> spotList = parkingLot.getSpotList();
            boolean available = false;
            for (Spot spot : spotList) {
                if (!spot.getOccupied()) {
                    available = true;
                    break;
                }
            }
            if (!available) {
                throw new Exception("Cannot make reservation");
            }

            available = false;
            int minPrice = Integer.MAX_VALUE;

            SpotType requestedSpotType;
            if (numberOfWheels > 4) {
                requestedSpotType = SpotType.OTHERS;
            } else if (numberOfWheels > 2) {
                requestedSpotType = SpotType.FOUR_WHEELER;
            } else {
                requestedSpotType = SpotType.TWO_WHEELER;
            }

            Spot spotBooked = null;
            for (Spot spot : spotList) {
                if (requestedSpotType.equals(SpotType.OTHERS) && spot.getSpotType().equals(SpotType.OTHERS)) {
                    if (spot.getPricePerHour() * timeInHours < minPrice && !spot.getOccupied()) {
                        minPrice = spot.getPricePerHour() * timeInHours;
                        available = true;
                        spotBooked = spot;
                    }
                } else if (requestedSpotType.equals(SpotType.FOUR_WHEELER) && spot.getSpotType().equals(SpotType.OTHERS) ||
                        spot.getSpotType().equals(SpotType.FOUR_WHEELER)) {
                    if (spot.getPricePerHour() * timeInHours < minPrice && !spot.getOccupied()) {
                        minPrice = spot.getPricePerHour() * timeInHours;
                        available = true;
                        spotBooked = spot;
                    }
                } else if (requestedSpotType.equals(SpotType.TWO_WHEELER) && spot.getSpotType().equals(SpotType.OTHERS) ||
                        spot.getSpotType().equals(SpotType.FOUR_WHEELER) || spot.getSpotType().equals(SpotType.TWO_WHEELER)) {
                    if (spot.getPricePerHour() * timeInHours < minPrice && !spot.getOccupied()) {
                        minPrice = spot.getPricePerHour() * timeInHours;
                        available = true;
                        spotBooked = spot;
                    }
                }

            }
            if (!available) {
                throw new Exception("Cannot make reservation");
            }
            spotBooked.setOccupied(true);
            Reservation reservation = new Reservation(timeInHours);
            reservation.setSpot(spotBooked);
            reservation.setUser(user);

            spotBooked.getReservationList().add(reservation);
            user.getReservationList().add(reservation);

            userRepository3.save(user);
            spotRepository3.save(spotBooked);


            return reservation;

        } catch (Exception e) {
            return null;
        }
    }
}
