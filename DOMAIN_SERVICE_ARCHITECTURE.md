# 🏗️ Domain Service 패턴 아키텍처

## 📋 개요

**Domain Service 패턴**을 도입하여 비즈니스 로직을 더 명확하게 분리하고, 관심사를 분리한 새로운 아키텍처를 구현했습니다.

## 🏛️ 새로운 아키텍처 구조

### 기존 구조 (Repository → Service → Controller)
```
Controller Layer (REST API)
    ↓
Service Layer (비즈니스 로직 + 데이터 변환)
    ↓
Repository Layer (데이터 접근)
    ↓
Entity Layer (데이터 모델)
```

### 새로운 구조 (Repository → Domain Service → Service → Controller)
```
Controller Layer (REST API)
    ↓
Service Layer (데이터 변환 + 조합)
    ↓
Domain Service Layer (비즈니스 로직)
    ↓
Repository Layer (데이터 접근)
    ↓
Entity Layer (데이터 모델)
```

## 📦 패키지별 역할

### 1. `domain` - 도메인 서비스 계층 (신규)
- **역할**: 핵심 비즈니스 로직 처리
- **특징**: 
  - Repository를 직접 호출
  - 비즈니스 규칙 검증
  - 도메인 엔티티 조작
  - 트랜잭션 관리

### 2. `service` - 애플리케이션 서비스 계층 (수정됨)
- **역할**: 
  - DTO ↔ Entity 변환
  - 여러 Domain Service 조합
  - 외부 서비스 호출
- **특징**: 
  - Domain Service를 호출
  - Repository 직접 호출하지 않음

### 3. `controller` - 컨트롤러 계층 (변경 없음)
- **역할**: HTTP 요청/응답 처리
- **특징**: Service 계층만 호출

## 🔧 구현된 Domain Service들

### 1. ChurchDomainService
```java
@Component
@Transactional(readOnly = true)
public class ChurchDomainService {
    
    // 비즈니스 로직 메서드들
    @Transactional
    public Church createChurch(Church church) {
        validateChurchCreation(church);
        return churchRepository.save(church);
    }
    
    @Transactional
    public Church updateChurch(Long id, Church churchData) {
        validateChurchUpdate(existingChurch, churchData);
        updateChurchFields(existingChurch, churchData);
        return churchRepository.save(existingChurch);
    }
    
    // 비즈니스 규칙 검증
    private void validateChurchCreation(Church church) {
        // 이름 중복 검증
        // 이메일 중복 검증
        // 필수 필드 검증
    }
}
```

### 2. MemberDomainService
```java
@Component
@Transactional(readOnly = true)
public class MemberDomainService {
    
    // 비즈니스 로직 메서드들
    @Transactional
    public Member createMember(Member member) {
        validateMemberCreation(member);
        return memberRepository.save(member);
    }
    
    // 복잡한 비즈니스 규칙 검증
    private void validateMemberCreation(Member member) {
        // 교회 존재 여부 검증
        // 부서 소속 검증
        // 이메일 중복 검증 (교회별)
        // 날짜 유효성 검증
        // 역할 제한 검증
    }
}
```

## 🎯 Domain Service 패턴의 장점

### 1. **관심사 분리 (Separation of Concerns)**
- **비즈니스 로직**: Domain Service에 집중
- **데이터 변환**: Application Service에 집중
- **HTTP 처리**: Controller에 집중

### 2. **비즈니스 규칙 중앙화**
```java
// 모든 비즈니스 규칙이 한 곳에 집중
private void validateChurchCreation(Church church) {
    if (existsByName(church.getName())) {
        throw new RuntimeException("Church name already exists");
    }
    if (church.getName() == null || church.getName().trim().isEmpty()) {
        throw new RuntimeException("Church name is required");
    }
    // ... 더 많은 규칙들
}
```

### 3. **재사용성 향상**
```java
// 여러 Service에서 동일한 비즈니스 로직 재사용
@Service
public class ChurchService {
    public ChurchDto createChurch(ChurchDto dto) {
        Church church = convertToEntity(dto);
        Church savedChurch = churchDomainService.createChurch(church); // 재사용
        return convertToDto(savedChurch);
    }
}

@Service
public class ChurchExcelService {
    public void exportChurches() {
        List<Church> churches = churchDomainService.findAll(); // 재사용
        // Excel 내보내기 로직
    }
}
```

