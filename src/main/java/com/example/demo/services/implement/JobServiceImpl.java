package com.example.demo.services.implement;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Category;
import com.example.demo.entities.Job;
import com.example.demo.models.job.JobResponseDTO;
import com.example.demo.models.job.JobSaveDTO;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.JobRepository;
import com.example.demo.services.JobService;

@Service("jobService")
public class JobServiceImpl implements JobService {

    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    @Qualifier("jobRepository")
    private JobRepository jobRepository;

    @Autowired
    @Qualifier("categoryRepository")
    private CategoryRepository categoryRepository;

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
        if (jobRepository.existsByName(jobSaveDTO.getName())) {
            throw new IllegalArgumentException("Service already exists with name: " + jobSaveDTO.getName());
        }

        Category category = categoryRepository.findByName(jobSaveDTO.getCategoryName())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Category does not exist with name: " + jobSaveDTO.getCategoryName()));

        Job job = modelMapper.map(jobSaveDTO, Job.class);
        job.setCategory(category);

        Job savedJob = jobRepository.save(job);
        return toResponseDTO(savedJob);
    }

    @Override
    public JobResponseDTO updateJob(Long id, JobSaveDTO jobSaveDTO) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service does not exist to update"));

        if (jobRepository.existsByNameAndIdNot(jobSaveDTO.getName(), id)) {
            throw new IllegalArgumentException("Another service already exists with name: " + jobSaveDTO.getName());
        }

        Category category = categoryRepository.findByName(jobSaveDTO.getCategoryName())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Category does not exist with name: " + jobSaveDTO.getCategoryName()));

        job.setName(jobSaveDTO.getName());
        job.setDescription(jobSaveDTO.getDescription());
        job.setPrice(jobSaveDTO.getPrice());
        job.setDurationMinutes(jobSaveDTO.getDurationMinutes());
        job.setCategory(category);

        Job updatedJob = jobRepository.save(job);
        return toResponseDTO(updatedJob);
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
