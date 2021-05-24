package cn.aposoft.tutorial.algorithm.tree.rbtree;

/**
 * @ref https://tech.meituan.com/2016/12/02/redblack-tree.html
 *
 * 为使代码更可读，调整了部分代码，并增加注释，不保证实现的正确性，请使用meituan package的实现验证正确性
 * @param <T>
 */
public class RBTree<T extends Comparable<T>> {
    private final RBTreeNode<T> root;
    // node number
    private java.util.concurrent.atomic.AtomicLong size =
            new java.util.concurrent.atomic.AtomicLong(0);

    //in overwrite mode,all node's value can not  has same	value
    //in non-overwrite mode,node can have same value, suggest don't use non-mode.
    private volatile boolean overrideMode = true;

    public RBTree() {
        this.root = new RBTreeNode<T>();
    }

    public RBTree(boolean overrideMode) {
        this();
        this.overrideMode = overrideMode;
    }

    public boolean isOverrideMode() {
        return overrideMode;
    }

    public void setOverrideMode(boolean overrideMode) {
        this.overrideMode = overrideMode;
    }

    /**
     * number of tree number
     *
     * @return
     */
    public long getSize() {
        return size.get();
    }

    /**
     * get the root node
     *
     * @return
     */
    private RBTreeNode<T> getRoot() {
        return root.getLeft();
    }

    /**
     * add value to a new node,if this value exist in this tree,
     * if value exist,it will return the exist value.otherwise return null
     * if override mode is true,if value exist in the tree,
     * it will override the old value in the tree
     *
     * @param value
     * @return
     */
    public T addNode(T value) {
        RBTreeNode<T> t = new RBTreeNode<T>(value);
        return addNode(t);
    }

    /**
     * find the value by give value(include key,key used for search,
     * other field is not used,@see compare method).if this value not exist return null
     *
     * @param value
     * @return
     *
     */
    public T find(T value) {
        RBTreeNode<T> node = getRoot();
        while (node != null) {
            int cmp = node.getValue().compareTo(value);
            if (cmp < 0) {
                node = node.getRight();
            } else if (cmp > 0) {
                node = node.getLeft();
            } else {
                return node.getValue();
            }
        }
        return null;
    }

    /**
     * remove the node by give value,if this value not exists in tree return null
     *
     * @param value include search key
     * @return the value contain in the removed node
     */
    public T remove(T value) {
        RBTreeNode<T> dataRoot = getRoot();
        RBTreeNode<T> parent = root;

        while (dataRoot != null) {
            int cmp = dataRoot.getValue().compareTo(value);
            if (cmp < 0) { // on the road of search the node
                parent = dataRoot;
                dataRoot = dataRoot.getRight();
            } else if (cmp > 0) { // on the road of search node
                parent = dataRoot;
                dataRoot = dataRoot.getLeft();
            } else {
                // find the node , do remove operations
                // 当右子树不为空时
                if (dataRoot.getRight() != null) {
                    // 当右子树不为空时，使用右子树最小节点替代当前节点
                    // 删除右子树最小节点，当右子树不存在left node的时候，没有操作，并且返回右子树的根节点
                    RBTreeNode<T> min = removeMin(dataRoot.getRight());
                    //x used for fix color balance
                    RBTreeNode<T> x = min.getRight() == null ? min.getParent() : min.getRight();
                    boolean isParent = min.getRight() == null;
                    // 设置树的替换节点的关系
                    // 1. 设置左子树节点
                    min.setLeft(dataRoot.getLeft());
                    setParent(dataRoot.getLeft(), min);
                    // 2. 替换父节点
                    if (parent.getLeft() == dataRoot) {
                        parent.setLeft(min);
                    } else {
                        parent.setRight(min);
                    }
                    setParent(min, parent);
                    // 拷贝min的颜色
                    boolean curMinIsBlack = min.isBlack();
                    //inherit dataRoot's color
                    min.setRed(dataRoot.isRed());
                    // 设置 right，如果min == 原始right，则不需要设置
                    if (min != dataRoot.getRight()) {
                        min.setRight(dataRoot.getRight());
                        setParent(dataRoot.getRight(), min);
                    }
                    //remove a black node,need fix color
                    if (curMinIsBlack) {
                        //
                        if (min != dataRoot.getRight()) {
                            fixRemove(x, isParent);
                        } else if (min.getRight() != null) {
                            fixRemove(min.getRight(), false);
                        } else { //
                            fixRemove(min, true);
                        }
                    }
                } else {
                    // 当右子树为空时, 直接设置 左子树替换当前节点
                    setParent(dataRoot.getLeft(), parent);
                    if (parent.getLeft() == dataRoot) {
                        parent.setLeft(dataRoot.getLeft());
                    } else {
                        parent.setRight(dataRoot.getLeft());
                    }
                    //current node is black and tree is not empty
                    if (dataRoot.isBlack() && !(root.getLeft() == null)) {
                        RBTreeNode<T> x = dataRoot.getLeft() == null
                                ? parent : dataRoot.getLeft();
                        boolean isParent = dataRoot.getLeft() == null;
                        fixRemove(x, isParent);
                    }
                }
                // 处理返回值
                setParent(dataRoot, null);
                dataRoot.setLeft(null);
                dataRoot.setRight(null);
                if (getRoot() != null) {
                    getRoot().setRed(false);
                    getRoot().setParent(null);
                }
                size.decrementAndGet();
                return dataRoot.getValue();
            }
        }
        return null;
    }

