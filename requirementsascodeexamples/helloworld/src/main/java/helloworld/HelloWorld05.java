package helloworld;

import java.util.function.Consumer;

import org.requirementsascode.Condition;
import org.requirementsascode.Model;
import org.requirementsascode.ModelRunner;

import helloworld.usercommand.EnterText;

public class HelloWorld05 extends AbstractHelloWorldExample {
	private final Runnable asksForName = this::askForName;
	private final Class<EnterText> entersName = EnterText.class;
	private final Consumer<EnterText> savesName = this::saveName;
	private final Runnable asksForAge = this::askForAge;
	private final Class<EnterText> entersAge = EnterText.class;
	private final Consumer<EnterText> savesAge = this::saveAge;
	private final Runnable greetsUser = this::greetUser;
	private final Runnable stops = super::stop;
	private final Condition ageIsOutOfBounds = this::ageIsOutOfBounds;
	private final Runnable displaysAgeIsOutOfBounds = this::displaysAgeIsOutOfBounds;
	private final Consumer<NumberFormatException> displaysAgeIsNonNumerical = this::displayAgeIsNonNumerical;
	private final Class<NumberFormatException> numberFormatException = NumberFormatException.class;

	private static final int MIN_AGE = 5;
	private static final int MAX_AGE = 130;

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
					.step("S6").system(stops)
				.flow("Handle out-of-bounds age").insteadOf("S5").condition(ageIsOutOfBounds)
					.step("S5a_1").system(displaysAgeIsOutOfBounds)
					.step("S5a_2").continuesAt("S3")
				.flow("Handle non-numerical age").insteadOf("S5")
					.step("S5b_1").on(numberFormatException).system(displaysAgeIsNonNumerical)
					.step("S5b_2").continuesAt("S3")
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

	private boolean ageIsOutOfBounds() {
		return age < MIN_AGE || age > MAX_AGE;
	}

	private void displaysAgeIsOutOfBounds() {
		System.out.println("Please enter your real age, between " + MIN_AGE + " and " + MAX_AGE);
	}

	private void displayAgeIsNonNumerical(NumberFormatException exception) {
		System.out.println("You entered a non-numerical age.");
	}

	public static void main(String[] args) {
		HelloWorld05 example = new HelloWorld05();
		example.start();
	}

	private void start() {
		Model model = buildModel();
		ModelRunner modelRunner = new ModelRunner().run(model);
		while (!systemStopped())
			modelRunner.reactTo(entersText());
		exitSystem();
	}
}
