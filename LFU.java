/*

Worked on LC : YES
Space complexity: O(N) for two hashmap
Time complexity: O(1) for get put or update
*/

class LFUCache {
    
    class Node {
        
        int key;
        int data;
        int freq;
        Node prev;
        Node next;
        
        Node() {
            key = -1;
            data = -1;
            freq = 1;
            prev = null;
            next = null;
            
        }
        
        
        Node(int key, int data) {
            this.key = key;
            this.data = data;
            this.freq = 1;
            prev = null;
            next = null;
        }
        
        
    }
    
    public class DLL {
        
        Node head;
        Node tail;
        int size;
        
        public DLL() {
            head = new Node();
            tail = new Node();
            size = 0;
            head.next = tail;
            tail.prev = head;
        }
        
        
        
       public void addToHead(Node node) {
            
            Node headNext = head.next;
            
            head.next = node;
            node.prev = head;
            node.next = headNext;
            headNext.prev = node;
            size+=1;
        }
        
       public void remove(Node node) {

            Node before = node.prev;
            Node after = node.next;
            
            after.prev = before;
            before.next = after;
            
            size-=1;
        }
        
        
      public  Node removeLast() {
            Node node = tail.prev; 
            remove(node);
            return node;  
        }
    }
    
    int minFreq;
    int capacity;
    int size;


    HashMap<Integer, Node> cache;
    HashMap<Integer, DLL> counterMap;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        size = 0;
        minFreq = 0;
        cache= new HashMap();
        counterMap = new HashMap();
        
    }
    
    
    private void update(Node node) {
        
        int count = node.freq;
        
        DLL oldDLL = counterMap.get(count);
        oldDLL.remove(node);
        
        if(count == minFreq && oldDLL.size == 0) {
            minFreq+=1;
        }
        
        int newCount = count+1;
        node.freq = newCount;
        
        counterMap.putIfAbsent(newCount, new DLL());
        counterMap.get(newCount).addToHead(node);
        
    }
    
    
    
    public int get(int key) {
        if(!cache.containsKey(key)) {
            return -1;
        }else{
            Node node = cache.get(key);
            update(node);
            return node.data;     
        }
    }
    
    public void put(int key, int value) {
        
            if(cache.containsKey(key)) {
                Node node = cache.get(key);
                node.data = value;
                update(node);
                return;
            }else{
                if(capacity == 0) {
                    return;

                }
                else if (cache.size() == capacity) {
                    Node node = counterMap.get(minFreq).removeLast();
                    cache.remove(node.key);

                }

                Node newNode = new Node(key, value);
                minFreq = 1;
                
                cache.put(key, newNode);
                counterMap.putIfAbsent(minFreq, new DLL());
                counterMap.get(minFreq).addToHead(newNode);

            }
            
            
            
        }
        
    }

/**
 * Your LFUCache object will be instantiated and called as such:
 * LFUCache obj = new LFUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */
