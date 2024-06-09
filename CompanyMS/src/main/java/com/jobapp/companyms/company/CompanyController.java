package com.jobapp.companyms.company;

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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/companies")
public class CompanyController {

	@Autowired
	private CompanyService companyService;
	
	@GetMapping
	public ResponseEntity<List<Company>>  getAllCompanies(){
		return ResponseEntity.ok(companyService.getAllCompanies());		
	}
	
	@PostMapping
	public ResponseEntity<String> createCompany(@RequestBody Company company){
		companyService.createCompany(company);
		return new ResponseEntity<String>("New Company Added",HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
		Company company = companyService.getCompanyById(id);
		if (company !=null) {
			return new ResponseEntity<Company>(company, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCompanyById(@PathVariable Long id){
		boolean deleted = companyService.deleteCompanyById(id);
		if (deleted) {
			return new ResponseEntity<String>("Company Deleted Successfully", HttpStatus.OK);
		}
		return new ResponseEntity<>("Company Not Found", HttpStatus.NOT_FOUND);
	}
	
	@PutMapping("/{id}")
//	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<String> updateCompany(@PathVariable Long id, @RequestBody Company company){
		boolean updated = companyService.updateCompany(id, company);
		if (updated) {
			return new ResponseEntity<String>("Company Updated Successfully", HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
