package akkaserver.commons.behaviour;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akkaserver.transactions.model.Transaction;

import java.io.Serializable;
import java.util.List;

public class ServerBehavior extends AbstractBehavior<ServerBehavior.Command> {

    private ServerBehavior(ActorContext<Command> context) {
        super(context);
    }

    public static Behavior<Command> create(){
        return Behaviors.setup(ServerBehavior::new);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(ReceiveListOfTransactionsCommand.class, command ->{
                    ActorRef<TransactionManagerBehavior.Command> transactionManager = getContext().spawn(TransactionManagerBehavior.create(), "tranaactionManager");
                    transactionManager.tell(new TransactionManagerBehavior.GetListOfTransactionsCommand(command.sender));
                    return Behaviors.same();
                })
                .build();
    }

    public static interface Command extends Serializable{}

    public static class ReceiveListOfTransactionsCommand implements Command{

        long serialVersionUID = 1L;
        ActorRef<List<Transaction>> sender;

        public ReceiveListOfTransactionsCommand(ActorRef<List<Transaction>> sender) {
            this.sender = sender;
        }

        public ActorRef<List<Transaction>> getSender() {
            return sender;
        }

        public void setSender(ActorRef<List<Transaction>> sender) {
            this.sender = sender;
        }
    }
}
