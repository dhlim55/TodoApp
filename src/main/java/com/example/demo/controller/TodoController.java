package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TodoDTO;
import com.example.demo.model.TodoEntity;
import com.example.demo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("todo")
public class TodoController {

    @Autowired
    private TodoService service;

    @GetMapping("/test") //여기 주소가 todo여야 하는데 잘못함.,
    public ResponseEntity<?> testTodo() {


        String str = service.testService(); //테스트 서비스 사용
        List<String> list = new ArrayList<>();
        list.add(str);

        //TodoDTO response = TodoDTO.builder().build();
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();


        return ResponseEntity.ok().body(response);
    }
@PostMapping
    public ResponseEntity<?> createTodo(
            @AuthenticationPrincipal String userId,
            @RequestBody TodoDTO dto) {
        try{
            String temporaryUserId = "temporary-user";

            //(1)TodoEntity로 변환
            TodoEntity entity = TodoDTO.toEntity(dto);

            //(2)ID를 null로 초기화한다.
            entity.setId(null);

            //(3)임시 사용자 아이디를 설정. 인증과 인가 기능이 없으므로 한 사용자만 로그인 없이 사용할 수 있는 어플리케이션인것이다.
            //entity.setUserId(temporaryUserId);
            entity.setUserId(userId);

            //(4)
            List<TodoEntity> entities = service.create(entity);

            //(5)
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            //(6)
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            //(7)
            return ResponseEntity.ok().body(response);

        } catch (Exception e){
            //(8)혹시 예외가 있는 경우 dto대신 error에 메시지를 넣어 리턴
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();


            return ResponseEntity.badRequest().body(response);
        }
    }
    @GetMapping
    public ResponseEntity<?> retrieveTodoList(
            @AuthenticationPrincipal String userId
    ) {
        //String temporaryUserId = "temporary-user";

        //List<TodoEntity> entities = service.retrieve(temporaryUserId);
        List<TodoEntity> entities = service.retrieve(userId);


        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        //(6)
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        //(7)
        return ResponseEntity.ok().body(response);

    }

    @PutMapping
    public ResponseEntity<?> updateTodo(@AuthenticationPrincipal String userId,
            @RequestBody TodoDTO dto) {
        //String temporaryUserId = "temporary-user";

        TodoEntity entity = TodoDTO.toEntity(dto);

        //entity.setUserId(temporaryUserId);
        entity.setUserId(userId);

        List<TodoEntity> entities = service.update(entity);

        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }
    @DeleteMapping
    public ResponseEntity<?> deleteTodo(
            @AuthenticationPrincipal String userId,
            @RequestBody TodoDTO dto) {
        try {
            //String temporaryUserId = "temporary-user";

            TodoEntity entity = TodoDTO.toEntity(dto);

            //entity.setUserId(temporaryUserId);
            entity.setUserId(userId);

            List<TodoEntity> entities = service.delete(entity);

            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            //(7)예외거 았는 경우 dto 대신 error에 메시지 리턴
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}
