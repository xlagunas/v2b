package net.i2cat.csade.web.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.i2cat.csade.exceptions.entity.EntityNotFoundException;
import net.i2cat.csade.exceptions.entity.ExistingEntityException;
import net.i2cat.csade.models.Relationship;
import net.i2cat.csade.models.User;
import net.i2cat.csade.models.User.Role;
import net.i2cat.csade.services.FileSystemService;
import net.i2cat.csade.services.RelationshipService;
import net.i2cat.csade.services.UserService;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
@RequestMapping("/users")
public class UserController extends AbstractExceptionController{
	
	private final static Logger log = LoggerFactory.getLogger(UserController.class);
	private final UserService userService;
	private final FileSystemService fileSystemService;
	private final RelationshipService relationshipService;
	
	@Autowired
	public UserController(UserService userService, FileSystemService fileSystemService, RelationshipService relationshipService) {
		log.info("Initializing User controller...");
		this.userService = userService;
		this.fileSystemService = fileSystemService;
		this.relationshipService = relationshipService;
	}
	@RequestMapping(value="/newUser",method=RequestMethod.GET)
	public String createNewUser(){
		return "newUser";
	}
	@RequestMapping(method=RequestMethod.GET)
    public User getLoggedUser() throws EntityNotFoundException{
            String username =  SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findUserByUsername(username);
            return user;
                    
    }
	
	@RequestMapping(value="/{username}", method=RequestMethod.GET)
	public User getUser(@PathVariable("username")String username) throws EntityNotFoundException{
		log.info("REST Interface. User with username {} was requested", username);
		User user;
			user = userService.findUserByUsername(username);
		return user;
	}
	
	@RequestMapping(value="/list", method=RequestMethod.POST)
	public List<User> listUsers(@RequestBody User user){
		log.info("REST Interface. Requesting all Users");
		List<Relationship> relationships = relationshipService.getRelationshipsByProposer(user);
		List<User> contacts = new ArrayList<User>(relationships.size());
		for (Relationship r : relationships) {
			contacts.add(r.getContact());
		}
//		List<User> users = userService.findAll();
		return contacts;
	}
	
	@RequestMapping(value="/create", method=RequestMethod.PUT)
	public User createUser(@RequestBody User user) throws ExistingEntityException{
		log.info("REST Interface. Requesting new User");
		user.setRole(Role.USER);
		return userService.createUser(user);
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public Map isUsernameAvailable(@RequestBody String username){
		log.info("REST Interface. Checking availability of username");
		Map m = new HashMap<String, String>(1);
		m.put("available", userService.isUsernameAvailable(username));
		return m;
	}
	
	@RequestMapping(value="/search/{keyword}", method=RequestMethod.POST)
	public List<User> searchContacts(@PathVariable("keyword")String keyword, @RequestBody User user){
		log.info("REST Interface. searching for contacts of User id: {}",user.getIdUser());
		return this.userService.getNonRelatedUsersMatchingKeyword(user, keyword);

	}
	
	@RequestMapping(value="/updateAvatar/{idUser}", method=RequestMethod.POST)
	public User updateUserImage(MultipartHttpServletRequest mFile, @PathVariable("idUser")Long idUser) throws EntityNotFoundException, IllegalStateException, IOException{
		log.info("REST Interface. Updating Image of User id: {}",idUser);
		User u = userService.findUserById(idUser);
		Iterator<String> it = mFile.getFileNames();
		while(it.hasNext()){
			String filename = it.next();
			MultipartFile file = mFile.getFile(filename);
			if (!file.isEmpty()){
				File imageRepositoryFolder = fileSystemService.getUserImageFolder();
				String outputFilename = String.format("%s.%s", idUser,FilenameUtils.getExtension(file.getOriginalFilename()));
				File destFile = new File(imageRepositoryFolder, outputFilename);
				file.transferTo(destFile);
				if (!u.getThumbnail().equals(outputFilename)){
					u.setThumbnail(outputFilename);
					userService.updateUser(u);
				}
			}
		}
		return u;
	}
	
	@RequestMapping(value="/{idUser}",method=RequestMethod.DELETE)
	@ResponseStatus(value= HttpStatus.OK)
	public void deleteUser(@PathVariable("idUser")Long idUser){
		userService.deleteUser(idUser);
	}
}
