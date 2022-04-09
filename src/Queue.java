class Queue
{
    private class DLLNode
    {
        public final int intNumber;
        public double key;
        public DLLNode next;
        public DLLNode prev;

        public DLLNode(DLLNode nextNode, DLLNode prevNode, int intNumber, double key)
        {
            next = nextNode;
            prev = prevNode;
            this.intNumber = intNumber;
            this.key = key;
        }
    }

    // Fields head and tail point to the first and last nodes of the list.
    private DLLNode head;

    public boolean isEmpty()
    {
        if(head==null)return true;
        else return false;
    }

    public void insert(int intNumber,Double key)
    {
        DLLNode newNode=new DLLNode(null,null, intNumber, key);
        if(!isEmpty())
        {
            newNode.next=head;
            head.prev=newNode;
        }
        head=newNode;
    }

    public int delMin()
    {
        if(isEmpty()) return -1;
        else
        {
            DLLNode currentNode=head;
            DLLNode lowestNode=head;
            while(currentNode!=null)
            {
                if(currentNode.key<lowestNode.key)lowestNode=currentNode;
                currentNode=currentNode.next;
            }
            if(lowestNode==head)
            {
                head=head.next;
            }
            else
            {
                lowestNode.prev.next=lowestNode.next;
                if(lowestNode.next!=null)
                {
                    lowestNode.next.prev=lowestNode.prev;
                }
            }
            return lowestNode.intNumber;
        }
    }

    public boolean decreaseKey(int searchInt, double newDist)
    {
        DLLNode currentNode=head;
        while(currentNode!=null)
        {
            if(currentNode.intNumber==searchInt)
            {
                currentNode.key=newDist;
                return true;
            }
            currentNode=currentNode.next;
        }
        return false;
    }
}