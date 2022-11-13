package tree;

import utils.DateFormat;

import java.text.ParseException;
import java.util.Date;

public class Node {

    private String value;
    private NodeTypes type;
    private Node left;
    private Node right;
    private int balanceFactor;
    private int height;

    public Node(String value, NodeTypes type) {
        this.value = value;
        this.type = type;
        this.left = null;
        this.right = null;
        this.balanceFactor = 0;
        this.height = 0;
    }

    public Node(String value) {
        this.value = value;
        this.type = NodeTypes.STRING;
        this.left = null;
        this.right = null;
        this.balanceFactor = 0;
        this.height = 0;
    }

    public Node(long value) {
        this.value = String.valueOf(value);
        this.type = NodeTypes.LONG;
        this.left = null;
        this.right = null;
        this.balanceFactor = 0;
        this.height = 0;
    }

    public Node(Date value) {
        this.value = DateFormat.format(value);
        this.type = NodeTypes.DATE;
        this.left = null;
        this.right = null;
        this.balanceFactor = 0;
        this.height = 0;
    }

    public String getValue() {
        return value;
    }

    public Node[] getChildren() {
        return new Node[]{this.left, this.right};
    }

    public Node find(String value) throws ParseException {
        if (this.type == NodeTypes.STRING) {
            if (this.value.compareToIgnoreCase(value) == 0) {
                return this;
            }
            if (value.compareToIgnoreCase(this.value) < 0 && this.left != null) {
                return this.left.find(value);
            }
            if (value.compareToIgnoreCase(this.value) > 0 && this.right != null) {
                return this.right.find(value);
            }
        } else if (this.type == NodeTypes.LONG) {
            long valueLong = Long.parseLong(value);
            long thisValueLong = Long.parseLong(this.value);
            if (thisValueLong == valueLong) {
                return this;
            }
            if (valueLong < thisValueLong && this.left != null) {
                return this.left.find(value);
            }
            if (valueLong > thisValueLong && this.right != null) {
                return this.right.find(value);
            }
        } else if (this.type == NodeTypes.DATE) {
            Date valueDate = DateFormat.parse(value);
            Date thisValueDate = DateFormat.parse(this.value);
            int compare = valueDate.compareTo(thisValueDate);
            if (compare == 0) {
                return this;
            }
            if (compare < 0 && this.left != null) {
                return this.left.find(value);
            }
            if (compare > 0 && this.right != null) {
                return this.right.find(value);
            }
        }
        return null;
    }

    public Node find(long value) throws ParseException {
        return find(String.valueOf(value));
    }

    public Node find(Date value) throws ParseException {
        return find(DateFormat.format(value));
    }

