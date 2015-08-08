Knight's Tour

Three classes are implemented:
Knight_recursive_hashMap: This class uses HashMap to saved visited positions in the board. A recursive algorithm is implemented to find the possible solutions;

Knight_recursive_array: This class uses n*n array to saved visited positions in the board. Similar recursive algorithm is implemented to find the possible solutions;

Knight_array_threaded: This class is multiple-threaded version. It uses Stack to keep current status of the game playing. It also supports lazy list. 
	For example:
		Knight_array_threaded tester = new Knight_array_threaded();
		tester.setUp(6); //set the board as 6*6, totally there should be 9862*2 closed tours.
		List<int[][]> r=tester.calculate(10);//return first 10 results
		              r=tester.calculate(100);//return next 100 results
		              r=tester.calculate(1000);//return next 1000 results
		              r=tester.calculate(0);//return all next results, that is 9862*2-1000-100-10

Just go to each class to run or use the unit test class Knight_array_threaded_test for Knight_array_threaded.