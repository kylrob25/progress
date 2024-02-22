package me.krob.controller;

import me.krob.model.Role;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @GetMapping
    public List<Role> getAll() {
        return Arrays.asList(Role.values());
    }
}
