package study.datajpa.dto;

import lombok.Data;
import study.datajpa.entity.Member;

@Data //Dto는 사용해도 되지만, Entity에서는 쓰지말자
public class MemberDto {
	private Long id;
	private String userName;
	private String teamName;

	public MemberDto(Long id, String userName, String teamName) {
		this.id = id;
		this.userName = userName;
		this.teamName = teamName;
	}

	public MemberDto(Member member) {
		this.id = member.getId();
		this.userName = member.getUsername();
	}
}
