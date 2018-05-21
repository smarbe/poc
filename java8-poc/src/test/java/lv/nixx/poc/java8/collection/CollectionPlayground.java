package lv.nixx.poc.java8.collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.*;
import java.util.stream.*;

import org.junit.Test;

public class CollectionPlayground {
	
	@Test
	public void effectiveFinal() {
		int[] numbers = { 1, 2, 20, 40, 77 };
		int threshold = 10;
		/* If we try to set: threshold = 20; 
		We get compilation error: 
		"Local variable threshold defined in an enclosing scope must be final or effectively final"
		*/
		final long count = Arrays.stream(numbers).filter(t -> t > threshold).count();
		assertEquals(3, count);
	}
	
	@Test
	public void listTraverse() {
		List<String> lst = Arrays.asList("10", "20",null,"30","40");
		
		ListIterator<String> iterator = lst.listIterator();
		while(iterator.hasNext()) {
			String s = iterator.next();
			if (s != null && s.equals("30")) {
				iterator.remove();
			}
		}
		assertEquals(Arrays.asList("10", "20", null, "40"), lst);
	}
	
	@Test
	public void linkedListVsArrayList() {
		long st = System.currentTimeMillis();
		final int iterationCount = 100000;
		
		ArrayList<String> c1 = new ArrayList<>();
		for (int i = 0; i < iterationCount; i++) {
			c1.add(0,String.valueOf(i));
		}
		
		System.out.println("ArrayList time: " + (System.currentTimeMillis() - st));
		st = System.currentTimeMillis();
		
		LinkedList<String> c2 = new LinkedList<>();
		for (int i = 0; i < iterationCount; i++) {
			c2.add(0, String.valueOf(i));
		}
		System.out.println("LinkedList time: " + (System.currentTimeMillis() - st));

		st = System.currentTimeMillis();
		LinkedHashSet<String> s1 = new LinkedHashSet<>();
		for (int i = 0; i < iterationCount; i++) {
			s1.add(String.valueOf(i));
		}
		System.out.println("HashSet time: " + (System.currentTimeMillis() - st));
	}
	
	@Test
	public void linkedHashSet() {
		Set<String> s1 = new LinkedHashSet<>();
		s1.add("1");
		s1.add("8");
		s1.add("2");
		s1.add(null);
		s1.add("10");
		s1.forEach(System.out::println);
	}
	
	@Test(expected = NullPointerException.class)
	public void nullToTreeSet() {
		Set<String> set = new TreeSet<>();
		set.add("s1");
		set.add(null);
	}
	
	@Test
	public void nullToLinkedHashSet() {
		// Тоже самое будет работать с HashSet
		Set<String> set = new LinkedHashSet<>();  
		set.add("s1");
		set.add("s2");
		set.add(null);
		set.add("s4");
		// Все хорошо, null можно добавлять в LinkedHashSet
		set.forEach(System.out::println);
	}
	
	@Test
	public void linkedHashMap() {
		Map<String, String> map = new LinkedHashMap<>();
		map.put("key1", "value1");
		map.put(null, null);
		map.put(null, "nullValue");
		map.put("key3", "value3");
		map.put("key4", "value4");
		// тут тоже все хорошо
		assertEquals(4, map.size());
		
		map.entrySet().forEach(System.out::println);
	}
	
	@Test(expected = NullPointerException.class)
	public void addNullToTreeMap() {
		Map<String, String> map = new TreeMap<>();
		map.put("key1", "value1");
		map.put(null, "nullValue");
	}
	
	@Test
	public void sumOfElement() {
		Collection<Integer> c = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
		final Optional<Integer> sum = c.stream().reduce((t1, t2) -> t1 + t2);
		assertEquals(Integer.valueOf(28), sum.get());
	}	
	
	@Test
	public void collectionDisjoint() {
		
		Collection<String> c1 = Arrays.asList("1","2","3");
		Collection<String> c2 = Arrays.asList("4","5","6");
		
		// Коллекции должны быть отсортированы
		// Возврашает true - если нет общих элементов
		assertTrue("Collections are equals", Collections.disjoint(c1, c2));
	}
	
	
	@Test
	public void arrayStreamProcessing() {
		int[] intArray = new int[]{5, 99, 60, 12, 7, 5, 100, 777};
		
		final int[] res = Arrays.stream(intArray)
				.filter(t-> !(t<10))
				.sorted()
				.toArray();
		
		Arrays.stream(res).forEach(System.out::println);
		assertTrue(Arrays.equals(new int[]{12, 60, 99, 100, 777}, res));
	}
	
