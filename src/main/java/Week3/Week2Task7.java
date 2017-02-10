package Week3;

/**
 * Created by Олексій on 09.02.2017.
 */
public class Week2Task7 {
    public static void main(String[] args) {
        Week2Task7 week2Task7 = new Week2Task7();
        week2Task7.A();
    }
    public void A (){
        Node node4 = new Node(4);
        Node node3 = new Node(node4,3);
        Node node2 = new Node(node3,2);
        Node node1 = new Node(node2,1);
        DoTask(node1);
    }
    public void DoTask(Node headNode){
        int size = 0;
        size = GetListLength(headNode,size);
        Node[] nodes = new Node[size];
        int counter = 0;
        FormArray(headNode,nodes,counter);
        for (int i = nodes.length-1; i > -1; i--) {
            if(i!=0){
                nodes[i].Next = nodes[i-1];
            }
            else {
                nodes[i].Next = null;
            }
        }
        Node newHead;
        newHead = nodes[nodes.length-1];
        for (Node n: nodes
             ) {
            System.out.println(n.toString());

        }
        PrintInverted(newHead);
    }
    public int GetListLength(Node node,int length){
        length++;
            if(node.Next==null){
                return length;
            }
            else
            {
                length = GetListLength(node.Next,length);
            }
            return length;
    }
    public void FormArray(Node node, Node[] array,int counter){
        array[counter] = node;
        counter++;
        {
            if (node.Next!=null){
                FormArray(node.Next,array,counter);
            }
        }
    }
    public void PrintInverted(Node node){
        if (node.Next!=null){
            System.out.println(node);
            PrintInverted(node.Next);
        }

    }
    public class Node{
        private Node Next;
        private int Number;
        public Node(){}
        public Node (int number){
            this.Number = number;
            this.Next = null;
        }
        public Node(Node next, int number){
            this.Next = next;
            this.Number = number;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "Number=" + Number +
                    '}';
        }
    }
}
