
// georgejammal 212755060
// abdallah lahawane 324034495
/**
 * BinomialHeap
 *
 * An implementation of binomial heap over non-negative integers.
 * Based on exercise from previous semester.
 */
public class BinomialHeap{
	
	public int size;
	public HeapNode last;
	public HeapNode min;
	public int numOfTrees;


	public BinomialHeap(){
		this.size = 0;
        this.last = null;
        this.min = null;
        this.numOfTrees = 0;
	}
	/**
	 * 
	 * pre: key > 0
	 *
	 * Insert (key,info) into the heap and return the newly generated HeapItem.
	  O(log n).
	 */
	public HeapItem insert(int key, String info) 
	{   
        HeapItem item = new HeapItem(key, info);
        item.node = new HeapNode(item);
        item.node.next = item.node;
        if(this.size == 0){
            this.numOfTrees += 1;
			this.min = item.node;
			this.last = item.node;
			item.node.next = item.node;
			this.size ++;
			item.node.next = item.node;
			
        }
        else{
            BinomialHeap newHeap = new BinomialHeap();
            newHeap.last = item.node;
			newHeap.min = item.node;
			newHeap.numOfTrees ++;
			newHeap.size = 1;
            this.meld(newHeap);
        }
        
        return item;
	}

	/**
	 * 
	 * Delete the minimal item
	 * O(logn)
	 */
		public void deleteMin(){
       	
		if(this.size == 1) {
			
			this.min = null;
			this.last = null;
			this.numOfTrees=0;
			this.size--;;
			
			
		}
		else {
		    
			this.size--;;
			HeapNode cur = this.last.next;
			BinomialHeap children = null;
			while (cur.next.rank != this.min.rank) { //we will certainly reach the Min before we enter the loop again
				cur = cur.next;

			}
			children = separate(this.min); //in order to create a new heap of the deleted-node's children and meld it with thisHeap
			if (this.numOfTrees == 1) {
				this.last = null;
				this.size = 0;
				this.min = null;
				this.numOfTrees = 0;
				this.meld(children);
			}
			else {
				HeapNode newlast = this.last;
				HeapNode first = null;
				HeapNode tmpmin = null;
				if(this.min.rank == this.last.next.rank) { //in case the Min was last.next
					 first = this.last.next.next;
					 tmpmin = this.last.next.next;
				}
				else {
					first=this.last.next;
					tmpmin=this.last.next;
				}
				cur.next = this.min.next;
				this.size -= children.size; //to avoid double adding the size of the children of the minimum
				this.numOfTrees-=1; //we removed one subtree 
				for(int i=0; i < this.numOfTrees; i++) { //to find the next minimum
					if(first.next.item.key <= tmpmin.item.key) {
						tmpmin=first.next;
					}
					if(first.next.rank == this.last.rank) {
						 newlast = first;
					}
					first = first.next;	
				}
				if(this.min.rank==this.last.rank) {
					this.last = newlast;
				}
				this.min = tmpmin;
				
				this.meld(children);
			}
			

			
		
		}
	}

    /**
	 * 
	 * Separates a node of rank greater than zero and make a bunomialHeap out of its xhildren  O(logn)
	 *
	 */  
    
     
	public BinomialHeap separate(HeapNode parent) { //separates a node of rank>0 into a binomialHeap of its children
		BinomialHeap children = new BinomialHeap();
        HeapNode child = parent.child;
        HeapNode tempMin = child;
        int parentRank = parent.rank;
		HeapNode p = child;
		
		if (parentRank != 0){
			for (int i=0 ; i<parentRank; i++) { 
				p.parent=null;
				p = p.next;
				if(p.item.key < tempMin.item.key) {
					tempMin = p;
				}
			}
			children.size = (int)Math.pow(2, parentRank) - 1;
			children.last = parent.child;
			children.min = tempMin;  
			children.numOfTrees = parentRank;
			}
		return children;
	}		
        

	/**
	 * 
	 * Return the minimal HeapItem
	 * O(1)
	 */
	public HeapItem findMin()
	{
		return this.min.item;
	} 

	/**
	 * 
	 * pre: 0 < diff < item.key
	 * 
	 * Decrease the key of item by diff and fix the heap. 
	 * O(logn)
	 */
	public void decreaseKey(HeapItem item, int diff) 
	{    
		item.key -= diff;
        while(item.node.parent != null && item.key < item.node.parent.item.key){
            HeapNode currNode = item.node;
            HeapNode currNodeParent = item.node.parent;
            HeapItem parentItem = item.node.parent.item;
            currNodeParent.item = item;
            item.node = currNodeParent;
            currNode.item = parentItem;
            parentItem.node = currNode;
        }
        if(item.node.parent == null && item.key < this.min.item.key){
            this.min = item.node;
        }
        return;
	}


	/**
	 * 
	 * Delete the item from the heap.
	 *O(logn)
	 */
	public void delete(HeapItem item) 
	{    
		int dif = item.key - this.min.item.key + 1;
        this.decreaseKey(item, dif);
        this.deleteMin();
        return;
	}



