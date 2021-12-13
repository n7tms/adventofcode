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

## Day 08 - Seven Segment Search    
*determine the correct 7-seg display, dispite random wire connections*  
(Solve Time: 0:48:00 and x:xx:xx)  
  
Part 1 was relatively easy for me. The most difficult thing was parsing the text. Once I had it in, it was fairly trivial to determine the 1, 4, 7, and 8 digits.  
  
Part 2 took me out! I could not wrap my head around any form of logic that would get me to the end. I borrowed a solution from tcsullivan (https://github.com/tcsullivan/advent-of-code/tree/master/day8).  
  
- `as->`, `subvec`, `comp`, `assoc`, and others that I need to understand better.  
-   

## Day 09 - Smoke Basin  
*Navigate a series of hydrothermal vents and basins*
(Solve time: 0:59:00 and >24hrs)   
  
Part1 was relatively easy. I used a series of functions to check surrounding digits for higher numbers.   
  
Part2 took me out. I could navigate the matrix of numbers, but there were numerous edge cases that my algrithm would not account for. For example:  
  
 999911999  
 993911199  
 911111999  

The 3 is not discovered as part of the basin it belongs in.  
  
I worked for over 24 hours before I finally caved and looked on reddit for a c++ solution that I could compile, get an answer and put day 9 to bed for a while.  
I want to revisit all the days anyway to improve and learn from the code.  
  
## Day 10 - Syntax Scoring  
*Given a series of open and closed brackets, find the broken sequence and complete the incomplete sequences.*  
(Solve time: 1:11:00 and 0:27:00)    
  
It took me just over 90 minutes to solve day 10. Once I got my `stack` working and remembered to check for all cases of brackets, things went very smoothly. The extra work in part1 paid off in part2.  
  
I had some issues keeping the strings *strings*. Some operations (e.g. `(first "(}(<")` would return a character `\(` rather than a string `"("`. It might have simplified the code to just deal with the characters, but I'm not very experienced with those operations...yet.  
  
- Notes about vectors as a stack  
  - `pop`, `conj`, `peek`  
  
Update: I went back and implemented day10 using vectors and `peek`, `pop`, and `conj`. The execution time was slightly improved; the readability (IMO) is slightly improved; but the overall flow of the program is the same.  
    
## Day 11 - Dumb Octopus    
*count the number of energy flashes from 100 octupi*   
(Completion time: 0:00 and 0:00)  *<= just hrs:mins from now on*  
  
OUCH!! I worked on this problem for over 8 hours. I think I have a sound algorithm, but I cannot get it implemented in Clojure. The lack of mutable variables is a serious source of confusion for me.  
  
I am so close on part1. Using the sample input, I am generating 33 instead of 35 flashes and I'm not sure why.  
   
In my github, you'll find a couple of clojure solutions from https://www.reddit.com/user/theronic/ and https://curls.it/2s2rZ.
  
  
But the intriging one to me is the bash script from https://www.reddit.com/user/Steinrikur/.  

My mind is beat. Going to bed. I'll tackle day 12 tomorrow.... *sigh*.  
  
## Day 12 - Passage Pathing  
*given a set of edges from a "graph" map all the possible routes from start to end*  
(Completion time: > 04:00)  
  
This is classic graphing DFS. But, do you know how long it has been since I've coded that stuff ?? A while!   
I spent a couple hours researching graphing algorithm -- found a good one in Loom. Tried to implement it. Failed!  
  
I threw that code away -- literally -- and tried to works something up on my own. Again, bogged down in the lanuage. (I keep reminding my selve that I'm learning Clojure because I want to!)  
  
The code posted here in github is from RedPenguin101 (https://github.com/RedPenguin101/aoc2021/blob/main/day12.md). I don't think it is the most optimized algorithm -- it took about 11 seconds to solve part 2 -- but his style makes it the most readable to me and I'll learn something about Clojure from him. Thank you.  
  
## Day 13 - Transparent Origami   
*fold transparent paper to align dots and generate letters.*  
(Completion time: ~3 hours total)  
  
This was a fun one. I love it when they are within my sphere of capability. The input was in two parts: the coordinates and fold instructions.   
The first part...just count the number of visible dots after one fold.  The second part...what eight letters are generated after the folds are complete?  
The fold functions gave me a bit of an issue -- but only because I wasn't paying attention. For example, the sample folds first on the Y axis in the middle of the page. I assumed that mine also folded first on the Y axis. But it continually produced the wrong result. Then I realized that 655 was not in the middle of the paper. So I adjusted my fold functions to handle non-symmetrical folds. Still not correct. Then I realized that my input's first fold was on the x-axis. Now I can solve part 1.  
  
For part 2, -- my quick and dirty solution, was to export the points to an Excel spreadsheet and use that program to generate a scatter plot of the points. It worked as planned, but it took me the better part of an hour to massage the data and generate the plot.  
In my final code iteration, I employed an external library, Incantor, which very slickly plots the points. In fact, I was able to produce a solution in about 10 minutes. (I wish I had started there last night!)  
- use of `re-matches` and `parse` functions to parse the disparate data types in the input
- of course, Incantor's `scatter-plot` and `view` functions.  
Note: The code posted here in github is not part of a Leiningen project. So it will not run without specifying the Incantor dependency. [incanter "1.9.3"]  
  
## Day 14 -   
**   
(Completion time:  )  
  


  



  


  



