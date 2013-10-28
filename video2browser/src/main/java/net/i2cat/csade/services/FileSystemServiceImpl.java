package net.i2cat.csade.services;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class FileSystemServiceImpl implements FileSystemService{
	Logger log = LoggerFactory.getLogger(FileSystemService.class);
	private final File userImageFolder;
	private final File attachmentFolder;
	private final Environment env;
	
	@Autowired
	public FileSystemServiceImpl(Environment env){
		log.info("Initializing FileSystem service...");
		this.env = env;
		userImageFolder = new File(env.getProperty("user.images.repository"));
		attachmentFolder = new File(env.getProperty("attachments.repository"));
		initFolders();
	}
	
	private void initFolders(){
		if (!userImageFolder.exists()){
			log.warn("User image folder not found, trying to create it...{}",userImageFolder.mkdir());
		}
		if (!attachmentFolder.exists()){
			log.warn("Attachments folder not found, trying to create it...{}", attachmentFolder.mkdir());
		}
	}

	@Override
	public File getUserImageFolder() {
		return userImageFolder;
	}

	@Override
	public File getAttachementFolder() {
		return attachmentFolder;
	}

	
}