    /**
     * fix remove action
     *  case 0 （实际删除节点）: boolean isParent = min.getRight() == null;
     * @param node
     * @param isParent
     */
    private void fixRemove(RBTreeNode<T> node, boolean isParent) {
        RBTreeNode<T> cur = isParent ? null : node;
        boolean isRed = isParent ? false : node.isRed();
        RBTreeNode<T> parent = isParent ? node : node.getParent();

        while (!isRed && !isRoot(cur)) {
            RBTreeNode<T> sibling = getSibling(cur, parent);
            //sibling is not null,due to before remove tree color is balance

            //if cur is a left node
            boolean isLeft = parent.getRight() == sibling;
            if (sibling.isRed() && !isLeft) {//case 1
                //cur in right
                parent.makeRed();
                sibling.makeBlack();
                rotateRight(parent);
            } else if (sibling.isRed() && isLeft) {
                //cur in left
                parent.makeRed();
                sibling.makeBlack();
                rotateLeft(parent);
            } else if (isBlack(sibling.getLeft()) && isBlack(sibling.getRight())) {//case 2
                sibling.makeRed();
                cur = parent;
                isRed = cur.isRed();
                parent = parent.getParent();
            } else if (isLeft && !isBlack(sibling.getLeft())
                    && isBlack(sibling.getRight())) {//case 3
                sibling.makeRed();
                sibling.getLeft().makeBlack();
                rotateRight(sibling);
            } else if (!isLeft && !isBlack(sibling.getRight())
                    && isBlack(sibling.getLeft())) {
                sibling.makeRed();
                sibling.getRight().makeBlack();
                rotateLeft(sibling);
            } else if (isLeft && !isBlack(sibling.getRight())) {//case 4
                sibling.setRed(parent.isRed());
                parent.makeBlack();
                sibling.getRight().makeBlack();
                rotateLeft(parent);
                cur = getRoot();
            } else if (!isLeft && !isBlack(sibling.getLeft())) {
                sibling.setRed(parent.isRed());
                parent.makeBlack();
                sibling.getLeft().makeBlack();
                rotateRight(parent);
                cur = getRoot();
            }
        }
        if (isRed) {
            cur.makeBlack();
        }
        if (getRoot() != null) {
            getRoot().setRed(false);
            getRoot().setParent(null);
        }

    }

    //get sibling node
    //
    private RBTreeNode<T> getSibling(RBTreeNode<T> node, RBTreeNode<T> parent) {
        parent = node == null ? parent : node.getParent();
        if (node == null) {
            return parent.getLeft() == null ? parent.getRight() : parent.getLeft();
        }
        if (node == parent.getLeft()) {
            return parent.getRight();
        } else {
            return parent.getLeft();
        }
    }

