package org.requirementsascode.builder;

import java.util.Optional;
import java.util.function.Predicate;

import org.requirementsascode.UseCaseFlow;
import org.requirementsascode.UseCaseRunner;
import org.requirementsascode.UseCaseStep;
import org.requirementsascode.exception.ElementAlreadyInModel;

public class UseCaseFlowPart {
	private UseCaseFlow useCaseFlow;
	private UseCasePart useCasePart;

	public UseCaseFlowPart(UseCaseFlow useCaseFlow, UseCasePart useCasePart) {
		this.useCaseFlow = useCaseFlow;
		this.useCasePart = useCasePart;
	}

	/**
	 * Creates the first step of this flow, with the specified name.
	 * 
	 * @param stepName the name of the step to be created
	 * @return the newly created step, to ease creation of further steps
	 * @throws ElementAlreadyInModel if a step with the specified name already exists in the use case
	 */
	public UseCaseStepPart step(String stepName) {
		UseCaseStep useCaseStep = 
			useCasePart.useCase().newStep(stepName, useCaseFlow, 
				Optional.empty(), useCaseFlow.flowPredicate());
		return new UseCaseStepPart(useCaseStep, this);
	}

	public UseCaseFlowPart insteadOf(String stepName) {
		useCaseFlow.insteadOf(stepName);
		return this;
	}

	public UseCaseFlowPart after(String stepName) {
		useCaseFlow.after(stepName);	
		return this;
	}

	public UseCaseFlowPart when(Predicate<UseCaseRunner> whenPredicate) {
		useCaseFlow.when(whenPredicate);
		return this;
	}
	
	public UseCaseFlow useCaseFlow(){
		return useCaseFlow;
	}
	
	public UseCasePart useCasePart(){
		return useCasePart;
	}

	public UseCaseModelBuilder useCaseModelBuilder(){
		return useCasePart.useCaseModelBuilder();
	}
}