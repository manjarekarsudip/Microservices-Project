package com.jobapp.reviewms.review.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobapp.reviewms.review.Review;
import com.jobapp.reviewms.review.dto.ReviewMessage;

@Service
public class ReviewMessageProducer {

	private final RabbitTemplate rabbitTemplate;

    ReviewMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    
    public void sendMessage(Review review) {
    	ReviewMessage reviewMessage = new ReviewMessage();
    	reviewMessage.setId(review.getId());
    	reviewMessage.setTitle(review.getTitle());
    	reviewMessage.setDescription(review.getDescription());
    	reviewMessage.setRating(review.getRating());
    	reviewMessage.setCompanyId(review.getCompanyId());
    	rabbitTemplate.convertAndSend("companyRatingQueue",reviewMessage);
    }
}
