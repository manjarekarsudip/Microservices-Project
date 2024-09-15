package com.jobapp.reviewms.review;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobapp.reviewms.review.messaging.ReviewMessageProducer;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private ReviewMessageProducer reviewMessageProducer;
	
	@GetMapping
	public ResponseEntity<List<Review>> getAllReviews(@RequestParam Long companyId){
		return new ResponseEntity<>(reviewService.getAllReviews(companyId), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<String> addReview(@RequestParam Long companyId, @RequestBody Review review){
		boolean isReviewSaved = reviewService.addReview(companyId, review);
		if (isReviewSaved) {
			reviewMessageProducer.sendMessage(review);
			return new ResponseEntity<>("Review Added Successfully", HttpStatus.OK);
		}
		else
			return new ResponseEntity<>("Review Not Saved - Company ID Not Found", HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/{reviewId}")
	public ResponseEntity<Review> getReview( @PathVariable Long reviewId){
		return new ResponseEntity<>(reviewService.getReview(reviewId),HttpStatus.OK);
	}
	
	@PutMapping("/{reviewId}")
	public ResponseEntity<String> updateReview(@PathVariable Long reviewId, @RequestBody Review review){
		boolean isReviewUpdated = reviewService.updateReview(reviewId, review);
		if(isReviewUpdated) 
			return new ResponseEntity<>("Review Updated Successfully", HttpStatus.OK);
		else
			return new ResponseEntity<>("Review Not Updated", HttpStatus.NOT_FOUND);
	}
	
	@DeleteMapping("/{reviewId}")
	public ResponseEntity<String> deleteReview(@PathVariable Long reviewId){
		boolean isReviewDeleted = reviewService.deleteReview(reviewId);
		if(isReviewDeleted) 
			return new ResponseEntity<>("Review Deleted Successfully", HttpStatus.OK);
		else
			return new ResponseEntity<>("Review Not Deleted", HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/averageRating")
	public double getAverageRating(@RequestParam Long companyId) {
		List<Review> reviewsList = reviewService.getAllReviews(companyId);
		return reviewsList.stream()
		        .mapToDouble(review -> {
		            try {
		                return Double.parseDouble(review.getRating());
		            } catch (NumberFormatException e) {
		                // Handle the case where rating cannot be parsed
		                // For example, you could log the error or return a default value
		                return 0.0; // Return 0 if parsing fails; adjust as needed
		            }
		        })
		        .average()
		        .orElse(0.0); // Return 0 if the list is empty	
		}
}