    private boolean isBlack(RBTreeNode<T> node) {
        return node == null || node.isBlack();
    }

    private boolean isRoot(RBTreeNode<T> node) {
        return root.getLeft() == node && node.getParent() == null;
    }

    /**
     * find the successor node
     * 在树中删除最小左节点，并返回，如果不存在最小左节点，则返回根节点，不做任何修改操作
     * @param node current node's right node
     * @return
     */
    private RBTreeNode<T> removeMin(RBTreeNode<T> node) {
        //find the min node
        RBTreeNode<T> parent = node;
        while (node != null && node.getLeft() != null) {
            parent = node;
            node = node.getLeft();
        }
        //--remove min node begin
        // 为什么当不存在左节点时不删除？函数定义存在问题
        if (parent == node) {
            return node;
        }
        //将最左节点的右子树赋值给parent节点
        parent.setLeft(node.getRight());
        setParent(node.getRight(), parent);
        //--remove min node end

        //don't remove right pointer,it is used for fixed color balance
        //node.setRight(null);
        return node;
    }


    private T addNode(RBTreeNode<T> node) {
        node.setLeft(null);
        node.setRight(null);
        node.makeRed();
        setParent(node, null);
        // root的左节点为实际的根节点。如果根节点不存在，直接设置根节点，并且设置为黑色
        if (root.getLeft() == null) {
            root.setLeft(node);
            //root node is black
            node.makeBlack();//replace: node.setRed(false);
            size.incrementAndGet();
        } else { // 处理非根节点
            RBTreeNode<T> x = findParentNode(node);
            int cmp = x.getValue().compareTo(node.getValue());

            if (this.overrideMode && cmp == 0) {
                T v = x.getValue();
                x.setValue(node.getValue());
                return v;
            } else if (cmp == 0) {
                //value exists,ignore this node
                return x.getValue();
            }

            setParent(node, x);

            if (cmp > 0) {
                x.setLeft(node);
            } else {
                x.setRight(node);
            }

            fixInsert(node);
            size.incrementAndGet();
        }
        return null;
    }

    /**
     * find the parent node to hold node x,
     * if parent value equals x.value return parent.
     *
     * @param x
     * @return
     */
    private RBTreeNode<T> findParentNode(RBTreeNode<T> x) {
        RBTreeNode<T> dataRoot = getRoot();
        RBTreeNode<T> child = dataRoot;

        while (child != null) {
            int cmp = child.getValue().compareTo(x.getValue());
            if (cmp == 0) {
                return child;
            }
            if (cmp > 0) {
                dataRoot = child;
                child = child.getLeft();
            } else if (cmp < 0) {
                dataRoot = child;
                child = child.getRight();
            }
        }
        return dataRoot;
    }

    /**
     * red black tree insert fix.
     *
     * @param x
     */
    private void fixInsert(RBTreeNode<T> x) {
        RBTreeNode<T> parent = x.getParent();

        // 只有红色的parent，才需要调整，黑色parent不需要调整
        while (parent != null && parent.isRed()) {
            RBTreeNode<T> uncle = getUncle(x);
            if (uncle == null) {//need to rotate
                RBTreeNode<T> ancestor = parent.getParent();
                //ancestor is not null due to before before add,tree color is balance
                if (parent == ancestor.getLeft()) {
                    boolean isRight = x == parent.getRight();
                    if (isRight) {
                        rotateLeft(parent);
                    }
                    rotateRight(ancestor);
                    // 将新子树的顶点设置为黑色
                    if (isRight) {
                        x.makeBlack();
                        parent = null;//end loop
                    } else {
                        parent.makeBlack();// end loop
                    }
                    ancestor.makeRed();
                } else {
                    boolean isLeft = x == parent.getLeft();
                    if (isLeft) {
                        rotateRight(parent);
                    }
                    rotateLeft(ancestor);

                    if (isLeft) {
                        x.makeBlack();
                        parent = null;//end loop
                    } else {
                        parent.makeBlack();
                    }
                    ancestor.makeRed();
                }
            } else {
                //uncle is red then change color
                parent.makeBlack();
                uncle.makeBlack();
                parent.getParent().makeRed();
                x = parent.getParent();
                parent = x.getParent();
            }
        }
        getRoot().makeBlack();
        getRoot().setParent(null);
    }

