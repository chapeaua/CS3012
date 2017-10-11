import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import org.junit.Test;

public class LowestCommonAncestorTest {


    //~ Constructor ........................................................
    @Test
    public void testConstructor()
    {
      new LowestCommonAncestor();
    }
	
	
	//~ Check that the implementation works with a null case ........................................................
	@Test
	public void TestNullCases() {
		assertEquals("LCA Failed with 3 null nodes ",null ,LowestCommonAncestor.LCA(null,null,null));
	}

}
