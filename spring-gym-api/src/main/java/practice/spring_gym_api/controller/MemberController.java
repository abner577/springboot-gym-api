package practice.spring_gym_api.controller;

import practice.spring_gym_api.dto.MemberMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import practice.spring_gym_api.service.MemberService;

@RestController
@RequestMapping(path = "api/v1/gym-api")
public class MemberController {

    private final MemberService memberService;
    private final MemberMapper memberMapper;

    public MemberController(MemberService memberService, MemberMapper memberMapper) {
        this.memberService = memberService;
        this.memberMapper= memberMapper;
    }
}