	/**
	 * 
	 * Meld the heap with heap2
	 *O(logn+logm) n and m are the sizes of the heaps
	 */
	public void meld(BinomialHeap heap2)
	{
		if(heap2.min == null){
            return;
        } 
        if(this.min == null){
            
			this.size = heap2.size;
			this.numOfTrees = heap2.numOfTrees;
			this.min = heap2.min;
			this.last = heap2.last;
			return;
        }
        if(this.size % 2 == 0 && heap2.size == 1 ){
            HeapNode tmp = this.last.next;
            this.last.next = heap2.last;
            this.last.next.next = tmp;
            if(this.last.next.item.key <= this.min.item.key) {
                this.min = this.last.next;
            }

            this.size++;
            this.numOfTrees++;
            return;
        }

        int newRank = 2 + Math.max(this.last.rank, heap2.last.rank);
        HeapNode[] thisArr = new HeapNode[newRank];
        HeapNode[] arr2 = new HeapNode[newRank];
        HeapNode[] res = new HeapNode[newRank];
        
		HeapNode p = this.last.next;
        
		for(int i = 0; i < newRank; i++){
            
			if(p.rank == i){
                thisArr[i] = p;
                p = p.next;
                //detach each tree from its next
                thisArr[i].next = thisArr[i];
            }
        }

        HeapNode q = heap2.last.next;
        for(int i = 0; i < newRank; i ++){
            if(q.rank == i){
                arr2[i] = q;
                q = q.next;
                //detach each tree from its next
                arr2[i].next = arr2[i];
            }
        }
        HeapNode carry = null;
		
		for (int i = 0; i < res.length ; i++) {
			if (thisArr[i] == null && arr2[i] == null && carry == null ) {

				continue;
			}
        
			else{
				if(carry == null) { 
					if(thisArr[i] != null && arr2[i] != null) { //conc of two trees with same rank
						carry = link(thisArr[i], arr2[i]); // link them using the aux funct link
					}
					else{
						if (thisArr[i]==null) {//copy arr2[i] into result
							res[i] = arr2[i];
							
						}
						else{
							res[i] = thisArr[i];//copy thisArr[]i into result
						}
					}    
	}
                else{
                    if(thisArr[i] == null && arr2[i] == null) { //carry isn't null
                    res[i] = carry;
                    carry = null;
                    }
                    else {
                        if(thisArr[i] != null && arr2[i] != null) { //add two trees same rank
                            res[i] = arr2[i];
                            carry = link(thisArr[i], carry);
                        }
                        else{
                            if(thisArr[i] == null) {//this only
                                carry = link(arr2[i] ,carry);
                            }
                            if(arr2[i] == null) {//heap2 only
                                carry = link(thisArr[i], carry);
                            }
                        }
                    }
                }
            
		}
	}

        HeapNode firstNode = null;
		this.size += heap2.size; // mkae the rught size
		
		int i = 0;
		int counter = 0; //counter for the ultimate number if ttres

        while(res[i] == null){
                i++;
            }
		firstNode = res[i];

		HeapNode tmp = firstNode; //link it to this's last
		while (i < res.length) {
			if (res[i] != null) {
				firstNode.next = res[i];
				firstNode = firstNode.next;
				counter++;
				if (res[i].item.key <= this.min.item.key) {
					this.min = res[i];
				}
			}
			i++;
		}
        this.last = firstNode;
		this.last.next = tmp;
		this.numOfTrees = counter;
		for(int j = 0; j < this.numOfTrees; j ++) {
			tmp.item.node = tmp;
			tmp = tmp.next;
		}		
	}
    
    /**
	 * 
	 * links two subtrees frim the same degree while deciding the minroot
	 *O(1)
	 */
    public HeapNode link(HeapNode sub1, HeapNode sub2){

        HeapNode small = sub1;
        HeapNode large = sub2;
        if(sub1.item.key > sub2.item.key){
            small = sub2;
            large = sub1;    
        }
        // connect one root to another
        large.parent = small;
        //connect smallermin tree child with the root with the larger minkey tree
        if(small.rank != 0){
            large.next = small.child.next;
            small.child.next = large;    
        }
        else{
            large.next = large;
        }
        small.child = large;
        small.rank += 1;
        small.item.node = small;
        large.item.node = large;

        return small;
    }
	/**
	 * 
	 * Return the number of elements in the heap
	 *   O(1)
	 */
	public int size()
	{
		return size; // should be replaced by student code
	}

	/**
	 * 
	 * The method returns true if and only if the heap
	 * is empty. O(1)
	 *   
	 */
	public boolean empty()
	{
		return size == 0; 
	}

	/**
	 * 
	 * Return the number of trees in the heap.
	 * O(1)
	 */
	public int numTrees()
	{
		return numOfTrees;
	}

	/**
	 * Class implementing a node in a Binomial Heap.
	 *  
	 */
	public class HeapNode{
		public HeapItem item;
		public HeapNode child;
		public HeapNode next;
		public HeapNode parent;
		public int rank;


        public HeapNode(HeapItem item){
        this.item = item;
        this.child = null;
        this.next = null;
        this.parent = null;
        this.rank = 0;
        }
	}

	/**
	 * Class implementing an item in a Binomial Heap.
	 *  
	 */
	public class HeapItem{
		public HeapNode node;
		public int key;
		public String info;

        public HeapItem(int key, String info){
            this.node = null;
            this.key = key;
            this.info = info;

	}}}