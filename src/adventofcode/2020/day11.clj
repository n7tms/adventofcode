;; Advent of Code 2020
;; Day 11
;;
;; https://adventofcode.com/2020/day/11
;;
;; Description of problem
;;
;;
;;
;; Credit to Fred Overflow (https://www.youtube.com/watch?v=1t6Monx_dsk&list=PLbPrugU2oQ8VURsQdZ6W_iovXRS24UmZQ&index=11)
;;
;; Honestly, the clojure constructs he employed are so far over my head, I'm not sure I can even explain them.
;; Especially in the case block in the successor function where he is checking for occupancy -- I have no clue
;; what is going on there....!!!


(ns user
  (:require [clojure.string :as string]
            [clojure.java.io :as io]))

;; This is the input data we'll be using.
;(def input-file "day11-input.txt")       ;; home
(def input-file "./src/working/day11-input.txt")        ;; office


;; This slurp line is just for testing. (Can I read the input file?)
;(slurp input-file)

(def large
  (slurp input-file))

(def small "L.LL.LL.LL
LLLLLLL.LL
L.L.L..L..
LLLL.LL.LL
L.LL.LL.LL
L.LLLLL.LL
..L.L.....
LLLLLLLLLL
L.LLLLLL.L
L.LLLLL.LL")


;; Fred chose to pad the edges with spaces. I chose periods because it is more visible.
;; That decision might come back to bite me in part 2.
(defn pad
  "pads white space above and below the seating so that every seat 
  appears to have 8 adjacent seats."
  [layout]
  (let [width (.indexOf layout "\n")
        spaces (.repeat " " width)]
    (str "\n" spaces "\n"
         layout
         spaces "\n")) 
  )

;(pad small)     ;; test pad function


(defn successor
  " "
  [^String grid]
  (let [stride (.indexOf grid "\n" 1)
        
        neighbors [(- -1 stride) (- 0 stride) (- +1 stride)
                      -1                         +1
                   (+ -1 stride) (+ 0 stride) (+ +1 stride)]
        occupied (fn [i]
                   (for [j neighbors
                         :when (= \# (.charAt grid (+ i j)))]
                     j))
        ]
    (string/join
     (for [i (range (.length grid))
           :let [cell (.charAt grid i)]]
       (case cell
         \L (.charAt "#LLLLLLLL" (count (occupied i)))
         \# (.charAt "####LLLLL" (count (occupied i)))
         cell))))
)


;; The following thread-last block of code generats an error
;;        Error printing return value (StringIndexOutOfBoundsException) at java.lang.StringLatin1/charAt (StringLatin1.java:48).
;;        String index out of range: 132
;; I cannot find source of the error.
;; I have written out the rest of Fred's code, just for completion's sake.
;;
; (->> small
;      pad
;      successor
;      )


;; Despite the errors during coding mentioned above, the following block executes fine.
(defn part1 []
  (->> large
       pad
       (iterate  successor)
       (partition 2 1)
       (take-while (fn [[ a b]] (not= a b)))
       last
       last
       (filter #(= \# %))
       count
       ))

(part1)   ;; => 2316



(defn successor2
  " "
  [^String grid]
  (let [stride (.indexOf grid "\n" 1)
        
        neighbors [(- -1 stride) (- 0 stride) (- +1 stride)
                      -1                         +1
                   (+ -1 stride) (+ 0 stride) (+ +1 stride)]
        occupied (fn [i]
                   (for [j neighbors
                         :when (loop [k (+ i j)]
                                 (let [cell (.charAt grid k)]
                                   (if (= \. cell)
                                     (recur (+ k j))
                                     (= \# cell))))]
                     j))
        ]
    (string/join
     (for [i (range (.length grid))
           :let [cell (.charAt grid i)]]
       (case cell
         \L (.charAt "#LLLLLLLL" (count (occupied i)))
         \# (.charAt "#####LLLL" (count (occupied i)))
         cell))))
)


(defn part2 []
  (->> large
       pad
       (iterate  successor2)
       (partition 2 1)
       (take-while (fn [[ a b]] (not= a b)))
       last
       last
       (filter #(= \# %))
       count
       ))

(part2)   ;; => 2128







