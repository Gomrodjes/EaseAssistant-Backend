package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.job.JobResponseDTO;
import com.example.demo.models.job.JobSaveDTO;
import com.example.demo.models.response.ResponseApi;
import com.example.demo.services.JobService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    @Qualifier("jobService")
    private JobService jobService;

    @GetMapping()
    public ResponseEntity<?> getAllJobs() {
        return ResponseEntity.ok(new ResponseApi<>(true, jobService.getAllJobs(), "Jobs retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getJobById(@PathVariable Long id) {
        JobResponseDTO job = jobService.getJobById(id);
        if (job == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseApi<>(false, null, "Job not found with id: " + id));
        }
        return ResponseEntity.ok(new ResponseApi<>(true, job, "Job retrieved successfully"));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createJob(@RequestBody @Valid JobSaveDTO jobSaveDTO) {
        try {
            JobResponseDTO createdJob = jobService.createJob(jobSaveDTO);
            return ResponseEntity.ok(new ResponseApi<>(true, createdJob, "Job created successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateJob(@PathVariable Long id, @RequestBody @Valid JobSaveDTO jobSaveDTO) {
        try {
            JobResponseDTO updatedJob = jobService.updateJob(id, jobSaveDTO);
            return ResponseEntity.ok(new ResponseApi<>(true, updatedJob, "Job updated successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Long id) {
        try {
            jobService.deleteJob(id);
            return ResponseEntity.ok(new ResponseApi<>(true, null, "Job deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }

}
