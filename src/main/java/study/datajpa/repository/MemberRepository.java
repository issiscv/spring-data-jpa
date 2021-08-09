package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.domain.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    //쿼리 메서드, @Query

    //동적 쿼리
    List<Member> findByUsernameAndAgeGreaterThanEqual(String username, int age);

    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);
    
    //정적 쿼리, 이름이 없는 named 쿼리
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);
}
