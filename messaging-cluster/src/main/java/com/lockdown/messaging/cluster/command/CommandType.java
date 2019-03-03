package com.lockdown.messaging.cluster.command;

import com.lockdown.messaging.cluster.exception.MessagingSerializeException;
import com.lockdown.messaging.cluster.sockethandler.ProtostuffUtils;

public enum CommandType implements CommandConverter {

    REGISTER_ASK((short) 1) {
        @Override
        public NodeCommand bytesToCommand(byte[] content) {
            try {
                if (null == content || content.length == 0) {
                    throw new IllegalArgumentException(" content can not be empty !");
                }
                return ProtostuffUtils.bytesToMessage(content, NodeRegister.class);
            } catch (IllegalAccessException | InstantiationException e) {
                throw new MessagingSerializeException(e);
            }
        }

        @Override
        public byte[] CommandToBytes(NodeCommand command) {
            return ProtostuffUtils.messageToBytes(command);
        }
    },
    REGISTER_FORWARD((short) 2) {
        @Override
        public NodeCommand bytesToCommand(byte[] content) {
            try {
                if (null == content || content.length == 0) {
                    throw new IllegalArgumentException(" content can not be empty !");
                }
                return ProtostuffUtils.bytesToMessage(content, NodeRegisterForward.class);
            } catch (IllegalAccessException | InstantiationException e) {
                throw new MessagingSerializeException(e);
            }
        }

        @Override
        public byte[] CommandToBytes(NodeCommand command) {
            return ProtostuffUtils.messageToBytes(command);
        }
    },
    GREETING((short) 3) {
        @Override
        public NodeCommand bytesToCommand(byte[] content) {
            try {
                if (null == content || content.length == 0) {
                    throw new IllegalArgumentException(" content can not be empty !");
                }
                return ProtostuffUtils.bytesToMessage(content, NodeGreeting.class);
            } catch (IllegalAccessException | InstantiationException e) {
                throw new MessagingSerializeException(e);
            }
        }

        @Override
        public byte[] CommandToBytes(NodeCommand command) {
            return ProtostuffUtils.messageToBytes(command);
        }
    },
    MONITORED((short) 4) {
        @Override
        public NodeCommand bytesToCommand(byte[] content) {
            try {
                if (null == content || content.length == 0) {
                    throw new IllegalArgumentException(" content can not be empty !");
                }
                return ProtostuffUtils.bytesToMessage(content, NodeMonitored.class);
            } catch (IllegalAccessException | InstantiationException e) {
                throw new MessagingSerializeException(e);
            }
        }

        @Override
        public byte[] CommandToBytes(NodeCommand command) {
            return ProtostuffUtils.messageToBytes(command);
        }
    },
    SYNC_REGISTER_ASK((short) 5) {
        @Override
        public NodeCommand bytesToCommand(byte[] content) {
            try {
                if (null == content || content.length == 0) {
                    throw new IllegalArgumentException(" content can not be empty !");
                }
                return ProtostuffUtils.bytesToMessage(content, SyncNodeRegister.class);
            } catch (IllegalAccessException | InstantiationException e) {
                throw new MessagingSerializeException(e);
            }
        }

        @Override
        public byte[] CommandToBytes(NodeCommand command) {
            return ProtostuffUtils.messageToBytes(command);
        }
    },
    ACTOR_MSG((short) 6) {
        @Override
        public NodeCommand bytesToCommand(byte[] content) {
            return null;
        }

        @Override
        public byte[] CommandToBytes(NodeCommand command) {
            return new byte[0];
        }
    },
    CLOSED((short) 9) {
        @Override
        public NodeCommand bytesToCommand(byte[] content) {
            try {
                if (null == content || content.length == 0) {
                    throw new IllegalArgumentException(" content can not be empty !");
                }
                return ProtostuffUtils.bytesToMessage(content, NodeClosed.class);
            } catch (IllegalAccessException | InstantiationException e) {
                throw new MessagingSerializeException(e);
            }
        }

        @Override
        public byte[] CommandToBytes(NodeCommand command) {
            return ProtostuffUtils.messageToBytes(command);
        }
    },
    SYNC_RECEIPT((short) 10) {
        @Override
        public NodeCommand bytesToCommand(byte[] content) {
            try {
                if (null == content || content.length == 0) {
                    throw new IllegalArgumentException(" content can not be empty !");
                }
                return ProtostuffUtils.bytesToMessage(content, SyncCommandReceipt.class);
            } catch (IllegalAccessException | InstantiationException e) {
                throw new MessagingSerializeException(e);
            }
        }

        @Override
        public byte[] CommandToBytes(NodeCommand command) {
            return ProtostuffUtils.messageToBytes(command);
        }
    };


    private short type;

    CommandType(short type) {
        this.type = type;
    }

    public static CommandType typeOf(short type) {
        for (CommandType t : values()) {
            if (t.getType() == type) {
                return t;
            }
        }
        throw new IllegalArgumentException(" can't support current type " + type);
    }


    public short getType() {
        return type;
    }
}
