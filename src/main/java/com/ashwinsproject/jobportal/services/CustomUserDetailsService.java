package com.ashwinsproject.jobportal.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ashwinsproject.jobportal.entity.Users;
import com.ashwinsproject.jobportal.repository.UsersRepository;
import com.ashwinsproject.jobportal.util.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	
	private final UsersRepository usersRepository;
	
	public CustomUserDetailsService(UsersRepository usersRepository) {
		this.usersRepository = usersRepository;
	}
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	Users user	= usersRepository.findByEmail(username).orElseThrow(()-> 
				  new UsernameNotFoundException("could not found user"));
	
		return new CustomUserDetails(user);
	}



}
