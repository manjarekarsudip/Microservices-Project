package com.jobapp.companyms.company.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobapp.companyms.company.Company;
import com.jobapp.companyms.company.CompanyRepository;
import com.jobapp.companyms.company.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	private CompanyRepository companyRepository;
	
	@Override
	public List<Company> getAllCompanies() {
		return companyRepository.findAll();
	}

	@Override
	public void createCompany(Company company) {
		companyRepository.save(company);
	}

	@Override
	public Company getCompanyById(Long id) {
		return companyRepository.findById(id).orElse(null);
	}

	@Override
	public boolean deleteCompanyById(Long id) {
		try {
			boolean existsById = companyRepository.existsById(id);
			if(existsById==true) {
			companyRepository.deleteById(id);
			return true;
			}
			else return false;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean updateCompany(Long id, Company company) {
		Optional<Company> companyOptional = companyRepository.findById(id);
		
		if(companyOptional.isPresent()) {
			Company companyToUpdate = companyOptional.get();
			companyToUpdate.setName(company.getName());
			companyToUpdate.setDescription(company.getDescription());
			companyRepository.save(companyToUpdate);
			return true;
		}
	return false;
	}

}
