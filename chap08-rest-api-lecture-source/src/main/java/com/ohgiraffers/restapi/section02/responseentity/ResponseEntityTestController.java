package com.ohgiraffers.restapi.section02.responseentity;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/entity")
public class ResponseEntityTestController {

    private List<UserDTO> users;

    public ResponseEntityTestController(List<UserDTO> users) {
        this.users = users;

        users.add(new UserDTO(1, "user01", "pass01", "홍길동", new java.util.Date()));
        users.add(new UserDTO(2, "user02", "pass02", "윤봉길", new java.util.Date()));
        users.add(new UserDTO(3, "user03", "pass03", "유관순", new java.util.Date()));
    }

    /* 설명. 1. 직접 ResponseEntity 객체 만들기 */
    @GetMapping("/users")
    public ResponseEntity<ResponseMessage> findAllUsers() {
        HttpHeaders headers = new HttpHeaders();            // 헤더의 정보를 담을 수 있는 객체

        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("users", users);

        ResponseMessage responseMessage = new ResponseMessage(200, "조회성공", responseMap);

        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    /* 설명. 2. 빌더를 활용한 메소드 체이닝 방식으로 ResponseEntity 객체 만들기(요즘 유행) */
    @GetMapping("/users/{userNo}")
    public ResponseEntity<ResponseMessage> findUserByNo(@PathVariable int userNo) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        /* 설명. 요청 리소스(회원 번호와 일치하는 회원 한명)를 추출 */
        UserDTO foundUser = users.stream().filter(user -> user.getNo() == userNo).collect(Collectors.toList()).get(0);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("user", foundUser);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new ResponseMessage(200, "조회 성공!", responseMap));
    }

    /* 설명.
     *  기존에 우리가 배웠던 @RequestParam과 달리 json 문자열이 해들러 메소드로 넘어올 때는 @RequestBody를 붙이고
     *  받아내야 한다. json 문자열의 각 프로퍼티 별로 받을 수도 있지만 한번에 하나의 타입으로 다 받아낼 때는 커맨드
     *  객체(UserDTO)를 활용해야 하며 커맨드 객체는 json 문자열의 프로퍼티명과 일치해야 한다.
     *
     *  {
     *    "id : "user01"
     *  }
     *  이 때 "id"를 프로퍼티명 "user01"을 프로퍼티라고 한다.
    * */
    @PostMapping("/users")
    public ResponseEntity<?> registUser(@RequestBody UserDTO newUser) {     // 바디에 실어서 내보낼 값이 없을 때 와일드카드(?) 사용 가능
        System.out.println("newUser = " + newUser);

        /* 설명. auto_increment 개념을 컬렉션 마지막에 있는 회원 번호 + 1로 해서 newUser에 추가하기 */
        int lastUserNo = users.get(users.size() -1).getNo();        // 컬렉션에 쌓인 마지막 회원의 회원번호
        newUser.setNo(lastUserNo + 1);                              // 마지막 회원번호 + 1을 newUser에 set

        users.add(newUser);

        return ResponseEntity
                .created(URI.create("/entity/users/" + users.get(users.size() -1).getNo()))
                .build();
    }

    @PutMapping("users/{userNo}")
    public ResponseEntity<?> modifyUser(@RequestBody UserDTO modifyInfo, @PathVariable int userNo) {

        UserDTO foundUser =
                users.stream().filter(user -> user.getNo() == userNo)
                        .collect(Collectors.toList())
                        .get(0);

        /* 설명. 사용자가 념겨준 수정하고자 하는 데이터로 회원 정보를 수정 */
        foundUser.setId(modifyInfo.getId());
        foundUser.setPwd(modifyInfo.getPwd());
        foundUser.setName(modifyInfo.getName());

        return ResponseEntity
                .created(URI.create("/entity/users/" + userNo))
                .build();
    }

    @DeleteMapping("/users/{userNo}")
    public ResponseEntity<?> removeUser(@PathVariable int userNo) {
        UserDTO foundUser =
                users.stream().filter(user -> user.getNo() == userNo)
                        .collect(Collectors.toList())
                        .get(0);

        /* 설명. ArrayList에서 제공하는 remove는 인덱스 대신 자신이 관리하는 객체를 찾아서 지워주기도 한다. */
        users.remove(foundUser);

        return ResponseEntity
                .noContent()
                .build();
    }
}
