package net.mardling;

import org.python.util.PythonInterpreter;

import java.io.InputStream;
import java.util.Map;

public class JythonExecutor {
	
	public static void execute(String path, Map<String, Object> objs) {
		
		PythonInterpreter interpreter = new PythonInterpreter();
		
		InputStream script = JythonExecutor.class.getResourceAsStream(path);
		
		for(Map.Entry<String, Object> entry : objs.entrySet()) {
			interpreter.set(entry.getKey(), entry.getValue());
		}
		
		interpreter.execfile(script);
		
		interpreter.cleanup();
	}

}
