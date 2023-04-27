import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A utility class for finding all text files in a directory using lambda
 * functions and streams.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Spring 2020
 */
public class TextFileFinder
{

	/**
	 * A lambda function that returns true if the path is a file that ends in a .txt
	 * or .text extension (case-insensitive). Useful for
	 * {@link Files#walk(Path, FileVisitOption...)}.
	 *
	 * @see Files#isRegularFile(Path, java.nio.file.LinkOption...)
	 * @see Path#getFileName()
	 * @see Files#walk(Path, FileVisitOption...)
	 */
	// DONE Fill in isText using a lambda expression
	public static final Predicate<Path> isText = (Path p) -> {
		return (Files.isRegularFile(p) && p.toString().toLowerCase().endsWith(".txt")
				|| p.toString().toLowerCase().endsWith(".text"));
	};

	/**
	 * A lambda function that returns true if the path is a file that ends in a .txt
	 * or .text extension (case-insensitive). Useful for
	 * {@link Files#find(Path, int, BiPredicate, FileVisitOption...)}.
	 *
	 * @see Files#find(Path, int, BiPredicate, FileVisitOption...)
	 */
	// DO NOT MODIFY; THIS IS PROVIDED FOR YOU
	// (Hint: This is only useful if you decide to use Files.find(...) instead of
	// Files.walk(...)
	public static final BiPredicate<Path, BasicFileAttributes> isTextWithAttribute = (path, attr) -> isText.test(path);

	/**
	 * Returns a stream of text files, following any symbolic links encountered.
	 *
	 * @param start the initial path to start with
	 * @return a stream of text files
	 *
	 * @throws IOException if an I/O error occurs
	 *
	 * @see #isText
	 * @see #isTextWithAttribute
	 *
	 * @see FileVisitOption#FOLLOW_LINKS
	 * @see Files#walk(Path, FileVisitOption...)
	 * @see Files#find(Path, int, java.util.function.BiPredicate,
	 *      FileVisitOption...)
	 *
	 * @see Integer#MAX_VALUE
	 */
	public static Stream<Path> find(Path start) throws IOException
	{
		// DONE Fill in find(Path) using streams
		return Files.find(start, Integer.MAX_VALUE, isTextWithAttribute, FileVisitOption.FOLLOW_LINKS);
	}

	/**
	 * Returns a list of text files.
	 *
	 * @param start the initial path to search
	 * @return list of text files
	 * @throws IOException if an I/O error occurs
	 *
	 * @see #find(Path)
	 */
	public static List<Path> list(Path start) throws IOException
	{
		// THIS METHOD IS PROVIDED FOR YOU DO NOT MODIFY
		return find(start).collect(Collectors.toList());
	}
}
