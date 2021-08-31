package com.firstapp.firstapp.Controller;

import com.firstapp.firstapp.exceptions.UserException;
import com.firstapp.firstapp.requests.UserRequest;
import com.firstapp.firstapp.responses.ErrorMessages;
import com.firstapp.firstapp.responses.UserResponse;
import com.firstapp.firstapp.services.UserService;
import com.firstapp.firstapp.shared.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@CrossOrigin("*")
@RestController
@RequestMapping("/users")
public class UserController
{

    // L'injection de dépendance
    @Autowired
    UserService userService;

    @GetMapping(path = "/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String id)
    {
        UserDto userDto = userService.getUserByUserId(id);

        UserResponse userResponse = new UserResponse();

        BeanUtils.copyProperties(userDto, userResponse);

        return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);

    }

    // Récupérer une liste des utilisateurs : avec pagination
    @GetMapping
    public List<UserResponse> getAllUsers(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "limit", defaultValue = "3") int limit)
    {
        List<UserResponse> usersResponse = new ArrayList<>();

        // Récupérer la liste des users
        List<UserDto> users = userService.getUsers(page, limit);

        // pour chaque objet userDto de type users
        for(UserDto userDto: users)
        {
            UserResponse user = new UserResponse();

            BeanUtils.copyProperties(userDto, user);

            // je rajoute chaque objet user dans la list List<UserDto>
            usersResponse.add(user);
        }

        return usersResponse;
    }



    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest userRequest)
            throws Exception
    {

        // gérer une exception une fois le firstname est Null
        if(userRequest.getFirstName().isEmpty())
        throw new UserException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

        // Couche représenttaion
        /* Créer un objet de type UserDto */
        // UserDto userDto = new UserDto();

        /* Modelmapper est le meilleur module de relier les objets entre eux */
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userRequest, UserDto.class);

        /* Faire passer les infos de userRequest vers userDto, contenant 2 params : d'où vient les données et où les mettez. */
        // BeanUtils.copyProperties(userRequest, userDto);


        // Couche Service
        /* faire passer les infos vers la couche service pour les traiter et vice-versa, via la classe UserDto (l'intermédiaire) */
        UserDto createUser = userService.createUser(userDto);


        // Créer une réponse, copier les données de createUser vers userResponse
        // UserResponse userResponse = new UserResponse();
        // BeanUtils.copyProperties(createUser, userResponse);

        UserResponse userResponse = modelMapper.map(createUser, UserResponse.class);

        // Retourner le User créé
        return new ResponseEntity<UserResponse>(userResponse, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable String id, @RequestBody UserRequest userRequest)
    {
        UserDto userDto = new UserDto();

        BeanUtils.copyProperties(userRequest, userDto);

        UserDto updateUser = userService.updateUser(id, userDto);

        UserResponse userResponse = new UserResponse();

        BeanUtils.copyProperties(updateUser, userResponse);

        return new ResponseEntity<UserResponse>(userResponse, HttpStatus.ACCEPTED);

    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable String id)
    {
        userService.deleteUser(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
