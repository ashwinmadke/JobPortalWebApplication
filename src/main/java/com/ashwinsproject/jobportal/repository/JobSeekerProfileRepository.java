package com.ashwinsproject.jobportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ashwinsproject.jobportal.entity.JobSeekerProfile;

public interface JobSeekerProfileRepository extends JpaRepository<JobSeekerProfile, Integer> {
	
}