### 4. **테스트 용이성**
```java
@ExtendWith(MockitoExtension.class)
class ChurchDomainServiceTest {
    
    @Mock
    private ChurchRepository churchRepository;
    
    @InjectMocks
    private ChurchDomainService churchDomainService;
    
    @Test
    void createChurch_WithDuplicateName_ThrowsException() {
        // Given
        Church church = new Church("Existing Church", "Address");
        when(churchRepository.existsByName("Existing Church")).thenReturn(true);
        
        // When & Then
        assertThrows(RuntimeException.class, () -> {
            churchDomainService.createChurch(church);
        });
    }
}
```

### 5. **트랜잭션 관리 개선**
```java
@Component
@Transactional(readOnly = true)  // 기본적으로 읽기 전용
public class ChurchDomainService {
    
    @Transactional  // 쓰기 작업만 트랜잭션
    public Church createChurch(Church church) {
        // 비즈니스 로직
    }
    
    public List<Church> findAll() {  // 읽기 전용
        return churchRepository.findAll();
    }
}
```

## 🔄 리팩토링된 Service 계층

### Before (기존 ChurchService)
```java
@Service
public class ChurchService {
    private final ChurchRepository churchRepository;
    
    public ChurchDto createChurch(ChurchDto churchDto) {
        // DTO → Entity 변환
        Church church = convertToEntity(churchDto);
        
        // 비즈니스 로직 (Repository 직접 호출)
        if (churchRepository.existsByName(church.getName())) {
            throw new RuntimeException("Name already exists");
        }
        Church savedChurch = churchRepository.save(church);
        
        // Entity → DTO 변환
        return convertToDto(savedChurch);
    }
}
```

### After (새로운 ChurchService)
```java
@Service
public class ChurchService {
    private final ChurchDomainService churchDomainService;
    
    public ChurchDto createChurch(ChurchDto churchDto) {
        // DTO → Entity 변환
        Church church = convertToEntity(churchDto);
        
        // Domain Service 호출 (비즈니스 로직 위임)
        Church savedChurch = churchDomainService.createChurch(church);
        
        // Entity → DTO 변환
        return convertToDto(savedChurch);
    }
}
```

## 📊 아키텍처 비교

| 측면 | 기존 구조 | Domain Service 구조 |
|------|-----------|-------------------|
| **비즈니스 로직 위치** | Service 계층에 분산 | Domain Service에 집중 |
| **재사용성** | 낮음 (Service별 중복) | 높음 (중앙화) |
| **테스트 용이성** | 어려움 (복잡한 의존성) | 쉬움 (단순한 의존성) |
| **유지보수성** | 낮음 (규칙 변경 시 여러 곳 수정) | 높음 (한 곳에서 관리) |
| **확장성** | 제한적 | 높음 (새로운 도메인 추가 용이) |

## 🚀 추가 개선 방향

### 1. **예외 처리 개선**
```java
// 도메인별 예외 클래스 생성
public class ChurchDomainException extends RuntimeException {
    public ChurchDomainException(String message) {
        super(message);
    }
}

// Domain Service에서 사용
private void validateChurchCreation(Church church) {
    if (existsByName(church.getName())) {
        throw new ChurchDomainException("Church name already exists: " + church.getName());
    }
}
```

### 2. **이벤트 기반 아키텍처**
```java
// 도메인 이벤트 발행
@Component
public class ChurchDomainService {
    
    @Transactional
    public Church createChurch(Church church) {
        Church savedChurch = churchRepository.save(church);
        
        // 도메인 이벤트 발행
        eventPublisher.publishEvent(new ChurchCreatedEvent(savedChurch));
        
        return savedChurch;
    }
}
```

### 3. **CQRS 패턴 도입**
```java
// Command (쓰기)
@Component
public class ChurchCommandService {
    private final ChurchDomainService churchDomainService;
    
    public ChurchDto createChurch(CreateChurchCommand command) {
        Church church = churchDomainService.createChurch(command.toEntity());
        return ChurchDto.fromEntity(church);
    }
}

// Query (읽기)
@Component
public class ChurchQueryService {
    private final ChurchRepository churchRepository;
    
    public List<ChurchDto> getAllChurches() {
        return churchRepository.findAll().stream()
                .map(ChurchDto::fromEntity)
                .collect(Collectors.toList());
    }
}
```

## 🎯 결론

Domain Service 패턴을 도입함으로써:

1. **비즈니스 로직이 명확하게 분리**되어 유지보수성이 향상
2. **재사용 가능한 도메인 로직**으로 중복 코드 제거
3. **테스트 용이성**이 크게 향상
4. **확장 가능한 아키텍처**로 새로운 도메인 추가가 쉬워짐

이 구조는 **도메인 주도 설계(DDD)**의 핵심 개념을 적용한 것으로, 복잡한 비즈니스 로직을 가진 애플리케이션에 매우 적합합니다. 