    /**
     * get uncle node
     *
     * @param node
     * @return
     */
    private RBTreeNode<T> getUncle(RBTreeNode<T> node) {
        RBTreeNode<T> parent = node.getParent();
        RBTreeNode<T> ancestor = parent.getParent();
        if (ancestor == null) {
            return null;
        }
        if (parent == ancestor.getLeft()) {
            return ancestor.getRight();
        } else {
            return ancestor.getLeft();
        }
    }

    private void rotateLeft(RBTreeNode<T> node) {
        RBTreeNode<T> right = node.getRight();
        if (right == null) {
            throw new java.lang.IllegalStateException("right node is null");
        }
        RBTreeNode<T> parent = node.getParent();
        node.setRight(right.getLeft());
        setParent(right.getLeft(), node);

        right.setLeft(node);
        setParent(node, right);

        if (parent == null) {//node pointer to root
            //right  raise to root node
            root.setLeft(right);
            setParent(right, null);
        } else {
            if (parent.getLeft() == node) {
                parent.setLeft(right);
            } else {
                parent.setRight(right);
            }
            //right.setParent(parent);
            setParent(right, parent);
        }
    }

    private void rotateRight(RBTreeNode<T> node) {
        RBTreeNode<T> left = node.getLeft();
        if (left == null) {
            throw new java.lang.IllegalStateException("left node is null");
        }
        RBTreeNode<T> parent = node.getParent();
        node.setLeft(left.getRight());
        setParent(left.getRight(), node);

        left.setRight(node);
        setParent(node, left);

        if (parent == null) {
            root.setLeft(left);
            setParent(left, null);
        } else {
            if (parent.getLeft() == node) {
                parent.setLeft(left);
            } else {
                parent.setRight(left);
            }
            setParent(left, parent);
        }
    }


    private void setParent(RBTreeNode<T> node, RBTreeNode<T> parent) {
        if (node != null) {
            node.setParent(parent);
            if (parent == root) {
                node.setParent(null);
            }
        }
    }


    public void printTree() {
        printTree(this.getRoot());
    }

    /**
     * debug method,it used print the given node and its children nodes,
     * every layer output in one line
     *
     * @param root
     */
    public void printTree(RBTreeNode<T> root) {
        java.util.LinkedList<RBTreeNode<T>> queue = new java.util.LinkedList<RBTreeNode<T>>();
        java.util.LinkedList<RBTreeNode<T>> queue2 = new java.util.LinkedList<RBTreeNode<T>>();
        if (root == null) {
            return;
        }
        queue.add(root);
        boolean firstQueue = true;

        while (!queue.isEmpty() || !queue2.isEmpty()) {
            java.util.LinkedList<RBTreeNode<T>> q = firstQueue ? queue : queue2;
            RBTreeNode<T> n = q.poll();

            if (n != null) {
                String pos = n.getParent() == null ? "" : (n == n.getParent().getLeft()
                        ? " LE" : " RI");
                String pstr = n.getParent() == null ? "" : n.getParent().toString();
                String cstr = n.isRed() ? "R" : "B";
                cstr = n.getParent() == null ? cstr : cstr + " ";
                System.out.print(n + "(" + (cstr) + pstr + (pos) + ")" + "\t");
                if (n.getLeft() != null) {
                    (firstQueue ? queue2 : queue).add(n.getLeft());
                }
                if (n.getRight() != null) {
                    (firstQueue ? queue2 : queue).add(n.getRight());
                }
            } else {
                System.out.println();
                firstQueue = !firstQueue;
            }
        }
    }


    public static void main(String[] args) {
        RBTree<String> bst = new RBTree<String>();
        bst.addNode("d");
        bst.addNode("d");
        bst.addNode("c");
        bst.addNode("c");
        bst.addNode("b");
        bst.addNode("f");

        bst.addNode("a");
        bst.addNode("e");

        bst.addNode("g");
        bst.addNode("h");

        bst.remove("c");

//        bst.printTree(bst.getRoot());
        bst.printTree();
    }
}
