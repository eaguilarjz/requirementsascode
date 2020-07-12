package org.requirementsascode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class ActorWithBehaviorTest extends AbstractTestCase{

	@Before
	public void setup() {
		setupWithRecordingModelRunner();
	}
	
  @Test
  public void actorDoesntDoAnything() {
  	Optional<Object> latestPublishedEvent = customer.reactTo(entersText());
  	assertFalse(customer.getBehavior().isPresent());
		assertTrue(latestPublishedEvent.isEmpty());
  }
	
  @Test
  public void actorReactsToEvent() {
		Model model = modelBuilder
			.step(CUSTOMER_ENTERS_TEXT).on(EntersText.class).system(displaysEnteredText())
		.build();
	
		customer.withBehavior(model).reactTo(entersText());
  	assertTrue(customer.getBehavior().isPresent());

		Optional<Step> latestStepRun = customer.getModelRunner().getLatestStep();
		assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
  }
  
  @Test
  public void actorReactsToTwoEvents() {
		Model model = modelBuilder
			.step(CUSTOMER_ENTERS_TEXT).on(EntersText.class).system(displaysEnteredText())
			.step(CUSTOMER_ENTERS_NUMBER).on(EntersNumber.class).system(displaysEnteredNumber())
		.build();
	
		customer.withBehavior(model).reactTo(entersText());
		Optional<Step> latestStepRun = customer.getModelRunner().getLatestStep();
		assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
	
		customer.reactTo(entersNumber());
		latestStepRun = customer.getModelRunner().getLatestStep();
		assertEquals(EntersNumber.class, latestStepRun.get().getMessageClass());
  }
  
  @Test
  public void actorReactsIfActorInModelMatches() {
		Model model = modelBuilder.useCase(USE_CASE).as(customer)
			.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
		.build();
	
		customer.withBehavior(model).reactTo(entersText());

		Optional<Step> latestStepRun = customer.getModelRunner().getLatestStep();
		assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
  }
  
  @Test
  public void actorDoesnReactIfActorInModelIsDifferent() {
  	Actor invalidActor = new Actor("InvalidUser");

		Model model = modelBuilder.useCase(USE_CASE).as(customer)
			.step(CUSTOMER_ENTERS_TEXT).user(EntersText.class).system(displaysEnteredText())
		.build();
	
		invalidActor.withBehavior(model).reactTo(entersText());

		Optional<Step> latestStepRun = invalidActor.getModelRunner().getLatestStep();
		assertFalse(latestStepRun.isPresent());
  }
  
  @Test
  public void twoActorsInteract() {
		Model model2 = Model.builder()
			.step(CUSTOMER_ENTERS_TEXT).on(EntersText.class).system(displaysEnteredText())
		.build();
		partner2.withBehavior(model2);
		
		Model model1 = modelBuilder
			.step(CUSTOMER_ENTERS_TEXT).on(EntersText.class).systemPublish(et -> et).to(partner2)
		.build();
		partner.withBehavior(model1).reactTo(entersText());

		Optional<Step> latestStepRun = partner2.getModelRunner().getLatestStep();
		assertEquals(EntersText.class, latestStepRun.get().getMessageClass());
  }
}