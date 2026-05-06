package com.example.demo.services;

import java.util.List;

import com.example.demo.models.job.JobResponseDTO;
import com.example.demo.models.job.JobSaveDTO;

public interface JobService {
    List<JobResponseDTO> getAllJobs();

    JobResponseDTO getJobById(Long id);

    JobResponseDTO createJob(JobSaveDTO jobSaveDTO);

    JobResponseDTO updateJob(Long id, JobSaveDTO jobSaveDTO);

    void deleteJob(Long id);

}
