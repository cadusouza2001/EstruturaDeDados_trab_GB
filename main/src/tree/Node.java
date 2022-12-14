package tree;

import person.Pessoa;
import utils.DateFormat;

import java.util.ArrayList;
import java.util.Date;

public class Node {

    private String value;
    private long secondaryKey;
    private Node left;
    private Node right;
    private int balanceFactor;
    private int height;
    private Pessoa pessoa;

    public Node(String value, Pessoa pessoa) {
        this.value = value;
        this.left = null;
        this.right = null;
        this.balanceFactor = 0;
        this.height = 0;
        this.pessoa = pessoa;
        this.secondaryKey = pessoa.getCpf();
    }

    public Node(long value, Pessoa pessoa) {
        this.value = String.valueOf(value);
        this.left = null;
        this.right = null;
        this.balanceFactor = 0;
        this.height = 0;
        this.pessoa = pessoa;
        this.secondaryKey = pessoa.getCpf();
    }

    public Node(Date value, Pessoa pessoa) {
        this.value = DateFormat.format(value);
        this.left = null;
        this.right = null;
        this.balanceFactor = 0;
        this.height = 0;
        this.pessoa = pessoa;
        this.secondaryKey = pessoa.getCpf();
    }

    public String getValue() {
        return value;
    }

    public Node[] getChildren() {
        return new Node[]{this.left, this.right};
    }

    public Node find(long value) {
        long thisValueLong = Long.parseLong(this.value);
        if (thisValueLong == value) {
            return this;
        }
        if (value < thisValueLong && this.left != null) {
            return this.left.find(value);
        }
        if (value > thisValueLong && this.right != null) {
            return this.right.find(value);
        }
        return null;
    }

    public ArrayList<Node> startsWith(String value, ArrayList<Node> nodes) {
        if (this.value.toLowerCase().startsWith(value)) {
            nodes.add(this);
        }
        if (this.left != null) {
            this.left.startsWith(value, nodes);
        }
        if (this.right != null) {
            this.right.startsWith(value, nodes);
        }
        return nodes;
    }

    public ArrayList<Node> dateBetween(Date start, Date end, ArrayList<Node> nodes) {
        Date thisValueDate = DateFormat.parse(this.value);
        if (thisValueDate.compareTo(start) >= 0 && thisValueDate.compareTo(end) <= 0) {
            nodes.add(this);
        }
        if (this.left != null) {
            this.left.dateBetween(start, end, nodes);
        }
        if (this.right != null) {
            this.right.dateBetween(start, end, nodes);
        }
        return nodes;
    }

    public boolean insert(String value, Pessoa pessoa) {
        if (this.value.compareToIgnoreCase(value) == 0) {
            long cpfInsert = pessoa.getCpf();
            if (this.secondaryKey == cpfInsert) {
                return false;
            } else if (cpfInsert < this.secondaryKey) {
                if (this.left == null) {
                    this.left = new Node(value, pessoa);
                    this.updateBalanceFactor();
                    this.updateHeight();
                    return true;
                } else {
                    if (this.left.insert(value, pessoa)) {
                        this.updateBalanceFactor();
                        this.updateHeight();
                        return true;
                    }
                }
            } else {
                if (this.right == null) {
                    this.right = new Node(value, pessoa);
                    this.updateBalanceFactor();
                    this.updateHeight();
                    return true;
                } else {
                    if (this.right.insert(value, pessoa)) {
                        this.updateBalanceFactor();
                        this.updateHeight();
                        return true;
                    }
                }
            }
            return false;
        }
        if (value.compareToIgnoreCase(this.value) < 0) {
            if (this.left == null) {
                this.left = new Node(value, pessoa);
                this.updateBalanceFactor();
                this.updateHeight();
                return true;
            } else {
                if (this.left.insert(value, pessoa)) {
                    this.updateBalanceFactor();
                    this.updateHeight();
                    return true;
                }
            }
        } else {
            if (this.right == null) {
                this.right = new Node(value, pessoa);
                this.updateBalanceFactor();
                this.updateHeight();
                return true;
            } else {
                if (this.right.insert(value, pessoa)) {
                    this.updateBalanceFactor();
                    this.updateHeight();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean insert(long value, Pessoa pessoa) {
        long thisValueLong = Long.parseLong(this.value);
        if (value == thisValueLong) {
            return false;
        }
        if (value < thisValueLong) {
            if (this.left == null) {
                this.left = new Node(String.valueOf(value), pessoa);
                this.updateBalanceFactor();
                this.updateHeight();
                return true;
            } else {
                if (this.left.insert(value, pessoa)) {
                    this.updateBalanceFactor();
                    this.updateHeight();
                    return true;
                }
            }
        } else {
            if (this.right == null) {
                this.right = new Node(String.valueOf(value), pessoa);
                this.updateBalanceFactor();
                this.updateHeight();
                return true;
            } else {
                if (this.right.insert(value, pessoa)) {
                    this.updateBalanceFactor();
                    this.updateHeight();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean insert(Date value, Pessoa pessoa) {
        Date thisValueDate = DateFormat.parse(this.value);
        int compare = value.compareTo(thisValueDate);
        if (compare == 0) {
            long cpfInsert = pessoa.getCpf();
            if (this.secondaryKey == cpfInsert) {
                return false;
            } else if (cpfInsert < this.secondaryKey) {
                if (this.left == null) {
                    this.left = new Node(value, pessoa);
                    this.updateBalanceFactor();
                    this.updateHeight();
                    return true;
                } else {
                    if (this.left.insert(value, pessoa)) {
                        this.updateBalanceFactor();
                        this.updateHeight();
                        return true;
                    }
                }
            } else {
                if (this.right == null) {
                    this.right = new Node(value, pessoa);
                    this.updateBalanceFactor();
                    this.updateHeight();
                    return true;
                } else {
                    if (this.right.insert(value, pessoa)) {
                        this.updateBalanceFactor();
                        this.updateHeight();
                        return true;
                    }
                }
            }
            return false;
        }
        if (compare < 0) {
            if (this.left == null) {
                this.left = new Node(DateFormat.format(value), pessoa);
                this.updateBalanceFactor();
                this.updateHeight();
                return true;
            } else {
                if (this.left.insert(value, pessoa)) {
                    this.updateBalanceFactor();
                    this.updateHeight();
                    return true;
                }
            }
        } else {
            if (this.right == null) {
                this.right = new Node(DateFormat.format(value), pessoa);
                this.updateBalanceFactor();
                this.updateHeight();
                return true;
            } else {
                if (this.right.insert(value, pessoa)) {
                    this.updateBalanceFactor();
                    this.updateHeight();
                    return true;
                }
            }
        }
        return false;
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
        this.pessoa = newNode.pessoa;
        this.secondaryKey = newNode.secondaryKey;

        if (equalsLeftNode) {
            this.left = duplicateNode;
            this.right = newNode.right;
        } else {
            this.right = duplicateNode;
            this.left = newNode.left;
        }
    }

    private Node duplicateNode() {
        Node newNode = new Node(this.value, this.pessoa);
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

    public String toString() {
        return "Node(" + this.value + ")" + " BF: " + this.balanceFactor + " H: " + this.height + " Left:[ " + (this.left == null ? "null" : this.left) + "] Right: [" + (this.right == null ? "null" : this.right) + "]";
    }

    public String personInfo() {
        return this.pessoa.toString();
    }
}
