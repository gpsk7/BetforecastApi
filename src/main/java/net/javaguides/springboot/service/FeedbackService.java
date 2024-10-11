package net.javaguides.springboot.service;

import net.javaguides.springboot.model.Feedback;
import net.javaguides.springboot.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;

  public Feedback save(Feedback feedback){
      return feedbackRepository.save(feedback);
  }
}







