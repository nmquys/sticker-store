package com.nmquys.springbootstore.service;

import com.nmquys.springbootstore.dto.ProfileRequestDto;
import com.nmquys.springbootstore.dto.ProfileResponseDto;

public interface IProfileService {

    ProfileResponseDto getProfile();

    ProfileResponseDto updateProfile(ProfileRequestDto profileRequestDto);
}
