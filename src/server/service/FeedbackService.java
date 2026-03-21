package com.carrental.server.service;

import com.carrental.server.model.Feedback;
import com.carrental.server.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    /**
     * Creare feedback nou
     */
    public Feedback createFeedback(Feedback feedback) {
        // Validare rating
        if (feedback.getRating() != null && (feedback.getRating() < 1 || feedback.getRating() > 5)) {
            throw new RuntimeException("Rating-ul trebuie să fie între 1 și 5 stele");
        }

        System.out.println("📝 Feedback nou salvat:");
        System.out.println("   De la: " + (feedback.getClient() != null ? feedback.getClient().getUsername() : "N/A"));
        System.out.println("   Către: " + (feedback.getEmployee() != null ? feedback.getEmployee().getUsername() : "N/A"));
        System.out.println("   Rating: " + feedback.getRating() + " ⭐");
        System.out.println("   Mesaj: " + feedback.getMessage());

        return feedbackRepository.save(feedback);
    }

    /**
     * Actualizare feedback existent
     */
    public Feedback updateFeedback(Long id, Feedback feedbackDetails) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));

        if (feedbackDetails.getMessage() != null) {
            feedback.setMessage(feedbackDetails.getMessage());
        }

        if (feedbackDetails.getRating() != null) {
            if (feedbackDetails.getRating() < 1 || feedbackDetails.getRating() > 5) {
                throw new RuntimeException("Rating-ul trebuie să fie între 1 și 5 stele");
            }
            feedback.setRating(feedbackDetails.getRating());
        }

        return feedbackRepository.save(feedback);
    }

    /**
     * Ștergere feedback
     */
    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }

    /**
     * Obține feedback după ID
     */
    public Optional<Feedback> getFeedbackById(Long id) {
        return feedbackRepository.findById(id);
    }

    /**
     * Obține tot feedback-ul
     */
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }

    /**
     * Obține feedback-ul trimis DE un client (employee = destinatar)
     */
    public List<Feedback> getFeedbackByClientId(Long clientId) {
        return feedbackRepository.findAll().stream()
                .filter(f -> f.getClient() != null && f.getClient().getId().equals(clientId))
                .toList();
    }

    /**
     * Obține feedback-ul PRIMIT de un employee/manager (employee = destinatar)
     */
    public List<Feedback> getFeedbackByEmployeeId(Long employeeId) {
        return feedbackRepository.findAll().stream()
                .filter(f -> f.getEmployee() != null && f.getEmployee().getId().equals(employeeId))
                .toList();
    }

    /**
     * Obține feedback pentru o mașină specifică
     */
    public List<Feedback> getFeedbackByCarId(Long carId) {
        return feedbackRepository.findByCarId(carId);
    }

    /**
     * Obține rating mediu pentru o mașină
     */
    public Double getAverageRatingForCar(Long carId) {
        Double avg = feedbackRepository.findAverageRatingByCarId(carId);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0;
    }

    /**
     * Obține feedback cu rating minim specificat
     */
    public List<Feedback> getHighRatedFeedback(Integer minRating) {
        if (minRating < 1 || minRating > 5) {
            throw new RuntimeException("Rating-ul trebuie să fie între 1 și 5");
        }
        return feedbackRepository.findByMinRating(minRating);
    }

    /**
     * Statistici feedback - câte feedback-uri are fiecare rating
     */
    public java.util.Map<Integer, Long> getFeedbackStatistics() {
        List<Feedback> allFeedback = feedbackRepository.findAll();
        return allFeedback.stream()
                .filter(f -> f.getRating() != null)
                .collect(java.util.stream.Collectors.groupingBy(
                        Feedback::getRating,
                        java.util.stream.Collectors.counting()
                ));
    }
}