	@Test
	public void remove() {
		Collection<String> old = Arrays.asList("1","2","3");
		Collection<String> changed = Arrays.asList("4","2","3");
		
		Collection<String> result = new ArrayList<>(changed);
		result.removeAll(old);
		// 4 - new element in collection 
		result.forEach(System.out::println);
	}
	
	@Test
	public void findCommonElementInCollection() {
		Collection<String> old = Arrays.asList("1","2","3");
		Collection<String> changed = Arrays.asList("4","2","3");

		Collection<String> result = new ArrayList<>(old);
		// common elements in two collections
		result.retainAll(changed);
		assertEquals(Arrays.asList("2","3"), result);
		
		result.forEach(System.out::println);
	}
	
	@Test
	public void createCharacterStatistic() {
		String text = "aaaBBbbCC11233546556";
		
		Collection<Character> collection = new ArrayList<>();
		for (Character c : text.toCharArray()) {
			collection.add(c);
		}
		
		final Map<Character, Long> statistic = 
				collection.stream()
				.map(t->Character.toLowerCase(t))
				.collect(
						Collectors.groupingBy(t->t, 
						Collectors.counting())
				);
		
		statistic.entrySet().forEach(System.out::println);
	}
	
	@Test
	public void createCharacterStatisticGroup() {
		String text = "aaaBBbbCC11233546556";
		Collection<Character> collection = new ArrayList<>();
		for (Character c : text.toCharArray()) {
			collection.add(c);
		}
		
		final Map<Group, Long> collect = collection.stream().map(Holder::new)
		.collect(
				Collectors.groupingBy(t->t.group, 
				Collectors.counting())
		);
		collect.entrySet().forEach(System.out::println);
	}
	
	@Test
	public void theLongestWord_CollectMethod() {
		List<String> str = Arrays.asList("123","1", "12", "12345");
		
		final Optional<String> collect =str.stream()
				.collect(
//						Collectors.maxBy( (t1, t2) -> t1.length() < t2.length() ? -1 : (t1.length() == t2.length() ? 0 : 1))
						Collectors.maxBy((t1,t2) -> Integer.compare(t1.length(),t2.length()))
				);
		
		assertTrue(collect.isPresent());
		assertEquals("12345", collect.get());
	}
	
	@Test
	public void peekTest() {
		
		List<String> str = Arrays.asList("10","1", "12", "22");
		
		final int sum = str.stream()
			.sorted()
			.peek(System.out::println)
			.map(t->Integer.parseInt(t))
			.filter(t->t>10)
			.mapToInt(t->t).sum();

		assertEquals(34, sum);
		
		System.out.println("Sum:" + sum);
	}
	
	@Test
	public void listToString() {
		Collection<String> lst = Arrays.asList("1","2","3");
		final String c = lst.stream().collect(Collectors.joining(",", "<e>", "</e>"));
		System.out.println(c);
	}
	
	@Test
	public void computeIfAbsent() {
		
		Map<String, String> map = new HashMap<>();
		
		map.computeIfAbsent("1", t -> System.currentTimeMillis() + t);
		
		map.forEach((k,v) -> System.out.println(k + ":" + v) );
		
	}

	
	@Test
	public void theLongestWord_ReduceMethod() {
		List<String> str = Arrays.asList("123", "1", "12", "12345");

		final Optional<String> collect = str.stream()
				.collect(Collectors.reducing((t, u) -> t.length() > u.length() ? t : u));
		
		assertTrue(collect.isPresent());
		assertEquals("12345", collect.get());
	}
	
	@Test
	public void initArray() {
		int[] a = new int[100];
		Arrays.parallelSetAll(a, i->i);
		
		System.out.println(Arrays.toString(a));
	}
	
		
	@Test
	public void findMissing() {
		int[] a = new int[]{1,2,5,3,2,1,3};
		
		final int r = Arrays.stream(a).reduce(0, (x,y)-> x^y);
		assertEquals(5, r);
	}
		
	
	class Holder {
		Group group;
		Character c;

		Holder(Character c) {
			this.c = c;
			if (Character.isLetter(c)) {
				group = Group.CHARACTER;
			} else if (Character.isDigit(c)) {
				group = Group.NUMBER;
			}
		}

		@Override
		public String toString() {
			return "Holder [group=" + group + ", c=" + c + "]";
		}
		
	}
	
	enum Group {
		CHARACTER,
		NUMBER
	}

}


