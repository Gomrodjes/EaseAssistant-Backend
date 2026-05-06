package com.example.demo.services.implement;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.example.demo.entities.Job;
import com.example.demo.models.job.JobResponseDTO;
import com.example.demo.models.job.JobSaveDTO;
import com.example.demo.repositories.JobRepository;
import com.example.demo.services.JobService;

@Service("jobService")
public class JobServiceImpl implements JobService {

    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    @Qualifier("jobRepository")
    private JobRepository jobRepository;

    @Override
    public List<JobResponseDTO> getAllJobs() {
        List<JobResponseDTO> jobs = new ArrayList<>();
        for (Job job : jobRepository.findAll()) {
            jobs.add(toResponseDTO(job));
        }
        return jobs;
    }

    @Override
    public JobResponseDTO getJobById(Long id) {
        Job job = jobRepository.findById(id).orElse(null);
        if (job == null) {
            return null;
        }
        return toResponseDTO(job);
    }

    @Override
    public JobResponseDTO createJob(JobSaveDTO jobSaveDTO) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JobResponseDTO updateJob(Long id, JobSaveDTO jobSaveDTO) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category does not exist to delete"));
        jobRepository.delete(job);
    }

    private JobResponseDTO toResponseDTO(Job job) {
        JobResponseDTO response = modelMapper.map(job, JobResponseDTO.class);
        response.setCategoryName(job.getCategory().getName());
        return response;
    }
}
