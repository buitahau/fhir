package owt.training.fhir.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import owt.training.fhir.security.JwtTokenProvider;
import owt.training.fhir.security.dto.JwtRequestDto;
import owt.training.fhir.security.dto.JwtResponseDto;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenUtil;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<JwtResponseDto> authenticate(@RequestBody JwtRequestDto jwtRequest) throws Exception {

        Authentication authentication = authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());

        final String token = jwtTokenUtil.generateToken(authentication);

        return ResponseEntity.ok(new JwtResponseDto(token));
    }

    private Authentication authenticate(String username, String password) throws Exception {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
