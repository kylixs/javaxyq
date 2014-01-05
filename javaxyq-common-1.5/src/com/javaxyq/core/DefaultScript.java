package com.javaxyq.core;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.EventListener;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import com.javaxyq.event.PanelListener;
import com.javaxyq.event.SceneListener;

/**
 * ƒ¨»œΩ≈±æ”Ô—‘“˝«Ê£®java£©
 * @author gongdewei
 * @date 2010-6-27 create
 */
public class DefaultScript implements ScriptEngine{

	private static DefaultScript instance = new DefaultScript();
	public static DefaultScript getInstance() {
		return instance;
	}
	/**
	 * @param args
	 */
//	public static void main(String[] args) {
//		DefaultScript.getInstance().compileAndLoadClass("ui.mainwin");
//	}
	private String classesDir = "tmp/script_classes";

	private boolean debug;
	private String sourceDir = "scripts/";

	private DefaultScript() {
	}

	private String classToJava(String classname) {
		return sourceDir +classname.replace('.', '/')+".java";
	}

	public boolean compile(String filename) {
		File dir = new File(classesDir);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		System.out.println("±‡“Î" + filename + " ..");
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		DiagnosticCollector diagnostics = new DiagnosticCollector();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(
				diagnostics, null, null);
		Iterable compilationUnits = fileManager
				.getJavaFileObjectsFromStrings(Arrays.asList(filename));
		String[] options = new String[] {"-d",classesDir};
		JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager,
				diagnostics, Arrays.asList(options) , null, compilationUnits);
		boolean success = task.call();
		try {
			fileManager.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println((success) ? "±‡“Î≥…π¶." : "±‡“Î ß∞‹!");
		if (!success) {
			List<Diagnostic> list = diagnostics.getDiagnostics();
			for (Diagnostic dia : list) {
				System.out.println(dia);
			}
		}
		return success;
	}

//	public Object compileAndLoadClass(String classname) {
//		compile(classToJava(classname));
//		return loadClass(classname);
//	}

	public String getClassesDir() {
		return classesDir;
	}

	public String getSourceDir() {
		return sourceDir;
	}
	
	@Override
	public boolean isDebug() {
		return debug;
	}

	public Object loadClass(String classname) {
		try {
			URLClassLoader classLoader = new URLClassLoader(new URL[] { new File(
					classesDir).toURI().toURL() });
			return classLoader.loadClass(classname).newInstance();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.err.println("ClassNotFound: "+e.getMessage());
			//e.printStackTrace();
		}
		return null;
	}

	@Override
	public <T> T loadClass(String classname, Class<T> clazz) {
		try {
			Object classobj = loadClass(classname);
			return (T) classobj;
		} catch (ClassCastException e) {
			System.err.println("º”‘ÿΩ≈±æ ß∞‹£¨¿‡–Õ◊™ªª¥ÌŒÛ£∫"+e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public EventListener loadNPCScript(String npcId) {
		try {
			EventListener obj = (EventListener) loadClass("npc.n"+npcId);
			if(isDebug() || obj==null) {
				compile("scripts/npc/n"+npcId+".java");
			}
			return (EventListener) loadClass("npc.n"+npcId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public SceneListener loadSceneScript(String sceneId) {
		try {
			SceneListener obj = (SceneListener) loadClass("scene.s"+sceneId);
			if(isDebug() || obj==null) {
				compile("scripts/scene/s"+sceneId+".java");
			}
			return (SceneListener) loadClass("scene.s"+sceneId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public PanelListener loadUIScript(String id) {
		try {
			PanelListener obj = (PanelListener) loadClass("ui."+id);
			if(isDebug() || obj==null) {
				compile("scripts/ui/"+id+".java");
			}
			return (PanelListener) loadClass("ui."+id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public void setClassesDir(String classesDir) {
		this.classesDir = classesDir;
	}

	@Override
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	public void setSourceDir(String sourceDir) {
		this.sourceDir = sourceDir;
	}

	@Override
	public void clearCache() {
		File dir = new File(classesDir);
		if(dir.exists()) {
			deleteAll(dir);
		}
	}

	private void deleteAll(File target) {
		if(target.isDirectory()) {
			File[] files = target.listFiles();
			for (File file : files) {
				deleteAll(file);
			}
		}
		target.delete();
	}

}
