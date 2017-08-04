// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/*
 * Hey, I'm really sorry that my solution is not complete. 
 * It's not the best example of my growth because I learned a ton 
 * but we just finished the seventh feature last night due to life
 * things on my teammates' ends, so I did the best I could in the 
 * time allotted.
 * 
 * Extra things to test if I'd finished earlier: error cases with 
 * one or two tokens out of place, such as a symbol token in a 
 * string " + "
 * 
 * the three-quote conundrum "\" \" \" ;"
 * 
 * whatever this is "\" \'\n \" ;"
 */

package com.google.codeu.mathlang.impl;

import java.io.IOException;

import com.google.codeu.mathlang.core.tokens.NameToken;
import com.google.codeu.mathlang.core.tokens.NumberToken;
import com.google.codeu.mathlang.core.tokens.StringToken;
import com.google.codeu.mathlang.core.tokens.SymbolToken;
import com.google.codeu.mathlang.core.tokens.Token;
import com.google.codeu.mathlang.parsing.TokenReader;

// MY TOKEN READER
//
// This is YOUR implementation of the token reader interface. To know how
// it should work, read src/com/google/codeu/mathlang/parsing/TokenReader.java.
// You should not need to change any other files to get your token reader to
// work with the test of the //System.
public final class MyTokenReader implements TokenReader {
  private StringBuilder token; // builds current token
  private String source;
  private int at; // char position in source, so to speak
  
  private final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
  private final String numeric = "1234567890.";
  private final String symbolic = ";=+-";
  
  public MyTokenReader(String s) {
    // Your token reader will only be given a string for input. The string will
    // contain the whole source (0 or more lines).
	  
	source = new String(s);
	token = new StringBuilder(source);
    at = 0;
  }
  
  @Override
  public Token next() throws IOException {
    // Most of your work will take place here. For every call to |next| you should
    // return a token until you reach the end. When there are no more tokens, you
    // should return |null| to signal the end of input.

    // If for any reason you detect an error in the input, you may throw an IOException
    // which will stop all execution.
    int start = 0;
    int end = 1;
    while(end < source.length()){
	  String current = source.substring(start,end);
	  if (alphabet.indexOf(current) > -1){
	    //System.out.println("name found");
	    String name = findName(start);
	    source = source.substring(start+name.length());
	    return new NameToken(name);
	  } else if (numeric.indexOf(current) > -1){
		//System.out.println("float found");
		String num = findNumber(start);
	    source = source.substring(start+num.length());
	    return new NumberToken(Double.parseDouble(num));
	  } else if (symbolic.indexOf(current) > -1){
		//System.out.println(" symbol found: "+current);
		source = source.substring(start+1);
		return new SymbolToken(current.charAt(0));
	  } else if (current.equals("\"")) {
		//System.out.println(" string mark found");
		String str = findString(start);
		source = source.substring(start+str.length()+2);
		return new StringToken(str);
	  } else if (current.equals("\0")) {
			//System.out.println("null mark found");
			return null;
	  } else {
		//System.out.println(" *space*");
	  }
	  start++;
	  end++;
    }
	return null;
  }

  private String findName(int start){
	int end = start+1;
	while(end < source.length()){
	  char c = source.charAt(end);
	  if(Character.isWhitespace(c)){
		  //System.out.print(" Name decoded: "+source.substring(start,end));
		  return source.substring(start, end);
	  }
	  end++;
	}
	return null;
  }
  
  private String findNumber(int start){
    int end = start+1;
	while(end < source.length()){
	  char c = source.charAt(end);
	  if( !Character.isDigit(c) && !(c=='.')){
		//System.out.print(" Number decoded: "+source.substring(start,end));
		  return source.substring(start, end);
		}
	  end++;
	}
	return null;
  }
  
  private String findString(int start){
    int end = source.indexOf("\"", start+1);
    if (end > -1){
      //System.out.print(" String found: "+ source.substring(start+1, end));
      return source.substring(start+1, end);
    }
    return null;
  }
  
 }
