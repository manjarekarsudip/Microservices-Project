package com.jobapp.companyms.company.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobapp.companyms.company.CompanyService;
import com.jobapp.companyms.company.dto.ReviewMessage;

@Service
public class ReviewMessageConsumer {

	private final CompanyService companyService;

    ReviewMessageConsumer(CompanyService companyService) {
        this.companyService = companyService;
    }
    
    @RabbitListener(queues = "companyRatingQueue")
    public void consumeMessage(ReviewMessage reviewMessage) {
    	companyService.updateCompanyRating(reviewMessage);
    }
}
