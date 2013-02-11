/**
 * Disjoint set class, using union by rank and path compression.
 * Elements in the set are numbered starting at 0.
 * Additional methods include: 
 * getElements() - returns all elements in the set in an int[]
 * printElements() - prints all elements in the set, in the form: {i1, i2, i3, ... , in}
 * @author Ashton Ondra
 */
public class MyDisjSets implements DisjointSets {



	int[] s;
	int numSets;

    /**
     * Construct the disjoint sets object.
     * @param numElements the initial number of disjoint sets.
     */
	public MyDisjSets(int numElements) {
		s = new int[numElements];
		for(int i = 0; i < numElements; i++){
			s[i] = -1;
		}
		numSets = numElements;
	}

	public int[] getS(){
		return s;
	}
	
	  /**
     * Union two disjoint sets using the height heuristic.
     * For simplicity, we assume root1 and root2 are distinct
     * and represent set names.
     * @param root1 the root of set 1.
     * @param root2 the root of set 2.
     */
	@Override
	public void union(int set1, int set2) {
		if (isSetName(set1) && isSetName(set2)) {

			if (s[set2] * -1 <= s[set1] * -1) {
				//int size = numElements(set2);
				int size = s[set2] *-1;
				s[set2] = set1;
				s[set1] = s[set1] - size;
			} else {

				//int size = numElements(set1);
				int size = s[set1] *-1;
				s[set1] = set2;
				s[set2] = s[set2] - size;

			}
			numSets--;
		}
	}

	// Finds which set the value belongs to 
	// uses path compression to make future finds more efficient
	public int find( int i ) {
		if( s[i] < 0 ){ // if the value is less than 0, it's negative and therefore must be the name of set
			return i; // return the value
		} else { // otherwise it's not negative and therefore points to another value
			return s[i] = find(s[i]); // use recursion to find the root. Compress the path by pointing every successive node to the root
		}
	}


	// Returns the number of sets 
	public int numSets() {
		return numSets;
	}

	@Override

	public boolean isSetName(int x) {
		if (x >= s.length) {
			throw new InvalidElementException();
		} else if(s[x]>0){
			throw new InvalidSetNameException();
		}
		return true;

	}


	@Override
	public int numElements(int setNum) {
		return getElements(setNum).length;
	}

	/** getElements() - returns all elements in the set in an int[]
	* @param setNum - the set which you would like to print out
	*/
	@Override
	public void printElements(int setNum) {
		if(!isSetName(setNum)){
			throw new InvalidSetNameException();
		}

		int [] elements = getElements(setNum);
		System.out.print("Set " + setNum + ": {" + elements[0]);
		for(int i = 1; i < elements.length; i++){
			System.out.print(", " + elements[i]);
		}

		System.out.println("}");
	}


	/** printElements() - prints all elements in the set, in the form: {i1, i2, i3, ... , in}
	* @param setNum - the set which you would like to print out
	*/
	@Override
	public int[] getElements(int setNum) {
		if(s[setNum] >= 0){
			throw new InvalidSetNameException();
		}
		int [] findElements = new int[s.length];
		findElements[0] = 1;
		findElements[1] = setNum;
		int count = 1;
		findElements = findElements(setNum, findElements, count);

		count = findElements[0];
		int[] elements = new int[count];

		for(int i = 0; i< count; i++){
			elements[i] =findElements[i+1];
		}

		return elements;

	}

	private int[] findElements(int setNum, int[] elements, int count){
		for (int i = 0; i < s.length; i++){
			if (s[i] == setNum){
				count ++;
				elements[0] = count;
				elements[count] = i;

				elements = findElements(i, elements, count);	
				count = elements[0];
			}
		}
		return elements;
	}

}


