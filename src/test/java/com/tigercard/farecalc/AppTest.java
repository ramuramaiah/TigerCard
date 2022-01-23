package com.tigercard.farecalc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import com.tigercard.farecalc.processor.ChainProcessor;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
	ChainProcessor chain;
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    @Override
    protected void setUp() throws Exception {
    	chain = new ChainProcessor();
    	chain.init();
	}
	
    @Override
	protected void tearDown() throws Exception {
	}
	
    /**
     * Rigourous Test :-)
     * @throws URISyntaxException 
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    public void testDailyCap() throws URISyntaxException, FileNotFoundException, IOException
    {
    	ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    	URL resource = classloader.getResource("file1.txt");
    	File file = new File(resource.toURI());
    	chain.processFares(file);
    }
    
    /**
     * Rigourous Test :-)
     * @throws URISyntaxException 
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    public void testWeeklyCap() throws URISyntaxException, FileNotFoundException, IOException
    {
    	ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    	URL resource = classloader.getResource("file2.txt");
    	File file = new File(resource.toURI());
    	chain.processFares(file);
    }
    
    /**
     * Rigourous Test :-)
     * @throws URISyntaxException 
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    public void testWeekBoundary() throws URISyntaxException, FileNotFoundException, IOException
    {
    	ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    	URL resource = classloader.getResource("file3.txt");
    	File file = new File(resource.toURI());
    	chain.processFares(file);
    }
}
