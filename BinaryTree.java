public class BinaryTree<T> 
{
    private Node<T> root;
    
    public BinaryTree(T rootItem)
    {
        root = new Node<T>(rootItem);
    }
    
    public Node<T> getRoot(){return root;}
    
    public class Node<T>
    {
        public T item;
        Node<T> leftChild;
        Node<T> rightChild;
        boolean isaLeaf;
        
        public Node(T item)
        {
            this.item = item;
            isaLeaf = true;
        }
        
        public Node<T> getLeftChild()
        {   return leftChild;}
        public Node<T> getRightChild()
        {   return rightChild;}
        public boolean isLeaf()
        {   return isaLeaf;}
        
        public void insertToLeft(Node<T> node)
        {   leftChild = node;
            isaLeaf = false;
        }
        public void insertToLeft(T item)
        {   insertToLeft(new Node<T>(item));}
        
        public void insertToRight(Node<T> node)
        {   rightChild = node;
            isaLeaf = false;
        }
        public void insertToRight(T item)
        {   insertToRight(new Node<T>(item));}
    }
}
