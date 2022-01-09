# 인프런 김영한님의 '실전! 스프링 데이터 JPA' 를 학습 후 핵심을 정리한 내용

## 공통 인터페이스 적용
- 인터페이스를 생성 후 JpaRepository<T, ID> 인터페이스를 상속 받는다.
- T: 타겟 엔티티
- ID: 타겟 엔티티의 PK 자료형



    public interface MemberRepository extends JpaRepository<Member, Long> {
        ...
    }

## 메소드 이름으로 쿼리 생성
- 스프링 데이터 JPA는 메소드 이름을 분석해서 JPQL을 생성하고 실행
- 조회: find…By ,read…By ,query…By get…By,


    public interface MemberRepository extends JpaRepository<Member, Long> {
        List<Member> findByUsernameAndAgeGreaterThan(String username, int age);
    }

> 이 기능은 엔티티의 필드명이 변경되면 인터페이스에 정의한 메서드 이름도 꼭 함께 변경해야 한다.
그렇지 않으면 애플리케이션을 시작하는 시점에 오류가 발생한다.
> 이렇게 애플리케이션 로딩 시점에 오류를 인지할 수 있는 것이 스프링 데이터 JPA의 매우 큰 장점이다.

### @Query, 리포지토리 메소드에 쿼리 정의하기
- @org.springframework.data.jpa.repository.Query 어노테이션을 사용
- 실행할 메서드에 정적 쿼리를 직접 작성하므로 이름 없는 Named 쿼리라 할 수 있음
- JPA Named 쿼리처럼 애플리케이션 실행 시점에 문법 오류를 발견할 수 있음(매우 큰 장점!)


    public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("select m from Member m where m.username= :username and m.age = :age")
        List<Member> findUser(@Param("username") String username, @Param("age") int age);
    }

> 참고: 실무에서는 메소드 이름으로 쿼리 생성 기능은 파라미터가 증가하면 메서드 이름이 매우
지저분해진다. 따라서 @Query 기능을 자주 사용하게 된다.

### DTO 조회하기

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

> 주의! DTO로 직접 조회 하려면 JPA의 new 명령어를 사용해야 한다. 그리고 다음과 같이 생성자가 맞는 DTO 가 필요하다. (JPA와 사용방식이 동일하다.)

### 파라미터 바인딩

    public interface MemberRepository extends JpaRepository<Member, Long> {
        @Query("select m from Member m where m.username = :name")
        Member findMembers(@Param("name") String username);
    }

### 스프링 데이터 JPA 페이징과 정렬

    public interface MemberRepository extends JpaRepository<Member, Long> {
        Page<Member> findByUsername(String name, Pageable pageable); //count 쿼리 사용
        Slice<Member> findByUsername(String name, Pageable pageable); //count 쿼리 사용 안함
        List<Member> findByUsername(String name, Pageable pageable); //count 쿼리 사용 안함
        List<Member> findByUsername(String name, Sort sort);
    }

- 두 번째 파라미터로 받은 <b>Pagable</b> 은 인터페이스다. 따라서 실제 사용할 때는 해당 인터페이스를 구현한
org.springframework.data.domain.PageRequest 객체를 사용한다.
- PageRequest 생성자의 첫 번째 파라미터에는 현재 페이지를, 두 번째 파라미터에는 조회할 데이터 수를
입력한다. 여기에 추가로 정렬 정보도 파라미터로 사용할 수 있다. 참고로 페이지는 0부터 시작한다.
  
#### PageRequest 를 통해 파라미터 바인딩

    PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
    Page<Member> page = memberRepository.findByAge(10, pageRequest);

#### count 쿼리를 다음과 같이 분리할 수 있음

    @Query(value = “select m from Member m”,
    countQuery = “select count(m.username) from Member m”)
    Page<Member> findMemberAllCountBy(Pageable pageable);

## 벌크성 수정 쿼리

    @Modifying
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

- 벌크성 수정, 삭제 쿼리는 @Modifying 어노테이션을 사용
- 벌크성 쿼리를 실행하고 나서 영속성 컨텍스트 초기화: @Modifying(clearAutomatically = true)
- 이 옵션 없이 회원을 findById 로 다시 조회하면 영속성 컨텍스트에 과거 값이 남아서 문제가 될 수
  있다. 만약 다시 조회해야 하면 꼭 영속성 컨텍스트를 초기화 하자
  

## @EntityGraph
- 연관된 엔티티들을 SQL 한번에 조회하는 방법
- member team은 지연로딩 관계이다. 따라서 다음과 같이 team의 데이터를 조회할 때 마다 쿼리가
  실행된다. (N+1 문제 발생)
  

    //공통 메서드 오버라이드
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    //JPQL + 엔티티 그래프
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    //메서드 이름으로 쿼리에서 특히 편리하다.
    @EntityGraph(attributePaths = {"team"})
    List<Member> findByUsername(String username)

#### EntityGraph 정리
- 사실상 페치 조인(FETCH JOIN)의 간편 버전
- LEFT OUTER JOIN 사용

## JPA Hint

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

- readOnly 로 적용하였기 때문에, 해당 쿼리로 조회된 엔티티 객체는 스냅샷을 만들지 않아 변경 감지가 일어나지 않아 변경되지 않는다.
