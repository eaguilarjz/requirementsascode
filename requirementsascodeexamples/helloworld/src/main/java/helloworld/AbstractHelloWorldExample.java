package helloworld;

import java.util.Scanner;

import helloworld.usercommand.EnterText;

public class AbstractHelloWorldExample {
	private Scanner scanner;
	private boolean isSystemStopped;

	public AbstractHelloWorldExample() {
		this.scanner = new Scanner(System.in);
		isSystemStopped = false;
	}

	protected EnterText entersText() {
		String text = scanner.next();
		return new EnterText(text);
	}

	protected void stop() {
		isSystemStopped = true;
	}

	protected boolean systemStopped() {
		return isSystemStopped;
	}

	protected void exitSystem() {
		System.out.println("Exiting system!");
		scanner.close();
		System.exit(0);
	}
}
