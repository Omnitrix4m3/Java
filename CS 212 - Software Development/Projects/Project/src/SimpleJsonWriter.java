import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.text.*;

/**
 * Outputs several simple data structures in "pretty" JSON format where newlines
 * are used to separate elements and nested elements are indented.
 *
 * Warning: This class is not thread-safe. If multiple threads access this class
 * concurrently, access must be synchronized externally.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Spring 2020
 */
public class SimpleJsonWriter
{

	/**
	 * Writes the elements as a pretty JSON array.
	 *
	 * @param elements the elements to write
	 * @param writer   the writer to use
	 * @param level    the initial indent level
	 * @throws IOException if an IO error occurs
	 */
	public static void asArray(Collection<Integer> elements, Writer writer, int level) throws IOException
	{
		// DONE Fill in the asArray method using iteration (not string replacement)

		Iterator iterator = elements.iterator();

		writer.write("[\n");

		while (iterator.hasNext())
		{
			indent(writer, level + 1);

			writer.write(iterator.next().toString());
			writer.write(",\n");
			
			if (!iterator.hasNext())
			{
				writer.write("\n");
			}
		}

		indent(writer, level);
		writer.write("]");

		writer.flush();
	}

	/**
	 * Writes the elements as a pretty JSON array to file.
	 *
	 * @param elements the elements to write
	 * @param path     the file path to use
	 * @throws IOException if an IO error occurs
	 *
	 * @see #asArray(Collection, Writer, int)
	 */
	public static void asArray(Collection<Integer> elements, Path path) throws IOException
	{
		// THIS CODE IS PROVIDED FOR YOU; DO NOT MODIFY
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8))
		{
			asArray(elements, writer, 0);
		}
	}

	/**
	 * Returns the elements as a pretty JSON array.
	 *
	 * @param elements the elements to use
	 * @return a {@link String} containing the elements in pretty JSON format
	 *
	 * @see #asArray(Collection, Writer, int)
	 */
	public static String asArray(Collection<Integer> elements)
	{
		// THIS CODE IS PROVIDED FOR YOU; DO NOT MODIFY
		try
		{
			StringWriter writer = new StringWriter();
			asArray(elements, writer, 0);
			return writer.toString();
		} catch (IOException e)
		{
			return null;
		}
	}

	/**
	 * Writes the elements as a pretty JSON object.
	 *
	 * @param elements the elements to write
	 * @param writer   the writer to use
	 * @param level    the initial indent level
	 * @throws IOException if an IO error occurs
	 */
	public static void asObject(Map<String, Integer> elements, Writer writer, int level) throws IOException
	{
		// DONE Fill in the asObject method using iteration (not string replacement)

		Iterator<String> setIterator = elements.keySet().iterator();

		indent("{", writer, level);

		writer.write('\n');

		String element;
		Integer key;

		if (!setIterator.hasNext())
		{
			writer.write("}");
		}

		else
		{
			if (setIterator.hasNext())
			{
				element = setIterator.next();
				key = elements.get(element);

				quote(element, writer, level + 1);
				writer.write(": ");
				writer.write(key.toString());
			}

			while (setIterator.hasNext())
			{
				writer.write(",");

				element = setIterator.next();
				writer.write('\n');

				key = elements.get(element);
				quote(element, writer, level + 1);

				writer.write(": ");
				writer.write(key.toString());
			}

			writer.write('\n');
			indent("}", writer, level);
		}
	}

	/**
	 * Writes the elements as a pretty JSON object to file.
	 *
	 * @param elements the elements to write
	 * @param path     the file path to use
	 * @throws IOException if an IO error occurs
	 *
	 * @see #asObject(Map, Writer, int)
	 */
	public static void asObject(Map<String, Integer> elements, Path path) throws IOException
	{
		// THIS CODE IS PROVIDED FOR YOU; DO NOT MODIFY
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8))
		{
			asObject(elements, writer, 0);
		}
	}

	/**
	 * Returns the elements as a pretty JSON object.
	 *
	 * @param elements the elements to use
	 * @return a {@link String} containing the elements in pretty JSON format
	 *
	 * @see #asObject(Map, Writer, int)
	 */
	public static String asObject(Map<String, Integer> elements)
	{
		// THIS CODE IS PROVIDED FOR YOU; DO NOT MODIFY
		try
		{
			StringWriter writer = new StringWriter();
			asObject(elements, writer, 0);
			return writer.toString();
		} catch (IOException e)
		{
			return null;
		}
	}

	/**
	 * Writes the elements as a pretty JSON object with a nested array. The generic
	 * notation used allows this method to be used for any type of map with any type
	 * of nested collection of integer objects.
	 *
	 * @param elements the elements to write
	 * @param writer   the writer to use
	 * @param level    the initial indent level
	 * @throws IOException if an IO error occurs
	 */
	public static void asNestedArray(Map<String, ? extends Collection<Integer>> elements, Writer writer, int level)
			throws IOException
	{
		// DONE Fill in the asNestedArray method using iteration (not string
		// replacement)

		Iterator<? extends Map.Entry<String, ? extends Collection<Integer>>> iterator = elements.entrySet().iterator();

		writer.write("{\n");

		while (iterator.hasNext())
		{
			indent(writer, level + 1);

			Map.Entry<String, ? extends Collection<Integer>> current = iterator.next();

			quote(current.getKey(), writer);
			writer.write(": ");
			asArray(current.getValue(), writer, level + 1);
			
			writer.write(",\n");
			
			if (!iterator.hasNext())
			{
				writer.write("\n");
			}
		}

		writer.write("}");

		writer.flush();

		/*
		 * The generic notation:
		 *
		 * Map<String, ? extends Collection<Integer>> elements
		 *
		 * ...may be confusing. You can mentally replace it with:
		 *
		 * HashMap<String, HashSet<Integer>> elements
		 */
	}

	/**
	 * Writes the elements as a nested pretty JSON object to file.
	 *
	 * @param elements the elements to write
	 * @param path     the file path to use
	 * @throws IOException if an IO error occurs
	 *
	 * @see #asNestedArray(Map, Writer, int)
	 */
	public static void asNestedArray(Map<String, ? extends Collection<Integer>> elements, Path path) throws IOException
	{
		// THIS CODE IS PROVIDED FOR YOU; DO NOT MODIFY
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8))
		{
			asNestedArray(elements, writer, 0);
		}
	}

	/**
	 * Returns the elements as a nested pretty JSON object.
	 *
	 * @param elements the elements to use
	 * @return a {@link String} containing the elements in pretty JSON format
	 *
	 * @see #asNestedArray(Map, Writer, int)
	 */
	public static String asNestedArray(Map<String, ? extends Collection<Integer>> elements)
	{
		// THIS CODE IS PROVIDED FOR YOU; DO NOT MODIFY
		try
		{
			StringWriter writer = new StringWriter();
			asNestedArray(elements, writer, 0);
			return writer.toString();
		} catch (IOException e)
		{
			return null;
		}
	}

	/**
	 * Indents using 2 spaces by the number of times specified.
	 *
	 * @param writer the writer to use
	 * @param times  the number of times to write a tab symbol
	 * @throws IOException if an IO error occurs
	 */
	public static void indent(Writer writer, int times) throws IOException
	{
		// THIS CODE IS PROVIDED FOR YOU; DO NOT MODIFY
		for (int i = 0; i < times; i++)
		{
			writer.write(' ');
			writer.write(' ');
		}
	}

	/**
	 * Indents and then writes the element.
	 *
	 * @param element the element to write
	 * @param writer  the writer to use
	 * @param times   the number of times to indent
	 * @throws IOException if an IO error occurs
	 *
	 * @see #indent(String, Writer, int)
	 * @see #indent(Writer, int)
	 */
	public static void indent(Integer element, Writer writer, int times) throws IOException
	{
		// THIS CODE IS PROVIDED FOR YOU; DO NOT MODIFY
		indent(element.toString(), writer, times);
	}

	/**
	 * Indents and then writes the element.
	 *
	 * @param element the element to write
	 * @param writer  the writer to use
	 * @param times   the number of times to indent
	 * @throws IOException if an IO error occurs
	 *
	 * @see #indent(Writer, int)
	 */
	public static void indent(String element, Writer writer, int times) throws IOException
	{
		// THIS CODE IS PROVIDED FOR YOU; DO NOT MODIFY
		indent(writer, times);
		writer.write(element);
	}

	/**
	 * Writes the element surrounded by {@code " "} quotation marks.
	 *
	 * @param element the element to write
	 * @param writer  the writer to use
	 * @throws IOException if an IO error occurs
	 */
	public static void quote(String element, Writer writer) throws IOException
	{
		// THIS CODE IS PROVIDED FOR YOU; DO NOT MODIFY
		writer.write('"');
		writer.write(element);
		writer.write('"');
	}

	/**
	 * Indents and then writes the element surrounded by {@code " "} quotation
	 * marks.
	 *
	 * @param element the element to write
	 * @param writer  the writer to use
	 * @param times   the number of times to indent
	 * @throws IOException if an IO error occurs
	 *
	 * @see #indent(Writer, int)
	 * @see #quote(String, Writer)
	 */
	public static void quote(String element, Writer writer, int times) throws IOException
	{
		// THIS CODE IS PROVIDED FOR YOU; DO NOT MODIFY
		indent(writer, times);
		quote(element, writer);
	}

	/**
	 * Writes the elements as a pretty JSON InvertedIndex to file.
	 *
	 * @param setOfElements	the elements to write
	 * @param path   the filepath to output at
	 * @throws IOException if an IO error occurs
	 * @see #asInvertedIndex(Map, Writer, int)
	 */
	public static void asInvertedIndex(TreeMap<String, TreeMap<String, TreeSet<Integer>>> setOfElements, Path path) throws IOException
	{
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8))
		{
			asInvertedIndex(setOfElements, writer, 0);
		}
	}

	/**
	 * Writes the elements as a pretty JSON InvertedIndex.
	 *
	 * @param setOfElements	the elements to write
	 * @param writer   the writer to use
	 * @param level    the initial indent level
	 * @throws IOException if an IO error occurs
	 */
	public static void asInvertedIndex(TreeMap<String, TreeMap<String, TreeSet<Integer>>> setOfElements, Writer writer, int level) throws IOException
	{
		Iterator<String> setIterator = setOfElements.keySet().iterator();

		writer.write("{");

		while (setIterator.hasNext())
		{
			String element = setIterator.next();

			writer.write('\n');
            quote(element, writer, level + 1);
			writer.write(": {");
            writer.write('\n');
            
            Iterator<String> pathIterator = setOfElements.get(element).keySet().iterator(); //TODO: See Code Review for simplification advice (Swap out part of code for asNestedArray functionality)

			while (pathIterator.hasNext())
			{
                String path = pathIterator.next();
                
				quote(path, writer, level + 2);
				writer.write(": [\n");

				Iterator<Integer> locationIterator = setOfElements.get(element).get(path).iterator();

				while (locationIterator.hasNext())
				{
					indent(locationIterator.next(), writer, level + 3);

					if (locationIterator.hasNext())
					{
						writer.write(",");
					}

					writer.write('\n');
				}

				indent("]", writer, level + 2);

				if (pathIterator.hasNext())
				{
					writer.write(',');
				}

				writer.write('\n');
			}

			indent("}", writer, level + 1);

			if (setIterator.hasNext())
			{
				writer.write(",");
			}
		}

		writer.write('\n');
		writer.append("}");
	}

	/**
	 * Writes the query as a pretty JSON format.
	 * 
	 * @param elements	the elements to write
	 * @param writer    the writer to use
	 * @param level     the initial indent level
	 * @throws IOException if an IO error occurs
	 */
	public static void asQueryOutput(Map<String, ? extends ArrayList<InvertedIndex.Result>> elements, Writer writer, int level) throws IOException
	{
		Iterator<String> index = elements.keySet().iterator();

		writer.write("{");
		
		while (index.hasNext())
		{
			String line = index.next();
			
			writer.write('\n');
			
			Iterator<InvertedIndex.Result> step = elements.get(line).iterator();
			quote(line, writer, level + 1);
			writer.write(": [");
			writer.write('\n');

			while (step.hasNext())
			{
				InvertedIndex.Result result = step.next();

				String location = result.getLocation();
				String count = result.getCount();

				DecimalFormat formatter = new DecimalFormat("0.00000000");
				
				String score = formatter.format(result.getPriority());

				indent("{\n", writer, level + 2);
				
				indent("\"where\": ", writer, level + 3);
				quote(location, writer, level);
				writer.write(",\n");
				
				indent("\"count\": ", writer, level + 3);
				writer.write(count);
				writer.write(",\n");
				
				indent("\"score\": ", writer, level + 3);
				writer.write(score);
				writer.write("\n");
				
				indent("}", writer, level + 2);

				if (step.hasNext())
				{
					writer.write(",");
				}

				writer.write('\n');
			}

			indent("]", writer, level + 1);

			if (index.hasNext())
			{
				writer.write(",");
			}

		}

		writer.write('\n');
		writer.append("}");
	}

	/**
	 * Writes the query as a pretty JSON format.
	 * 
	 * @param elements	the elements to write
	 * @param path		the filepath to output at
	 * @throws IOException if an IO error occurs
	 * @see SimpleJsonWriter#asSearchOutput(Map<String, ? extends ArrayList<InvertedIndex.Result>>, Writer, int)
	 */
	public static void asQueryOutput(TreeMap<String, ArrayList<InvertedIndex.Result>> elements, Path path) throws IOException
	{
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8))
		{
			asQueryOutput(elements, writer, 0);
		}
	}
}