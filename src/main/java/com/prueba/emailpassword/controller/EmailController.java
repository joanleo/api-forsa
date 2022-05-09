package com.prueba.emailpassword.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.emailpassword.dto.ChangePasswordDTO;
import com.prueba.emailpassword.dto.EmailDTO;
import com.prueba.emailpassword.service.EmailService;
import com.prueba.security.entity.Usuario;
import com.prueba.security.repository.UsuarioRepository;

import net.bytebuddy.utility.RandomString;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/email-password")
public class EmailController {

    @Autowired
    EmailService emailService;
    
    @Autowired
    UsuarioRepository usuarioRepo;

    @Value("${spring.mail.username}")
    private String mailFrom;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @PostMapping("/send-email")
    public ResponseEntity<?> sendEmailTemplate(@RequestBody EmailDTO dto) {
    	
    	Optional<Usuario> usuarioOpt = usuarioRepo.findByUsernameOrEmail(dto.getMailTo(), dto.getMailTo());
    	if(!usuarioOpt.isPresent()) {
    		return new ResponseEntity<String>("Correo enviado con éxito", HttpStatus.OK);
    	}
    	Usuario usuario = usuarioOpt.get(); 
    	dto.setMailFrom(mailFrom);
    	dto.setMailTo(usuario.getEmail());
    	
    	dto.setSubject("Cambio de contraseña");
    	dto.setUserName(usuario.getNombre());
    	//UUID uuid = UUID.randomUUID();
    	String uuid = RandomString.make(6);
    	String tokenPassword = uuid;//.toString();
    	String newPassword = passwordEncoder.encode(tokenPassword);
    	dto.setTokenPassword(tokenPassword);
    	//usuario.setTokenPassword(tokenPassword);
    	usuario.setPassword(newPassword);
    	usuarioRepo.save(usuario);
        emailService.sendEmail(dto);
        return new ResponseEntity<String>("Correo enviado con éxito", HttpStatus.OK);
    }
    
    @ApiIgnore
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDTO dto, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return new ResponseEntity<String>("Campos mal puestos", HttpStatus.BAD_REQUEST);
        if(!dto.getPassword().equals(dto.getConfirmPassword()))
            return new ResponseEntity<String>("Las contraseñas no coinciden", HttpStatus.BAD_REQUEST);
        Optional<Usuario> usuarioOpt = usuarioRepo.findByTokenPassword(dto.getTokenPassword());
        if(!usuarioOpt.isPresent())
            return new ResponseEntity<String>("No existe ningún usuario con esas credenciales", HttpStatus.NOT_FOUND);
        Usuario usuario = usuarioOpt.get();
        String newPassword = passwordEncoder.encode(dto.getPassword());
        usuario.setPassword(newPassword);
        usuario.setTokenPassword(null);
        usuarioRepo.save(usuario);
        return new ResponseEntity<String>("Contraseña actualizada", HttpStatus.OK);
    }
}
