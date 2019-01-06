## Cosine Distance Computer – Matthew Sloyan G00348036 

For this project I made various decisions from brainstorming, writing it out on paper and trial and error. 
I also used the lecture notes for guidance regarding loose-coupling, high cohesion, abstraction, encapsulation, 
composition and inheritance. Some of the decisions and a guide for each feature be found below under their relevant headings.

### To run the Jar file:
java –cp ./oop.jar ie.gmit.sw.Runner
java -cp .;./oop.jar;./jsoup.jar ie.gmit.sw.Runner
(Includes external jsoup.jar file to parse a URL)

## How it works:

### UI:
Overall, I wanted to keep it simple, clear, easy to use and understand. 
When running initially you will be presented with the option to enter the subject file directory. 
The full path to the directory must be entered. This program can parse and compare many text documents 
(.txt, .docx, .html and .pdf) along with others. A separate folder of just text documents works best from testing.

The user is then presented with the option to select whether to compare a url or file. If 1 is selected the 
option to enter the file path including the file name and extension is presented. If 2 is selected the user 
can enter a URL (Extra) to compare against the subject directory. Both options include error handling to check 
if file or URL exists (Extra).

Once both are correct the system handles the inputs parsing them, adding to a rapidly searchable structure and 
calculating the cosine distance for each file. How this works is described below. 

### Processor class:
Processor is a dependancy in the UI class. It's the base of program and handles only thread execution (SRP). 
The files are added to an array and for each file a new thread is created for the FileParser class. I used a 
thread pool to achieve this to maximize parsing speed. From testing I found that a thread pool of size 2 was 
the fastest no matter the number of files. Two more thread executors are set up for ShingleTaker, and 
QueryFileParser (more on these below). I used a newSingleThreadExecutor as there’s only one instance and to 
cut down on overhead. Both these return a ConcurrentHashMap for speed, and once they have completed they are 
sent to the CalculateDotProductAndMagnitude() class for processing again adhering to SRP. Processor also contains
a dependancy to FileParser, QueryFileParser, ShingleTaker, and CalculateDotProductAndMagnitude so once they go 
out of scope they will be garbage collected.

### FileParser and QueryFileParser class:
Even though these files share their similarities I decided to make them two separate classes 
for the following reasons. First, as there’s multiple threads running for each file in the subject 
directory it worked best to add them to a queue and then put them into a map with the shingleTaker class. 
Also, it has an arrayList for the map value, so loops are needed to add to it. Whereas the query file is 
one file, so it only needs a simpler ConcurrentHashMap<Integer, Integer> map and can be added to and 
returned directly using a callable and future to cut down on memory usage, code and time. I tried having 
both classes together, but it made both the FileParser and ShingleTaker quite messy and I found they were 
doing too many things breaking high cohesion. Also, I added the feature to be able to parse a URL as 
a query file, so it made it much simpler and cleaner to add as it was in a separate class. Because of 
this if down the line another type of file was to be added it could be simply done without breaking the 
FileParser and QueryFileParser classes promoting loose coupling. 

This class reads in the file and parses it line by line striping out all characters except for letters, 
numbers and spaces. I used a pattern to slightly speed up this process. If it’s a URL then JSoup is used 
to remove all HTML and just leave the text. For more realistic results (Extra) I developed a simple way of adding 
three word shingles. First it checks the length, if it’s divided by three equally then it’s perfect, 
if it has a remainder of one then last word be added to the end as its own shingle. Lastly if it has a 
remainder of two then a two word shingle is added at the end. Finally, for both classes hashcode it 
called on all words as they are put into the map or queue to cut down on memory and increase the speed. 
Also in the QueryFileParser the count for each file is added which would have increased complexity again 
if classes were merged as it’s handled in the SingleTaker for the subject directory.

