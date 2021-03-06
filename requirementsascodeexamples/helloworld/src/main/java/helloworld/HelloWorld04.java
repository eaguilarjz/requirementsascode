package helloworld;

import java.util.function.Consumer;

import org.requirementsascode.Model;
import org.requirementsascode.ModelRunner;

import helloworld.usercommand.EnterText;

public class HelloWorld04 extends AbstractHelloWorldExample {
	private final Runnable asksForName = this::askForName;
	private final Class<EnterText> entersName = EnterText.class;
	private final Consumer<EnterText> savesName = this::saveName;
	private final Runnable asksForAge = this::askForAge;
	private final Class<EnterText> entersAge = EnterText.class;
	private final Consumer<EnterText> savesAge = this::saveAge;
	private final Runnable greetsUser = this::greetUser;

  private String firstName;
  private int age;

  public Model buildModel() {
  	Model model = Model.builder()
  		.useCase("Get greeted")
  			.basicFlow()
  				.step("S1").system(asksForName)
  				.step("S2").user(entersName).system(savesName)
  				.step("S3").system(asksForAge)
  				.step("S4").user(entersAge).system(savesAge)
  				.step("S5").system(greetsUser)
  		.build();
  	
  	return model;
  }

	private void askForName() {
		System.out.print("Please enter your name: ");
	}

	private void saveName(EnterText enterText) {
		firstName = enterText.text;
	}

	private void askForAge() {
		System.out.print("Please enter your age: ");
	}

	private void saveAge(EnterText enterText) {
		age = Integer.parseInt(enterText.text);
	}

	private void greetUser() {
		System.out.println("Hello, " + firstName + " (" + age + ").");
	}

	public static void main(String[] args) {
		HelloWorld04 example = new HelloWorld04();
		example.start();
	}

	private void start() {
		Model model = buildModel();
		ModelRunner modelRunner = new ModelRunner().run(model);
		modelRunner.reactTo(entersText());
		modelRunner.reactTo(entersText());
	}
}
