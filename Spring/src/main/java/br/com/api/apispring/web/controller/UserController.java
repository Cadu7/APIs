package br.com.api.apispring.web.controller;

import br.com.api.apispring.domain.dto.UserDTO;
import br.com.api.apispring.domain.interfaces.IUserService;
import br.com.api.apispring.domain.model.User;
import br.com.api.apispring.web.exception.InvalidRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController("/api/user")
public class UserController {
    
    @Autowired
    private IUserService userService;
    
    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getUsers(@PageableDefault(page = 0, sort = "name", size = 10, direction = Sort.Direction.DESC) Pageable pageable){
        return ResponseEntity.status(OK).body(userService.getAll(pageable));
    }
    
    @GetMapping
    public ResponseEntity<UserDTO> getUserById(@RequestParam(name = "userId") String id) throws InvalidRequestException {
        return ResponseEntity.status(OK).body(userService.getById(UUID.fromString(id)));
    }
    
    @PostMapping
    public ResponseEntity<Void> postUser(@RequestBody User user) throws InvalidRequestException {
        userService.persist(user);
        return ResponseEntity.status(CREATED).build();
    }
    
    @DeleteMapping
    public ResponseEntity<Void> deleteUserById(@RequestParam(name = "userId")String id) throws InvalidRequestException {
        userService.deleteById(UUID.fromString(id));
        return ResponseEntity.status(OK).build();
    }
    
    @PutMapping
    public ResponseEntity<Void> updateUser(@RequestParam(name = "userId")String id, @RequestBody User user) throws InvalidRequestException {
        userService.updateUser(UUID.fromString(id),user);
        return ResponseEntity.status(OK).build();
    }
    
}
