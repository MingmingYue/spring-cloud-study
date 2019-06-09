package com.ming.tcc.transaction.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.ming.tcc.transaction.InvocationContext;
import com.ming.tcc.transaction.Participant;
import com.ming.tcc.transaction.Terminator;
import com.ming.tcc.transaction.Transaction;
import com.ming.tcc.transaction.common.TransactionType;
import com.ming.tcc.transation.api.TransactionStatus;
import com.ming.tcc.transation.api.TransactionXid;

/**
 * Create on 2019-06-08.
 */
public class KryoTransactionSerializer implements ObjectSerializer<Transaction> {

    private static Kryo kryo = null;

    static {
        kryo = new Kryo();
        kryo.register(Transaction.class);
        kryo.register(TransactionXid.class);
        kryo.register(TransactionStatus.class);
        kryo.register(TransactionType.class);
        kryo.register(Participant.class);
        kryo.register(Terminator.class);
        kryo.register(InvocationContext.class);
    }


    @Override
    public byte[] serialize(Transaction transaction) {
        Output output = new Output(256, -1);
        kryo.writeObject(output, transaction);
        return output.toBytes();
    }

    @Override
    public Transaction deserialize(byte[] bytes) {
        Input input = new Input(bytes);
        Transaction transaction = kryo.readObject(input, Transaction.class);
        return transaction;
    }
}