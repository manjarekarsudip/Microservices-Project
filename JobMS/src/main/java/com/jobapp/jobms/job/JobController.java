package com.jobapp.jobms.job;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jobapp.jobms.job.dto.JobDTO;

@RestController
@RequestMapping("/jobs")
public class JobController {

	@Autowired
	JobService jobService;
	
	@GetMapping
	public ResponseEntity<List<JobDTO>> findAll(){
		return ResponseEntity.ok(jobService.findAll());
	}
	
	@PostMapping
	public ResponseEntity<String> createJob(@RequestBody Job job)  {
		jobService.createJob(job);
		return new ResponseEntity<String>("Job added successfully",HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<JobDTO> getJobById(@PathVariable Long id) {
		JobDTO jobDTO = jobService.getJobById(id);
		if (jobDTO !=null) {
			return new ResponseEntity<JobDTO>(jobDTO, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteById(@PathVariable Long id){
		boolean deleted = jobService.deleteJobById(id);
		if (deleted) {
			return new ResponseEntity<String>("Job Deleted Successfully", HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@PutMapping("/{id}")
//	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<String> updateJob(@PathVariable Long id, @RequestBody Job updatedJob){
		boolean updated = jobService.updateJob(id, updatedJob);
		if (updated) {
			return new ResponseEntity<String>("Job Updated Successfully", HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
