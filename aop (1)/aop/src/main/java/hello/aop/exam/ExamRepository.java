package hello.aop.exam;

import hello.aop.exam.annotation.ReTry;
import hello.aop.exam.annotation.Trace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
@Slf4j
@Repository
public class ExamRepository {

    private static  int seq=0;

    @Trace
    @ReTry(4)

    public String save(String itemid){
        seq++;
        log.info("이번회차 seq:{}",seq);
        if(seq%5==0){
            throw new IllegalStateException("예외발생");
        }
        return "ok";
    }
}
