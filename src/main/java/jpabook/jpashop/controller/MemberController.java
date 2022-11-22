package jpabook.jpashop.controller;


import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model){
        // 빈껍데기 객체를 가지고가서 뭔가 검증이라던가 해준다?
        model.addAttribute("memberForm",new MemberForm());

        return "members/createMemberForm";

    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result){

        if (result.hasErrors()){
            return "members/createMemberForm";
        }
        //넘어온 memberForm 객체를 검증 가능하다 ?>
        // notEmpty 쓰기?
        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);
        memberService.join(member);
        // 리로딩 되는게 별로라서 바로 적용?
        return "redirect:/";
    }

    @GetMapping(value = "/members")
    public String list(Model model){
        List<Member> members = memberService.findMembers();
        model.addAttribute("members",members);
        return "members/memberList";
    }
}
