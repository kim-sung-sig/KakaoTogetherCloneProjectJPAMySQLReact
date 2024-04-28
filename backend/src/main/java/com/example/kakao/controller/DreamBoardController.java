package com.example.kakao.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DreamBoardController {
    /*
    @Autowired
    private DreamBoardService boardService;
    @Autowired
    private DreamBoardFileServie boardFileService;

    @GetMapping("/dreamBoards")
    public ResponseEntity<List<DreamBoardDTO>> getBoardList(@ModelAttribute ScrollRequest sc){
        List<DreamBoardDTO> list = boardService.getPagingList(sc.getLastItemIdx(), sc.getSize(), sc.getCategoryNum(), sc.getSearch());
        
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/dreamBoards/{idx}")
    public ResponseEntity<DreamBoardDTO> getDreamBoardByIdx(@PathVariable(name = "idx") Long idx) {
        DreamBoardDTO res = new DreamBoardDTO();
        return ResponseEntity.ok().body(res);
    }
    
    @PostMapping(value = "/dreamBoards", consumes = "multipart/form-data; charset=UTF-8")
    @Transactional
    public ResponseEntity<Boolean> insertDreamBoard(MultipartHttpServletRequest request, @ModelAttribute DreamBoardDTO dto) {
        String ipAddress = request.getRemoteAddr();
        String uploadPath = request.getServletContext().getRealPath("/upload/");
        File file2 = new File(uploadPath);
        if(!file2.exists()){
            file2.mkdirs();
        }
        List<MultipartFile> fileList = request.getFiles("file");
        boolean isFirstFile = true;
        
        if(fileList != null && fileList.size() > 0){ // 사진이 있는 경우
            try {
                for(var file : fileList) { // 사진을 조회하며 저장
                    if(file != null && file.getSize() > 0){
                        String saveFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                        File saveFile = new File(uploadPath, saveFileName);
                        FileCopyUtils.copy(file.getBytes(), saveFile);
                        
                        if(isFirstFile){

                            isFirstFile = false;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else { // 사진이 없는 경우

        }

        return ResponseEntity.ok().body(true);
    }

    @PutMapping("/dreamBoards/{idx}")
    public ResponseEntity<Boolean> updateDreamBoard(@PathVariable(name = "idx") Long idx, @RequestBody DreamBoardDTO dto){
        return ResponseEntity.ok().body(true);
    }

    @DeleteMapping("/dreamBoards/{idx}")
    public ResponseEntity<Boolean> deleteDreamBoardByIdx(@PathVariable(name = "idx") Long idx){
        return ResponseEntity.ok().body(true);
    }
     */
}
