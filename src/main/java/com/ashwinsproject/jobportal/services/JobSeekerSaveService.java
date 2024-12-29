package com.ashwinsproject.jobportal.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ashwinsproject.jobportal.entity.JobPostActivity;
import com.ashwinsproject.jobportal.entity.JobSeekerProfile;
import com.ashwinsproject.jobportal.entity.JobSeekerSave;
import com.ashwinsproject.jobportal.repository.JobSeekerSaveRepository;

import jakarta.transaction.Transactional;

@Service
public class JobSeekerSaveService {
	
	private final JobSeekerSaveRepository jobSeekerSaveRepository;

	public JobSeekerSaveService(JobSeekerSaveRepository jobSeekerSaveRepository) {
		this.jobSeekerSaveRepository = jobSeekerSaveRepository;
	}
	
	public List<JobSeekerSave> getCandidatesJob(JobSeekerProfile userAccountId){
		return jobSeekerSaveRepository.findByUserId(userAccountId);
	}
	
	public List<JobSeekerSave> getJobCandidates(JobPostActivity job){
		return jobSeekerSaveRepository.findByJob(job);
	}
	 
	public void addNew(JobSeekerSave jobSeekerSave) {
		
		jobSeekerSaveRepository.save(jobSeekerSave);
		
	}
	
	

}
