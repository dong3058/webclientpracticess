package hello.core.web;
import hello.core.Common.MyLogger;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor

public class LogDemoController {
    private final LogDemoService logDemoSerivce;
    private final MyLogger mylogger;


}
