import java.util.ArrayList;
import java.util.*;
/**
 * This class implements an AVLTree implementation of a tree
 * @author Dana Curca, dcurca, 250976773
 */
public class AVLTree implements AVLTreeADT {
	//initialize instance variables 
	private AVLTreeNode root;
	private int size;

	public AVLTree() {
		this.size = 0; 
		this.root = new AVLTreeNode(); 
	}
	/**
	 * Given node set the root of the tree to node 
	 */
	public void setRoot(AVLTreeNode node) {
		root = node; 
	}
	/**
	 * returns the root of the BST tree 
	 */
	public AVLTreeNode root() {
		return root;
	}
	/**
	 * checks to make sure that the node given is the root node 
	 */
	public boolean isRoot(AVLTreeNode node) {
		return node.isRoot();
	}
	/**
	 * returns the size of the BST tree 
	 */
	public int getSize() {
		return size; 
	}
	/**
	 * returns the node of the BST that contains node, key given otherwise return leaf node 
	 */
	public AVLTreeNode get(AVLTreeNode node, int key) {
		if(node.isLeaf()) {
			return node;
		}
		else { 
			if (key == node.getKey()) { 
				return node; 
			}
			if (key < node.getKey()) {
				return get(node.getLeft(), key);
			}
			else {
				return get(node.getRight(), key);
			}
		}
	}
	/**
	 * returns the node with the smallest key given the root node 
	 */
	public AVLTreeNode smallest(AVLTreeNode node) {
		if (node.isLeaf()) {
			return null; 
		}
		else { 
			AVLTreeNode temp = node;
			while (temp.isInternal()) {
					temp = temp.getLeft();
				}
			return temp.getParent();
		}
	}
	/**
	 * returns the node that contains the new key,data information otherwise if duplicate key throw an exception 
	 */
	public AVLTreeNode put(AVLTreeNode node, int key, int data) throws TreeException {
		AVLTreeNode temp = get(node,key);
		if (temp.isInternal()) {
			throw new TreeException("Duplicate Key " + key);
		}
		else { 
			size++;
			temp.setKey(key);
			temp.setData(data);
			//creates two leaf nodes to point at new node inserted
			AVLTreeNode newLeft = new AVLTreeNode(temp);
			AVLTreeNode newRight = new AVLTreeNode(temp);
			temp.setLeft(newLeft);
			temp.setRight(newRight);
			return temp;
		}
	}
	/**
	 * removes the record that contains key within the binary search tree returns the node that used to store the node removed 
	 */
	public AVLTreeNode remove(AVLTreeNode node, int key) throws TreeException {
		AVLTreeNode temp = get(node,key);
		AVLTreeNode prime, child;
		//if key does not exist within tree throw exception 
		if (temp.isLeaf()) {
			throw new TreeException("Null Pointer, Leaf Node " + key); 
			}
		else { 
			if (temp.getLeft().isLeaf() || temp.getRight().isLeaf()) {
				prime = temp.getParent();
				//checks to see which side of the BST tree is a leaf 
				if(temp.getLeft().isLeaf() == true) {
					size--;
					child = temp.getRight();
					if (temp.isRoot()) {
						setRoot(child); 
						}
					else {
						child.setParent(prime);
						if(child == temp.getRight()) {
							prime.setRight(child);
						}
						else {
							prime.setLeft(child); 
							}
						}
					}
				else { 
					size--;
					child = temp.getLeft();
					if (temp.isRoot()) {
						setRoot(child); 
						}
						else {
							child.setParent(prime);
							if(child == temp.getRight()) {
								prime.setRight(child);
							}
							else {
								prime.setLeft(child); 
							}
						}
					}
			}
			else { 
				AVLTreeNode small = smallest(temp.getRight());
				temp.setKey(small.getKey());
				temp.setData(small.getData());
				temp = remove(small, small.getKey());
			}
		}
		return temp;
	}
	/**
	 * returns an arraylist that contains AVLTreeNode objects after an inorder traversal 
	 */
	public ArrayList<AVLTreeNode> inorder(AVLTreeNode node) {
		ArrayList<AVLTreeNode> array = new ArrayList<AVLTreeNode>();
		inorderRec(node,array);
		return array;
	}
	/**
	 * performs an inorder traversal each time appending to a list 
	 */
	public void inorderRec(AVLTreeNode node, ArrayList<AVLTreeNode> list) {
		if(!node.isLeaf()) {
			inorderRec(node.getLeft(), list);
			list.add(node);
			inorderRec(node.getRight(), list);
		}
	}
	/**
	 * recomputes the height of a subtree given node 
	 */
	public void recomputeHeight(AVLTreeNode node) {
		node.setHeight(1 + Math.max(node.getLeft().getHeight(), node.getRight().getHeight()));
	}
	/**
	 * rebalances the tree and updates the height of the tree as it moves upwards toward the root of the tree 
	 */
	public void rebalanceAVL(AVLTreeNode r, AVLTreeNode v) {
		AVLTreeNode tallest;
		if (!v.isLeaf()) {
			recomputeHeight(v); }
			while (v != r) {
				v = v.getParent();
				if(Math.abs(v.getLeft().getHeight()-v.getRight().getHeight()) > 1) {
					if(v.getLeft().getHeight() > v.getRight().getHeight()) {
						tallest = taller(v,true);
					} 
					else {
						tallest = taller(v,false);
					}
					
					AVLTreeNode notLeft = taller(tallest,false);
					rotation(v,tallest,notLeft);
			}
			recomputeHeight(v);
		}
	}
	/**
	 * given node insert key,data into AVLTree and rebalance the tree after 
	 */
	public void putAVL(AVLTreeNode node, int key, int data) throws TreeException {
		AVLTreeNode newNode = put(node,key,data);
		rebalanceAVL(node,newNode);
	}
	/**
	 * given node remove the node that contains the key and rebalance the tree after 
	 */
	public void removeAVL(AVLTreeNode node, int key) throws TreeException {
		AVLTreeNode oldNode = remove(node,key);
		rebalanceAVL(node, oldNode);
	}
	/**
	 * checks the children of node and returns the subtree that is taller, if a tie in height use onleft to determine if the parent is on the left or right  
	 */
	public AVLTreeNode taller(AVLTreeNode node, boolean onLeft) {
		if (node.getLeft().getHeight() > node.getRight().getHeight()) {
			return node.getLeft();
		}
		if (node.getLeft().getHeight() < node.getRight().getHeight()) {
			return node.getRight();
		}
		if (node.isRoot()) {
			return node.getLeft();
		}
		if (onLeft == true) {
			return node.getLeft();
		}
		else { 
			return node.getRight();
		}
	}
	/**
	 * a left or right rotation which promotes node as the new parent 
	 */
	public AVLTreeNode rotate(AVLTreeNode node) {
		AVLTreeNode parent = node.getParent();
		boolean right = check(node);
		node.setParent(parent.getParent());
		//if parent is a root there are no other nodes to check therefore promote node as new root 
		if (parent.isRoot() == true) {
			setRoot(node); 
		}
		//check if parent is on the left if so set node to be on the left subtree 
		else if (check(parent) == true) {
			node.getParent().setLeft(node);
		}
		//else set node to be on the right subtree 
		else {
			node.getParent().setRight(node);
		}
		//R rotation 
		if (right == true) {
			parent.setParent(node);
			parent.setLeft(node.getRight());
			node.getRight().setParent(parent);
			node.setRight(parent);
		}
		//L rotation 
		else {
			parent.setParent(node);
			parent.setRight(node.getLeft());
			node.getLeft().setParent(parent);
			node.setLeft(parent);
		}
		
		recomputeHeight(parent);
		recomputeHeight(node);
		return node;
	}
	/**
	 * private method that checks whether the node given is in the left subtree of a tree 
	 */
	private boolean check(AVLTreeNode node) {
		if(node.getParent().getLeft() == node) {
			return true;
		}
		else { 
			return false;
		}
	}
	/**
	 * performs LL,RR,LR,RL given z where the rebalance occurs, using rotate either once or twice given the rotation is single (LL,RR) or double (LR,RL)
	 */
	public AVLTreeNode rotation(AVLTreeNode z, AVLTreeNode y, AVLTreeNode x) {
		if((z.getRight() == y && y.getRight() == x )|| (z.getLeft() == y && y.getLeft() == x)) {
			rotate(y); 
			return y;
		}
		else { 
			rotate(x);
			rotate(x);
			return x;
		}
	}
}
