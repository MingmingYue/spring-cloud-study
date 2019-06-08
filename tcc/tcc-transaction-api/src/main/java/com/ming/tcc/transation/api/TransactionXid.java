package com.ming.tcc.transation.api;

import javax.transaction.xa.Xid;
import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;

/**
 * Xid 接口是 X/Open 事务标识符 XID 结构的 Java 映射。
 * 此接口指定三个访问器方法，以检索全局事务格式 ID、全局事务 ID 和分支限定符。
 * Xid 接口供事务管理器和资源管理器使用。此接口对应用程序不可见。
 * Create on 2019-06-07.
 */
public class TransactionXid implements Xid, Serializable {

    private static final long serialVersionUID = -6817267250789142043L;

    /**
     * XID 的格式标识符
     */
    private int formatId = 1;

    /**
     * 全局事务ID.
     */
    private byte[] globalTransactionId;

    /**
     * 分支限定符.
     */
    private byte[] branchQualifier;

    public TransactionXid() {
        globalTransactionId = UuidUtils.uuidToByteArray(UUID.randomUUID());
        branchQualifier = UuidUtils.uuidToByteArray(UUID.randomUUID());
    }

    public TransactionXid(byte[] globalTransactionId) {
        this.globalTransactionId = globalTransactionId;
        branchQualifier = UuidUtils.uuidToByteArray(UUID.randomUUID());
    }

    public TransactionXid(byte[] globalTransactionId, byte[] branchQualifier) {
        this.globalTransactionId = globalTransactionId;
        this.branchQualifier = branchQualifier;
    }

    /**
     * 获取 XID 的格式标识符部分。
     */
    @Override
    public int getFormatId() {
        return formatId;
    }

    /**
     * 获取 XID 的全局事务标识符部分作为字节数组。
     */
    @Override
    public byte[] getGlobalTransactionId() {
        return globalTransactionId;
    }

    /**
     * 获取 XID 的事务分支标识符部分作为字节数组。
     */
    @Override
    public byte[] getBranchQualifier() {
        return branchQualifier;
    }

    @Override
    public String toString() {
        return UUID.nameUUIDFromBytes(globalTransactionId).toString() + "|" + UUID.nameUUIDFromBytes(branchQualifier).toString();
    }

    /**
     * 克隆事务ID.
     */
    @Override
    public TransactionXid clone() {

        byte[] cloneGlobalTransactionId = new byte[globalTransactionId.length];
        byte[] cloneBranchQualifier = new byte[branchQualifier.length];

        System.arraycopy(globalTransactionId, 0, cloneGlobalTransactionId, 0, globalTransactionId.length);
        System.arraycopy(branchQualifier, 0, cloneBranchQualifier, 0, branchQualifier.length);

        TransactionXid clone = new TransactionXid(cloneGlobalTransactionId, cloneBranchQualifier);
        return clone;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.getFormatId();
        result = prime * result + Arrays.hashCode(branchQualifier);
        result = prime * result + Arrays.hashCode(globalTransactionId);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        }
        TransactionXid other = (TransactionXid) obj;
        if (this.getFormatId() != other.getFormatId()) {
            return false;
        } else if (!Arrays.equals(branchQualifier, other.branchQualifier)) {
            return false;
        } else if (!Arrays.equals(globalTransactionId, other.globalTransactionId)) {
            return false;
        }
        return true;
    }
}
