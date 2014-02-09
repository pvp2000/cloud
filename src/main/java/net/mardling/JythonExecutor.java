package net.mardling;

import org.python.util.PythonInterpreter;
import java.io.InputStream;

public class JythonExecutor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PythonInterpreter interpreter = new PythonInterpreter();

		InputStream script = JythonExecutor.class.getResourceAsStream("/HelloWorld.jy");
		interpreter.set("Words","Hello World!");
		interpreter.execfile(script);

	}

}
