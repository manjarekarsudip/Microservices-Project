package com.jobapp.jobms.job.mapper;

import java.util.List;

import com.jobapp.jobms.job.Job;
import com.jobapp.jobms.job.dto.JobDTO;
import com.jobapp.jobms.job.external.Company;
import com.jobapp.jobms.job.external.Review;

public class JobMapper {

	public static JobDTO mapToJobWithCompanyDTO(Job job, Company company, List<Review> reviews) {
		JobDTO jobWithCompanyDTO = new JobDTO();
		jobWithCompanyDTO.setId(job.getId());
		jobWithCompanyDTO.setTitle(job.getTitle());
		jobWithCompanyDTO.setDescription(job.getDescription());
		jobWithCompanyDTO.setLocation(job.getLocation());
		jobWithCompanyDTO.setMaxSalary(job.getMaxSalary());
		jobWithCompanyDTO.setMinSalary(job.getMinSalary());
		jobWithCompanyDTO.setCompany(company);
		jobWithCompanyDTO.setReviews(reviews);
		return jobWithCompanyDTO;
	}
}
