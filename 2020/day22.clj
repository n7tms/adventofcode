;; Advent of Code
;; Day 22 - Crab Combat
;;

(ns adventofcode
  (:require [clojure.string :as string]))

;; I'll put this messing starting-deck business at the end of the code.
;; (declare deck1a)
;; (declare deck2a)
(declare deck1b)
(declare deck2b)

(def deck1a "9
2
6
3
1")

(def deck2a "5
8
4
7
10")

(defn split [regex s]
  "swaps string/split parameters of idiomatic ->> threading"
  (string/split s regex))

(def player1
  (->> deck1b
       (split #"\n")
       (map #(Integer/parseInt %))
       ))
(def player2
  (->> deck2b
       (split #"\n")
       (map #(Integer/parseInt %))
       ))

(defn combat []
  (loop [p1 player1
         p2 player2]

    (if (or (empty? p1) (empty? p2))
      (if (empty? p1)
        p2
        p1)
      
      (do
        (if (> (first p1) (first p2))
          (recur (flatten (list (rest p1) (first p1) (first p2))) (rest p2))
          (recur (rest p1) (flatten (list (rest p2) (first p2) (first p1))) ))))
    
    ))

(defn part1 []
  (reduce + (map * (combat) (range (count (combat)) 0 -1))) 
  )

(part1)  ;; => 30197



;; part 1 is all me. That was fun.
;; part 2 caused a great deal of consternation.
;; I really like this elegant solution: https://github.com/rjray/advent-2020-clojure/blob/master/src/advent_of_code/day22.clj









(def deck1b "48
23
9
34
37
36
40
26
49
7
12
20
6
45
14
42
18
31
39
47
44
15
43
10
35")
(def deck2b "13
19
21
32
27
16
11
29
41
46
33
1
30
22
38
5
17
4
50
2
3
28
8
25
24")
