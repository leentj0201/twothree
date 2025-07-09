package com.twothree.backend.validation;

import com.twothree.backend.entity.Church;
import com.twothree.backend.exception.ChurchException;
import com.twothree.backend.repository.ChurchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class ChurchValidator {
    
    private final ChurchRepository churchRepository;
    
    // 이메일 정규식 패턴
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    // 전화번호 정규식 패턴 (한국 전화번호)
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^01[0-9]-?[0-9]{3,4}-?[0-9]{4}$"
    );
    
    // 웹사이트 URL 정규식 패턴
    private static final Pattern URL_PATTERN = Pattern.compile(
        "^(https?://)?(www\\.)?[a-zA-Z0-9-]+(\\.[a-zA-Z]{2,})+(/.*)?$"
    );
    
    /**
     * 교회 생성 시 검증
     */
    public void validateForCreation(Church church) {
        validateName(church.getName());
        validateAddress(church.getAddress());
        validateEmail(church.getEmail());
        validatePhone(church.getPhone());
        validatePastorPhone(church.getPastorPhone());
        validatePastorEmail(church.getPastorEmail());
        validateWebsite(church.getWebsite());
        validateNameUniqueness(church.getName());
        validateEmailUniqueness(church.getEmail());
    }
    
    /**
     * 교회 수정 시 검증
     */
    public void validateForUpdate(Church existingChurch, Church churchData) {
        validateName(churchData.getName());
        validateAddress(churchData.getAddress());
        validateEmail(churchData.getEmail());
        validatePhone(churchData.getPhone());
        validatePastorPhone(churchData.getPastorPhone());
        validatePastorEmail(churchData.getPastorEmail());
        validateWebsite(churchData.getWebsite());
        
        // 이름 변경 시 중복 검증
        if (!existingChurch.getName().equals(churchData.getName())) {
            validateNameUniqueness(churchData.getName());
        }
        
        // 이메일 변경 시 중복 검증
        if (churchData.getEmail() != null && 
            !churchData.getEmail().equals(existingChurch.getEmail())) {
            validateEmailUniqueness(churchData.getEmail());
        }
    }
    
    /**
     * 교회 삭제 시 검증
     */
    public void validateForDeletion(Church church) {
        if (church.getStatus() == com.twothree.backend.enums.ChurchStatus.ACTIVE) {
            throw ChurchException.cannotDeleteActive();
        }
    }
    
    private void validateName(String name) {
        if (!StringUtils.hasText(name)) {
            throw ChurchException.nameRequired();
        }
        
        if (name.length() < 2 || name.length() > 100) {
            throw new ChurchException(
                "Church name must be between 2 and 100 characters",
                "CHURCH_NAME_LENGTH_INVALID"
            );
        }
    }
    
    private void validateAddress(String address) {
        if (!StringUtils.hasText(address)) {
            throw ChurchException.addressRequired();
        }
        
        if (address.length() < 5 || address.length() > 200) {
            throw new ChurchException(
                "Church address must be between 5 and 200 characters",
                "CHURCH_ADDRESS_LENGTH_INVALID"
            );
        }
    }
    
    private void validateEmail(String email) {
        if (email != null && !email.trim().isEmpty()) {
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                throw new ChurchException(
                    "Invalid email format: " + email,
                    "CHURCH_INVALID_EMAIL"
                );
            }
        }
    }
    
    private void validatePhone(String phone) {
        if (phone != null && !phone.trim().isEmpty()) {
            if (!PHONE_PATTERN.matcher(phone.replaceAll("\\s", "")).matches()) {
                throw new ChurchException(
                    "Invalid phone format: " + phone,
                    "CHURCH_INVALID_PHONE"
                );
            }
        }
    }
    
    private void validatePastorPhone(String pastorPhone) {
        if (pastorPhone != null && !pastorPhone.trim().isEmpty()) {
            if (!PHONE_PATTERN.matcher(pastorPhone.replaceAll("\\s", "")).matches()) {
                throw new ChurchException(
                    "Invalid pastor phone format: " + pastorPhone,
                    "CHURCH_INVALID_PASTOR_PHONE"
                );
            }
        }
    }
    
    private void validatePastorEmail(String pastorEmail) {
        if (pastorEmail != null && !pastorEmail.trim().isEmpty()) {
            if (!EMAIL_PATTERN.matcher(pastorEmail).matches()) {
                throw new ChurchException(
                    "Invalid pastor email format: " + pastorEmail,
                    "CHURCH_INVALID_PASTOR_EMAIL"
                );
            }
        }
    }
    
    private void validateWebsite(String website) {
        if (website != null && !website.trim().isEmpty()) {
            if (!URL_PATTERN.matcher(website).matches()) {
                throw new ChurchException(
                    "Invalid website URL format: " + website,
                    "CHURCH_INVALID_WEBSITE"
                );
            }
        }
    }
    
    private void validateNameUniqueness(String name) {
        if (churchRepository.existsByName(name)) {
            throw ChurchException.nameExists(name);
        }
    }
    
    private void validateEmailUniqueness(String email) {
        if (email != null && churchRepository.existsByEmail(email)) {
            throw ChurchException.emailExists(email);
        }
    }
} 