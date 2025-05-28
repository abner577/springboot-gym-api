package controller;

import dto.CoachMapper;
import dto.MemberMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.CoachService;
import service.MemberService;

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
