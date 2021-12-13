package akkaserver.commons.behaviour;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akkaserver.transactions.model.Transaction;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class TransactionManagerBehavior extends AbstractBehavior<TransactionManagerBehavior.Command> {

    private TransactionManagerBehavior(ActorContext<Command> context) {
        super(context);
    }

    public static Behavior<Command> create(){
        return Behaviors.setup(TransactionManagerBehavior::new);
    }

    List<Transaction> transactions = List.of(
            new Transaction(1, "kdhakfhadfjaade", BigDecimal.valueOf(762)),
            new Transaction(2, "hldhfiefnaf", BigDecimal.valueOf(500))
    );

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(GetListOfTransactionsCommand.class, command ->{
                    command.sender.tell(transactions);
                    return Behaviors.stopped();
                })
                .build();
    }

    public static interface Command extends Serializable {}

    public static class GetListOfTransactionsCommand implements Command{
        long serialVersionUID = 1L;
        ActorRef<List<Transaction>> sender;

        public GetListOfTransactionsCommand(ActorRef<List<Transaction>> sender) {
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






