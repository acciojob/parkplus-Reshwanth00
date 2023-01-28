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
        // need to throw the error
        if(userRepository3.existsById(userId) && parkingLotRepository3.existsById(parkingLotId)){
            User user = userRepository3.findById(userId).get();
            ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();
            Reservation reservation = new Reservation();
            reservation.setNumberOfHours(timeInHours);
            user.getReservationList().add(reservation);
            userRepository3.save(user);
            List<Spot> spotList = parkingLot.getSpotList();
            int max = Integer.MAX_VALUE;
            Spot spot1 = new Spot();
            boolean check = true;
            for(Spot spot:spotList){
                if(numberOfWheels>4 && (spot.getSpotType().equals(SpotType.OTHERS))){
                    if(spot.getPricePerHour()<max){
                        spot1.setPricePerHour(spot.getPricePerHour());
                        max=spot.getPricePerHour();
                        spot1.setSpotType(SpotType.OTHERS);
                        check=false;
                    }
                } else if (numberOfWheels>=4 && (spot.getSpotType().equals(SpotType.FOUR_WHEELER) || (spot.getSpotType().equals(SpotType.OTHERS)))) {
                    if(spot.getPricePerHour()<max){
                        spot1.setPricePerHour(spot.getPricePerHour());
                        max=spot.getPricePerHour();
                        spot1.setSpotType(SpotType.FOUR_WHEELER);
                        check=false;
                    }
                }
                else{
                    if(spot.getPricePerHour()<max){
                        spot1.setPricePerHour(spot.getPricePerHour());
                        max=spot.getPricePerHour();
                        spot1.setSpotType(SpotType.OTHERS);
                        check=false;
                    }
                }
            }
            if(check)throw new Exception( "Cannot make reservation" );
            spot1.getReservationList().add(reservation);
            spotRepository3.save(spot1);
            parkingLotRepository3.save(parkingLot);
            return reservation;
        }
        else{
            throw new Exception("Cannot make reservation");
        }
    }
}
