package study.datajpa.controller;

import javax.annotation.PostConstruct;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

	private final MemberRepository memberRepository;

	@GetMapping("/members/{id}")
	public String findMember(@PathVariable("id") Long id) {
		Member member = memberRepository.findById(id).get();
		return member.getUsername();
	}

	@GetMapping("/members2/{id}") //조회용으로만 사용하기. 권장방식은 아님
	public String findMember2(@PathVariable("id") Member member) {
		return member.getUsername();
	}

	@GetMapping("/members") //Entity를 반환하지말자!!
	public Page<Member> list(Pageable pageable) {
		return memberRepository.findAll(pageable);
	}

	@GetMapping("/members-config") //Entity를 반환하지말자!!
	public Page<Member> list2(@PageableDefault(size = 5, sort = "username") Pageable pageable) {
		return memberRepository.findAll(pageable);
	}

	@GetMapping("/members-dto") //이렇게 DTO로 반환하자
	public Page<MemberDto> list3(Pageable pageable) {
		return memberRepository.findAll(pageable).map(member -> new MemberDto(member.getId(), member.getUsername(), null));
	}

	@GetMapping("/members-dto2")
	public Page<MemberDto> list4(Pageable pageable) {
		return memberRepository.findAll(pageable)
			// .map(member -> new MemberDto(member));
			.map(MemberDto::new);
	}

	@PostMapping("/api/member")
	public CreateMemberResponse saveMemberV2(@RequestBody CreateMemberRequest request) {
		Member member = new Member();
		member.setUsername(request.getName());
		Long id = memberRepository.save(member).getId();
		return new CreateMemberResponse(id);
	}

	// @PostConstruct
	public void init() {
		for (int i = 0; i < 100; i++) {
			memberRepository.save(new Member("user" + i, i));
		}
	}

	@Data
	static class CreateMemberRequest {
		private String name;
	}

	@Data
	static class CreateMemberResponse {
		private Long id;
		public CreateMemberResponse(Long id) {
			this.id = id;
		}
	}
}
