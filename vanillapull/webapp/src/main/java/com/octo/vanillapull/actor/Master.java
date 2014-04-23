package com.octo.vanillapull.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.routing.RoundRobinRouter;

public class Master extends UntypedActor {
	private final ActorRef workerRouter;

    public int processors = Runtime.getRuntime().availableProcessors();

	public Master(final double interestRate) {
		workerRouter = this.getContext().actorOf(
				new Props(new UntypedActorFactory() {
					public UntypedActor create() {
						return new Worker(interestRate);
					}
				}).withRouter(new RoundRobinRouter(
						processors)), "workerRouter");
	}

	
	
	public void onReceive(Object message) {
		if (message instanceof Work) {
			Work work = (Work) message;

			for (int i = 0; i < processors; i++) {
				workerRouter.tell(work, getSender());
			}
			return;
		} 
		unhandled(message);
	}
}
