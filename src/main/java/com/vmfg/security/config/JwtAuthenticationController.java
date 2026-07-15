package com.vmfg.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vmfg.authentication.UserLoginDAO;
import com.vmfg.general.response.ResponseAsList;

@RestController
@CrossOrigin

public class JwtAuthenticationController {

	@Autowired
	private UserLoginDAO iuserLoginDAO;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	@CrossOrigin(origins = "*")
	public ResponseEntity<ResponseAsList> createAuthenticationToken(@RequestBody CreateAuthenticationTokenRequest createAuthenticationTokenReq) throws Exception {
		ResponseAsList userDtlInfo  = null;
		authenticate(createAuthenticationTokenReq.getUserName(), createAuthenticationTokenReq.getPassword());

		final UserDetails userDetails = iuserLoginDAO.loadUserByUsername(createAuthenticationTokenReq.getUserName());

		final String token = jwtTokenUtil.generateToken(userDetails);
		
		if(!token.equalsIgnoreCase("")) {
			userDtlInfo =  iuserLoginDAO.getUserDtlForAuth(createAuthenticationTokenReq.getUserName(), createAuthenticationTokenReq.getPassword(),token);
			
		}

		return new ResponseEntity<ResponseAsList>(userDtlInfo, HttpStatus.OK);
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
