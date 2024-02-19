package com.ohgiraffers.fileupload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
public class FileUploadController {

    /* 설명. build된 파일 업로드 할 경로를 가져오기 위해 ResourceLoader 의존성 주입 받기 */
    @Autowired
    private ResourceLoader resourceLoader;

    @PostMapping("single-file")
    public String singleFileUpload(@RequestParam MultipartFile singleFile,  // 파일은 String형으로 받을 수 없기 때문에 MultipartFile 사용
                                   @RequestParam String singleFileDescription,
                                   RedirectAttributes rttr) throws IOException {

        /* 설명. encType = "multipart/form-data" 형태로 넘어온 파일은 MultipartFile 타입으로 받게 된다. */
        System.out.println("singleFile = " + singleFile);
        System.out.println("singleFileDescription = " + singleFileDescription);

        /* 설명. build 경로의 static에 있는 파일 업로드 할 곳의 경로를 받아온다.(미리 경로에 해당하는 디렉토리 생성 및 빌드 확인) */
        Resource resource = resourceLoader.getResource("classpath:static/uploadFiles/img/single");
//        System.out.println("빌드 된 single 디렉토리의 절대 경로: " + resource.getFile().getAbsoluteFile());
        String filePath = resource.getFile().getAbsolutePath();

        /* 설명. 사용자가 넘긴 파일을 확인하고, rename해 보자.(자바의 UUID 클래스를 활용해 rename)*/
        /* 필기. DB에 넣을 정보를 확인. */
        String originFileName = singleFile.getOriginalFilename();
        System.out.println("originFileName = " + originFileName);

        String ext = originFileName.substring(originFileName.lastIndexOf("."));     // apple.png -> .png 라는 확장자명만 자름
        System.out.println("ext = " + ext);

        String savedName = UUID.randomUUID().toString().replace("-", "") + ext; // UUID는 '-'이 붙어서 나오기 때문에 '-'을 제거하고 확장자명을 붙임.
        System.out.println("savedName = " + savedName);

        /* 설명. 우리가 지정한 경로로 파일을 저장 (build까지 업로드 하는데 시간이 걸리므로 build로 바로 저장)*/
        try {
            singleFile.transferTo(new File(filePath + "/" + savedName));

            /* 설명. DB를 다녀오는 Business Logic 구문 작성(DB에 저장) */
            /* 설명. BL이 성공하면 화면의 재료를 RedirectAttributes로 담는다.(flashAttribute) */
            /* 필기.
             *  flashAttributes는 클라이언트가 서버에 값을 주면 다시 돌려주는게 아니라
             *  서버에 Session으로 담겨있고 redirect 후 사용자가 재요청할 때 @Parameter로 꺼낼 수 있다
             * */
            rttr.addFlashAttribute("message", "파일 업로드 성공!");
            rttr.addFlashAttribute("img", "uploadFiles/img/single/" + savedName);
            rttr.addFlashAttribute("singleFileDescription", singleFileDescription);

        } catch (IOException e) {

            /* 설명. try 구문 안에서(DB를 다녀오는 business logic 처리) 예외가 발생하면 파일만 올라가면 안되므로 찾아서 다시 지워줌 */
            new File(filePath + "/" + savedName).delete();
        }


        return "redirect:/result";          // 사용자가 새로고침은 누르면 업로드가 계속 되기 때문에 방지하는 목적으로 redirect 사용
    }

    @PostMapping("multi-file")
        public String multiFileUpload(@RequestParam List<MultipartFile> multiFiles,
                @RequestParam String multiFileDescription,
                RedirectAttributes rttr) throws IOException {
//        System.out.println("multiFile = " + multiFile);
//        System.out.println("multiFileDescription = " + multiFileDescription);
            String filePath = resourceLoader.getResource("classpath:static/uploadFiles/img/multi")
                    .getFile()
                    .getAbsolutePath();

        /* 설명. 사용자가 올린 파일들을 전부 rename하고 저장하는 작업 및 DB로 전달하기 위한 List에 쌓기 */
        List<Map<String, String>> files = new ArrayList<>();    // DB에 저장할 값들을 지닌 List
        List<String> saveFiles = new ArrayList<>();             // 화면단에서 img 태그가 참조할 정적 리소스 경로(src 경로)

        try {

            for (int i = 0; i < multiFiles.size(); i++) {
                String originFileName = multiFiles.get(i).getOriginalFilename();
                String ext = originFileName.substring(originFileName.lastIndexOf("."));
                String savedName = UUID.randomUUID().toString().replace("-", "") + ext;

                /* 설명. DB에 저장할 사용자가 올린 하나의 파일 정보(DB 테이블 컬럼을 참고할 것.) */
                Map<String, String> file = new HashMap<>();
                file.put("originFileName", originFileName);
                file.put("savedName", savedName);
                file.put("filePath", filePath);
                file.put("multiFileDescription", multiFileDescription);

                files.add(file);

                multiFiles.get(i).transferTo(new File(filePath + "/" + savedName));
                saveFiles.add("uploadFiles/img/multi/" + savedName);
            }

            /* 설명. 여기까지 문제가 없었다면 화면의 재료를 던져준다. */
            rttr.addFlashAttribute("message", "다중 파일 업로드 성공!");
            rttr.addFlashAttribute("imgs", saveFiles);
            rttr.addFlashAttribute("multiFileDescription", multiFileDescription);

        } catch (Exception e) {

            /* 설명. List<Map<String, String>> files로 쌓인 업로드 된 파일들을 찾아 일일히 다시 지운다. */
            for(int i= 0; i < files.size(); i++ ) {
                Map<String, String> file = files.get(i);
                new File(filePath + "/" + file.get("savedName")).delete();
            }

            /* 설명. 실패했다면 해당 메시지 반환 */
            rttr.addFlashAttribute("message", "파일 업로드 실패!");
        }

        return "redirect:/result";
    }

    @GetMapping("result")
    public void result() {
    }
}
