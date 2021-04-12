package study.datajpa.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

	List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

	List<Member> findHelloBy(); //By뒤에 아무것도 없으면 전체조회. 있으면 Where로 들어감

	List<Member> findTop3HelloBy(); //Top숫자가 붙으면 limit조건으로 검색된다.

	// @Query(name = "Member.findByUsername")
	List<Member> findByUsername(@Param("username") String username); //NamedQuery(거의안씀)

	@Query("select m from Member m where m.username = :username and m.age = :age")
	List<Member> findUser(@Param("username") String username, @Param("age") int age);

	//특정 데이터를 조회
	@Query("select m.username from Member m")
	List<String> findUsernameList();

	//DTO를 조회
	@Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
	List<MemberDto> findMemberDto();

	//컬렉션 파라미터 바인딩
	@Query("select m from Member m where m.username in :names")
	List<Member> findByNames(@Param("names") Collection<String> names);

	List<Member> findListByUsername(String username); //컬렉션
	Member findMemberByUsername(String username); //단건
	Optional<Member> findOptionalByUsername(String username); //단건 Optional

	@Query(value = "select m from Member m left join m.team t",
		countQuery = "select count(m) from Member m") // 카운트 쿼리의 분리
	Page<Member> findByAge(int age, Pageable pageable);
	// Slice<Member> findByAge(int age, Pageable pageable);

	@Modifying(clearAutomatically = true) // executeQuery해주는 어노테이션
	@Query("update Member m set m.age = m.age + 1 where m.age >= :age")
	int bulkAgePlus(@Param("age") int age);

	@Query("select m from Member m left join fetch m.team")
	List<Member> findmemberFetchJoin();

	@Override
	@EntityGraph(attributePaths = {"team"})
	List<Member> findAll();

	@EntityGraph(attributePaths = {"team"})
	@Query("select m from Member m")
	List<Member> findMemberEntityGraph();

	@EntityGraph(attributePaths = ("team")) //간단한거는 이거로 사용하고, 복잡한거는 JPQL fetch join을 직접 사용
	// @EntityGraph("Member.all")
	List<Member> findEntityGraphByUsername(@Param("username") String username);

	@QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true")) // 크게 신경안써도된다.
	Member findReadOnlyByUsername(String username);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	List<Member> findLockByUsername(String username);

	<T> List<T> findProjectionsByUsername(@Param("username") String username, Class<T> type);

	@Query(value = "select * from member where username = ?", nativeQuery = true)
	Member findByNativeQuery(String username);

	@Query(value ="select m.member_id as id, m.username, t.name as teamName"
		+ " from member m left join team t",
		countQuery = "select count(*) from member",
		nativeQuery = true)
	Page<MemberProjection> findByNativeProjection(Pageable pageable);
}
