package se.fredsfursten.jumppadplugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SavingAndLoadingBinary {
	public static <T extends Object> void save(T obj,String path) throws Exception
	{
		File file = new File(path);
		File directory = file.getParentFile();
		if (!directory.exists())
		{
			directory.mkdirs();
		}
		
		FileOutputStream fileOutputStream = new FileOutputStream(path);
		ObjectOutputStream oos = new ObjectOutputStream(fileOutputStream);

		oos.writeObject(obj);
		oos.flush();
		oos.close();
	}
	
	public static <T extends Object> T load(String path) throws Exception
	{
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
		@SuppressWarnings("unchecked")
		T result = (T)ois.readObject();
		ois.close();
		return result;
	}
}
