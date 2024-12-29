package com.ashwinsproject.jobportal.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.ashwinsproject.jobportal.entity.IRecruiterJobs;
import com.ashwinsproject.jobportal.entity.JobCompany;
import com.ashwinsproject.jobportal.entity.JobLocation;
import com.ashwinsproject.jobportal.entity.JobPostActivity;
import com.ashwinsproject.jobportal.entity.RecruiterJobsDto;
import com.ashwinsproject.jobportal.repository.JobPostActivityRepository;

@Service
public class JobPostActivityService {
	
	private final JobPostActivityRepository jobPostActivityRepository;
	
	public JobPostActivityService(JobPostActivityRepository jobPostActivityRepository) {
		this.jobPostActivityRepository = jobPostActivityRepository;
	}

	public JobPostActivity addNew(JobPostActivity jobPostActivity) {
		return jobPostActivityRepository.save(jobPostActivity);
	}
	
	public List<RecruiterJobsDto> getRecruiterJobs(int recruiter){
		List<IRecruiterJobs> recruiterJobsDtos = jobPostActivityRepository.getRecruiterJobs(recruiter);
		
		List<RecruiterJobsDto> recruiterJobsDtosList = new ArrayList<>();
		
		for(IRecruiterJobs rec: recruiterJobsDtos) {
			JobLocation loc = new JobLocation(rec.getLocationId(),rec.getCity(),rec.getState(),rec.getCountry());
			JobCompany com = new JobCompany(rec.getCompanyId(), rec.getName(),"");
			recruiterJobsDtosList.add(new RecruiterJobsDto(rec.getTotalCandidates(),rec.getJob_post_id(),
					rec.getJob_title(),loc,com));
		}
		return recruiterJobsDtosList;
		
	}

	public JobPostActivity getOne(int id) {
		return jobPostActivityRepository.findById(id).orElseThrow(()-> 
										new RuntimeException("Job not found"));
	}

	public List<JobPostActivity> getAll() {
		
		return jobPostActivityRepository.findAll();
	}

	public List<JobPostActivity> search(String job, String location, List<String> type, List<String> remote,
			LocalDate searchDate) {
		
		return Objects.isNull(searchDate)? jobPostActivityRepository.searchWithoutDate(job,location,type,remote):
			jobPostActivityRepository.search(job,location,remote,type,searchDate);
	}

}
