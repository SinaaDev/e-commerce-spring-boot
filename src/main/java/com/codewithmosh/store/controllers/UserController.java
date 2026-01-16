package com.codewithmosh.store.controllers;


import com.codewithmosh.store.dtos.RegisterUserRequest;
import com.codewithmosh.store.dtos.UpdateUserRequest;
import com.codewithmosh.store.dtos.UserDto;
import com.codewithmosh.store.mappers.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @GetMapping
    public Iterable<UserDto> getAllUsers(@RequestParam(required = false, defaultValue = "", name = "sort") String sort) { // when we set the name to "sort" then it no matter what the variable name is we can set it to sortBy
        if(!Set.of("name","email").contains(sort)){
            sort = "name";
        }
        return userRepository.findAll(Sort.by(sort))
                .stream()
//                .map(user -> new UserDto(user.getId(), user.getName(), user.getEmail())).toList(); // OLD WAY
//                .map(user -> userMapper.toDto(user)).toList(); // LONG WAY
                .map(userMapper::toDto).toList(); // SHORT WAY
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
//        var userDto = new UserDto(user.getId(), user.getName(), user.getEmail()); // NO NEED ANYMORE
//        return ResponseEntity.ok(userDto);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(
            @RequestBody RegisterUserRequest request,
            UriComponentsBuilder uriBuilder){
        var user = userMapper.toEntity(request);
        System.out.println(userMapper.toEntity(request));

        userRepository.save(user);
        var userDto = userMapper.toDto(user);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto) ;
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request
            ){
        var user = userRepository.findById(id).orElse(null);
        System.out.println("Found User: " + user);
        if(user == null){
            return ResponseEntity.notFound().build();
        }

        System.out.println("Request User: " + request);
        userMapper.update(request, user);
        System.out.println("Updated User: " + user);

        userRepository.save(user);

        return ResponseEntity.ok(userMapper.toDto(user));
    }
}
