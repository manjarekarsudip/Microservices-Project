package com.jobapp.jobms.job.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jobapp.jobms.job.Job;
import com.jobapp.jobms.job.JobRepository;
import com.jobapp.jobms.job.JobService;
import com.jobapp.jobms.job.external.Company;

@Service
public class JobServiceImpl implements JobService {

//	private List<Job> jobs = new ArrayList<>();
	@Autowired
	JobRepository jobRepository;
	
	@Override
	public List<Job> findAll() {
		RestTemplate restTemplate = new RestTemplate();
		Company company = restTemplate.getForObject("http://localhost:8081/companies/1", Company.class);
		System.out.println("Company : " + company.getName());
		System.out.println("Company : " + company.getId());
		return jobRepository.findAll();
	}

	@Override
	public void createJob(Job job) {
		jobRepository.save(job);
	}

	@Override
	public Job getJobById(Long id) {
		return jobRepository.findById(id).orElse(null);
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
