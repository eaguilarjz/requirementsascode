package org.requirementsascode.builder;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.requirementsascode.Condition;
import org.requirementsascode.Model;

/**
 * Part used by the {@link ModelBuilder} to build a {@link Model}.
 * 
 * @author b_muth
 */
public class FlowlessSystemPart<T> {
	private UseCasePart useCasePart;
	private long flowlessStepCounter;

	private FlowlessSystemPart(UseCasePart useCasePart, long flowlessStepCounter) {
		this.useCasePart = useCasePart;
		this.flowlessStepCounter = flowlessStepCounter;
	}

	static <T> FlowlessSystemPart<T> flowlessSystemPart(StepUserPart<T> stepUserPart, Runnable systemReaction,
		long flowlessStepCounter) {
		StepSystemPart<T> stepSystemPart = stepUserPart.system(systemReaction);
		UseCasePart useCasePart = stepSystemPart.getStepPart().getUseCasePart();
		return new FlowlessSystemPart<>(useCasePart, flowlessStepCounter);
	}

	static <T> FlowlessSystemPart<T> flowlessSystemPart(StepUserPart<T> stepUserPart, Consumer<? super T> systemReaction,
		long flowlessStepCounter) {
		StepSystemPart<T> stepSystemPart = stepUserPart.system(systemReaction);
		UseCasePart useCasePart = stepSystemPart.getStepPart().getUseCasePart();
		return new FlowlessSystemPart<>(useCasePart, flowlessStepCounter);
	}

	static <T> FlowlessSystemPart<T> flowlessSystemPublishPart(StepUserPart<T> stepUserPart,
		Supplier<? super T> systemReaction, long flowlessStepCounter) {
		StepSystemPart<T> stepSystemPart = stepUserPart.systemPublish(systemReaction);
		UseCasePart useCasePart = stepSystemPart.getStepPart().getUseCasePart();
		return new FlowlessSystemPart<>(useCasePart, flowlessStepCounter);
	}

	static <T> FlowlessSystemPart<T> flowlessSystemPublishPart(StepUserPart<T> stepUserPart,
		Function<? super T, ?> systemReaction, long flowlessStepCounter) {
		StepSystemPart<T> stepSystemPart = stepUserPart.systemPublish(systemReaction);
		UseCasePart useCasePart = stepSystemPart.getStepPart().getUseCasePart();
		return new FlowlessSystemPart<>(useCasePart, flowlessStepCounter);
	}

	/**
	 * Constrains the condition for triggering a system reaction: only if the
	 * specified condition is true, a system reaction can be triggered.
	 *
	 * @param condition the condition that constrains when the system reaction is
	 *                  triggered
	 * @return the created condition part
	 */
	public FlowlessConditionPart condition(Condition condition) {
		FlowlessConditionPart conditionPart = new FlowlessConditionPart(condition, useCasePart, ++flowlessStepCounter);
		return conditionPart;
	}

	/**
	 * Creates a named step.
	 * 
	 * @param stepName the name of the created step
	 * @return the created step part
	 */
	public FlowlessStepPart step(String stepName) {
		Objects.requireNonNull(stepName);
		FlowlessStepPart stepPart = condition(null).step(stepName);
		return stepPart;
	}

	/**
	 * Defines the type of commands that will cause a system reaction.
	 *
	 * <p>
	 * The system reacts to objects that are instances of the specified class or
	 * instances of any direct or indirect subclass of the specified class.
	 *
	 * @param commandClass the class of commands the system reacts to
	 * @param <U>          the type of the class
	 * @return the created user part
	 */
	public <U> FlowlessUserPart<U> user(Class<U> commandClass) {
		Objects.requireNonNull(commandClass);
		FlowlessUserPart<U> flowlessUserPart = condition(null).user(commandClass);
		return flowlessUserPart;
	}

	/**
	 * Defines the type of messages or exceptions that will cause a system reaction.
	 *
	 * <p>
	 * The system reacts to objects that are instances of the specified class or
	 * instances of any direct or indirect subclass of the specified class.
	 *
	 * @param messageClass the class of messages the system reacts to
	 * @param <U>          the type of the class
	 * @return the created user part
	 */
	public <U> FlowlessUserPart<U> on(Class<U> messageClass) {
		Objects.requireNonNull(messageClass);
		FlowlessUserPart<U> flowlessUserPart = condition(null).on(messageClass);
		return flowlessUserPart;
	}

	/**
	 * Creates a new use case in the current model, and returns a part for building
	 * its details. If a use case with the specified name already exists, returns a
	 * part for the existing use case.
	 *
	 * @param useCaseName the name of the existing use case / use case to be
	 *                    created.
	 * @return the created / found use case's part.
	 */
	public UseCasePart useCase(String useCaseName) {
		Objects.requireNonNull(useCaseName);
		UseCasePart newUseCasePart = useCasePart.getModelBuilder().useCase(useCaseName);
		return newUseCasePart;
	}

	/**
	 * Returns the model that has been built.
	 * 
	 * @return the model
	 */
	public Model build() {
		return useCasePart.build();
	}
}