/*************************************************************************
 *  Compilation:  javac Wildcard.java
 *  Execution:    java Wildcard pattern < wordlist.txt
 *  Dependencies: In.java
 *  
 *  Find all lines in wordlist.txt that match the given pattern by
 *  simulating a nondeterminstic finite state automaton using an
 *  Boolean array states[] which records all states that the NFSA
 *  could be in after reading in a certain number of characters.
 *
 *     Patterns supported
 *     ------------------------------
 *     *  any zero or more characters
 *     ?  any one character
 *     c  character c
 *  
 *
 *  Sample execution:
 *
 *     % java Wildcard *wa*t**c*d* < wordlist.txt
 *     unwatched
 *     waistcoated
 *     watchdog
 *     watched
 *     watchword
 *
 *     % java Wildcard ?a?e*i*o*u*y < wordlist.txt
 *     facetiously
 *
 *     % java Wildcard ........................ < wordlist.txt
 *     formaldehydesulphoxylate
 *     pathologicopsychological
 *     scientificophilosophical
 *     tetraiodophenolphthalein
 *     thyroparathyroidectomize
 *
 *  Note: not the most efficient algorithm.
 *
 *************************************************************************/
package com.javaxyq.util;

public class Wildcard { 

  /***********************************************************************
   *  Check if pattern string matches text string.
   *
   *  At the beginning of iteration i of main loop
   *
   *     old[j]    = true if pattern[0..j] matches text[0..i-1] 
   *
   *  By comparing pattern[j] with text[i], the main loop computes
   *
   *     states[j] = true if pattern[0..j] matches text[0..i] 
   *
   ***********************************************************************/
   static public boolean matches(String pattern, String text) {
      // add sentinel so don't need to worry about *'s at end of pattern
      text    += '\0';
      pattern += '\0';

      int N = pattern.length();

      boolean[] states = new boolean[N+1];
      boolean[] old = new boolean[N+1];
      old[0] = true;

      for (int i = 0; i < text.length(); i++) {
         char c = text.charAt(i);
         states = new boolean[N+1];       // initialized to false
         for (int j = 0; j < N; j++) {
            char p = pattern.charAt(j);

            // hack to handle *'s that match 0 characters
            if (old[j] && (p == '*')) old[j+1] = true;

            if (old[j] && (p ==  c )) states[j+1] = true;
            if (old[j] && (p == '?')) states[j+1] = true;
            if (old[j] && (p == '*')) states[j]   = true;
            if (old[j] && (p == '*')) states[j+1] = true;
         }
         old = states;
      }
      return states[N];
   }


//   public static void main(String[] args) { 
//      String pattern = args[0];
//
//      while (!StdIn.isEmpty()) {
//         String text = StdIn.readString();
//         if (matches(pattern, text))
//            System.out.println(text);
//      }    
//   }

}

