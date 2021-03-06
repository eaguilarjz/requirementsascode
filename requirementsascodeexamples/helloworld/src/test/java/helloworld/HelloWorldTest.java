package helloworld;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Before;
import org.junit.Test;
import org.requirementsascode.Model;
import org.requirementsascode.ModelRunner;

import helloworld.usercommand.EnterText;

public class HelloWorldTest {
	private ModelRunner modelRunner;
	private Model model;

	@Before
	public void setUp() throws Exception {
		modelRunner = new ModelRunner().startRecording();
	}

	@Test
	public void testHelloWorld01() {
		HelloWorld01 example = new HelloWorld01();
		model = example.buildModel();

		modelRunner.run(model);

		assertRecordedStepNames("S1");
	}

	@Test
	public void testHelloWorld02() {
		HelloWorld02 example = new HelloWorld02();
		model = example.buildModel();

		modelRunner.run(model);

		assertRecordedStepNames("S1", "S1", "S1");
	}

	@Test
	public void testHelloWorld03() {
		HelloWorld03 example = new HelloWorld03();
		model = example.buildModel();

		modelRunner.run(model).reactTo(new EnterText("John Q. Public"));

		assertRecordedStepNames("S1", "S2");
	}

	@Test
	public void testHelloWorld03a() {
		HelloWorld03a example = new HelloWorld03a();
		model = example.buildModel();

		modelRunner.run(model);

		modelRunner.as(example.invalidUser()).reactTo(new EnterText("Ignored"));
		modelRunner.as(example.validUser()).reactTo(new EnterText("John Q. Public"));

		assertRecordedStepNames("S1", "S2");
	}

	@Test
	public void testHelloWorld04() {
		HelloWorld04 example = new HelloWorld04();
		model = example.buildModel();

		modelRunner.run(model).reactTo(new EnterText("John"), new EnterText("39"));

		assertRecordedStepNames("S1", "S2", "S3", "S4", "S5");
	}

	@Test
	public void testHelloWorld05_WithCorrectNameAndAge() {
		HelloWorld05 example = new HelloWorld05();
		model = example.buildModel();

		modelRunner.run(model).reactTo(new EnterText("John"), new EnterText("39"));

		assertRecordedStepNames("S1", "S2", "S3", "S4", "S5", "S6");
	}

	@Test
	public void testHelloWorld05_WithOutOfBoundsAge() {
		HelloWorld05 example = new HelloWorld05();
		model = example.buildModel();

		modelRunner.run(model).reactTo(new EnterText("John"), new EnterText("1000"));

		assertRecordedStepNames("S1", "S2", "S3", "S4", "S5a_1", "S5a_2", "S3");
	}

	@Test
	public void testHelloWorld05_WithNonNumericalAge() {
		HelloWorld05 example = new HelloWorld05();
		model = example.buildModel();

		modelRunner.run(model).reactTo(new EnterText("John"), new EnterText("NON-NUMERICAL-AGE"));

		assertRecordedStepNames("S1", "S2", "S3", "S4", "S5b_1", "S5b_2", "S3");
	}

	@Test
	public void testHelloWorld06_AsNormalUser() {
		HelloWorld06 example = new HelloWorld06();
		model = example.buildModel();

		modelRunner.as(example.normalUser()).run(model).reactTo(new EnterText("John"), new EnterText("39"));

		assertRecordedStepNames("S1", "S2", "S3", "S4", "S5", "S6", "S7");
	}

	@Test
	public void testHelloWorld06_AsAnonymousUserAgeIsOk() {
		HelloWorld06 example = new HelloWorld06();
		model = example.buildModel();

		modelRunner.as(example.anonymousUser()).run(model).reactTo(new EnterText("39"));

		assertRecordedStepNames("S1a_1", "S3", "S4", "S5c_1", "S6", "S7");
	}

	@Test
	public void testHelloWorld06_AsAnonymousUserHandleNonNumericalAge() {
		HelloWorld06 example = new HelloWorld06();
		model = example.buildModel();

		modelRunner.as(example.anonymousUser()).run(model).reactTo(new EnterText("NotANumber"));

		assertRecordedStepNames("S1a_1", "S3", "S4", "S5b_1", "S5b_2", "S3");
	}

	protected void assertRecordedStepNames(String... expectedStepNames) {
		String[] actualStepNames = modelRunner.getRecordedStepNames();
		assertArrayEquals(expectedStepNames, actualStepNames);
	}
}
