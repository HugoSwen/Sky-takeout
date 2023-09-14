package com.hugo.service;

import com.hugo.dto.UserLoginDTO;
import com.hugo.entity.User;

public interface UserService {
    User login(UserLoginDTO userLoginDTO);
}
