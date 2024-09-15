package com.jobapp.jobms.job.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jobapp.jobms.job.Job;
import com.jobapp.jobms.job.JobRepository;
import com.jobapp.jobms.job.JobService;
import com.jobapp.jobms.job.clients.CompanyClient;
import com.jobapp.jobms.job.clients.ReviewClient;
import com.jobapp.jobms.job.dto.JobDTO;
import com.jobapp.jobms.job.external.Company;
import com.jobapp.jobms.job.external.Review;
import com.jobapp.jobms.job.mapper.JobMapper;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@Service
public class JobServiceImpl implements JobService {

//	private List<Job> jobs = new ArrayList<>();
	@Autowired
	JobRepository jobRepository;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	private CompanyClient companyClient;
	
	@Autowired
	private ReviewClient reviewClient;
	
	int attempt = 0;
	
	@Override
//	@CircuitBreaker(name = "companyBreaker" , fallbackMethod = "companyBreakerFallback")
//	@Retry(name = "companyBreaker" , fallbackMethod = "companyBreakerFallback")
	@RateLimiter(name = "companyBreaker" , fallbackMethod = "companyBreakerFallback")
	public List<JobDTO> findAll() {
		
		System.out.println("No of Attempts : "+ ++attempt);
		List<Job> jobs = jobRepository.findAll();
		List<JobDTO> jobWithCompanyDTOs = new ArrayList<>();
		
		return jobs.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public List<String> companyBreakerFallback(Exception e){
		List<String> list = new ArrayList<>();
		list.add("Dummy");
		return list;
	}
	
	private JobDTO convertToDTO(Job job) {
		
			//RestTemplate restTemplate = new RestTemplate();
//			Company company = restTemplate.getForObject("http://CompanyMS:8081/companies/"+
//														job.getCompanyId(), Company.class);
				
//			ResponseEntity<List<Review>> reviewResponse = restTemplate.exchange("http://ReviewMS:8083/reviews?companyId=" + job.getCompanyId(),
//					HttpMethod.GET, null, new ParameterizedTypeReference<List<Review>>() {
//					});
//			List<Review> reviews = reviewResponse.getBody();

		Company company = companyClient.getCompany(job.getCompanyId());	

		List<Review> reviews= reviewClient.getReviews(job.getCompanyId());
		
		JobDTO jobDTO = JobMapper.mapToJobWithCompanyDTO(job, company,reviews);

//		jobDTO.setCompany(company);	
//		jobWithCompanyDTOs.add(jobWithCompanyDTO);
			
		return jobDTO;
		
	}
	
	@Override
	public void createJob(Job job) {
		jobRepository.save(job);
	}

	@Override
	public JobDTO getJobById(Long id) {
		 Job job = jobRepository.findById(id).orElse(null);
		 JobDTO jobDTO = convertToDTO(job);
		 return jobDTO;
	}

	@Override
	public boolean deleteJobById(Long id) {
		try {
			jobRepository.deleteById(id);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean updateJob(Long id, Job updatedJob) {
		
		Optional<Job> jobOptional = jobRepository.findById(id);
		
			if(jobOptional.isPresent()) {
				Job job = jobOptional.get();
				job.setTitle(updatedJob.getTitle());
				job.setDescription(updatedJob.getDescription());
				job.setMinSalary(updatedJob.getMinSalary());
				job.setMaxSalary(updatedJob.getMaxSalary());
				job.setLocation(updatedJob.getLocation());
				jobRepository.save(job);
				return true;
			}
		return false;
	}

}
