package org.requirementsascode;

import org.requirementsascode.exception.ElementAlreadyInModel;
import org.requirementsascode.flowposition.FlowPosition;

/**
 * Part used by the {@link ModelBuilder} to build a {@link Model}.
 *
 * @see Flow#getCondition()
 * @author b_muth
 */
public class FlowConditionPart {
	private FlowPositionPart flowPositionPart;
	private Condition condition;

	FlowConditionPart(FlowPositionPart flowPositionPart, Condition condition) {
		this.flowPositionPart = flowPositionPart;
		this.condition = condition;
	}
	
	/**
	 * Creates the first step of this flow. It can be run when the runner is at the
	 * right position.
	 *
	 * @param stepName the name of the step to be created
	 * @return the newly created step part, to ease creation of further steps
	 * @throws ElementAlreadyInModel if a step with the specified name already
	 *                               exists in the use case
	 */
	public StepPart step(String stepName) {
		FlowPart flowPart = flowPositionPart.getFlowPart();
		UseCasePart useCasePart = flowPart.getUseCasePart();
		UseCase useCase = useCasePart.getUseCase();
		Flow flow = flowPart.getFlow();
		FlowPosition flowPosition = flowPositionPart.getFlowPosition();
		FlowStep step = useCase.newInterruptingFlowStep(stepName, flow, flowPosition, getCondition());
		StepPart stepPart = new StepPart(step, useCasePart, flowPart);
		return stepPart;
	}
	
	Condition getCondition() {
		return condition;
	}
}