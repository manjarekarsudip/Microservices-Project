package com.jobapp.companyms.company;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jobapp.companyms.company.dto.ReviewMessage;

@Service
public interface CompanyService {

	List<Company> getAllCompanies();
	void createCompany(Company company);
	Company getCompanyById(Long id);
	boolean deleteCompanyById(Long id);
	boolean updateCompany(Long id, Company company);
	public void updateCompanyRating(ReviewMessage reviewMessage);
}
