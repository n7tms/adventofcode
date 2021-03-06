# Advent Of Code 2020

Because I can't "tag" individual files, this is my attempt to track which days have examples of
types of clojure constructs, patterns, and examples so I can quickly find examples when working
on future problems.

## Day 01  
First introductions to `slurp`, `split-lines`, and `parseInt`  
`let` constructs  
nested `for` loops  

## Day 02  
Parsing a text file  
`re-matches`  
`io/reader`  
`mapv`  
`filter`  

## Day 03  
`io/reader`  
`slurp` and `split-lines`  
a lot of work is done in a single `let` block  

## Day 04  
a function to reverse the parameters of `String/split`  
thread-last (`->>`)  
`walk`  
`case`  
`re-matches` and other string and line parsing  
`map`  
`filter`  

## Day 05
Using `parseInt` to convert from binary to decimal  
thread-first (`->`) and thread-last (`->>`)   
`String/replace`  
`io/reader`  
`apply`  
`vec`  
`line-seq`  
`sort`  
`partition`  
`filter`  
... lots of stuff in this day  

## Day 06  
a function to reverse the parameters of `String/split`  
`disj`  
`set` and `set/intersection`  
`split`  
`map`  
`reduce`  

## Day 07  
`re-seq`  
`let`  
`io/reader`  
`line-seq`  
`map` and `apply`  
thread-last (`->>`)  
`some`  
anonymous function `fn`  

## Day 08  
(mine: day08.clj)  
a function to reverse the parameters of `String/split`  
`split-lines  
`let` and key mappings  
`re-matches`  
`nth`  
`some`  
`cond`  
`get`  
a poor example of the use of `cons`  

(Fred Overflows: day08-fo.clj)  
`re-matches  
`keyword  
a good example of a `loop` construct  
`let` and nested bindings `(let [[op arg] (program idx)]`  
`case`  
`conj`  
`mapv`  
`update-in` (to change "jmp --> nop" and "nop --> jmp") (interesting...and a bit confusing!)  

## Day 09  
`Long/parseLong`  
`mapv`  
`split-lines`  
`let`  
`for` loop (:let and :when)  
nested `for` loop  
`loop` `cond` `recur`  

## Day 10  
`memoize`, `apply`, `partition`  
`let` and `cond`  
`sort`, `mapv`  
thread-last `->>`
`conj`, `loop`


## Day 11  
Honestly, this was WAY over my head as far as my abilities in clojure.  
I let Fred "provide" my solution and I moved on to day 12.  

## Day 12  
`re-matches`  
`keyword`  
`update`  
`case`  


## Day 13
I managed to solve part 1 completely on my own. I later looked at Fred's code  
for help in part 2 and to see where I could optimize my part 1 code.   
I'm a noob to clojure, but IMHO Fred is a genius!  
`re-seq`  
`min-key`  
`interleave`  
`partition`  
`remove`  

## Day 14
Coming soon...

