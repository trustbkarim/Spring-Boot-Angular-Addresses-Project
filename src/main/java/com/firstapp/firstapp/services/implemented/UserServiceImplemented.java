package com.firstapp.firstapp.services.implemented;

import com.firstapp.firstapp.repositories.UserRepository;
import com.firstapp.firstapp.services.UserService;
import com.firstapp.firstapp.shared.all.Utils;
import com.firstapp.firstapp.shared.dto.UserDto;
import com.firstapp.firstapp.ws.entities.AddressEntity;
import com.firstapp.firstapp.ws.entities.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImplemented implements UserService
{
    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    Utils util;


    @Override
    public UserDto createUser(UserDto user)
    {
        // Check the user if exists
        UserEntity checkUser = userRepository.findByEmail(user.getEmail());
        if(checkUser != null) throw new RuntimeException("User already exists !");

        // UserEntity userEntity = new UserEntity();
        // BeanUtils.copyProperties(user, userEntity);
        
        user.getContact().setContactId(util.generateStringId(30));
        user.getContact().setUser(user);

        ModelMapper modelMapper = new ModelMapper();
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);

        // Afin de persister l'addresse qui correspond au utilisateur en cours !
        for(int i=0; i < user.getAddresses().size(); i++)
        {
            AddressEntity address = user.getAddresses().get(i);
            address.setUser(userEntity);
            address.setAddressId(util.generateStringId(30));
            user.getAddresses().set(i, address);
        }
        

        // Crypter le mot de passe
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        // Générer ID
        userEntity.setUserId(util.generateStringId(32));

        UserEntity newUser = userRepository.save(userEntity);

        // UserDto userDto = new UserDto();
        // BeanUtils.copyProperties(newUser, userDto);

        UserDto userDto = modelMapper.map(newUser, UserDto.class);

        return userDto;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Récupére user conecté de la DB
        UserEntity userEntity = userRepository.findByEmail(email);

        if(userEntity == null)
        throw new UsernameNotFoundException(email);

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }

    // get User object
    @Override
    public UserDto getUser(String email)
    {
        // Récupére user connecté de la DB
        UserEntity userEntity = userRepository.findByEmail(email);

        if(userEntity == null)
        throw new UsernameNotFoundException(email);

        // Créer UserDto object, pour que je puiss le retourner
        UserDto userDto = new UserDto();

        BeanUtils.copyProperties(userEntity, userDto);

        return userDto;
    }

    @Override
    public UserDto getUserByUserId(String userId) {

        // Récupére le user via son ID de la DB
        UserEntity userEntity = userRepository.findByUserId(userId);

        // Check if null
        if(userEntity == null)
        throw new UsernameNotFoundException(userId);

        // Créer UserDto object, pour que je puiss le retourner
        UserDto userDto = new UserDto();

        BeanUtils.copyProperties(userEntity, userDto);

        return userDto;
    }

    @Override
    public UserDto updateUser(String userId, UserDto userDto) {

        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity == null)
        throw new UsernameNotFoundException(userId);

        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());

        UserEntity userUpdated = userRepository.save(userEntity);

        UserDto userDtoUpdated = new UserDto();

        BeanUtils.copyProperties(userUpdated, userDtoUpdated);

        return userDtoUpdated;
    }

    @Override
    public void deleteUser(String userId) {

        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity == null)
        throw new UsernameNotFoundException(userId);

        userRepository.delete(userEntity);
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {

        // débuter la numérotation des page de 1
        if(page > 0) page -= 1;

        List<UserDto> usersDto = new ArrayList<>();

        // initialiser pageable request
        Pageable pageableRequest = PageRequest.of(page, limit);

        Page<UserEntity> userPage = userRepository.findAllUserByFirstName(pageableRequest);

        List<UserEntity> users = userPage.getContent();

        for (UserEntity userEntity: users)
        {
            ModelMapper modelMapper = new ModelMapper();            
            UserDto user = modelMapper.map(userEntity, UserDto.class);

            usersDto.add(user);
        }

        return usersDto;
    }
}
