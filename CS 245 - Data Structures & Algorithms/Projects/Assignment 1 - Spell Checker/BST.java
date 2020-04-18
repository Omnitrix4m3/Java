public class BST
{
    //Nested 'Node' class allows for initialization of Node objects 
	class Node
    {
        String data;
        Node left, right;

        Node(String data)
        {
            this.data =  data;
            left = right = null;
        }
    }

    //Instance data
	private Node root;
    public String match;

    //BST constructor
	public BST()
    {
        root = null;
    }

    //Public find method that takes an item and returns if it is found or not
	public boolean find(String item)
    {
        return find(root, item);
    }

    //Private find method that allows for Node recursion
	private boolean find(Node node, String item)
    {
        if (node == null)
            return false;

        if (item.compareTo(node.data) == 0)
        {
            return true;
        }

        if (item.compareTo(node.data) < 0)
        {
            return find(node.left, item);
        }

        else
        {
            return find(node.right, item);
        }
    }

	//Public insert method that allows for the insertion of a node into a tree
    public void insert(String data)
    {
        root = insert(this.root, data);
    }

	//Private insert method that allows for Node recursion
	private Node insert(Node node, String item)
    {
        if (node == null)
        {
            return new Node(item);
        }

        else if (item.compareTo(node.data) < 0)
        {
            node.left = insert(node.left, item);
        }

        else if (item.compareTo(node.data) > 0 || item.compareTo(node.data) == 0)
        {
            node.right = insert(node.right, item);
        }

        return node;
    }

    //Custom method that deletes the contents of a match
    public void clearMatch()
    {
        this.match = "";
    }

    //Public suggestion method that generates suggestions based on Node proximity
    public void getSuggestions(String item)
    {
        this.match = getSuggestions(this.root, item);
    }

    //Private suggestion method that allows for Node recursion
	private String getSuggestions(Node node, String item)
    {
        if (item.charAt(0) == node.data.charAt(0))
        {
            return node.data;
        }

        else if (item.charAt(0) < node.data.charAt(0))
        {
            return getSuggestions(node.left, item);
        }

        else
        {
            return getSuggestions(node.right, item);
        }
    }
}