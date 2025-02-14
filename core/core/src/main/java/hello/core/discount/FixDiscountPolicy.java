package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
//@Qualifier("fix")
public class FixDiscountPolicy implements DiscountPolicy {

    private int discountFixAmount1=1000;
    @Override
    public int discount(Member member, int price) {
        if(member.getGrade()== Grade.VIP){
            return discountFixAmount1;
        }
        else {
            return 0;
        }
    }
}
