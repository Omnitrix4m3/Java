import java.io.*;
import java.nio.file.*;

/**
 * Implements a Thread-Safe version of the InvertedIndexBuilder class
 * 
 * @author Omar Hussain
 * @version v3.0.0
 */
public class ThreadSafeInvertedIndexBuilder extends InvertedIndexBuilder
{
    /**
     * Nested class allows for the creation of Runnable objects to support multithreading
     */
    public class Task implements Runnable
    {
        /**
         * The filepath specifying the file to be used
         */
        private Path path;

        /**
         * Single-arg constructor which implements the path variable for the class
         * @param path
         */
        public Task(Path path)
        {
            this.path = path;
        }

        /**
         * Overriden run() method allows for custom tasks to be carried out
         */
        @Override
        public void run()
        {
            try
            {
                InvertedIndex originIndex = new InvertedIndex();

                fileParse(path, originIndex);
                index.addAll(originIndex);
            }

            catch (IOException e)
            {
                System.out.println("Invalid path! Unable to add '" + path + "' to InvertedIndex!");
            }
        }
    }

    /**
	 * Initializes a variable to store information from text files that can be globally accessed and modified.
	 */
	private final ThreadSafeInvertedIndex index;

    /**
     * Initializes a WorkQueue object to be used by the class
     */
    private final WorkQueue queue;

    /**
     * Multi-arg constructor that implements index and queue variables for the class
     * @param index     the ThreadSafeInvertedIndex to be used by the class
     * @param queue     the WorkQueue to be used by the class
     */
    public ThreadSafeInvertedIndexBuilder(ThreadSafeInvertedIndex index, WorkQueue queue)
    {
        super(index);
        this.index = index;
        this.queue = queue;
    }

    /**
	 * Recursively searches a filepath for files to fileParse and add to the InvertedIndex
	 *
	 * @param path     the origin filepath to search at
	 * @throws IOException if an IO error occurs
     * @throws FileNotFoundException if a FileNotFound error occurs
	 */
    @Override
    public void directoryTraverseAndParse(Path path) throws IOException, FileNotFoundException
    {
        super.directoryTraverseAndParse(path);

        try
        {
            queue.finish();
        }

        catch (InterruptedException e)
        {
            throw new IOException();
        }
    }

    /**
     * fileParse helper method which takes in only a filepath
     * 
     * @param path  the filepath to search at
     * @throws FileNotFoundException
     * @throws IOException
     * @see #fileParse(Path, InvertedIndex)
     */
    @Override
    public void fileParse(Path path) throws FileNotFoundException, IOException
    {
        queue.execute(new Task(path));
    }
}