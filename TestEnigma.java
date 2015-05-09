import java.util.Arrays;

/** 
 * Students are not required to use or implement this class.
 * It is included as an example of one way to unit test your methods
 * as you develop them.
 * 
 * DO NOT HAND IN THIS FILE (IT WON'T BE ACCEPTED)
 * Follow commenting and style guide for your own use.
 */
public class TestEnigma {

	/** Run the tests that are created */
	public static void main(String[] args) {
		int [] expected = { 2, 3, 1 };
		test_parseRotorIndices("2 3 1",expected);

		// TODO: add more tests here if you like
	}
	
	/** 
	 * Tests the parseRotorIndices method (just a little)
	 * @param sampleRotorList an example of possible rotor index values
	 * @param expected The expected integer array from parseRotorIndices()
	 */
	public static void test_parseRotorIndices(String sampleRotorList, int[] expected) {
		
		// Print a test label for this test
		System.out.println("testing parseRotorIndices(\"" + 
		sampleRotorList + "\")");

		// Get actual result
		int [] actual = Enigma.parseRotorIndices(sampleRotorList);

		// Print the results for comparison
		System.out.println("Expected: " + Arrays.toString(expected));
		System.out.println("But got:  " + Arrays.toString(actual));
		
	}

}
