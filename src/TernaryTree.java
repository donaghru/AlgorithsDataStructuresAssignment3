import java.util.ArrayList;

public class TernaryTree
{
    private Node root;             // root of TST

    /**
     * Private node class.
     */
    private class Node
    {
        private char key;
        private int val;
        private Node left, mid, right;
    }

    /**
     *  Search TST for given word.
     *
     *  @param word the search word
     *  @return value associated with the given word if found, or -1 if no such word exists.
     */
    public ArrayList<Integer> get(String word)
    {
        Node x = getInitial(root, word, 0);
        if (x == null) return null;
        ArrayList<Integer> matches = new ArrayList<Integer>();
        if (x.val!=-1) matches.add(x.val);
        if (x.mid==null) return matches;
        return search(x.mid, matches);
    }
    private Node getInitial(Node x, String word, int length)
    {
        if (x == null) return null;
        char key = word.charAt(length);
        if (key < x.key) return getInitial(x.left, word, length);
        else if (key > x.key) return getInitial(x.right, word, length);
        else if (length < word.length() - 1) return getInitial(x.mid, word, length+1);
        else return x;
    }
    private ArrayList<Integer> search(Node x, ArrayList<Integer> matches)
    {
        if (x.left!=null) search(x.left, matches);
        if (x.mid!=null) search(x.mid, matches);
        if (x.right!=null) search(x.right, matches);
        if (x.val!=-1) matches.add(x.val);
        return matches;
    }

    /**
     *  Insert word made up of key-value pairs into TST.
     *
     *  @param word the keys to insert
     *  @param val the value associated with the final key
     */
    public void put(String word, int val)
    {
        root = put(root, word, val, 0);
    }
    private Node put(Node x, String word, int val, int length)
    {
        char key = word.charAt(length);
        if (x == null)
        {
            x = new Node(); x.key = key;
            x.val=-1;
        }
        if      (key < x.key) x.left  = put(x.left, word, val, length);
        else if (key > x.key) x.right = put(x.right, word, val, length);
        else if (length < word.length() - 1) x.mid = put(x.mid, word, val, length+1);
        else                  x.val = val;
        return x;
    }
}