package net.i2cat.csade.services;

import java.io.File;

public interface FileSystemService {
	public enum FolderType {
		USER_IMAGE, ATTACHMENT
	}

	public File getUserImageFolder();

	public File getAttachementFolder();


}
