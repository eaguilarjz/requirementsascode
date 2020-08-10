package org.requirementsascode.flowposition;

import org.requirementsascode.FlowStep;
import org.requirementsascode.ModelRunner;
import org.requirementsascode.UseCase;

public class InsteadOf extends FlowPosition{
	public InsteadOf(UseCase useCase, String stepName) {
	  super(useCase, stepName);
  }

  @Override
	protected boolean isRunnerAtRightPositionFor(FlowStep step, ModelRunner modelRunner) {
		FlowStep previousStep = step.getPreviousStepInFlow().orElse(null);
		After afterPreviousStep = after(previousStep);
		return afterPreviousStep.test(modelRunner);
	}
  
  private After after(FlowStep lastFlowStep) {
    UseCase useCase = lastFlowStep == null? null: lastFlowStep.getUseCase();
    String stepName = lastFlowStep == null? null: lastFlowStep.getName();
    After afterLastFlowStep = new After(useCase, stepName);
    return afterLastFlowStep;
  }
}
