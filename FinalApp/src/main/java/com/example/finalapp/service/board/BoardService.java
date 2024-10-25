package com.example.finalapp.service.board;

import com.example.finalapp.dto.board.BoardListDTO;
import com.example.finalapp.dto.board.BoardUpdateDTO;
import com.example.finalapp.dto.board.BoardViewDTO;
import com.example.finalapp.dto.board.BoardWriteDTO;
import com.example.finalapp.dto.file.FileDTO;
import com.example.finalapp.dto.page.Criteria;
import com.example.finalapp.mapper.board.BoardMapper;
import com.example.finalapp.mapper.file.FileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BoardService{
    private final BoardMapper boardMapper;
    private final FileMapper fileMapper;

    //    application.properties에 저장해든 file.dir 프로퍼티 값을 가져와서 아래 필드에 넣어준다
    @Value("C:/upload/")
    private String fileDir;

    public void registerBoard(BoardWriteDTO boardWriteDTO) {
        boardMapper.insertBoard(boardWriteDTO);
    }

    /*
     * 파일 저장 및 DB 등록 처리
     * @param boardWriteDTO 게시물 정보를 가진 DTO
     * @param files 여러 파일을 담은 리스트
     * @throws IOException
     *
     */

    public void registerBoardWithFile(BoardWriteDTO boardWriteDTO, List<MultipartFile> files) throws IOException {
        boardMapper.insertBoard(boardWriteDTO);
        Long boardId = boardWriteDTO.getBoardId();

        for(MultipartFile file : files) {
            if(file.isEmpty()){
                break;
            }

            FileDTO fileDTO = saveFile(file);
            fileDTO.setBoardId(boardId);
            fileMapper.insertFile(fileDTO);
        }
    }

    /*
     * 실질적인 파일저장을 담당하는 메소드
     *
     * @param file 저장할 파일 객체
     * @return boardId를 제외한 fileDTO 반환
     * @throws IOException
     *
     *
     */

    public FileDTO saveFile(MultipartFile files) throws IOException {
        //사용자가 올린 파일이름(확장자를 포함한다)
        String originalFilename = files.getOriginalFilename();
        //파일 이름에 붙여줄 uuid 생성
        UUID uuid = UUID.randomUUID();
        //uuid와 파일이름 합쳐준다
        String systemName = uuid.toString() + "_" + originalFilename;
        //상위 경로와 하위 경로를 합쳐준다
        File uploadPath = new File(fileDir, getUploadPath());

        //경로가 존재하지 않는다면 (폴더가 만들어지지 않았다면)
        if(!uploadPath.exists()){
            //경로에 필요한 모든 폴더를 생성한다
            uploadPath.mkdirs();
        }

        //전체경로와 파일이름을 연결한다
        File uploadFile = new File(uploadPath, systemName);

        //매개변수로 받은 Multipart 객체가 가진 파일을 우리가 만든 경로와 이름으로 저장한다
        files.transferTo(uploadFile);

        FileDTO fileDTO = new FileDTO();
        fileDTO.setUuid(uuid.toString());
        fileDTO.setName(originalFilename);
        fileDTO.setUploadPath(getUploadPath());

        return fileDTO;
    }

    private String getUploadPath() {
        return new SimpleDateFormat("yyyy/MM/dd").format(new Date());
    }


//    public void removeBoard(Long boardId) {
//        List<FileDTO> fileList = fileMapper.selectList(boardId);
//        fileMapper.deleteFile(boardId);
//        boardMapper.deleteBoard(boardId);
//
//        for(FileDTO file : fileList){
//            File target = new File(fileDir, file.getUploadPath() + "/" + file.getUuid() + "_" + file.getName());
//
//            if(target.exists()){
//                target.delete();
//
//            }
//        }
//    }
public void removeBoard(Long boardId) {
    List<FileDTO> fileList = fileMapper.selectList(boardId);
    fileMapper.deleteFile(boardId);
    boardMapper.deleteBoard(boardId);

    for(FileDTO file : fileList){


        Path targetPath = Paths.get(fileDir, file.getUploadPath(), file.getUuid() + "_" + file.getName());

        try {
            if (Files.exists(targetPath)) {
                Files.delete(targetPath);
            } else {
                System.out.println("File does not exist: " + targetPath.toString());
            }
        } catch (IOException e) {
            System.err.println("Failed to delete file: " + targetPath.toString());
            e.printStackTrace();
        }
    }
}

    public void modifyBoard(BoardUpdateDTO boardUpdateDTO, List<MultipartFile> files) throws IOException {
        boardMapper.updateBoard(boardUpdateDTO);
        Long boardId = boardUpdateDTO.getBoardId();

        fileMapper.deleteFile(boardId);

        for(MultipartFile file : files) {
            if(file.isEmpty()){
                break;
            }

            FileDTO fileDTO = saveFile(file);
            fileDTO.setBoardId(boardId);
            fileMapper.insertFile(fileDTO);
        }
    }

    public BoardViewDTO findById(Long boardId) {
        return boardMapper.selectById(boardId).orElseThrow(()-> new IllegalStateException("유효하지 않은 게시물 번호"));
    }

    public List<BoardListDTO> findAll() {
        return boardMapper.selectAll();
    }

    public List<BoardListDTO> findAllPage(Criteria criteria) {
        return boardMapper.selectAllPage(criteria);
    }

    public int findTotal() {
        return boardMapper.selectTotal();
    }


    public File getFile(String dir, String filename) {
        return new File(dir, filename);
    }


    public void setFileDir(String fileDir) {
    }
}
