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

## Day 06 - Lanternfish   
*count how many lanternfish are reproduced after 80 and 256 days.*   
(Solve time: 0:25:00 and 1:38:30)  
  
Part1 was very easy. A quick for loop rendered the answer in about 4 seconds.  
However, the answer to part2 was in the trillions. A for loop wasn't going to cut it!  
I decided to modify all the code to keep track of how many fish were in each category or bucket, ie. the number of fish with 1 day before birthing, fish with 4 days before birthing, etc. It proved to be VERY efficient and FAST -- about 0.6 msec. to evaluate 256 days.  
- `hash-map` to keep track of the number of fish in each category/bucket   
- `(apply + (vals bucks))` to calculate the total number of fish produced.   
  
This was my best ranking day so far on the leaderboard -- 7619 on part 1.  

## Day 07 - The Treachery of Whales   
*find the minimun number of moves to align the crabs*  
(Solve Time: 0:28:00 and 0:58:00)  
  
Again, fairly straight forward and easy puzzles. (I'm not complaining!)  
Essentially, I iterated over a list of integers `map`ping the difference to various positions and then `sort`ing and taking the lowest (`first`) number.  
I knew I could use the same algorithm in part2, but was stumped when my answer kept coming up wrong. Then I discovered, because I was iterating over the list of crab positions/fuels, that there were some positions/fuels missing from the list -- which just happened to be the optimum shift. So I adjusted the part2 algorithm to iterate over a `range` from 0 to the max fuel number...so as not to miss any numbers.  
- `for`, `sort`, `range`, `map`, `reduce`, `cond`.  
- I attempted to use `deftest` to report correct evaluations. Pseudo-successful.  

## Day 08 -    
**  
(Solve Time: x:xx:xx and x:xx:xx)  


  


  