    public boolean insert(String value) {
        if (this.type == NodeTypes.STRING) {
            if (this.value.compareToIgnoreCase(value) == 0) {
                return false;
            }
            if (value.compareToIgnoreCase(this.value) < 0) {
                if (this.left == null) {
                    this.left = new Node(value, this.type);
                    this.updateBalanceFactor();
                    this.updateHeight();
                    return true;
                } else {
                    if (this.left.insert(value)) {
                        this.updateBalanceFactor();
                        this.updateHeight();
                        return true;
                    }
                }
            } else {
                if (this.right == null) {
                    this.right = new Node(value, this.type);
                    this.updateBalanceFactor();
                    this.updateHeight();
                    return true;
                } else {
                    if (this.right.insert(value)) {
                        this.updateBalanceFactor();
                        this.updateHeight();
                        return true;
                    }
                }
            }
        } else if (this.type == NodeTypes.LONG) {
            long valueLong = Long.parseLong(value);
            long thisValueLong = Long.parseLong(this.value);
            if (valueLong == thisValueLong) {
                return false;
            }
            if (valueLong < thisValueLong) {
                if (this.left == null) {
                    this.left = new Node(value, this.type);
                    this.updateBalanceFactor();
                    this.updateHeight();
                    return true;
                } else {
                    if (this.left.insert(value)) {
                        this.updateBalanceFactor();
                        this.updateHeight();
                        return true;
                    }
                }
            } else {
                if (this.right == null) {
                    this.right = new Node(value, this.type);
                    this.updateBalanceFactor();
                    this.updateHeight();
                    return true;
                } else {
                    if (this.right.insert(value)) {
                        this.updateBalanceFactor();
                        this.updateHeight();
                        return true;
                    }
                }
            }
        } else if (this.type == NodeTypes.DATE) {

            Date valueDate = DateFormat.parse(value);
            Date thisValueDate = DateFormat.parse(this.value);
            int compare = valueDate.compareTo(thisValueDate);
            if (compare == 0) {
                return false;
            }
            if (compare < 0) {
                if (this.left == null) {
                    this.left = new Node(value, this.type);
                    this.updateBalanceFactor();
                    this.updateHeight();
                    return true;
                } else {
                    if (this.left.insert(value)) {
                        this.updateBalanceFactor();
                        this.updateHeight();
                        return true;
                    }
                }
            } else {
                if (this.right == null) {
                    this.right = new Node(value, this.type);
                    this.updateBalanceFactor();
                    this.updateHeight();
                    return true;
                } else {
                    if (this.right.insert(value)) {
                        this.updateBalanceFactor();
                        this.updateHeight();
                        return true;
                    }
                }
            }

        }

        return false; //acho que nunca chega aqui
    }

    //TODO se der tempo fazer isso de uma forma menos preguiçosa
    public boolean insert(long value) {
        return insert(String.valueOf(value));
    }

    public boolean insert(Date value) {
        return insert(DateFormat.format(value));
    }

    private void updateBalanceFactor() {
        int leftHeight = this.left == null ? -1 : this.left.height;
        int rightHeight = this.right == null ? -1 : this.right.height;
        this.balanceFactor = leftHeight - rightHeight;
        if (this.balanceFactor > 1 || this.balanceFactor < -1) {
            Node newNode = this.balanceTree();

            if (newNode.left == this) {
                this.updateNodeReference(newNode, true);
            } else if (newNode.right == this) {
                this.updateNodeReference(newNode, false);
            }
        }
    }

    private Node balanceTree() {
        if (this.balanceFactor > 1) {
            if (this.left.balanceFactor < 0) {
                this.left = this.left.rotateLeft();
            }
            return this.rotateRight();
        } else if (this.balanceFactor < -1) {
            if (this.right.balanceFactor > 0) {
                this.right = this.right.rotateRight();
            }
            return this.rotateLeft();
        }
        return this;
    }

    private Node rotateLeft() {
        Node newRoot = this.right;
        this.right = newRoot.left;
        newRoot.left = this;
        this.updateBalanceFactor();
        this.updateHeight();
        newRoot.updateBalanceFactor();
        newRoot.updateHeight();
        return newRoot;
    }

    private Node rotateRight() {
        Node newRoot = this.left;
        this.left = newRoot.right;
        newRoot.right = this;
        this.updateBalanceFactor();
        this.updateHeight();
        newRoot.updateBalanceFactor();
        newRoot.updateHeight();
        return newRoot;
    }

    private void updateNodeReference(Node newNode, boolean equalsLeftNode) {
        Node duplicateNode = this.duplicateNode();
        this.value = newNode.value;
        this.height = newNode.height;
        this.balanceFactor = newNode.balanceFactor;

        if (equalsLeftNode) {
            this.left = duplicateNode;
            this.right = newNode.right;
        } else {
            this.right = duplicateNode;
            this.left = newNode.left;
        }
    }

    private Node duplicateNode() {
        Node newNode = new Node(this.value, this.type);
        newNode.left = this.left;
        newNode.right = this.right;
        newNode.balanceFactor = this.balanceFactor;
        newNode.height = this.height;
        return newNode;
    }

