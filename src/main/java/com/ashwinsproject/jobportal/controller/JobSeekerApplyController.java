package com.ashwinsproject.jobportal.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.ashwinsproject.jobportal.entity.JobPostActivity;
import com.ashwinsproject.jobportal.entity.JobSeekerApply;
import com.ashwinsproject.jobportal.entity.JobSeekerProfile;
import com.ashwinsproject.jobportal.entity.JobSeekerSave;
import com.ashwinsproject.jobportal.entity.RecruiterProfile;
import com.ashwinsproject.jobportal.entity.Users;
import com.ashwinsproject.jobportal.services.JobPostActivityService;
import com.ashwinsproject.jobportal.services.JobSeekerApplyService;
import com.ashwinsproject.jobportal.services.JobSeekerProfileService;
import com.ashwinsproject.jobportal.services.JobSeekerSaveService;
import com.ashwinsproject.jobportal.services.RecruiterProfileService;
import com.ashwinsproject.jobportal.services.UsersService;

@Controller
public class JobSeekerApplyController {

    private final JobPostActivityService jobPostActivityService;
    private final UsersService usersService;
    private final JobSeekerApplyService jobSeekerApplyService;
    private final JobSeekerSaveService jobSeekerSaveService;
    private final RecruiterProfileService recruiterProfileService;
    private final JobSeekerProfileService jobSeekerProfileService;

    public JobSeekerApplyController(JobPostActivityService jobPostActivityService,
                                    UsersService usersService,
                                    JobSeekerApplyService jobSeekerApplyService,
                                    JobSeekerSaveService jobSeekerSaveService,
                                    RecruiterProfileService recruiterProfileService,
                                    JobSeekerProfileService jobSeekerProfileService) {
        this.jobPostActivityService = jobPostActivityService;
        this.usersService = usersService;
        this.jobSeekerApplyService = jobSeekerApplyService;
        this.jobSeekerSaveService = jobSeekerSaveService;
        this.recruiterProfileService = recruiterProfileService;
        this.jobSeekerProfileService = jobSeekerProfileService;
    }

    @GetMapping("job-details-apply/{id}")
    public String display(@PathVariable("id") int id, Model model) {
        JobPostActivity jobDetails = jobPostActivityService.getOne(id);

        // Check if jobDetails is null
        if (jobDetails == null) {
            // Redirect to an error page or show a message
            return "error-page";
        }

        List<JobSeekerApply> jobSeekerApplyList = jobSeekerApplyService.getJobCandidates(jobDetails);
        List<JobSeekerSave> jobSeekerSaveList = jobSeekerSaveService.getJobCandidates(jobDetails);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
                RecruiterProfile user = recruiterProfileService.getCurrentRecruiterProfile();
                if (user != null) {
                    model.addAttribute("applyList", jobSeekerApplyList);
                }
            }else {
            	JobSeekerProfile user = jobSeekerProfileService.getCurrentSeekerProfile();
            	if(user != null) {
            		boolean exists = false;
            		boolean saved = false;
            		for(JobSeekerApply jobSeekerApply : jobSeekerApplyList) {
            			if(jobSeekerApply.getUserId().getUserAccountId() == user.getUserAccountId()) {
            				exists = true;
            				break;
            			}
            		}
            		
            		for(JobSeekerSave jobSeekerSave : jobSeekerSaveList) {
            			if(jobSeekerSave.getUserId().getUserAccountId() == user.getUserAccountId()) {
            				saved = true;
            				break;
            			}
            		}
            		model.addAttribute("alreadyApplied", exists);
            		model.addAttribute("alreadySaved", saved);
            	}
            }
        }
        
        JobSeekerApply jobSeekerApply = new JobSeekerApply();
        model.addAttribute("applyJob",jobSeekerApply);

        model.addAttribute("jobDetails", jobDetails);
        model.addAttribute("user", usersService.getCurrentUserProfile());
        return "job-details";
    }
    
    @PostMapping("job-details/apply/{id}")
    public String apply(@PathVariable("id") int id, JobSeekerApply jobSeekerApply) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	if(!(authentication instanceof AnonymousAuthenticationToken)) {
    		String currentUsername = authentication.getName();
    		Users user = usersService.findByEmail(currentUsername);
    		Optional<JobSeekerProfile> seekerProfile = jobSeekerProfileService.getOne(user.getUserId());
            JobPostActivity jobPostActivity = jobPostActivityService.getOne(id);
            if (seekerProfile.isPresent() && jobPostActivity != null) {
                jobSeekerApply = new JobSeekerApply();
                jobSeekerApply.setUserId(seekerProfile.get());
                jobSeekerApply.setJob(jobPostActivity);
                jobSeekerApply.setApplyDate(new Date());
            } else {
                throw new RuntimeException("User not found");
            }
            jobSeekerApplyService.addNew(jobSeekerApply);
        }

        return "redirect:/dashboard/";
    	}
    }