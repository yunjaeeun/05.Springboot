package com.ohgiraffers.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/* 설명.
 *  인터셉터를 사용하는 경우(목적)
 *  : 로그인 체크, 권한 체크, 프로그램 실행 시간 계산 작업 로그 처리, 업로드 파일 처리, 로케일(지역) 설정 등
* */

/* 필기.
 *  필터 -> 인터셉터 방향으로 진행된다.
 *   필터는 bean들을 건들 수 없지만 interceptor는 bean을 건들 수 있다.(bean이기 때문에 건너 뛰어서 조작 가능)
 *   인터셉터는 핸들러메소드보다 먼저 실행되기 때문에 실행될 핸들러 메소드를 선택 가능하다.
* */
@Configuration      // 인터셉터: 서블릿에서 필터가 걸러준다면 스프링컨테이너에선 인터셉터가 걸러줌(전처리 후처리)
public class StopwatchInterceptor implements HandlerInterceptor {

    /* 설명. 필터와 달리 인터셉터는 빈을 활용할 수 있다.(생성자 주입 활용) */
    private final MenuService MENUSERVICE;

    @Autowired
    public StopwatchInterceptor(MenuService MENUSERVICE) {
        this.MENUSERVICE = MENUSERVICE;
    }

    /* 설명. boolean형에 따라 핸들러 메소드가 실행 될 수도 있고 안 될 수도 있도록 할 수 있으며 핸들러 메소드 이전 전처리 목적. */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        /* 설명. 핸들러 인터셉터는 bean을 활용할 수 있다.(@Service 계층의 객체도 Bean이다.) */
//        MENUSERVICE.method();         // bean을 활용할 수 있음.
        System.out.println("preHandle 호출함...(핸들러 메소드 이전)");

        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);       // postHandle에 전달해주기 위해 request에 담음

        /* 설명. 반환형을 false로 하면 특정 조건에 의해 이후 핸들러 메소드가 실행되지 않게 할 수 있다. */
//        return false;
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle 호출함....(핸들러 메소드 이후)");

        long startTime = (long)request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();

        request.removeAttribute("startTime");

        modelAndView.addObject("interval", endTime - startTime); // ModelAndView로 추가를 해 HandlerMethod가 가려는 View에 값을 추가해 줌.(ViewResolver로 감)
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
