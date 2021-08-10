package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.domain.Member;
import study.datajpa.dto.MemberDto;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    //쿼리 메서드, @Query

    //동적 쿼리
    List<Member> findByUsernameAndAgeGreaterThanEqual(String username, int age);
    
    //named 쿼리
    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);
    
    //정적 쿼리, 이름이 없는 named 쿼리 -> 컴파일 단계에서 오류검사 가능
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, m.team.name) from Member m join m.team")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m join fetch m.team")
    List<Member> findMember();
    
    //in 절 파라미터 바인딩
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    List<Member> findListByUsername(String username);//컬렉션
    Member findMemberByUsername(String username);//단건
    Optional<Member> findOptionalByUsername(String username);//단건 Optional
    
    //성능이 복잡해지면 count 쿼리를 만들자
    Page<Member> findPageByAge(int age, Pageable pageable);
    
    //벌크 연산
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);
    
    //페치조인
    @Query("select m from Member m join fetch m.team")
    List<Member> findMemberFetchJoin();
    
    //JPQL 안짜고도 가능 오버라이딩해서 사용
    @Override
    @EntityGraph(attributePaths ={"team"})
    List<Member> findAll();
    
    //JPQL에 엔티티그래프 추가
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    //쿼리 메서드에 엔티티 그래프 추가
    @EntityGraph(attributePaths = {"team"})
    List<Member> findMember2ByUsername(String username);

    //읽기전용, 수정 X 스냅샷을 생성X
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);
}