### ShingleTaker Class:
This class takes the shingles added to the queue by the FileParser and adds them to a 
Future<ConcurrentHashMap<Integer, List<Index>>>, this allows for O(1) searching and insertion. 
Each shingle is taken from the BlockingQueue and if it’s an instance of Poison then the filecount is 
decremented signifying one file is complete. Else the map is checked to see if it includes 
the shingle from the queue, it then checks if it’s already in the map with the same book, 
if so then count is incremented. If the book is not in the map, then a new index is added 
to the arrayList. Or else a new Shingle is added to the map with the book. Once complete the 
full map is returned to be sent to the CalculateDotProductAndMagnitude class through the Processor.

### CalculateDotProductAndMagnitude Class:
Both maps for query file and subject directory are taken in and initialized. Two new maps a created 
for each file in the subject directory to be able to calulate the dotproduct and magnitude for each. 
First is a loop through the subject directory to count up the magnitude. Then a loop through the query 
directory which counts up the magnitude first, and then checks if the word is also in the subject 
directory map. If it is then it calculates the dot product for each file using both frequencies for 
the current shingle. The data is then passed into the CalculateCosine class for final calculation 
and printing of results.

### CalculateCosine Class:
Using the two file maps and the counted query frequency the cosine distance can be calculated for each 
file and printed to the screen, a StringBuilder is used to cut down on memory and pass it into the PrintResults.

### PrintResults Class (Extra):
This gives the user the option to print the cosine distance results to a file, which could be useful 
for future records or for uploading to a database. If 1 is selected the user can enter a file name and 
extension and the results are printed to a file, with the query file printed at the top the subject 
file and result printed in order below.

### Word, Poison, and Index Classes:
These three classes are used to instantiate some of the elements throughout the FileParser and ShingleTaker class. 
Word is used to add new shingles to the BlockingQueue and is composed (aggregation) as an instance variable in the BlockingQueue in 
both FileParser and ShingleTaker. Poison inherits from Word and is used by the FileParser to tell the ShingleTaker when 
the file is completed. Lastly Index is used to add a new array index to the subject file map with the frequency and file name.

## Overall goals and thoughts:
•  I originally started with a basic implementation of just adding the subject directory to the queue and 
   building up the map with one-word shingles. I then implemented a basic file parser in the processor for 
   the query file to get it up and running. But I felt there was a more efficient way to do this. 
•  So, began improving the algorithm by separating out code so that each class has one purpose and doesn’t
   rely heavily on another class to adhere to SRP, loose coupling and high cohesion. This allows changes/additions 
   to be made to other classes without breaking the whole program. 
•  I then tried to increase the speed by implemented hashcodes on all shingles and file names to cut down on the 
   overhead of strings and comparison speeds. 
•  After I added the three-word shingles to give a more realistic result and increase speed as there is 
   less loops of each line in the file. 
•  Also, I implemented callable and futures for both the ShingleTaker and FileParser which increased simplicity and speed. 
•  Lastly, I added a thread pool for parsing the subject files. 
•  By using ConcurrentHashMap’s where possible it allowed for O(1) searching and insertion (Running times tests can be found below)
•  I also tried implemented the shingle taker with a nested ConcurrentHashMap which but it increased the memory usage, and didn't 
   increase the speed so I left it out. It also was complicating the CalculateDotProductAndMagnitude Class.


## Speed tests (I7 Processor):
War & Peace – compared against PoblachtNaHEireann, DeBelloGallico & War & Peace = 1.1s
War & Peace – compared against above + 15 other large books = 1.5 second (avg)

Time goes up very slowly no matter the number of books in the directory, most of the time seems to be setting up threads etc. 
I also tried 96 books which gave a running time of 6 seconds. So, running time seems to be linear O(n).

## Additions/Extras:
•	The option to print the results to a file, which would be useful for future records or for uploading to a database. 
	This would stop the need for running again.
•	The option to also compare a URL to the subject directory using JSoup (removes HTML tags).
•	Ability to compare multiple different text files (.txt, .docx, .html and .pdf)
•	Ability to compare a large number of files in the subject directory to a query file/URL.
•	Hashcode, thread pools and ConcurrentHashMap implementation for speed.
•	Implemented three-word shingles for more realistic results.
•	Validation on all menu inputs especially for query file, directory and url selection to check if they exist.
