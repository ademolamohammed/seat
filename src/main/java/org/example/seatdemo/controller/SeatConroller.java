package org.example.seatdemo.controller;

import org.example.seatdemo.entity.SeatEntity;
import org.example.seatdemo.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class SeatConroller {

    @Autowired
    private SeatRepository seatRepository;

    private final String[] seatCodes = {
            "1A", "2A", "3A", "4A", "1B", "2B", "3B", "4B",
            "1C", "2C", "3C", "4C", "1D", "2D", "3D", "4D",
            "1E", "2E", "3E", "4E"
    };


    @GetMapping("/")
    public String home(Model model) {
        List<SeatEntity> reservedSeats = seatRepository.findAll();
        model.addAttribute("seats", seatCodes);
        model.addAttribute("reservedSeats", reservedSeats);
        model.addAttribute("remainingSeats", seatCodes.length - reservedSeats.size());
        model.addAttribute("seatz", reservedSeats);
        model.addAttribute("newReservation", new SeatEntity());
        return "seats";
    }


    @PostMapping("/reserve")
    public String reserveSeat(@RequestParam("seatCode") String seatCode,
                              @RequestParam("customerName") String customerName,
                              @RequestParam("tdate") @DateTimeFormat(pattern = "yyyy-MM-dd") String tdate,
                              Model model) {

        // Check if the seat is already reserved
        Optional<SeatEntity> existingSeat = seatRepository.findBySeatCode(seatCode);
        if (existingSeat.isPresent()) {
            model.addAttribute("message", "Seat code already reserved. Please choose another.");
            return "seats"; // Return to the same view with error message
        }

        SeatEntity seat = new SeatEntity();
        seat.setSeatCode(seatCode);
        seat.setCustomerName(customerName);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date parsedDate = dateFormat.parse(tdate);
            seat.setTdate(parsedDate);
        } catch (ParseException e) {
            seat.setTdate(new Date()); // Default to today's date if parsing fails
        }

        seatRepository.save(seat);
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deleteSeat(@RequestParam("id") Long id) {
        seatRepository.deleteById(id);
        return "redirect:/";
    }


    @GetMapping("/edit/{id}")
    public String editSeat(@PathVariable("id") Long id, Model model) {
        Optional<SeatEntity> seat = seatRepository.findById(id);
        if (seat.isPresent()) {
            model.addAttribute("editSeat", seat.get());
        }
        return "edit";
    }

    @PostMapping("/update")
    public String updateSeat(@RequestParam("id") Long id,
                             @RequestParam("seatCode") String seatCode,
                             @RequestParam("customerName") String customerName,
                             @RequestParam("tdate") @DateTimeFormat(pattern = "yyyy-MM-dd") String tdate,
                             Model model) {

        Optional<SeatEntity> optionalSeat = seatRepository.findById(id);
        if (optionalSeat.isPresent()) {
            SeatEntity seat = optionalSeat.get();
            seat.setSeatCode(seatCode);
            seat.setCustomerName(customerName);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date parsedDate = dateFormat.parse(tdate);
                seat.setTdate(parsedDate);
            } catch (ParseException e) {
                seat.setTdate(new Date());
            }

            seatRepository.save(seat);
        }
        return "redirect:/";


    }
}