    private void updateHeight() {
        int leftHeight = this.left == null ? -1 : this.left.height;
        int rightHeight = this.right == null ? -1 : this.right.height;
        this.height = Math.max(leftHeight, rightHeight) + 1;
    }

    public Node delete(String value) {
        if (this.type == NodeTypes.STRING) {
            if (this.value.compareToIgnoreCase(value) == 0) {
                if (this.left == null && this.right == null) {
                    return null;
                } else if (this.left == null) {
                    return this.right;
                } else if (this.right == null) {
                    return this.left;
                } else {
                    String maxNode = this.left.maxValue();
                    this.value = maxNode;
                    this.left = this.left.delete(maxNode);
                    this.updateBalanceFactor();
                    this.updateHeight();
                    return this;
                }
            } else if (value.compareToIgnoreCase(this.value) < 0) {
                if (this.left != null) {
                    this.left = this.left.delete(value);
                    this.updateBalanceFactor();
                    this.updateHeight();
                    return this;
                }
            } else {
                if (this.right != null) {
                    this.right = this.right.delete(value);
                    this.updateBalanceFactor();
                    this.updateHeight();
                    return this;
                }
            }
        } else if (this.type == NodeTypes.LONG) {
            long valueLong = Long.parseLong(value);
            long thisValueLong = Long.parseLong(this.value);
            if (thisValueLong == valueLong) {
                if (this.left == null && this.right == null) {
                    return null;
                } else if (this.left == null) {
                    return this.right;
                } else if (this.right == null) {
                    return this.left;
                } else {
                    String maxNode = this.left.maxValue();
                    this.value = maxNode;
                    this.left = this.left.delete(maxNode);
                    this.updateBalanceFactor();
                    this.updateHeight();
                    return this;
                }
            } else if (valueLong < thisValueLong) {
                if (this.left != null) {
                    this.left = this.left.delete(value);
                    this.updateBalanceFactor();
                    this.updateHeight();
                    return this;
                }
            } else {
                if (this.right != null) {
                    this.right = this.right.delete(value);
                    this.updateBalanceFactor();
                    this.updateHeight();
                    return this;
                }
            }
        } else if (this.type == NodeTypes.DATE) {

            Date valueDate = DateFormat.parse(value);
            Date thisValueDate = DateFormat.parse(this.value);
            if (thisValueDate.compareTo(valueDate) == 0) {
                if (this.left == null && this.right == null) {
                    return null;
                } else if (this.left == null) {
                    return this.right;
                } else if (this.right == null) {
                    return this.left;
                } else {
                    String maxNode = this.left.maxValue();
                    this.value = maxNode;
                    this.left = this.left.delete(maxNode);
                    this.updateBalanceFactor();
                    this.updateHeight();
                    return this;
                }
            } else if (valueDate.compareTo(thisValueDate) < 0) {
                if (this.left != null) {
                    this.left = this.left.delete(value);
                    this.updateBalanceFactor();
                    this.updateHeight();
                    return this;
                }
            } else {
                if (this.right != null) {
                    this.right = this.right.delete(value);
                    this.updateBalanceFactor();
                    this.updateHeight();
                    return this;
                }
            }

        }
        return this; //acho que não chega aqui tbm
    }

    public Node delete(long value) {
        return delete(String.valueOf(value));
    }

    public Node delete(Date value) {
        return delete(DateFormat.format(value));
    }

    private String maxValue() {

        if (this.right == null) {
            return this.value;
        } else {
            return this.right.maxValue();
        }
    }

    public String preOrder() {
        String result = "";
        result += this.value + " ";
        if (this.left != null) {
            result += this.left.preOrder();
        }
        if (this.right != null) {
            result += this.right.preOrder();
        }
        return result;
    }

    public String toString() {
        return "Node(" + this.value + ")" + " BF: " + this.balanceFactor + " H: " + this.height + " Left:[ " + (this.left == null ? "null" : this.left) + "] Right: [" + (this.right == null ? "null" : this.right) + "]";
    }
}
