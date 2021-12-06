# Advent Of Code 2021



## 2021
I'm on my own! There is no Fred Overflow to guide me when I get stuck. It is all me.

I'm no speed coder. I don't do this for the accolades. I do it for the satisfaction of figuring it out. It is one of the things I truly enjoy about coding, and particularly about learning a new language.


## Day 00  
This is a template file I use to create subsequent day files. It contains the general basic structure I use for all AoC problems.  
`:require [clojure.string...]`  
small and large defs  
custom `split` function...reverses arguments  

## Day 01 - Sonar Sweep
*Count the number of times the depth increases.*  
- Parsed list of numbers  
- Use of `partition` to `map` and evaluate those numbers in groups  
- Also thread-last (`->>`), `apply`, and `filter`.  

## Day 02 - Dive!  
*Parse a list of command/directions and determine the final position.*  
- Used map tags (`:depth` and `:horiz`) to track positions  
- mapped and reduced to find totals  

## Day 03 - Binary Diagnostic
*Parse a set of binary numbers to determine the power consumption, O2 and CO2 ratings*  
- Between part1 and part2, I discoverd that Clojure has bit-wise operations. That made part2 MUCH easier!  
- I did a poor job of generalizing my code. I wrote some functions specifically for the small dataset and other functions for the large dataset.  
- a function to generate the 1's complement of a number  
- `bit-and` and `bit-xor`  
- `loop` through various powers of 2 to determine what bits are set in what binary columns.   

## Day 04 - Giant Squid   
*Play bingo against a giant squid*  
- This was a fun puzzle!  
- two different sets of data in the input -- a string of numbers and the bingo cards.  
- I treated the cards as a list of 25 integers.  
- When a number was "called", I change that number on the cards to a -1.  
- nested `for` loops  
- Also `some`, `partition`, and `reduce`.  
- I'm quite proud of my `winning-col?` function!  
Part2 really stumped me for a while. I found the last card, but the score was wrong. Then I realized that I still needed to "play" that card until it became a winning card. I ended up passing just the last card to my part1 code to let it play out.  

## Day 05 - Hydrothermal Venture  
*map out lines of thermal vents*  
(Solve time: 2:08:08 and 0:47:07)  
Given a slightly more complex input `0,9 -> 5,9`, determine how many points the lines intersect.  
Horizontal and vertical lines we pretty straight forward. The diagonals added a little complexity.     
- parsed the input using `re-matches`  
- used `frequencies` to "group" the redundant pairs of points and `filter`ed out the ones with less than 2 intersections.  
  - I probably would have solved it in half the time had I found/remembered `frequencies` earlier.  
- the use of `cond` to determine which direction the line was going.




First introductions to `slurp`, `split-lines`, and `parseInt`  
`let` constructs  
nested `for` loops  
