package com.twothree.backend.scheduler;

import com.twothree.backend.repository.ChurchRepository;
import com.twothree.backend.repository.MemberRepository;
import com.twothree.backend.enums.ChurchStatus;
import com.twothree.backend.enums.MemberStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataCleanupScheduler {

    private final ChurchRepository churchRepository;
    private final MemberRepository memberRepository;

    // 매일 새벽 2시에 실행
    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    @CacheEvict(value = {"churches", "members", "departments"}, allEntries = true)
    public void cleanupOldData() {
        log.info("🧹 데이터 정리 작업 시작");
        
        LocalDateTime cutoffDate = LocalDateTime.now().minusMonths(6);
        
        // 6개월 이상 된 비활성 교회 정리
        long deletedChurches = churchRepository.deleteByStatusAndUpdatedAtBefore(ChurchStatus.INACTIVE, cutoffDate);
        log.info("삭제된 비활성 교회: {}개", deletedChurches);
        
        // 6개월 이상 된 비활성 멤버 정리
        long deletedMembers = memberRepository.deleteByStatusAndUpdatedAtBefore(MemberStatus.INACTIVE, cutoffDate);
        log.info("삭제된 비활성 멤버: {}개", deletedMembers);
        
        log.info("✅ 데이터 정리 작업 완료");
    }

    // 매주 일요일 새벽 3시에 실행
    @Scheduled(cron = "0 0 3 ? * SUN")
    @CacheEvict(value = {"churches", "members", "departments"}, allEntries = true)
    public void clearAllCaches() {
        log.info("🗑️ 모든 캐시 정리");
    }

    // 매시간 실행
    @Scheduled(fixedRate = 3600000) // 1시간 = 3600000ms
    public void healthCheck() {
        log.info("💓 시스템 상태 확인 - 현재 시간: {}", LocalDateTime.now());
        
        long churchCount = churchRepository.count();
        long memberCount = memberRepository.count();
        
        log.info("📊 현재 데이터 현황 - 교회: {}개, 멤버: {}개", churchCount, memberCount);
    }
} 