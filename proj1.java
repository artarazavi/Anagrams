import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.io.*;
import java.io.FileWriter;
public class proj1 {
    public static void main(String[] args) {
        String dict = args[0];
        String inputfile = args[1];
        String[] arr = getano(dict);
        try{
        FileWriter writer = new FileWriter(inputfile);
        //create new file writer used for writing anagram classes to file
        for(String str: arr) {
          //parsing through each string in the list of strings which contains the anagram classes
          if(str!=null){
            //because the table is essentially the size of our dictionary there will be some null spots so we skip those when writing to the file
          writer.write(str);
          //this writes each string of anagram classes to the file
          writer.write(System.getProperty( "line.separator" ));
          // this puts in a blank line
          }
        }
        writer.close();
        }
        catch(IOException e){
          e.printStackTrace();
        }
    }
    public static String[] getano(String dict){
      //the function that essentially creates the anagram classes
      File file = new File(dict);
      String path = file.getAbsolutePath();
      //looks in the system for the file path of the dict
      String filePath = path.substring(0,path.lastIndexOf(File.separator));
      ArrayList<String> ar = new ArrayList<String>();
      //this Array List is used to store the words parsed in from the dictionary
        try (BufferedReader br = new BufferedReader(new FileReader(filePath+"//"+dict)))
        {//buffered reader is used to parse in the file and and store each word into the Array List
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {//check to see if line is null
                ar.add(sCurrentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
      int tablesize = ar.size();
      //the size chosen for the hash table based on assumption thers no anagrams
      int[] hashtable = new int[tablesize];
      //this is our keys of the hash table which are the size of the dict
      String[] group = new String[tablesize];
       //this is our values of the hash table which are the size of the dict
      for(int i = 0; i<ar.size(); i++){
        //parsing through the list of words one word at a time
        String nonsorted = ar.get(i);
        int[] ans = hashIt(nonsorted, tablesize);
        //sends word to hash function to be hashed and its returned {mul=raw val,hash} value
        int hash = ans[1];
        //the hash value is the index of the hash table we are going to try and insert the word in
        int rawval = ans[0];
        //the raw value is what the value was before it got moded by the size of the hash table
        //the raw value is used for checking wether the word you are string has the same prime
        //multiplication value as the other words in your hash table if the prime multiplication
        //value matches this means that the word has anagrams at that spot in the table
        //this step is linearly probing
          if(hashtable[hash]==0){
            //case 1 there is nothing in that spot
            hashtable[hash]=rawval;
            //insert your raw value in the hash index of your keys table
            group[hash]=nonsorted;
            //insert your word in the hash index of your values table
          }
          else if(hashtable[hash]==rawval){
            //case 2 there is something there and the associated raw value matches
            //your raw value from the hash function
            //this is a hit
            group[hash] = group[hash]+", "+nonsorted;
            //retrieve the words from the values table at the hash index and
            //add your word to the list of words
            //and store it back into that location
          }
          else{
            //case 3 something else in its spot and the associated raw values dont match this is a miss
            int location = findit(hashtable,nonsorted,hash,rawval);
            //march down the keys table looking for the word or the next empty spot
            if(group[location]==null){
              //you didnt find the word and you hit a null spot this means you store your word here
              hashtable[location]=rawval;
              //place your raw valye in the keys table at the hash index
              group[location] = nonsorted;
              //store your word in the values table at the hash index
            }
            else{//you were able to find the word
            group[location]=group[location]+", "+nonsorted;
            // you pull the string in the hash index of the values table
            //add your word to it and store it back into that spot
            }
          }
      }
      return group;
      //returns the anagram classes int the form of an array of strings
    }
    public static int[] hashIt(String s, int size){
      int[] PRIMES = new int[] { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31,
        37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103,
        107, 109, 113 };//array of first 30 primes
      int mul = 1;//stores raw value possible overflow not moded with anything
      int hash = 1;//stores hash table index moded by size of table at each step to avoid overflow
      for(int i=0; i<s.length(); i++){
        int c = (int)(s.charAt(i));
        //fetches the character at index i of the word and casts it to an integer
        c=c-96;
        //to get associated location of the word in the alphabet you subtract
        //96 from the obtained int value
        mul=mul*PRIMES[c-1];
        //multiplies the raw value by the words associated prime number
        hash = hash * PRIMES[c-1];
        //multiplies the hash value by the words associated prime number
        hash = hash % size;
        //mods hash value by size of hash table at every step to avoid overflow
      }
      int[] ret = {mul, hash};//returns the raw value and the hash value
      return ret;
    }
    public static int findit(int[] arr, String s, int start, int rawval){
      //this function is used to find the word in the case of a miss
      //or return a the index of the first null location this is how linear
      //probing works if you try to map to a spot and a non matching value is
      //there you march down your hash table looking for that word but if you
      // hit a null spot before you find the word it means that the word is
      //not there and you just return the null spot and the value would get
      //inserted into that null spot.
      for(int i = start; i<arr.length; i++){
        //marching down the hash from the hash index to the end
        if(arr[i] == 0){//if null spot found return null spot
          return i;
        }
        if(arr[i]==rawval){//if the desired key was found return that spot
          return i;
        }
      }
      for(int i = 0; i<start; i++){
        //for the rare cases that we reach the end and dont find it
        //we wrap around and start at the begginig of the list and
        //serch up to the hash location looking for it
        if(arr[i] == 0){
          return i;
        }
        if(arr[i]==rawval){
          return i;
        }
      }
      return -1;
    }
}
