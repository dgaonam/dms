package com.openkm.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class BinaryClassLoader extends ClassLoader implements MultipleClassLoader {
	private Hashtable<String, Class<?>> classes = new Hashtable<String, Class<?>>();
	private Hashtable<String, byte[]> resources = new Hashtable<String, byte[]>();
	private String mainClassName = null;
	
	public BinaryClassLoader(byte[] buf) throws IOException {
		super();
		ByteArrayInputStream bais = null;
		JarInputStream jis = null;
		byte[] buffer = new byte[1024 * 4];
		
		try {
			bais = new ByteArrayInputStream(buf);
			jis = new JarInputStream(bais);
			Attributes attr = jis.getManifest().getMainAttributes();
			mainClassName = attr != null ? attr.getValue(Attributes.Name.MAIN_CLASS) : null;
			
			for (JarEntry entry = null; (entry = jis.getNextJarEntry()) != null; ) {
				String name = entry.getName();
				
				if (!entry.isDirectory()) {
					ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
					
					for (int n = 0; -1 != (n = jis.read(buffer)); ) {
						byteStream.write(buffer, 0, n);
			        }
					
					if (name.endsWith(".class")) {
						String className = name.substring(0, name.indexOf('.')).replace('/', '.');
						resources.put(className, byteStream.toByteArray());
					} else {
						resources.put(name, byteStream.toByteArray());
					}
					
					byteStream.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jis != null) {
				try {
					jis.close();
				} catch (IOException e) {
					// Ignore
				}
			}
			
			if (bais != null) {
				try {
					bais.close();
				} catch (IOException e) {
					// Ignore
				}
			}
		}
	}
	
	/**
	 * Get main class name
	 */
	@Override
	public String getMainClassName() throws IOException {
		return mainClassName;
	}
	
	/**
	 * Find class
	 */
	@Override
	public Class<?> findClass(String className) {
		//System.out.println("findClass("+className+")");
		Class<?> ret = classes.get(className);
		
		if (ret != null) {
			return ret;
		}
		
		// Check for system class
		try {
			return findSystemClass(className);
		} catch (ClassNotFoundException e) {
			// Ignore
		}
		
		byte[] classByte = resources.get(className);
		
		if (classByte != null) {
			ret = defineClass(className, classByte, 0, classByte.length, null);  
			classes.put(className, ret);
			resources.remove(className);
			return ret;
		}
		
		return null;
	}
	
	/**
	 * Get resource input stream
	 */
	@Override
	public InputStream getResourceAsStream(String name) {
		//System.out.println("getResourceAsStream("+name);
		byte[] bytes = resources.get(name);
		
		if (bytes != null) {
			return new ByteArrayInputStream(bytes);
		}
		
		return null;
	}
}
